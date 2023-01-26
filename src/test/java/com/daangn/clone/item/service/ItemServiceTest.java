package com.daangn.clone.item.service;

import com.daangn.clone.category.Category;
import com.daangn.clone.category.repository.CategoryRepository;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.chatting.chattingroom.repository.ChattingRoomRepository;
import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.common.enums.Role;
import com.daangn.clone.common.enums.Status;
import com.daangn.clone.encryption.AES256;
import com.daangn.clone.file.FileServiceUtil;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.ChangedSituationDto;
import com.daangn.clone.item.dto.ExpectedBuyerDto;
import com.daangn.clone.item.dto.ItemDto;
import com.daangn.clone.item.dto.RegisterItemDto;
import com.daangn.clone.item.dto.paging.ItemPageDto;
import com.daangn.clone.item.dto.paging.ItemSummaryPageDto;
import com.daangn.clone.item.dto.paging.SortCriteria;
import com.daangn.clone.item.repository.ItemRepository;
import com.daangn.clone.itemimage.repository.ItemImageRepository;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.town.Town;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemImageRepository itemImageRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ChattingRoomRepository chattingRoomRepository;
    @Mock
    private FileServiceUtil fileServiceUtil;
    @Mock
    private AES256 aes256;

    @Value("${sample.dir}")
    private String sampleDir;

    /**
     * [API. 7_1]: 상품 이미지가 하나도 없는, 상품의 등록
     * => itemRepository.save()가 실제로 동작하지 않기 떄문에 - item의 Id 값이 setting되지 않고 - 그래서 register() 리턴값으로 null이 나옴
     * */
    @Test
    void 상품이미지가없는_상품의등록() {
        //given

        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();



        when(fileServiceUtil.checkExtension(any())).thenReturn(true);
        when(categoryRepository.existsById(32L)).thenReturn(true);
        when(memberRepository.findById(49L).orElseThrow(any())).thenReturn(sellerMember);


        //when
        RegisterItemDto registerItemDto = RegisterItemDto.builder()
                .title("상품 판매 제목")
                .content("상품 판매 내용")
                .price(10000L)
                .townId(28L)
                .categoryId(32L)
                .imageList(new ArrayList<>())
                .build();

        Long registerItemId = itemService.register(49L, registerItemDto);

        //then
        assertEquals(50L, registerItemId);
    }

    /**
     * [API. 7_2] : 상품 이미지가 함께 있는 상의 등록
     * -> MulipartFile을 어떻게 생성해줘야 할지 모르겠음.
     * */

    @Test
    void 상품이미지가있는_상품의등록() throws Exception{
        //given

        //when


        //then

    }

    /**
     * [API. 8] : 특정 상품 조회
     * */
    @Test
    void api8() throws Exception{

        //given
        Town town28 = new Town("서울특별시_동작구_대방동");
        Category category2 = new Category("디지털기기");

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        Long id = 50L;
        when(itemRepository.findItemById(id)).thenReturn(Optional.ofNullable(item));


        List<String> encrpytedPathList = List.of("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D");
        when(fileServiceUtil.getEncryptedPathList(item, sampleDir, aes256)).thenReturn(encrpytedPathList);

        //when
        ItemDto findItemDto = itemService.getItem(50L);

        //then
        assertAll(
                () -> assertEquals(50L, findItemDto.getItemeId()),
                () -> assertEquals("아이폰 12 팔아요", findItemDto.getTitle()),
                () -> assertEquals("아이폰 12 팔아요" , findItemDto.getContent()),
                () -> assertEquals(500000L , findItemDto.getPrice()),
                () -> assertEquals(DelYn.N , findItemDto.getDelYn()),
                () -> assertEquals(ItemStatus.FOR_SALE , findItemDto.getItemStatus()),
                () -> assertEquals("디지털기기" , findItemDto.getCategoryName()),
                () -> assertEquals("서울특별시_동작구_대방동" , findItemDto.getTownName()),
                () -> assertEquals("nickname1" , findItemDto.getSellerMemberName()),
                () -> assertEquals(encrpytedPathList , findItemDto.getItemImagePathList()),
                () -> assertEquals(0, findItemDto.getNumOfWish()),
                () -> assertEquals(0, findItemDto.getNumOfChattingRoom()),
                () -> assertEquals("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D" , findItemDto.getItemImagePathList().get(0))
        );
    }


    /**
     * [API 9] : 상품 이미지 조회
     * -> 어떻게 TC를 짜야 할지 잘 모르겠음
     * */
    @Test
    void getItemImage() throws Exception{
        //given
//        when(aes256.decrypt("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D")).thenReturn("/Users/lupi./Desktop/carrot/image/2023_1_9/member49_item50/image1.jpeg");
//        when(fileServiceUtil.getImage("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D", aes256))
//                .thenReturn( new InputStreamResource(new FileInputStream("/Users/lupi./Desktop/carrot/image/2023_1_9/member49_item50/image1.jpeg")));
//        //when
//        InputStreamResource image = itemService.getItemImage("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D");

        //then



    }


    /**
     * [API 10] : 최신 상품 목록 조회 (단 조회할 상품 목록이 1개인 상황임)
     * */
    @Test
    void api10() throws Exception{
        //given
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        when(memberRepository.findById(49L).orElseThrow() ).thenReturn(sellerMember);

        List<Item> itemList = List.of(item);
        ItemPageDto itemPageDto = ItemPageDto.builder().itemList(itemList).totalCount(1).build();
        when(itemRepository.searchItems(28L, 32L, ItemStatus.FOR_SALE, SortCriteria.MIN_PRICE.getSpecifier(), PageRequest.of(0, 5)))
                .thenReturn(itemPageDto);

        List<String> pathList = List.of("XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D");
        when(fileServiceUtil.getEncryptedPathList(item, sampleDir, aes256)).thenReturn(pathList);

        //when
        ItemSummaryPageDto summaryPageDto = itemService.getItemList(49L, 28L, 32L, ItemStatus.FOR_SALE, SortCriteria.MIN_PRICE, PageRequest.of(0, 5));

        //then
        assertAll(
                () -> assertEquals(1, summaryPageDto.getTotalCount()),
                () -> assertEquals(50L, summaryPageDto.getItemSummaryDtoList().get(0).getItemId()),
                () -> assertEquals("아이폰 12 팔아요", summaryPageDto.getItemSummaryDtoList().get(0).getTitle()),
                () -> assertEquals("서울특별시_동작구_대방동", summaryPageDto.getItemSummaryDtoList().get(0).getTownName()),
                () -> assertEquals(500000L, summaryPageDto.getItemSummaryDtoList().get(0).getPrice()),
                () -> assertEquals( "XKcgIm1Epdg1RL5IAhFs4U5RWaAb0BcCqWVMnvSN%2BXYzNB9jY097eZGtbuSnW%2F4xJLB1jz0T32Swb3HQ5IgDYmdujzHZr9l%2FBwfrzkqr9xQ%3D", summaryPageDto.getItemSummaryDtoList().get(0).getItemImageUrl()),
                () -> assertEquals(0, summaryPageDto.getItemSummaryDtoList().get(0).getNumOfWish()),
                () -> assertEquals(0, summaryPageDto.getItemSummaryDtoList().get(0).getNumOfChattingRoomList())
        );
    }

    /**
     * [API. 11] : 해당 상품에 대해 채팅을 요청한 예비 구매자들 목록 조회
     * */
    @Test
    void api11() throws Exception{
        //given
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .chattingMemberList(new ArrayList<>())
                .build();

        item.getChattingRoomList().add(chattingRoom);

        ChattingMember buyer = ChattingMember.builder()
                .id(54L)
                .role(Role.EXPECTED_BUYER)
                .member(buyerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(0L)
                .build();

        ChattingMember seller = ChattingMember.builder()
                .id(55L)
                .role(Role.SELLER)
                .member(sellerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(56L)
                .build();


        when(itemRepository.findItemById(50L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findOne(50L)).thenReturn(item);

        chattingRoom.getChattingMemberList().add(seller);
        chattingRoom.getChattingMemberList().add(buyer);
        when(chattingRoomRepository.findOneWithMember(53L)).thenReturn(chattingRoom);


        //when
        ExpectedBuyerDto expectedBuyerDto = itemService.getExpectedBuyers(49L, 50L);

        //then
        assertAll(
                () -> assertEquals(1, expectedBuyerDto.getNumOfExpectedBuyer()),
                () -> assertEquals(52L, expectedBuyerDto.getExpectedBuyerIdList().get(0)),
                () -> assertEquals(50L, expectedBuyerDto.getItemId())
        );

    }

    /**
     * [API 12] : 상품의 ItemStatus 변경
     * */
    @Test
    void api12_FOR_SALE() throws Exception{
        //given
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        item.setItemStatus(ItemStatus.RESERVED);

        when(itemRepository.findItemById(50L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findOne(50L)).thenReturn(item);
        when(itemRepository.findOne(50L)).thenReturn(item);

        //when
        ChangedSituationDto changedSituationDto = itemService.changeToFOR_SALE(49L, 50L);

        //then
        assertAll(
                () -> assertEquals(ItemStatus.FOR_SALE, changedSituationDto.getChangedItemStatus()),
                () -> assertEquals(50L, changedSituationDto.getChangedItemId()),
                () -> assertEquals(null, changedSituationDto.getBuyerMemberId())
        );

    }

    @Test
    void api12_RESERVED() throws Exception{
        //given
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .chattingMemberList(new ArrayList<>())
                .build();

        ChattingMember buyer = ChattingMember.builder()
                .id(54L)
                .role(Role.EXPECTED_BUYER)
                .member(buyerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(0L)
                .build();

        ChattingMember seller = ChattingMember.builder()
                .id(55L)
                .role(Role.SELLER)
                .member(sellerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(56L)
                .build();

        chattingRoom.getChattingMemberList().add(seller);
        chattingRoom.getChattingMemberList().add(buyer);
        item.getChattingRoomList().add(chattingRoom);


        when(itemRepository.findItemById(50L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findOne(50L)).thenReturn(item);
        when(chattingRoomRepository.findOneWithMember(53L)).thenReturn(chattingRoom);

        //when
        ChangedSituationDto changedSituationDto = itemService.changeToRESERVED(49L, 50L, 52L);

        //then
        assertAll(
                () -> assertEquals(ItemStatus.RESERVED, changedSituationDto.getChangedItemStatus()),
                () -> assertEquals(50L, changedSituationDto.getChangedItemId()),
                () -> assertEquals(52L, changedSituationDto.getBuyerMemberId())
        );

    }

    @Test
    void api12_SOLD_OUT_FROM_FOR_SALE() throws Exception{
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .chattingMemberList(new ArrayList<>())
                .build();

        ChattingMember buyer = ChattingMember.builder()
                .id(54L)
                .role(Role.EXPECTED_BUYER)
                .member(buyerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(0L)
                .build();

        ChattingMember seller = ChattingMember.builder()
                .id(55L)
                .role(Role.SELLER)
                .member(sellerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(56L)
                .build();



        chattingRoom.getChattingMemberList().add(seller);
        chattingRoom.getChattingMemberList().add(buyer);
        item.getChattingRoomList().add(chattingRoom);


        when(itemRepository.findItemById(50L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findOne(50L)).thenReturn(item);
        when(chattingRoomRepository.findOneWithMember(53L)).thenReturn(chattingRoom);

        //when
        ChangedSituationDto changedSituationDto = itemService.changeToSOLD_OUT(49L, 50L, 52L);

        //then
        assertAll(
                () -> assertEquals(ItemStatus.SOLD_OUT, changedSituationDto.getChangedItemStatus()),
                () -> assertEquals(50L, changedSituationDto.getChangedItemId()),
                () -> assertEquals(52L, changedSituationDto.getBuyerMemberId())
        );
    }

    @Test
    void api12_SOLD_OUT_FROM_RESERVED() throws Exception{
        Town town28 = Town.builder().id(28L).name("서울특별시_동작구_대방동").build();
        Category category2 = Category.builder().id(32L).name("디지털기기").build();

        Member sellerMember = Member.builder()
                .id(49L)
                .username("sample1")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname1")
                .town(town28)
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        Item item = Item.builder()
                .id(50L)
                .title("아이폰 12 팔아요")
                .content("아이폰 12 팔아요")
                .price(500000L)
                .visitCount(0)
                .delYn(DelYn.N)
                .itemStatus(ItemStatus.FOR_SALE)
                .sellerMember(sellerMember)
                .category(category2)
                .town(town28)
                .wishList(new ArrayList<>())
                .chattingRoomList(new ArrayList<>())
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .chattingMemberList(new ArrayList<>())
                .build();

        ChattingMember buyer = ChattingMember.builder()
                .id(54L)
                .role(Role.EXPECTED_BUYER)
                .member(buyerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(0L)
                .build();

        ChattingMember seller = ChattingMember.builder()
                .id(55L)
                .role(Role.SELLER)
                .member(sellerMember)
                .chattingRoom(chattingRoom)
                .lastReadContentId(56L)
                .build();

        item.changeItemStatus(ItemStatus.RESERVED);
        item.setBuyer_member_id(52L);
        chattingRoom.getChattingMemberList().add(seller);
        chattingRoom.getChattingMemberList().add(buyer);
        item.getChattingRoomList().add(chattingRoom);


        when(itemRepository.findItemById(50L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findOne(50L)).thenReturn(item);
//        when(chattingRoomRepository.findOneWithMember(53L)).thenReturn(chattingRoom);

        //when
        ChangedSituationDto changedSituationDto = itemService.changeToSOLD_OUT(49L, 50L, 52L);

        //then
        assertAll(
                () -> assertEquals(ItemStatus.SOLD_OUT, changedSituationDto.getChangedItemStatus()),
                () -> assertEquals(50L, changedSituationDto.getChangedItemId()),
                () -> assertEquals(52L, changedSituationDto.getBuyerMemberId())
        );
    }
}
