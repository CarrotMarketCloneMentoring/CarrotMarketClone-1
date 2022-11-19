package com.daangn.clone.member.service;

import com.daangn.clone.common.enums.Status;
import com.daangn.clone.common.jwt.JwtUtil;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import com.daangn.clone.encryption.AES128;
import com.daangn.clone.encryption.Sha256;
import com.daangn.clone.member.Member;
import com.daangn.clone.member.dto.login.LoginMemberDto;
import com.daangn.clone.member.dto.signup.SignUpResponseDto;
import com.daangn.clone.member.dto.signup.SignUpRequestDto;
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
    private final AES128 aes128;
    private final JwtUtil jwtUtil;


    public Boolean validateUsername(String username){
        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 username이 아니어야 함 - 즉 상태와 관계 없이! */
        if(memberRepository.existsByUsername(username)){
            //throw new ApiException(ApiResponseStatus.NESTED_USERNAME, "회원가입 시점 : 사용하려는 아이디가 이미 사용되고 있는 아이디 입니다.");
            return false;
        }

        return true;
    }

    public Boolean validateNickname(String nickname){
        /** 주의할 점 : ACTIVE이건 INACTIVE 이건 어쨌든 한번 등록된 nickname이 아니어야 함 - 즉 상태와 관계 없이! */
        if(memberRepository.existsByNickname(nickname)){
            //throw new ApiException(ApiResponseStatus.NESTED_NICKNAME, "회원가입 시점 : 사용하려는 닉네임이 이미 사용되고 있는 닉네임 입니다.");
            return false;
        }

        return true;
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
        String decryptPassword = aes128.decrypt(encryptPassword);
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$"; // 영문, 숫자, 특수문자

        if(!Pattern.matches(pattern, decryptPassword)){
            throw new ApiException(ApiResponseStatus.NOT_MATCHED_PASSWORD_RULE, "회원가입 시점 : 비밀번호는 대.소문자 영문, 숫자, 특수문자로 이루어진 8~20자리 문자열 이어야 합니다.");
        }
    }

    /** 회원가입 서비스 */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto){

        //0. 유효성 검사 : username과 nickname은 "상태에 관계 없이" 이미 사용되고 있지는 않은지 검사 + 실제로 존재하는 townName인지 검사
        validateMemberInfo(signUpRequestDto.getUsername(), signUpRequestDto.getNickname(), signUpRequestDto.getTownName());
        validatePassword(signUpRequestDto.getEncryptPassword());


        //1. 클라이언트에서 넘어온 암호화된 비밀번호를 복호화 한 후 , 해쉬화 수행
        String decryptPassword = aes128.decrypt(signUpRequestDto.getEncryptPassword());
        String hashedPassword = sha256.encrypt(decryptPassword);

        //2. username과 nickname, 해쉬화한 password, 그리고 townName을 가지고 Member 엔티티를 생성하여 db에 insert
        Member member = Member.builder()
                .username(signUpRequestDto.getUsername())
                .password(hashedPassword)
                .nickname(signUpRequestDto.getNickname())
                .townId(townRepository.findByName(signUpRequestDto.getTownName()))
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        return SignUpResponseDto.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
    }

    /** ----------------------------------------------------------------------------------------------------------*/

   private void validateUsernameAtLogin(String username){
       if(!memberRepository.existsByUsername(username)){
           throw new ApiException(ApiResponseStatus.INVALID_USERNAME_AT_LOGIN, "로그인 시점 : 그런 아이디로 된 사용자는 존재하지 않습니다.");
       }
   }

   private void validateUsernameAndPassword(String username, String hashedPassword){

       if(!memberRepository.existsByUsernameAndPassword(username, hashedPassword)){
           throw new ApiException(ApiResponseStatus.INVALID_PASSWORD_AT_LOGIN, "로그인 시점 : 비밀번호를 잘못 입력하였습니다.");
       }
   }

   private void validateMemberStatus(String username, String hashedPassword){
       if(!memberRepository.existsByUsernameAndPasswordAndStatus(username, hashedPassword, Status.ACTIVE)){
           throw new ApiException(ApiResponseStatus.INVALID_MEMBER_STATUS, "로그인 시점 : 아이디와 비밀번호는 맛게 입력하였지만, 해당 회원은 이미 탈퇴한 회원입니다.");
       }
   }

   private void validateLoginInfo(String username, String hashedPassword){

       //1_1 해당 아이디로 된 LoginMember가 있는지 검사 - 이부분에서 걸리면 -> 해당 아이디가 잘못된 정보임
       validateUsernameAtLogin(username);

       //1_2. 해당 아이디는 맞고 들어갔을 때 , 비밀번호까지 맞는 LoginMember가 있는지 검사 - 이부분에서 걸리면 -> 비밀번호를 잘못 입력한거임
       validateUsernameAndPassword(username, hashedPassword);

       //1_3. 아이디도 맞고 비밀번호도 맞지만 , 상태가 INVALID 여서 이미 탈퇴한 회원인지 검사
       validateMemberStatus(username, hashedPassword);
   }



    /** [로그인 서비스] */
    public LoginMemberDto login(String username, String encryptPassword){

        //0. 비밀번호 복호화 후 다시 해시화
        String decryptPassword = aes128.decrypt(encryptPassword);
        String hashedPassword = sha256.encrypt(decryptPassword);

        //1. 로그인에 필요한  username과 encryptPassword 가 맞게 들어왔는지 유효성 검사
        validateLoginInfo(username, hashedPassword);


        //2. jwt 생성 (memberId 라는 PK 값을 기반으로)
        Long memberId = memberRepository.findByUsername(username).getId();
        String token = jwtUtil.createToken(memberId.toString());

        return LoginMemberDto.builder().memberId(memberId).token(token).build();
    }


}
