package com.daangn.clone.chatting.chattingroom.service;

import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.chatting.chattingroom.repository.ChattingRoomRepository;
import com.daangn.clone.chatting.dto.ChattingDto;
import com.daangn.clone.chatting.dto.ChattingListDto;
import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import com.daangn.clone.chatting.chattingcontent.repository.ChattingContentRepository;

import com.daangn.clone.chatting.dto.last_read.LastReadDto;
import com.daangn.clone.chatting.dto.ChattingContentDto;
import com.daangn.clone.chatting.dto.receive.ReceiveDto;
import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.repository.ItemRepository;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.chatting.chattingmember.repository.ChattingMemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static com.daangn.clone.common.enums.DelYn.Y;
import static com.daangn.clone.common.enums.ItemStatus.RESERVED;
import static com.daangn.clone.common.enums.ItemStatus.SOLD_OUT;
import static com.daangn.clone.common.enums.Role.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChattingRoomService {

    private final ItemRepository itemRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingContentRepository chattingContentRepository;
    private final ChattingMemberRepository chattingMemberRepository;
    private final MemberRepository memberRepository;

    private Item getItemWithSellerMember(Long itemId){
        return itemRepository.findOneWithSellerMember(itemId).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.FAIL_CREATE_CHATTING_ROOM, "채팅을 하고자 하는 판매자의 상품이 존재하지 않습니다.");
                }
        );
    }

    private void checkAvaliableChatting(Item item){
        /** 함수로 추출 -> 1. 채팅을 못하는 상황을 따로 함수로 추출 for 가독성 */
        if(item.getDelYn()==Y || item.getItemStatus()==SOLD_OUT || item.getItemStatus()==RESERVED){
            throw new ApiException(ApiResponseStatus.FAIL_CREATE_CHATTING_ROOM, "채팅을 하고자 하는 판매자의 상품이 삭제되었거나 or 이미 예약되었거나 or 판매된 상품입니다.");
        }
    }

    private void checkCorrectBuyer(Long buyerMemberId, Item item){

        //2. 그 뒤 혹시 모를 상황을 대비해 , 채팅을 요청하는 쪽과 - 판매자가 같은 Member인지를 판벌햐는 로직을 수행함 (이걸 검사하는게 맞는지 질문)
        if(item.getSellerMember().getId().equals(buyerMemberId)){
            throw new ApiException(ApiResponseStatus.FAIL_CREATE_CHATTING_ROOM, "채팅하고자 하는 판매자와, 구매하려는 사용자가 같을 수는 없습니다.");
        }
    }

    /** 유효성 검사 메소드 */
    private void validateBindChatting(Long buyerMemberId, Item item){

        //1. 이 상품이 채팅을 보낼 수 있는 상태인지 확인하고
        checkAvaliableChatting(item);

        //2. 나아가 채팅을 보내는 구매자와 , 상품을 올린 판매자가 서로 같은 사람은 아닌지 check
        // (왜냐하면 자기가 자기한테 채팅을 보내는건 막아야 하니깐)
        checkCorrectBuyer(buyerMemberId, item);
    }


    // make 대신에 get이란 용어 사용
    private ChattingDto getChattingDto(Long buyerMemberId, Long sellerMemberId, Long itemId, Long chattingRoomId, Long buyerId, Long sellerId){

        return ChattingDto.builder()
                .memberId(buyerMemberId)
                .targetMemberId(sellerMemberId)
                .itemId(itemId)
                .chattingRoomId(chattingRoomId)
                .chattingMemberId(buyerId)
                .targetChattingMemberId(sellerId)
                .build();
    }

    private ChattingDto createChatting(Long itemId, Long sellerMemberId, Long buyerMemberId){

        //2_1. ChattingRoom 생성
        ChattingRoom chattingRoom = ChattingRoom.builder()
                .status(Status.ACTIVE)
                .itemId(itemId)
                .build();
        chattingRoomRepository.save(chattingRoom);

        //2_2. ChattingContent 생성은 하지 않음 -> 왜냐하면 ChattingContent는 사실상 각각의 메세지 단위의 row 이므로 ,
        // 미리 생성한다고 별 의미가 없음

        //2_3. ChattingMember 생성 - 이때 요청한 Member , 즉 Buyer와 / 상품 판매자인 Seller 모두에 대해서 생성해줘야 함
        ChattingMember buyer = ChattingMember.builder()
                .role(EXPECTED_BUYER)
                .memberId(buyerMemberId)
                .chattingRoomId(chattingRoom.getId())
                .lastReadContentId(0L)
                .build();

        ChattingMember seller = ChattingMember.builder()
                .role(SELLER)
                .memberId(sellerMemberId)
                .chattingRoomId(chattingRoom.getId())
                .lastReadContentId(0L)
                .build();

        chattingMemberRepository.save(buyer);
        chattingMemberRepository.save(seller);

        chattingRoom.setSellerMemberId(sellerMemberId);
        chattingRoom.setBuyerMemberId(buyerMemberId);

        return getChattingDto(buyerMemberId, sellerMemberId, itemId,
                chattingRoom.getId(), buyer.getId(), seller.getId());
    }

    /** 이 비지니스 로직은, 무조건 Buyer 만의 입장에서 -> 상품을 보고 구매자가 판매자에게 채팅을 요청하면 -> 그 요청한 채팅을 설정해주는 서비스
     * (모든 채팅은 어쨌든 , Buyer가 Seller에게 채팅을 요청하므로 써 시작된다 -> so 이 비지니스 로직에서 채팅에 관한 첫 설정을 모두 마쳐야 한다) */
    @Transactional
    public ChattingDto bindChatting(Long memberId, Long itemId){

        //0. 유효성 검사 -> 유효한 값이라는건 , 이미 존재하는 값을 대상으로 유효성을 검사하는것이 논리적으로 맞으므로
        // 일단 존재하는 Item을 조회한 후 -> 그 Item을 넘겨 , 그 Item의 유효성을 검사해야 한다.
        Item item = getItemWithSellerMember(itemId);
        validateBindChatting(memberId, item);

        Member buyerMember = memberRepository.findOne(memberId);

        /**
         *
         * (1) 해당 item에 대해 , 해당 memberId의 EXPECTED BUYER가 한번도 채팅을 요청하지 않았다면
         *          * -> 채팅과 관련된 엔티티들을 생성해서 save 해줘야 하고
         *
         * (2) 해당 item에 대해, 해당 memberId의 EXPECTED BUYER가 이미 채팅을 요청했다면
         *          * -> 그 채팅과 관련된 엔티티들을 조회해여 DTO로 변환해서 응답으로 리턴해야 한다.
         * */

        /** ChattingRoom에 sellerMemberId와 buyerMemberId를 두는 방향으로 역정규화를 해서, 특정 ChattingRoom 만을 조회 가능!! */
        Optional<ChattingRoom> chattingRoom = chattingRoomRepository.findByItemIdAndBuyerMemberId(itemId, buyerMember.getId());

        /** (1) 채팅을 요청하지 않은 경우 (해당 아이템에 대해 채팅방이 처음 생성되거나 or 그 EXPECTED_BUYER에 대한 채팅방만 처음이거나) */
        if(chattingRoom.isEmpty()){
            return createChatting(itemId, item.getSellerMember().getId(), buyerMember.getId());
        }

        /** (2) 기존에 채팅을 요청했던 경우 */

        ChattingMember buyer = chattingMemberRepository.findByChattingRoomIdAndMemberId(chattingRoom.get().getId(), chattingRoom.get().getBuyerMemberId());
        ChattingMember seller = chattingMemberRepository.findByChattingRoomIdAndMemberId(chattingRoom.get().getId(), chattingRoom.get().getSellerMemberId());
        return getChattingDto(buyerMember.getId(), item.getSellerMember().getId(), itemId,
                chattingRoom.get().getId(), buyer.getId(), seller.getId());

    }

    /** ---------------------------------------------------------------------------------------------------------*/

    /** 채팅 탭에서 , 이 사용자가 { BUYER로써 + SELLER로써 } 과거에 연결되었던 유효한 모든 채팅 정보를 조회하여 반환한다 */
    public ChattingListDto getChattingList(Long memberId){

        //0. 만약 해당 memberId의 Member가 아직 한번도 채팅을 시도하지 않았다면 (SELLER 로든 BUYER로든) 빈 응답을 반환해야 함
        if(isAnyChattingRoom(memberId, memberId)){
            return ChattingListDto.builder().chattingDtoList(new ArrayList<>()).sizeOfChatting(0).build();
        }


        /**1. ChattingRoom에 역정규화 한 sellerMemberId와 buyerMemberId를 이용해서 -> 이들을 OR조건으로 묶어, 이 Member가
          * Seller로도 or Buyer로도 참여한 ChattingRoom을 한꺼번에 초회
          * 동시에 chattingRoom의 createdAt을 기준으로 내림차순 정렬
          */

        List<ChattingDto> chattingDtoList = new ArrayList<>();
        List<ChattingRoom> chattingRoomList = chattingRoomRepository
                                                .findBySellerMemberIdOrBuyerMemberId(memberId, memberId);

        if(CollectionUtils.isNotEmpty(chattingRoomList)){
             chattingDtoList = chattingRoomList.stream()
                    .map(cr -> ChattingDto.builder().memberId(memberId)
                            .targetMemberId(cr.getSellerMemberId().equals(memberId) ? cr.getBuyerMemberId() : cr.getSellerMemberId())
                            .itemId(cr.getItem().getId())
                            .chattingRoomId(cr.getId())
                            .chattingMemberId(chattingMemberRepository.findByChattingRoomIdAndMemberId(cr.getId(), memberId).getId())
                            .targetChattingMemberId(
                                    chattingMemberRepository.findByChattingRoomIdAndMemberId(cr.getId(), cr.getSellerMemberId().equals(memberId) ? cr.getBuyerMemberId() : cr.getSellerMemberId()).getId()
                            )
                            .build())
                    .collect(Collectors.toList());
        }



        /** 3. 해당 List<ChattingDto> 정보를 ChattingListDto로 감싸서 반환 */
        return ChattingListDto.builder()
                .sizeOfChatting(chattingDtoList.size())
                .chattingDtoList(chattingDtoList)
                .build();


    }

    private boolean isAnyChattingRoom(Long sellerMemberId, Long buyerMemberId){
        return ((!chattingRoomRepository.existsBySellerMemberId(sellerMemberId)) && (!chattingRoomRepository.existsByBuyerMemberId(buyerMemberId)));
    }






    /**------------------------------------------------------------------------------------------------------*/

    /** [메세지 전송 서비스] */
    @Transactional
    public ChattingContentDto sendMessage(Long memberId, Long chattingRoomId, String content){

        //1. 유효성 검사
        Member sendMember = memberRepository.findOne(memberId);

        // 1_1. 해당 senderMember와 내가 참여하고 있는 해당 chattingRoom이 정발로 존재하는지
        // "즉 보낼 그 SenderMember가 그 ChattingRoom에 속한게 맞는지!"
        // (내 API 구현 논리상, 그리고 ERD 논리상 , 이 한방만 검사해주면 됨)
        existRoomWithMember(chattingRoomId, sendMember.getId());


        //2. 넘어온 정보를 기반으로 ChattingContent 엔티티를 생성하여 insert -> 이 자 체가 곧 메세지 전송 역할
        ChattingContent chattingContent = ChattingContent.builder()
                .chattingRoomId(chattingRoomId)
                .senderMemberId(sendMember.getId())
                .content(content)
                .build();
        chattingContentRepository.save(chattingContent);

        //3. 내가 보낸 메세지 까지 내가 읽었다고 update (결과적으로 내 메세지든 , 니가 보낸 메세지든 , 나는 어디까지 읽었는지만을 check! )
        ChattingMember chattingMember = chattingMemberRepository.findByChattingRoomIdAndMemberId(
                                                                    chattingRoomId, sendMember.getId());
        chattingMember.setLastReadContentId(chattingContent.getId());

        //4. 내가 보낸 메세지에 대한 정보를 리턴
        return ChattingContentDto.builder()
                .chattingContentId(chattingContent.getId())
                .chattingRoomId(chattingContent.getChattingRoomId())
                .content(chattingContent.getContent())
                .senderMemberId(sendMember.getId())
                .createdAt(chattingContent.getCreatedAt())
                .build();
    }


    private void existRoomWithMember(Long chattingRoomId, Long memberId){
        if(!chattingMemberRepository.existsByChattingRoomIdAndMemberId(chattingRoomId, memberId)){
            throw new ApiException(ApiResponseStatus.INVALID_SEND_MESSAGE, "메세지 전송 또는 수신 시점 : 그런 ID를 가진 ChattingRoom  또는 그런 ChattingRoom에 참여하고 있는 targetMember는 존재하지 않습니다.");
        }
    }



    /**------------------------------------------------------------------------------- ----------------------*/

    /** [메세지 수신 서비스] */
    @Transactional
    public List<ChattingContentDto> receiveMessage(Long memberId, ReceiveDto receiveDto){

        Member receiverMember = memberRepository.findOne(memberId);

        //1. 해당 receiverMember가 참여하고 있는 해당 chattingRoom이 정발로 존재하는지
        // (내 API 구현 논리상, 그리고 ERD 논리상 , 이 한방만 검사해주면 됨)
        existRoomWithMember(receiveDto.getChattingRoomId(), receiverMember.getId());

        //2_1. 요청에서 함께 넘어온 contentId 이후에 , "그 ChattingRoom에" 온 content가 있는지?
        // 새로운 메세지가 오지 않은것 이므로 -> 그에 따른 응답을 보내고 끝 (빈 리스트 반환)

        if(!chattingContentRepository.existsByChattingRoomIdAndIdAfter(
                receiveDto.getChattingRoomId(), receiveDto.getLastReadContentId())){
            return new ArrayList<>();
        }

        //2_2. 그렇지 않다면 , 안읽은 새로운 메세지가 왔다는 것 이므로 -> 그 메세지들을 조회하여 오름차순 정렬하고 보내주면 됨
        // 심지어 아직 읽은 메세지가 없어서 lastReadContentId가 0 이면 -> 0보다 큰 content가 조회되어 리턴된다! (페이징 처리 수행)
        List<ChattingContentDto> notReadContentList = chattingContentRepository.findNotReadMessage(
                receiveDto.getChattingRoomId(), receiveDto.getLastReadContentId(),  receiveDto.getLimit())
                .stream().map(cc -> ChattingContentDto.builder()
                        .chattingContentId(cc.getId())
                        .chattingRoomId(cc.getChattingRoom().getId())
                        .senderMemberId(cc.getSenderMemberId())
                        .content(cc.getContent())
                        .createdAt(cc.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());


        //3. 이후 notReadContentList의 가장 마지막 Id로 , lastReadContentId를 udpate
        /** (주의할 점) limit을 20으로 설정해도 20개가 없으면 20개가 넘어오질 못하니 , 실제 넘어온 거에 마지막 contentId로 update 쳐야 함
         * 근데 , DB에 저장된 lastReadContentId 보다 이전 값이라면 , update 치면 안됨  <- 조건문으로 들어가 줘야 함 */
        ChattingMember receiverChattingMember = chattingMemberRepository.findByChattingRoomIdAndMemberId(receiveDto.getChattingRoomId(), receiverMember.getId());
        if(notReadContentList.get(notReadContentList.size()-1).getChattingContentId() > receiverChattingMember.getLastReadContentId()) {
            receiverChattingMember.setLastReadContentId(notReadContentList.get(notReadContentList.size() - 1).getChattingContentId());
        }

        //4. 응답 리턴
        return notReadContentList;

    }
    /**------------------------------------------------------------------------------------------------------*/

    /**------------------------------------------------------------------------------------------------------*/

    /** [특정 채팅방에서 , 상대방이 마지막으로 읽은 content의 Id를 알려주는 서비스]
     * => 이를 통해 상대방이 어디까지 수신 확인했는지 알 수 있음
      */
    public LastReadDto getLastReadContentId(Long memberId, Long chattingRoomId){

        //1. 일단 나는 그 ChattingRoom에 소속된 것이 맞는지 검사
        existRoomWithMember(chattingRoomId, memberId);

        //2. 넘어온 정보를 기반으로 상대방 ChattingMember를 조회
        ChattingRoom chattingRoom = chattingRoomRepository.findOne(chattingRoomId);
        ChattingMember targetChattingMember = chattingMemberRepository
                                                .findByChattingRoomIdAndMemberId(chattingRoomId,
                                                        chattingRoom.getSellerMemberId().equals(memberId) ? chattingRoom.getBuyerMemberId() : chattingRoom.getSellerMemberId());

        //3_1. 아직 읽은 메세지가 없다면 0을 리턴 or 값이 있다면 있는 값을 리턴
        /** 이때 어차피 ChattingMember의 경우 디폴트로 lastReadContentId를 0L로 초기화 시켰으므로 , 그냥 그 값을 가져오기만 하면 됨 */
        LastReadDto lastReadDto = LastReadDto.builder()
                                    .lastReadContentId(targetChattingMember.getLastReadContentId())
                                    .build();


        //4. 값이 setting된 LastReadDto를 리턴
        return lastReadDto;

    }

    /**------------------------------------------------------------------------------------------------------*/




}
