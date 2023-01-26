package com.daangn.clone.member.service;

import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.jwt.JwtUtil;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.encryption.AES256;
import com.daangn.clone.encryption.Sha256;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.dto.login.LoginMemberDto;
import com.daangn.clone.member.dto.signup.SignUpResultDto;
import com.daangn.clone.member.dto.signup.SignUpParameterDto;
import com.daangn.clone.member.repository.MemberRepository;
import com.daangn.clone.town.repository.TownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final TownRepository townRepository;
    private final Sha256 sha256;
    private final AES256 aes256;
    private final JwtUtil jwtUtil;


    public boolean validateUsername(String username){
        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 username이 아니어야 함 - 즉 상태와 관계 없이! */
        return !memberRepository.existsByUsername(username);

    }

    public boolean validateNickname(String nickname){
        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 nickname이 아니어야 함 - 즉 상태와 관계 없이! */
        return !memberRepository.existsByNickname(nickname);

    }



    private void validateMemberInfo(String username, String nickname, String townName){

        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 username이 아니어야 함 - 즉 상태와 관계 없이! */
        if(memberRepository.existsByUsername(username)){
            throw new ApiException(ApiResponseStatus.NESTED_USERNAME, "회원가입 시점 : 사용하려는 아이디가 이미 사용되고 있는 아이디 입니다.");
        }

        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 nickname이 아니어야 함 - 즉 상태와 관계 없이! */
        if(memberRepository.existsByNickname(nickname)){
            throw new ApiException(ApiResponseStatus.NESTED_NICKNAME, "회원가입 시점 : 사용하려는 닉네임이 이미 사용되고 있는 닉네임 입니다.");
        }

        /** Town 객체는 Status 필드를 가지고 있지 않음 */
        if(!townRepository.existsByName(townName)){
            throw new ApiException(ApiResponseStatus.INVALID_TOWN_NAME, "회원가입 시점 : 유효하지 않은 행정동 이름 입니다.");
        }
    }

    private void validatePassword(String encryptPassword){
        String password = aes256.decrypt(encryptPassword);
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$"; // 영문, 숫자, 특수문자

        if(!Pattern.matches(pattern, password)){
            throw new ApiException(ApiResponseStatus.NOT_MATCHED_PASSWORD_RULE, "회원가입 시점 : 비밀번호는 대.소문자 영문, 숫자, 특수문자로 이루어진 8~20자리 문자열 이어야 합니다.");
        }
    }

    /** 회원가입 서비스 */
    @Transactional
    public SignUpResultDto signUp(SignUpParameterDto signUpParameterDto){

        //0. 유효성 검사 : username과 nickname은 "상태에 관계 없이" 이미 사용되고 있지는 않은지 검사 + 실제로 존재하는 townName인지 검사
        validateMemberInfo(signUpParameterDto.getUsername(), signUpParameterDto.getNickname(), signUpParameterDto.getTownName());
        validatePassword(signUpParameterDto.getEncryptPassword());


        //1. 클라이언트에서 넘어온 암호화된 비밀번호를 복호화 하지 않고 , 암호화 된 채로 해쉬화를 진행하여 DB에 저장
        // (굳이 한번 더 복호화를 할 필요가 없음)
        String hashedPassword = sha256.encrypt(signUpParameterDto.getEncryptPassword());

        //2. username과 nickname, 해쉬화한 password, 그리고 townName을 가지고 Member 엔티티를 생성하여 db에 insert
        Member member = Member.builder()
                .username(signUpParameterDto.getUsername())
                .password(hashedPassword)
                .nickname(signUpParameterDto.getNickname())
                .townId(townRepository.findByName(signUpParameterDto.getTownName()))
                .status(Status.ACTIVE)
                .build();

        memberRepository.save(member);

        return SignUpResultDto.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    /** ----------------------------------------------------------------------------------------------------------*/



    /** [로그인 서비스] */
    public LoginMemberDto login(String username, String encryptPassword){

        //0. 암호화 된 비밀번호를 바로 해시화
        String hashedPassword = sha256.encrypt(encryptPassword);

        //1. Member를 username과 password로 조회 -> 만약 여기서 예외가 터지면, 아이디 또는 비밀번호가 잘못된것
        /** 단 이때 아이디와 비밀번호의 유효성을 각각 알려주지 않고, 아이디 또는 비밀번호가 잘못되었다고 알려줘야 함*/
        Member member = memberRepository.findByUsernameAndPassword(username, hashedPassword).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_USERNAME_OR_PASSWORD, "로그인 시점 : 아이디 또는 비밀번호가 잘못되었습니다.");
                }
        );

        //여기서 안걸리면 ACTIVE라는것의 반증 -> 그니까 다음 문장을 정상 수행하여 , JWT를 생성하는 방향으로 로그인 성공!!!
        if(member.getStatus() == Status.INACTIVE){
            throw new ApiException(ApiResponseStatus.INVALID_MEMBER_STATUS, "로그인 시점 : 해당 회원은 이미 회원탈퇴 한 회원 입니다.");
        }


        //2. jwt 생성 (memberId 라는 PK 값을 기반으로) (어차피 위에서 ACTIVE 한 username 검사를 했으니, 무조건 username으로 찾을 수 O)
        String token = jwtUtil.createToken(member.getId().toString());

        return LoginMemberDto.builder().memberId(member.getId()).token(token).build();
    }


}
