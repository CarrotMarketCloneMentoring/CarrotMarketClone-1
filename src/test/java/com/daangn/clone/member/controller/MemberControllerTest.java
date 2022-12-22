package com.daangn.clone.member.controller;

import com.daangn.clone.common.jwt.JwtUtil;
import com.daangn.clone.encryption.AES128;
import com.daangn.clone.encryption.AES256;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.dto.login.LoginMemberDto;
import com.daangn.clone.member.dto.signup.SignUpParameterDto;
import com.daangn.clone.member.dto.signup.SignUpResultDto;
import com.daangn.clone.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Rollback(value = false)
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AES256 aes256;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @Transactional
    void api1(){
        //given
        Member username1 = Member.builder().username("username1").build();
        Member username2 = Member.builder().username("username2").build();

        //when
        em.persist(username1);
        Boolean resultOfUsername1 = memberService.validateUsername(username1.getUsername());
        Boolean resultOfUsername2 = memberService.validateUsername(username2.getUsername());

        //then
        assertThat(resultOfUsername1).isFalse();
        assertThat(resultOfUsername2).isTrue();

    }

    @Test
    @Transactional
    void api2(){
        //given
        Member nickname1 = Member.builder().nickname("nickname1").build();
        Member nickname2 = Member.builder().nickname("nickname2").build();

        //when
        em.persist(nickname1);
        Boolean resultOfNickname1 = memberService.validateNickname(nickname1.getNickname());
        Boolean resultOfNickname2 = memberService.validateNickname(nickname2.getNickname());

        //then
        assertThat(resultOfNickname1).isFalse();
        assertThat(resultOfNickname2).isTrue();
    }

    @Test
    void api3(){
        //given
        String encryptPassword = aes256.encrypt("Abcde12345!");

        SignUpParameterDto signUpParameterDto = SignUpParameterDto.builder()
                .username("memberA")
                .encryptPassword(encryptPassword)
                .nickname("memberAA")
                .townName("서울특별시_동작구_대방동")
                .build();


        //when
        SignUpResultDto signUpResultDto = memberService.signUp(signUpParameterDto);

        //then
        assertThat(signUpResultDto.getUsername()).isEqualTo("memberA");
        assertThat(signUpResultDto.getNickname()).isEqualTo("memberAA");
    }

    @Test
    @Transactional
    void api4(){

        //given
        String encryptPassword = aes256.encrypt("Abcde12345!");
        SignUpParameterDto signUpParameterDto = SignUpParameterDto.builder()
                .username("memberA")
                .encryptPassword(encryptPassword)
                .nickname("memberAA")
                .townName("서울특별시_동작구_대방동")
                .build();

        SignUpResultDto signUpResultDto = memberService.signUp(signUpParameterDto);

        //when
        LoginMemberDto loginMemberDto = memberService.login("memberA", encryptPassword);

        //then
        Member member = em.find(Member.class, loginMemberDto.getMemberId());
        assertThat(member.getUsername()).isEqualTo("memberA");
        assertThat(member.getNickname()).isEqualTo("memberAA");

        long findMemberId = Long.parseLong(jwtUtil.parseJwtToken(loginMemberDto.getToken()).getSubject());
        assertThat(findMemberId).isEqualTo(member.getId());

    }

}