package com.daangn.clone.common.init;

import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.common.enums.*;
import com.daangn.clone.category.Category;
import com.daangn.clone.item.Item;
import com.daangn.clone.itemimage.ItemImage;
import com.daangn.clone.member.Member;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.town.Town;
import com.daangn.clone.wish.Wish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class initDb {


    private final InitService initService;



    /** 샘플데이터로 DB 초기화 -> 스프링 빈 의존관계 주입이 끝난 직후 수행되는 로직 by @PostConstruct*/
    @PostConstruct
    void init(){
        initService.doInit1();
    }

    /** 실질적으로 샘플 데이터를 DB에 넣는 Service 로직 */
    @Service
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        @Value("${file.dir}")
        private String fileDir;


        public void doInit1() {

            /** 1. 먼저 값이 들어가 있어야 하는 Town과 Category를 초기화 */

            Town town1 = new Town("서울특별시_광진구_중곡제1동"); Town town2 = new Town("서울특별시_광진구_중곡제2동");
            Town town3 = new Town("서울특별시_광진구_중곡제3동"); Town town4 = new Town("서울특별시_광진구_중곡제4동");
            Town town5 = new Town("서울특별시_광진구_능동");
            Town town6 = new Town("서울특별시_광진구_구의제1동"); Town town7 = new Town("서울특별시_광진구_구의제2동");
            Town town8 = new Town("서울특별시_광진구_구의제3동");
            Town town9 = new Town("서울특별시_광진구_광장동");
            Town town10 = new Town("서울특별시_광진구_자양제1동"); Town town11 = new Town("서울특별시_광진구_자양제2동");
            Town town12 = new Town("서울특별시_광진구_자양제3동"); Town town13 = new Town("서울특별시_광진구_자양제4동");
            Town town14 = new Town("서울특별시_광진구_화양동"); Town town15 = new Town("서울특별시_광진구_군자동");

            Town town16 = new Town("서울특별시_동작구_노량진제1동");
            Town town17 = new Town("서울특별시_동작구_노량진제2동");
            Town town18 = new Town("서울특별시_동작구_상도제1동");
            Town town19 = new Town("서울특별시_동작구_상도제2동");
            Town town20 = new Town("서울특별시_동작구_상도제3동");
            Town town21 = new Town("서울특별시_동작구_상도제4동");
            Town town22 = new Town("서울특별시_동작구_흑석동");
            Town town23 = new Town("서울특별시_동작구_사당제1동");
            Town town24 = new Town("서울특별시_동작구_사당제2동");
            Town town25 = new Town("서울특별시_동작구_사당제3동");
            Town town26 = new Town("서울특별시_동작구_사당제4동");
            Town town27 = new Town("서울특별시_동작구_사당제5동");
            Town town28 = new Town("서울특별시_동작구_대방동");
            Town town29 = new Town("서울특별시_동작구_신대방제1동");
            Town town30 = new Town("서울특별시_동작구_신대방제2동");



            Category category1 = new Category("중고차");
            Category category2 = new Category("디지털기기");
            Category category3 = new Category("생활가전");
            Category category4 = new Category("가구/인테리어");
            Category category5 = new Category("유아동");
            Category category6 = new Category("유아도서");
            Category category7 = new Category("생활/가공식품");
            Category category8 = new Category("스포츠/레저");
            Category category9 = new Category("여성잡화");
            Category category10 = new Category("여성의류");
            Category category11 = new Category("남성패션/잡화");
            Category category12 = new Category("게임/취미");
            Category category13 = new Category("뷰티/미용");
            Category category14 = new Category("반려동물용품");
            Category category15 = new Category("도서/티켓/음반");
            Category category16 = new Category("식물");
            Category category17 = new Category("기타 중고물품");
            Category category18 = new Category("삽니다");



            em.persist(town1); em.persist(town2); em.persist(town3); em.persist(town4); em.persist(town5);
            em.persist(town6); em.persist(town7); em.persist(town8); em.persist(town9); em.persist(town10);
            em.persist(town11); em.persist(town12); em.persist(town13); em.persist(town14); em.persist(town15);
            em.persist(town16); em.persist(town17); em.persist(town18); em.persist(town19); em.persist(town20);
            em.persist(town21); em.persist(town22); em.persist(town23); em.persist(town24); em.persist(town25);
            em.persist(town26); em.persist(town27); em.persist(town28); em.persist(town29); em.persist(town30);

            em.persist(category1); em.persist(category2); em.persist(category3); em.persist(category4); em.persist(category5);
            em.persist(category6); em.persist(category7); em.persist(category8); em.persist(category9); em.persist(category10);
            em.persist(category11); em.persist(category12); em.persist(category13); em.persist(category14); em.persist(category15);
            em.persist(category16); em.persist(category17); em.persist(category18);


            /** 2. 이후 샘플 Member와 샘플 Post 그리고 샘플 Wish 등록*/
            Member member1 = Member.builder()
                    .username("aaa")
                    .password("aaaaaa")
                    .nickname("aaa")
                    .status(Status.ACTIVE)
                    .townId(14L)
                    .build();
            Member member2 = Member.builder()
                    .username("bbb")
                    .password("bbbbbb")
                    .nickname("bbb")
                    .status(Status.ACTIVE)
                    .townId(14L)
                    .build();

            Member member3 = Member.builder()
                    .username("ccc")
                    .password("cccccc")
                    .nickname("ccc")
                    .status(Status.ACTIVE)
                    .townId(14L)
                    .build();

            Member member4 = Member.builder()
                    .username("ddd")
                    .password("dddddd")
                    .nickname("ddd")
                    .status(Status.ACTIVE)
                    .townId(6L)
                    .build();

            Member member5 = Member.builder()
                    .username("eee")
                    .password("eeeeee")
                    .nickname("eee")
                    .status(Status.ACTIVE)
                    .townId(6L)
                    .build();

            Member member6 = Member.builder()
                    .username("fff")
                    .password("ffffff")
                    .nickname("fff")
                    .status(Status.ACTIVE)
                    .townId(6L)
                    .build();

            em.persist(member1); em.persist(member2); em.persist(member3);
            em.persist(member4); em.persist(member5); em.persist(member6);

            Member member_aaa123 = Member.builder()
                    .username("aaa123")
                    .password("E415C40B0BA776EB31FC768EEA1B8727C922AACC046682CD5935061458D81139")
                    .nickname("aaa123")
                    .status(Status.ACTIVE)
                    .townId(town28.getId())
                    .build();

            Member member_bbb123 = Member.builder()
                    .username("bbb123")
                    .password("BEEE9B68541C646879F32ABD11816335E654B0E825D2CB8D49586704514B5957")
                    .nickname("bbb123")
                    .status(Status.ACTIVE)
                    .townId(town28.getId())
                    .build();

            em.persist(member_aaa123); em.persist(member_bbb123);

            Item item11 = Item.builder()
                    .title("아이폰6 팝니다")
                    .content("아이폰6 팝니다")
                    .price(100000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member1.getId())
                    .categoryId(category3.getId())
                    .townId(member1.getTownId())
                    .build();

            Item item12 = Item.builder()
                    .title("아이폰7 팝니다")
                    .content("아이폰7 팝니다")
                    .price(200000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member1.getId())
                    .categoryId(category3.getId())
                    .townId(member1.getTownId())
                    .build();

            Item item13 = Item.builder()
                    .title("아이폰8 팝니다")
                    .content("아이폰8 팝니다")
                    .price(300000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member1.getId())
                    .categoryId(category3.getId())
                    .townId(member1.getTownId())
                    .build();

            Item item21 = Item.builder()
                    .title("아이폰11 팝니다")
                    .content("아이폰11 팝니다")
                    .price(400000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member2.getId())
                    .categoryId(category3.getId())
                    .townId(member2.getTownId())
                    .build();

            Item item22 = Item.builder()
                    .title("아이폰12 팝니다")
                    .content("아이폰12 팝니다")
                    .price(500000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member2.getId())
                    .categoryId(category3.getId())
                    .townId(member2.getTownId())
                    .build();

            Item item23 = Item.builder()
                    .title("아이폰13 팝니다")
                    .content("아이폰13 팝니다")
                    .price(600000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member2.getId())
                    .categoryId(category3.getId())
                    .townId(member2.getTownId())
                    .build();

            Item item31 = Item.builder()
                    .title("쇼파 팝니다")
                    .content("쇼파 팝니다")
                    .price(700000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member3.getId())
                    .categoryId(category5.getId())
                    .townId(member3.getTownId())
                    .build();

            Item item32 = Item.builder()
                    .title("침대 팝니다")
                    .content("침대 팝니다")
                    .price(800000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member3.getId())
                    .categoryId(category5.getId())
                    .townId(member3.getTownId())
                    .build();

            Item item33 = Item.builder()
                    .title("이불 팝니다")
                    .content("이불 팝니다")
                    .price(900000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member3.getId())
                    .categoryId(category5.getId())
                    .townId(member3.getTownId())
                    .build();

            Item item41 = Item.builder()
                    .title("아이패드5 팝니다")
                    .content("아이패드5 팝니다")
                    .price(1000000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member4.getId())
                    .categoryId(category3.getId())
                    .townId(member4.getTownId())
                    .build();

            Item item42 = Item.builder()
                    .title("아이패드6 팝니다")
                    .content("아이패드6 팝니다")
                    .price(200000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member4.getId())
                    .categoryId(category3.getId())
                    .townId(member4.getTownId())
                    .build();

            Item item43 = Item.builder()
                    .title("아이패드7 팝니다")
                    .content("아이패드7 팝니다")
                    .price(300000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member4.getId())
                    .categoryId(category3.getId())
                    .townId(member4.getTownId())
                    .build();

            Item item51 = Item.builder()
                    .title("아이패드8 팝니다")
                    .content("아이패드8 팝니다")
                    .price(400000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member5.getId())
                    .categoryId(category3.getId())
                    .townId(member5.getTownId())
                    .build();

            Item item52 = Item.builder()
                    .title("아이패드9 팝니다")
                    .content("아이패드9 팝니다")
                    .price(500000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.SOLD_OUT)
                    .sellerMemberId(member5.getId())
                    .categoryId(category3.getId())
                    .townId(member5.getTownId())
                    .build();

            Item item53 = Item.builder()
                    .title("아이패드10 팝니다")
                    .content("아이패드10 팝니다")
                    .price(600000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member5.getId())
                    .categoryId(category3.getId())
                    .townId(member5.getTownId())
                    .build();

            Item item61 = Item.builder()
                    .title("밥솥 팝니다")
                    .content("밥솥 팝니다")
                    .price(700000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member6.getId())
                    .categoryId(category4.getId())
                    .townId(member6.getTownId())
                    .build();

            Item item62 = Item.builder()
                    .title("에어컨 팝니다")
                    .content("에어컨 팝니다")
                    .price(800000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member6.getId())
                    .categoryId(category4.getId())
                    .townId(member6.getTownId())
                    .build();

            Item item63 = Item.builder()
                    .title("냉장고 팝니다")
                    .content("냉장고 팝니다")
                    .price(900000L)
                    .visitCount(0)
                    .delYn(DelYn.N)
                    .itemStatus(ItemStatus.FOR_SALE)
                    .sellerMemberId(member6.getId())
                    .categoryId(category4.getId())
                    .townId(member6.getTownId())
                    .build();

            em.persist(item11); em.persist(item12); em.persist(item13);
            em.persist(item21); em.persist(item22); em.persist(item23);
            em.persist(item31); em.persist(item32); em.persist(item33);

            em.persist(item41); em.persist(item42); em.persist(item43);
            em.persist(item51); em.persist(item52); em.persist(item53);
            em.persist(item61); em.persist(item62); em.persist(item63);

            Wish wish1 = Wish.builder()
                    .status(Status.ACTIVE)
                    .memberId(member1.getId())
                    .itemId(item23.getId())
                    .build();

            Wish wish2 = Wish.builder()
                    .status(Status.ACTIVE)
                    .memberId(member3.getId())
                    .itemId(item23.getId())
                    .build();

            em.persist(wish1); em.persist(wish2);

            ItemImage itemImage11 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item11.getId())
                    .build();

            ItemImage itemImage12 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item12.getId())
                    .build();

            ItemImage itemImage13 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item13.getId())
                    .build();

            ItemImage itemImage21 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item21.getId())
                    .build();

            ItemImage itemImage22 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item22.getId())
                    .build();

            ItemImage itemImage23 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item23.getId())
                    .build();

            ItemImage itemImage31 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item31.getId())
                    .build();

            ItemImage itemImage32 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item32.getId())
                    .build();

            ItemImage itemImage33 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item33.getId())
                    .build();

            ItemImage itemImage41 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item41.getId())
                    .build();

            ItemImage itemImage42 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item42.getId())
                    .build();

            ItemImage itemImage43 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item43.getId())
                    .build();


            ItemImage itemImage51 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item51.getId())
                    .build();

            ItemImage itemImage52 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item52.getId())
                    .build();

            ItemImage itemImage53 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item53.getId())
                    .build();


            ItemImage itemImage61 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item61.getId())
                    .build();

            ItemImage itemImage62 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item62.getId())
                    .build();

            ItemImage itemImage63 = ItemImage.builder()
                    .path("/Users/lupi./Desktop/carrot/sample/sample.png")
                    .itemId(item63.getId())
                    .build();


            em.persist(itemImage11); em.persist(itemImage12); em.persist(itemImage13);
            em.persist(itemImage21); em.persist(itemImage22); em.persist(itemImage23);
            em.persist(itemImage31); em.persist(itemImage32); em.persist(itemImage33);
            em.persist(itemImage41); em.persist(itemImage42); em.persist(itemImage43);
            em.persist(itemImage51); em.persist(itemImage52); em.persist(itemImage53);
            em.persist(itemImage61); em.persist(itemImage62); em.persist(itemImage63);





            /** 3. item23에 대해서 member1과 member3아 member2에게 채팅을 시도했다고 가정 */


            ChattingRoom chattingRoom12 = ChattingRoom.builder()
                    .status(Status.ACTIVE)
                    .itemId(item23.getId())
                    .build();

            ChattingRoom chattingRoom13 = ChattingRoom.builder()
                    .status(Status.ACTIVE)
                    .itemId(item23.getId())
                    .build();

            em.persist(chattingRoom12); em.persist(chattingRoom13);

            ChattingContent chattingContent12 = ChattingContent.builder()
                    .content("안녕하세요 ~ member1인데 아이폰 13 구매하고 싶어서요~")
                    .chattingRoomId(chattingRoom12.getId())
                    .build();

            ChattingContent chattingContent13 = ChattingContent.builder()
                    .content("안녕하세요 ~ member3인데 아이폰 13 구매하고 싶어서요~")
                    .chattingRoomId(chattingRoom13.getId())
                    .build();

            em.persist(chattingContent12); em.persist(chattingContent13);


            ChattingMember chattingMember12 = ChattingMember.builder()
                    .role(Role.EXPECTED_BUYER)
                    .memberId(member1.getId())
                    .chattingRoomId(chattingRoom12.getId())
                    .build();

            ChattingMember chattingMember13 = ChattingMember.builder()
                    .role(Role.EXPECTED_BUYER)
                    .memberId(member3.getId())
                    .chattingRoomId(chattingRoom13.getId())
                    .build();

            ChattingMember chattingMember21 = ChattingMember.builder()
                    .role(Role.SELLER)
                    .memberId(member2.getId())
                    .chattingRoomId(chattingRoom12.getId())
                    .build();

            ChattingMember chattingMember23 = ChattingMember.builder()
                    .role(Role.SELLER)
                    .memberId(member2.getId())
                    .chattingRoomId(chattingRoom13.getId())
                    .build();


            em.persist(chattingMember12); em.persist(chattingMember13);
            em.persist(chattingMember21); em.persist(chattingMember23);


            /** 이렇게 오늘 생성된 아이템의 아이템 이미지를 저장할 - 오늘날짜 디렉터리는 요청에 의해서가 아니라 , 미리미리 생성해두는것이 좋음
             * 그렇지 않은면 , 동시성 문제가 발생할 측면이 있다고 생각함 */
            String today = LocalDateTime.now().getYear() + "_" +(LocalDateTime.now().getMonth().getValue()) + "_"  + LocalDateTime.now().getDayOfMonth();
            Path todayPath = Paths.get(fileDir + File.separator+ today);
            try{
                Files.createDirectories(todayPath);
            } catch (IOException e){
                e.printStackTrace();
            }



        }

    }


}
