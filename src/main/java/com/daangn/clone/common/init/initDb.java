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
