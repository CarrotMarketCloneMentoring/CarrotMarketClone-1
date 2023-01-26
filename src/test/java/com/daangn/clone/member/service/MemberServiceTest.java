package com.daangn.clone.member.service;

import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.jwt.JwtUtil;
import com.daangn.clone.encryption.AES256;
import com.daangn.clone.encryption.Sha256;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.dto.login.LoginMemberDto;
import com.daangn.clone.member.dto.signup.SignUpParameterDto;
import com.daangn.clone.member.dto.signup.SignUpResultDto;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.town.repository.TownRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TownRepository townRepository;

    @Mock
    private Sha256 sha256;

    @Mock
    private AES256 aes256;

    @Mock
    private JwtUtil jwtUtil;


    @BeforeAll
    static void beforeAll(){

    }


    @Test
    void username이_중복되지않아_유효한경우(){
        //given
        when(memberRepository.existsByUsername("username1")).thenReturn(false); // "username1" 은 아직 사용중이지 않은 username 이라는 가정

        //when
        Boolean resultOfUsername1 = memberService.validateUsername("username1");

        //then
        assertEquals(true, resultOfUsername1, "아직 사용중이지 않은 username의 경우 , 검증 로직 결과가 true 여야 한다");

    }

    @Test
    void username이_중복되어_유효하지않은경우(){
        //given
        when(memberRepository.existsByUsername("username2")).thenReturn(true); // "username2" 는 이미 사용중인 username 이라는 가정

        //when
        Boolean resultOfUsername2 = memberService.validateUsername("username2");

        //then
        assertEquals(false, resultOfUsername2, "이미 사용중인 username의 경우, 검증 로직 결과가 false 여야 한다.");


    }

    @Test
    void 닉네임이_중복되지않아_유효한경우(){
        //given
        when(memberRepository.existsByNickname("nickname1")).thenReturn(false);

        //when
        Boolean resultOfNickname1 = memberService.validateNickname("nickname1");

        //then
        assertEquals(true, resultOfNickname1, "아직 사용되지 않는 nickname의 경우 , 검증 로직 결과가 true 여야 한다");

    }

    @Test
    void 닉네임이_중복되어_유효하지않은경우(){
        //given
        when(memberRepository.existsByNickname("nickname2")).thenReturn(true);

        //when
        Boolean resultOfNickname2 = memberService.validateNickname("nickname2");

        //then
        assertEquals(false, resultOfNickname2, "이미 사용되는 nickname의 경우 , 검증 로직 결과가 false 여야 한다");

    }

    @Test
    void 회원가입(){

        //given
        when(memberRepository.existsByUsername("sample1")).thenReturn(false);
        when(memberRepository.existsByNickname("nickname1")).thenReturn(false);
        when(townRepository.existsByName("서울특별시_동작구_대방동")).thenReturn(true);
        when(aes256.decrypt("09VSPHFbNZIcY79+HgM43A==")).thenReturn("Abcde12345!");

        when(sha256.encrypt("09VSPHFbNZIcY79+HgM43A==")).thenReturn("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8");
        when(townRepository.findByName("서울특별시_동작구_대방동")).thenReturn(28L);

        Member sampleMember = Member.builder().id(30L).username("sample1").password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8").nickname("nickname1").townId(28L).status(Status.ACTIVE).build();
        when(memberRepository.save(any())).thenReturn(sampleMember);

        SignUpParameterDto signUpParameterDto = SignUpParameterDto.builder()
                .username("sample1")
                .encryptPassword("09VSPHFbNZIcY79+HgM43A==")
                .nickname("nickname1")
                .townName("서울특별시_동작구_대방동")
                .build();

        //when
        SignUpResultDto signUpResultDto = memberService.signUp(signUpParameterDto);


        //then
        assertAll(
                () -> assertEquals("sample1", signUpResultDto.getUsername()),
                () -> assertEquals("nickname1", signUpResultDto.getNickname())
        );
    }

    @Test
    void 로그인(){

        //given
        when(sha256.encrypt("09VSPHFbNZIcY79+HgM43A==")).thenReturn("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8");

        Member member = Member.builder().id(49L).username("sample1").password("425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8").nickname("nickname1").townId(28L).status(Status.ACTIVE).build();
        when(memberRepository.findByUsernameAndPassword("sample1", "425d5da5529e125212fac4b2a584ad01e2348f214855920df0a9ade0b4a7f0c8")).thenReturn(Optional.ofNullable(member));
        when(jwtUtil.createToken("49")).thenReturn("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTcHJpbmdfU2V2ZXIiLCJpYXQiOjE2NzI4OTkyMTAsImV4cCI6MTY3Mjk4NTYxMCwic3ViIjoiNDkifQ._WCJCl56KNcENfrFBmdkS09j-iQ4zfOiIAQLvJnsfQE");

        //when
        LoginMemberDto loginMemberDto = memberService.login("sample1", "09VSPHFbNZIcY79+HgM43A==");

        //then
        assertAll(
                () -> assertEquals(49L, loginMemberDto.getMemberId()),
                () -> assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTcHJpbmdfU2V2ZXIiLCJpYXQiOjE2NzI4OTkyMTAsImV4cCI6MTY3Mjk4NTYxMCwic3ViIjoiNDkifQ._WCJCl56KNcENfrFBmdkS09j-iQ4zfOiIAQLvJnsfQE", loginMemberDto.getToken())
        );
    }

}
