package com.daangn.clone.item.service;

import com.daangn.clone.category.repository.CategoryRepository;
import com.daangn.clone.chatting.chattingroom.repository.ChattingRoomRepository;
import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.common.enums.Role;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.encryption.AES256;
import com.daangn.clone.file.FileServiceUtil;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.*;
import com.daangn.clone.item.dto.paging.*;
import com.daangn.clone.item.repository.ItemRepository;
import com.daangn.clone.itemimage.ItemImage;
import com.daangn.clone.itemimage.repository.ItemImageRepository;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.town.repository.TownRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.daangn.clone.common.enums.ItemStatus.*;
import static com.daangn.clone.common.response.ApiResponseStatus.FAIL_GET_ITEM_LIST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService{

    @Value("${file.dir}")
    private String fileDir;

    @Value("${sample.dir}")
    private String sampleDir;



    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final FileServiceUtil fileServiceUtil;
    private final AES256 aes256;




    /** 암호화/복호화를 위한 private 서비스 */


    /**
     * [조회]
     * */

    /** <특정 Member 조회 - JWT를 복호화 해서 얻은 memberId로! > */
    private Member getMember(Long memberId){
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_MEMBER, "JWT를 복호화 한 memberId로 Member를 조회했음에도 , Member를 조회할 수 없습니다.");
                }
        );

        return findMember;
    }

    /** <특정 아이템 조회> */
    @Transactional
    public ItemDto getItem(Long id) {


        //0. itemId 유효성 검사와 동시에 , Item 엔티티 조회
        Item item = itemRepository.findItemById(id).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.FAIL_GET_ITEM, "itemId : " + id + " 의 유효하지 않은 itemId 값으로 인해 특정 Item 조회에 실패했습니다.");
                }
        );

        //1. 해당 Item 엔티티의 조회수 필드값 업데이트 후 (단 동시성 문제 남아있음) -> 해당 Item 정보를 가공한 ItemDto 반환
        item.increaseVisitCount();

        //2. 해당 상품과 관련된 상품 이비지의 리스트를 "AES128 암호화 후 URL 인코딩" 시킴
        // 단 이때 상품 이미지가 저장된 로컬 경로는 , AES128로 암호화 한후 -> URL에서 인삭하지 못하는 특수문자들을 한번 더 인코딩한 후 -> 보낸다.
        // -> 예를들어 +라는 문자를 %2B로 인코딩 해서 보내야 - 그 %2B 가 url로써 네트워크를 타고 넘어왔을 때 + 로 바뀐다 (아마 이런게 URL인코딩의미) (그런데도 만약 여기서 이미 +로 변한 결과를 또 URL 디코딩 하면 문제!)

        List<String> encrpytedPathList = fileServiceUtil.getEncryptedPathList(item, sampleDir, aes256);

        return ItemDto.builder()
                .itemeId(item.getId())
                .title(item.getTitle())
                .content(item.getContent())
                .price(item.getPrice())
                .visitCount(item.getVisitCount())
                .categoryName(item.getCategory().getName())
                .townName(item.getTown().getName())
                .sellerMemberName(item.getSellerMember().getNickname())
                .createdAt(item.getCreatedAt())
                .delYn(item.getDelYn())
                .itemStatus(item.getItemStatus())
                .numOfWish(item.getWishList().size())
                .numOfChattingRoom(item.getChattingRoomList().size())
                .itemImagePathList(encrpytedPathList)
                .build();

    }



    /** <아이템 목록 조회> */
    public ItemSummaryPageDto getItemList(Long memberId,
                                          Long townId,
                                          Long categoryId, ItemStatus itemStatus,
                                          SortCriteria sortCriteria,
                                          Pageable pageable){

        //0. 유효성 검사 : 설정한 townId가 , 그 사용자가 속한 town과 관련된 town의 Id 인지 검사
        /** 실제 당근마켓에서는 여러 town들이 설정될 수 있지만, 여기서는 사용자와 Item모두 하나의 town에 속한다는 가정 */
        if(getMember(memberId).getTown().getId() != townId){
            throw new ApiException(FAIL_GET_ITEM_LIST, "상품 목록을 가져올 town이 , 사용자가 속한 town과 다른 town으로 요청이 들어왔습니다.");
        }


        //1. 이후 조건에 따른 페이징을 수행하여 - 조회된 ItemSummaryDto 들을 반환
        /** 그냥 순수 Item으로 조회하면 , Item의 필드 중 , content 필드도 함꼐 조회해오는데 , 이게 양이 어마어마할 수 있음.
         * 따라서 DB에 무리가 가는 작업을 막기 위해 , ItemSummaryDto에 필요한 컬럼만 추출하는 [DTO로 조회하기] 를 사용 */
        ItemPageDto itemPageDto = itemRepository.searchItems(townId,
                categoryId, itemStatus, sortCriteria.getSpecifier(), pageable);

//        Page<TestDto> pageResult = itemRepository.searchItemSummaryDtos(townId, categoryId, itemStatus, sortCriteria.getSpecifier(), pageable);
//        List<TestDto> content = pageResult.getContent();

        //2_1. 만약 조회한 상품이 하나도 없다면 -> 그에 따른 예외를 터뜨림 (클라이언트는 이 응답 결과를 이용)
        if(CollectionUtils.isEmpty(itemPageDto.getItemList())){
            throw new ApiException(ApiResponseStatus.NO_ITEMLIST, "더이상 등록된 상품이 없습니다.");
        }

        //2_2. 조회한 상품이 하나라도 있다면 - 조회한 Entity들을 Dto로 변환하여 반환
        List<ItemSummaryDto> itemSummaryDtoList = itemPageDto.getItemList().stream()
                .map(i -> ItemSummaryDto.builder()
                        .itemId(i.getId()).title(i.getTitle()).townName(i.getTown().getName()).createdAt(i.getCreatedAt())
                        .price(i.getPrice())
                        .itemImageUrl(fileServiceUtil.getEncryptedPathList(i, sampleDir, aes256).get(0))
                        .numOfWish(i.getWishList().size()).numOfChattingRoomList(i.getChattingRoomList().size()).build())
                .collect(Collectors.toList());

        return ItemSummaryPageDto.builder()
                .itemSummaryDtoList(itemSummaryDtoList)
                .totalCount(itemPageDto.getTotalCount())
                .build();
    }

    /** <특정 Path의 이미지 조회> */
    public InputStreamResource getItemImage(String encryptedPath){

       /** 단 , 복호화 이후 실제 상품과 관련되었는지의 추가검사는 할 필요가 없다 - 매우 희박한 가능성이면서 ,
        *  뚫린다고 해도 어차피 open된 이미지 이니 큰 문제가 되지 x */
        return fileServiceUtil.getImage(encryptedPath, aes256);
    }


    /** *
     * [등록서비스에서 사용되는 유효성 체크 로직]
     */
    private void checkRegister(List<MultipartFile> files, Long categoryId, Long townId, Long memberId){

       //1. 사진 확장자에 대한 유효성 검사
        if(!fileServiceUtil.checkExtension(files)){
            throw new IllegalArgumentException(
                    files.stream()
                            .map(f -> FilenameUtils.getExtension(f.getOriginalFilename())).collect(Collectors.toList()).toString()
            );
        }

        //2. categortyId에 대한 유효성 검사
        if(!categoryRepository.existsById(categoryId)){
            throw new ApiException(ApiResponseStatus.FAIL_REGISTER_ITEM, "categoryId = " + categoryId + " , 유효하지 않은 categoryId 값이 들어왔기 때문에, 상품 등록에 실패했습니다.");
        }

        //3. townId에 대한 유효성 검사 (만약 살제 당근마켓처럼 사용자가 여러 town에 속하게 된다면, 이게 같지 않냐로 비교하는게 아니라 -> 속하지 않는걸로 비교)
        /** 참고로 굳이 townRepository에서 townId가 유효한 아이디 인지 확인할 필요가 없는게 , 어차피 SellerMember의 Town의 ID라면 유효한 값일 것이기 떄문에
         * 먼저 townId가 유효한 아이디 인지를 검사하고 나중에 그 Member의 townId임을 검사한다면 -> 이는 사실상 중복검사다 */
        if(getMember(memberId).getTown().getId() != townId){
            throw new ApiException(ApiResponseStatus.FAIL_REGISTER_ITEM, "townId = " + townId + " , 유효하지 않은 townId 값이 들어왔기 때문에, 상품 등록에 실패했습니다.");
        }

    }

    /**
     * [등록 서비스]
     */
    @Transactional
    public Long register(Long memberId, RegisterItemDto registerItemDto){


        //0. 인자로 넘어온 값들의 유효성 검증
        checkRegister(registerItemDto.getImageList(), registerItemDto.getCategoryId(), registerItemDto.getTownId(), memberId);

        //1. 인자로 넘어온 유효한 값들을 기반으로 Item 엔티티 , ItemImage 엔티티를 생성하여 DB에 먼저 저장 (DB 작업을 먼저 수행해야 함이 핵심)
        Member sellerMember = getMember(memberId);

        Item item = createItem(registerItemDto, sellerMember);
        itemRepository.save(item);


        /**2_1. 이때 함께 등록할 이미지가 하나도 없으면 -> 곧바로 Item 엔티티만 생성한 후 , itemId값을 리턴  */
        if(CollectionUtils.isEmpty(registerItemDto.getImageList())){
            return item.getId();
        }

        /** 2_2.그렇지 않고 함께 등록할 이미지가 하나 이상 존재하면   <항상 DB먼저 수행 후 - File 작업을 수행해야 함>
         * -> 각 이미지들을 저장할 경로를 가진 ItemImage 엔티티들을 DB에 save한 후 -> 실제 그 경로에 각 사진을 저장한다. */

        // (1) 일단 각 이미지들이 저장될 경로를 가진 ItemImage 엔티티를 DB에 save (이때 동시성 문제를 고려하여 AtomicInteger 사용)

        //각 이미지 별로 내부 로컬에 저장할 path를 결정하고
        List<String> pathList = fileServiceUtil.getPathList(fileDir, registerItemDto, sellerMember.getId(), item.getId());

        //그 pathList에 담긴 각 이미지가 저장될 절대경로를 가지고 ItemImage 엔티티를 생성해서 save
        /** 이떄 각각 save하는 것 보다 , saveAll 하는게 트랜젝션상 성능이 더 좋다.*/
        List<ItemImage> itemImageList = createItemImageList(pathList, item);
        itemImageRepository.saveAll(itemImageList);


        // (2) 이후 해당 이미지를 실제로 로컬에 저장
        fileServiceUtil.saveImages(fileDir, registerItemDto, pathList, sellerMember.getId(), item.getId());

        return item.getId();
    }

    private Item createItem(RegisterItemDto registerItemDto, Member sellerMember){
        return Item.builder().title(registerItemDto.getTitle())
                .content(registerItemDto.getContent())
                .price(registerItemDto.getPrice())
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(FOR_SALE)
                .sellerMemberId(sellerMember.getId())
                .categoryId(registerItemDto.getCategoryId())
                .townId(registerItemDto.getTownId())
                .build();
    }

    private List<ItemImage> createItemImageList(List<String> pathList, Item item){
        return pathList.stream()
                .map(p -> ItemImage.builder().path(p).itemId(item.getId()).build())
                .collect(Collectors.toList());
    }

    /**
     * [유효성 체크 로직] : 채팅을 시도한 EXPECTED_BUYER들의 목록을 가져오는 서비스의 검증 로직
     * */
    private void checkGetExpectedBuyers(Long memberId, Long itemId){

        //  결국 그 Item을 올린 판매자가 == username으로 된 Member가 맞는지를 검증

        // 1. 이게 일치하려면 일단 itemId가 진짜 유효한 상품의 id 여야 함은 물론이고
        // 2. 나아가 그 id로 된 Item의 sellerMember가 == username으로 된 Member 여야 한다.
        Item item = itemRepository.findItemById(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 id를 가진 Item은 존재하지 않습니다.");
                }
        );

        if(item.getSellerMember().getId()!=memberId){
            throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 상품이 , 해당 memberId의 Member가 올린 상품이 아닙니다.");
        }
    }


    /**
     * [이 아이템에게 채팅을 시도한 예비 구매자들 목록 조회 서비스]
     * -> 결국 이들중에서만 진짜 구매자가 나오게 되어 있다. (서비스가 실제로 그렇게 동작)
     * */

    public ExpectedBuyerDto getExpectedBuyers(Long memberId, Long itemId){

        //0. 유효성 체크 -> 이를 통과하면 결국 해당 Item은 해당 Member가 올린 상품이라는것이 증명이 됨
        /** 그래야 이 아이템에게 채팅을 요청한 예비 구매자들을 조회 가능하게 됨 (그럴 권한이 주어진다고 판단 가능)*/
        checkGetExpectedBuyers(memberId, itemId);

        //1. 해당 상품에 채팅을 요청한 모든 EXPECTED_BUYER들을 조회

        /** 여기서 무작정 Item과 연관된 ChattingRoomList를 페치조인하면 안됨.
         * 만약 연관된 ChattingRoomList가 없을 경우 , 조회되는 결과는 null이고 , 그에따라 NPE 가 발생함.
         * -> 즉 어쩔 수 없이 LAZY LOADING을 쓸 수 밖에 없는 상황 */
        Item item = itemRepository.findItemById(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 id를 가진 Item은 존재하지 않습니다.");
                }
        );

        //단 이 상품에 대해 채팅을 요청한 사람이 한명도 없으면 , 바로 리턴
        if(CollectionUtils.isEmpty(item.getChattingRoomList())){
            return ExpectedBuyerDto.builder()
                    .numOfExpectedBuyer(0)
                    .expectedBuyerIdList(new ArrayList<>())
                    .itemId(itemId).build();
        }

        //채팅룸이 하나라도 열렸다면 -> 그에 따른 MemberChattingList와 Member도 모두 존재한다는 의미이므로
        //이들을 페치조인으로 한꺼번에 조회한 후 -> 그중엥서 EXPECTED_BUYER들만 필터링하여 리턴한다 (그 ID값만)
        List<Long> idListOfexpectedBuyerMembers = getBuyerIdList(item);

        return ExpectedBuyerDto.builder()
                .numOfExpectedBuyer(idListOfexpectedBuyerMembers.size())
                .expectedBuyerIdList(idListOfexpectedBuyerMembers)
                .itemId(itemId)
                .build();

    }

    /** [해당 Item의 EXPECTED_BUYER들의 IdList를 반환하는 내부 로직]*/
    private List<Long> getBuyerIdList(Item item){
        List<ChattingMember> chattingMemberList = new ArrayList<>();
        //어차피 각 ChattingRoom 별로 연관되 MemberChatting은 2개밖에 없으니까 -> 그냥 일일이 add 해주면 됨
        item.getChattingRoomList().stream()
                .map(cr -> chattingRoomRepository.findOneWithMember(cr.getId()))
                .peek(cr -> chattingMemberList.add(cr.getChattingMemberList().get(0)))
                .forEach(cr -> chattingMemberList.add(cr.getChattingMemberList().get(1)));

        //그렇게 1차원 리스트로 모아진 MemberChattingList에서 -> EXPECTED_BUYER들만으로 필터링 하여 리턴
        return  chattingMemberList.stream()
                .filter(mc -> mc.getRole() == Role.EXPECTED_BUYER)
                .map(mc -> mc.getMember().getId())
                .collect(Collectors.toList());
    }


    /**
     * [판매 상태 변경에 대한 검증 로직]
     *
     * 1. 판매중으로 변경시
     * -> 그 이전 상태가 예약중 or 판매완료 여야 한다
     * -> 그리고 설정되었던 buyerMemberId가 지워져야 한다.
     *
     * 2. 예약중으로 변경시
     * -> 그 이전 상태가 판매중 or 판매완료 여야 한다.
     * -> 또한 채팅을 보냈던 EXPECTED_BUYER들 중 한명으로 예약자를 선택할 수 있다. (그렇지 않다면 예외)
     *
     * 3. 판매완료로 변경시
     * -> 그 이전 상태가 판매중 or 예약중이어야 하고
     * -> 판매중에서 판매 완료로 변경시에는 , 채팅을 보냈던 EXPECTED_BUYER들 중 한명으로만 구매자를 선택할 수 있고
     * -> 만약 예약중에서 판매완료로 변경하는 경우에는 -> 자동으로 판매 완료로 변경할 때 , 예약자로 선택한 사람에게 판매가 완료된다
     *
     * */
    private void checkChangeToFOR_SALE(Long memberId, Item item){

        //결국 itemId가 유효해야 하고 , 그 Item이 이 Member가 올린 상품이어야 한다는 검증 로직이 여기서도 적용되어야 하므로 , 메서드 호출


        if(item.getSellerMember().getId()!=memberId){
            throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 상품이 , 해당 memberId의 Member가 올린 상품이 아닙니다.");
        }

        //이제 username, itemId에 대한 검증은 마쳤으니 , nextSituation에 관련한 검증 시작
        if(item.getItemStatus()==ItemStatus.FOR_SALE){
            throw new ApiException(ApiResponseStatus.INVALID_PREV_SITUATION, "판매중으로 변경시 : 이전 상품 상태가 예약완료 or 판매완료 인 경우에만 , 상품 상태를 판매중으로 변경시킬 수 있습니다.");
        }

    }

    private void checkChangeToRESERVED(Long memberId, Item item, Long buyerMemberId){

        //결국 itemId가 유효해야 하고 , 그 Item이 이 Member가 올린 상품이어야 한다는 검증 로직이 여기서도 적용되어야 하므로
        //  결국 그 Item을 올린 판매자가 == username으로 된 Member가 맞는지를 검증

        // 1. 이게 일치하려면 일단 itemId가 진짜 유효한 상품의 id 여야 함은 물론이고
        // 2. 나아가 그 id로 된 Item의 sellerMember가 == username으로 된 Member 여야 한다.


        if(item.getSellerMember().getId()!=memberId){
            throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 상품이 , 해당 memberId의 Member가 올린 상품이 아닙니다.");
        }

        //이제 username, itemId에 대한 검증은 마쳤으니 , nextSituation에 관련한 검증 시작
        if(item.getItemStatus()== RESERVED){
            throw new ApiException(ApiResponseStatus.INVALID_PREV_SITUATION, "예약중으로 변경시 : 이전 상품 상태가 판매중 or 판매완료 인 경우에만 , 상품 상태를 예약중으로 변경시킬 수 있습니다.");
        }

        //마지막으로 예약하고자 하는 Member의 Id값은 , 채팅을 보낸 EXPECTED_BUYERS들의 Id 중 하나여야 한다
        if(CollectionUtils.containsAny(getBuyerIdList(item), buyerMemberId)==false){
            throw new ApiException(ApiResponseStatus.INVALID_RESERVE_MEMBER, "예약중으로 변경시 : 예약할 수 있는 사용자는, 해당 상품의 상세페이지에서 채팅을 시도한 사람으로 한정됩니다.");
        }

    }

    private void checkChangeToSOLD_OUT(Long memberId, Item item, Long buyerMemberId){

        //결국 itemId가 유효해야 하고 , 그 Item이 이 Member가 올린 상품이어야 한다는 검증 로직이 여기서도 적용되어야 하므로
        //  결국 그 Item을 올린 판매자가 == username으로 된 Member가 맞는지를 검증

        // 1. 이게 일치하려면 일단 itemId가 진짜 유효한 상품의 id 여야 함은 물론이고
        // 2. 나아가 그 id로 된 Item의 sellerMember가 == username으로 된 Member 여야 한다.

        if(item.getSellerMember().getId()!=memberId){
            throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 상품이 , 해당 memberId의 Member가 올린 상품이 아닙니다.");
        }

        //이제 username, itemId에 대한 검증은 마쳤으니 , nextSituation에 관련한 검증 시작
        if(item.getItemStatus()==SOLD_OUT){
            throw new ApiException(ApiResponseStatus.INVALID_PREV_SITUATION, "판매완료로 변경시 : 이전 상품 상태가 판매중 or 예약중 인 경우에만 , 상품 상태를 판매완료로 변경시킬 수 있습니다.");
        }

        //마지막으로 예약하고자 하는 Member의 Id값은 ,
        // 예약중에서 -> 판매완료로 변경하는 경우는 , 그 Member가 같아야 하고 (즉 같지 않으면 예외)
        // 판매중에서 -> 거래완료로 변경하는 경우는 , 채팅을 건 EXPECTED_BUYERS 들 중에 하나여야 한다. (즉 이들중 한명이 아니면 예외)
        else if(item.getItemStatus()==RESERVED && buyerMemberId!=item.getBuyer_member_id()){
            throw new ApiException(ApiResponseStatus.FAIL_CHANGE_TO_SOLD_OUT, "판매완료로 변경시 : 예약중에서 변경시에는 예약한 그 사용자에게만 자동으로 상품이 판매됩니다. 별도의 판매자를 선택할수 없습니다. ");
        }
        else if(item.getItemStatus()==FOR_SALE && !CollectionUtils.containsAny(getBuyerIdList(item), buyerMemberId)){
            throw new ApiException(ApiResponseStatus.FAIL_CHANGE_TO_SOLD_OUT, "판매완료로 변경시 : 판매중에서 변경시에는, 채팅을 시도한 EXPECTED_BUYER들 중 한명에게만 상품을 판매할 수 있습니다.");
        }
    }

    /**
     * [판매 상태를 변경시키는 서비스]
     * */
    @Transactional
    public ChangedSituationDto changeToFOR_SALE(Long memberId, Long itemId){

        //0. 검증 로직
        Item item = itemRepository.findItemById(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 id를 가진 Item은 존재하지 않습니다.");
                }
        );

        checkChangeToFOR_SALE(memberId, item);

        //1. 상품 상태를 판매중으로 변경하고 && buyerMemberId 값을 null로 비워줌
        item.changeItemStatus(FOR_SALE);
        item.changeBuyerMemberId(null);

        return ChangedSituationDto.builder()
                .changedItemStatus(FOR_SALE)
                .buyerMemberId(null)
                .changedItemId(itemId)
                .build();
    }

    @Transactional
    public ChangedSituationDto changeToRESERVED(Long memberId, Long itemId, Long buyerMemberId){

        //0. 검증 로직
        Item item = itemRepository.findItemById(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 id를 가진 Item은 존재하지 않습니다.");
                }
        );
        checkChangeToRESERVED(memberId, item, buyerMemberId);

        //1. 상품 상태를 예약중으로 변경하고 && buyerMemberId 값을 넘어온 값으로 설정해줌
        // 어차피 위 검증로직에서 , 이 Item은 이 username의 사용자가 올린게 맞고
        // 또한 예약자도 EXPECTED_BUYERS 들 중 하나라는게 검증되었으니!
        item.changeItemStatus(RESERVED);
        item.changeBuyerMemberId(buyerMemberId);

        return ChangedSituationDto.builder()
                .changedItemStatus(RESERVED)
                .buyerMemberId(buyerMemberId)
                .changedItemId(itemId)
                .build();
    }

    @Transactional
    public ChangedSituationDto changeToSOLD_OUT(Long memberId, Long itemId, Long buyerMemberId){

        //0. 검증 로직
        Item item = itemRepository.findItemById(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_ITEM_ID, "EXPECTED_BUYERS 조회시 : 해당 id를 가진 Item은 존재하지 않습니다.");
                }
        );
        checkChangeToSOLD_OUT(memberId, item, buyerMemberId);

        //1. 상품 상태를 예약중으로 변경하고 && buyerMemberId 값을 넘어온 값으로 설정해줌
        item.changeItemStatus(SOLD_OUT);
        item.changeBuyerMemberId(buyerMemberId);

        return ChangedSituationDto.builder()
                .changedItemStatus(SOLD_OUT)
                .buyerMemberId(buyerMemberId)
                .changedItemId(itemId)
                .build();
    }







}
