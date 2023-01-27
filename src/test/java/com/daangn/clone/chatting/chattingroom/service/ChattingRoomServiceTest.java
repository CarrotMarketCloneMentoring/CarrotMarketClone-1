package com.daangn.clone.chatting.chattingroom.service;

import com.daangn.clone.category.Category;
import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import com.daangn.clone.chatting.chattingcontent.repository.ChattingContentRepository;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.chatting.chattingmember.repository.ChattingMemberRepository;
import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.chatting.chattingroom.repository.ChattingRoomRepository;
import com.daangn.clone.chatting.dto.ChattingContentDto;
import com.daangn.clone.chatting.dto.ChattingDto;
import com.daangn.clone.chatting.dto.ChattingListDto;
import com.daangn.clone.chatting.dto.last_read.LastReadDto;
import com.daangn.clone.chatting.dto.receive.ReceiveParameterDto;
import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.common.enums.Role;
import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.repository.ItemRepository;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.town.Town;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChattingRoomServiceTest {

    @InjectMocks
    private ChattingRoomService chattingRoomService;

    @Mock
    private  ItemRepository itemRepository;
    @Mock
    private  ChattingRoomRepository chattingRoomRepository;
    @Mock
    private  ChattingContentRepository chattingContentRepository;
    @Mock
    private  ChattingMemberRepository chattingMemberRepository;
    @Mock
    private  MemberRepository memberRepository;

    /**
     * [api13] : 구매를 위한 채팅방 생성 (기존 채팅방이 있을 경우 기존 채팅방 조회 or 없을 경우 새 채팅방 생성)
     * */
    @Test
    void 채팅요청_새로운채팅방생성(){

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
                .build();
        when(itemRepository.findOneWithSellerMember(any())).thenReturn(Optional.ofNullable(item));

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(buyerMember));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();
        when(chattingRoomRepository.findByItemIdAndBuyerMemberId(50L, 52L)).thenReturn(Optional.ofNullable(null));

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
                .lastReadContentId(0L)
                .build();



        //when
        ChattingDto chattingDto = chattingRoomService.bindChatting(52L, 50L);

        //then
        assertAll(
                () -> assertEquals(52L, chattingDto.getMemberId()),
                () -> assertEquals(49L, chattingDto.getTargetMemberId()),
                () -> assertEquals(50L, chattingDto.getItemId()),
                () -> assertEquals(null, chattingDto.getChattingRoomId())
        );
    }

    @Test
    void 채팅요청_기존채팅방불러오기(){

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
                .build();
        when(itemRepository.findOneWithSellerMember(any())).thenReturn(Optional.ofNullable(item));

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(buyerMember));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();
        when(chattingRoomRepository.findByItemIdAndBuyerMemberId(50L, 52L)).thenReturn(Optional.ofNullable(chattingRoom));

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
                .lastReadContentId(0L)
                .build();



        //when
        ChattingDto chattingDto = chattingRoomService.bindChatting(52L, 50L);

        //then
        assertAll(
                () -> assertEquals(52L, chattingDto.getMemberId()),
                () -> assertEquals(49L, chattingDto.getTargetMemberId()),
                () -> assertEquals(50L, chattingDto.getItemId()),
                () -> assertEquals(53L, chattingDto.getChattingRoomId())
        );
    }

    /**
     * [api14_1] : 참여한 채팅이 하나도 없을 때
     */
    @Test
    void 참여한모든채팅방조회_참여한채팅이하나도없는경우(){
        //given
        when(chattingRoomRepository.existsBySellerMemberId(49L)).thenReturn(false);
        when(chattingRoomRepository.existsByBuyerMemberId(49L)).thenReturn(false);

        //when
        ChattingListDto chattingList = chattingRoomService.getChattingList(49L);

        //then
        assertAll(
                () -> assertEquals(true, CollectionUtils.isEmpty(chattingList.getChattingDtoList())),
                () -> assertEquals(0, chattingList.getSizeOfChatting())
        );

    }

    /**
     * [api14_2] : 참여한 채팅이 하나 이상 있을 떄
     */
    @Test
    void 참여한모든채팅방조회_참여한채팅이존재하는경우(){
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();

        when(chattingRoomRepository.existsBySellerMemberId(49L)).thenReturn(true);
        /** && 조건의 특성으로 인해 아래 문장은 실행되지 않음 */
        //when(chattingRoomRepository.existsByBuyerMemberId(49L)).thenReturn(false);

        List<ChattingRoom> chattingRoomList = List.of(chattingRoom);
        when(chattingRoomRepository
                .findBySellerMemberIdOrBuyerMemberId(49L, 49L))
                .thenReturn(chattingRoomList);


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
                .lastReadContentId(0L)
                .build();

        //when
        ChattingListDto chattingList = chattingRoomService.getChattingList(49L);

        //then
        assertAll(
                () -> assertEquals(false, CollectionUtils.isEmpty(chattingList.getChattingDtoList())),
                () -> assertEquals(49L, chattingList.getChattingDtoList().get(0).getMemberId()),
                () -> assertEquals(52L, chattingList.getChattingDtoList().get(0).getTargetMemberId()),
                () -> assertEquals(50L, chattingList.getChattingDtoList().get(0).getItemId()),
                () -> assertEquals(53L, chattingList.getChattingDtoList().get(0).getChattingRoomId())
        );
    }

    /**
     * [api15] : 52L의 Buyer가 49L의 Seller에게 메세지 전송
     * -> 확인할 수 있는 값중 대표적으로, 실제 content 내용이 있음
     * */
    @Test
    void 메세지전송_성공() {
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();

        ChattingContent chattingContent = ChattingContent.builder()
                .id(56L)
                .chattingRoomId(53L)
                .senderMemberId(52L)
                .content("hello")
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
                .lastReadContentId(0L)
                .build();

        when(memberRepository.findById(52L)).thenReturn(Optional.ofNullable(buyerMember));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(53L, 52L)).thenReturn(Optional.ofNullable(buyer));



        //when
        ChattingContentDto chattingContentDto = chattingRoomService.sendMessage(52L, 53L, "hello");

        //then
        assertAll(
                () -> assertEquals(53L , chattingContentDto.getChattingRoomId()),
                () -> assertEquals("hello" , chattingContentDto.getContent()),
                () -> assertEquals(52L , chattingContentDto.getSenderMemberId())
        );

    }

    @Test
    void 메세지전송_실패_전송하려는사용자가_그채팅룸에속하지않은경우() {
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();



        when(memberRepository.findById(52L)).thenReturn(Optional.ofNullable(buyerMember));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(53L, 52L)).thenReturn(Optional.ofNullable(null));


        //when

        //then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            chattingRoomService.sendMessage(52L, 53L, "hello");
        });
        assertEquals(ApiResponseStatus.INVALID_CHATTING_MEMBER, apiException.getStatus());

    }

    /**
     * [api16_1] : 새로운 메세지가 없는 경우
     **/
    @Test
    void 메세지수신_새로운메세지가없는경우(){
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();

        ChattingContent chattingContent = ChattingContent.builder()
                .id(56L)
                .chattingRoomId(53L)
                .senderMemberId(52L)
                .content("hello")
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
                .lastReadContentId(0L)
                .build();

        when(memberRepository.findById(49L)).thenReturn(Optional.ofNullable(sellerMember));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(53L, 49L)).thenReturn(Optional.ofNullable(seller));
        when(chattingContentRepository.findByChattingRoomIdAndLastReadContentIdOver(53L, 56L, 20)).thenReturn(new ArrayList<>());


        //when
        ReceiveParameterDto receiveParameterDto = ReceiveParameterDto.builder().chattingRoomId(53L).lastReadContentId(56L).limit(20).build();
        List<ChattingContentDto> chattingContentDtoList = chattingRoomService.receiveMessage(49L, receiveParameterDto);

        //then
        assertEquals(true, CollectionUtils.isEmpty(chattingContentDtoList));

    }

    /**
     * [api16_2] : 새로운 메세지가 있는 경우
     **/
    @Test
    void 메세지수신_새로운메세지가있는경우(){
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();

        ChattingContent chattingContent = ChattingContent.builder()
                .id(56L)
                .chattingRoomId(53L)
                .senderMemberId(52L)
                .content("hello")
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
                .lastReadContentId(0L)
                .build();

        List<ChattingContentDto> notReadContentList = new ArrayList<>();
        notReadContentList.add(ChattingContentDto.builder()
                .chattingContentId(chattingContent.getId())
                .chattingRoomId(chattingRoom.getId())
                .content(chattingContent.getContent())
                .senderMemberId(buyerMember.getId())
                .createdAt(LocalDateTime.now()).build());


        when(memberRepository.findById(49L)).thenReturn(Optional.ofNullable(sellerMember));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(53L, 49L)).thenReturn(Optional.ofNullable(seller));
        when(chattingContentRepository.findByChattingRoomIdAndLastReadContentIdOver(53L, 0L, 20)).thenReturn(notReadContentList);



        //when
        ReceiveParameterDto receiveParameterDto = ReceiveParameterDto.builder().chattingRoomId(53L).lastReadContentId(0L).limit(20).build();
        List<ChattingContentDto> receiveMessageList = chattingRoomService.receiveMessage(49L, receiveParameterDto);

        //then
        assertAll(
                () -> assertEquals(1 , receiveMessageList.size()),
                () -> assertEquals(56L, receiveMessageList.get(0).getChattingContentId()),
                () -> assertEquals(53L, receiveMessageList.get(0).getChattingRoomId()),
                () -> assertEquals("hello", receiveMessageList.get(0).getContent()),
                () -> assertEquals(52L, receiveMessageList.get(0).getSenderMemberId()),
                () -> assertEquals(56L, seller.getLastReadContentId())
        );
    }

    /**
     * [api16_3] : 수신자인 seller가 해당 ChattingRoom에 속하지 않는 경우
     **/
    @Test
    void 메세지수신_실패() {
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
                .build();


        when(memberRepository.findById(49L)).thenReturn(Optional.ofNullable(sellerMember));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(53L, 49L)).thenReturn(Optional.ofNullable(null));


        ReceiveParameterDto receiveParameterDto = ReceiveParameterDto.builder().chattingRoomId(53L).lastReadContentId(0L).limit(20).build();

        //when

        //then
        ApiException apiException = assertThrows(ApiException.class, () -> {
            chattingRoomService.receiveMessage(49L, receiveParameterDto);
        });

        assertEquals(ApiResponseStatus.INVALID_CHATTING_MEMBER, apiException.getStatus());
    }

    /**
     * [api17] : Seller가 Buyer가 보낸 hello를 읽었다는 가정하에 , Buyer가 Seller가 마지막으로 읽은 메세지를 확인
     * */
    @Test
    void 상대방이_마지막으로읽은메세지확인(){
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
                .build();

        Member buyerMember = Member.builder()
                .id(52L)
                .username("sample2")
                .password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")
                .nickname("nickname2")
                .town(town28)
                .build();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .id(53L)
                .status(Status.ACTIVE)
                .item(item)
                .sellerMemberId(49L)
                .buyerMemberId(52L)
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

        when(chattingRoomRepository.findById(53L)).thenReturn(Optional.ofNullable(chattingRoom));
        when(chattingMemberRepository.findByChattingRoomIdAndMemberId(chattingRoom.getId(), sellerMember.getId())).thenReturn(Optional.of(seller));


        //when
        LastReadDto lastReadDto = chattingRoomService.getLastReadContentId(buyerMember.getId(), chattingRoom.getId());

        //then
        assertEquals(56L, lastReadDto.getLastReadContentId());

    }
}