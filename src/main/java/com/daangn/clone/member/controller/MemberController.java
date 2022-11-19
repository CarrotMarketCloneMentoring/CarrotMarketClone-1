package com.daangn.clone.member.controller;

import com.daangn.clone.common.response.ApiResponse;
import com.daangn.clone.encryption.AES128;
import com.daangn.clone.member.dto.login.LoginMemberDto;
import com.daangn.clone.member.dto.login.LoginRequest;
import com.daangn.clone.member.dto.nickname.NicknameRequest;
import com.daangn.clone.member.dto.signup.SignUpRequest;
import com.daangn.clone.member.dto.signup.SignUpRequestDto;
import com.daangn.clone.member.dto.signup.SignUpResponseDto;
import com.daangn.clone.member.dto.username.UsernameRequest;
import com.daangn.clone.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AES128 aes128;

    @GetMapping("/aes128")
    ApiResponse<String> convertAes128(@RequestParam String password){
        return ApiResponse.success(aes128.encrypt(password));
    }

    /** [API.1] : username 중복성 검사 */
    @GetMapping("/signup/username")
    public ApiResponse<Boolean> uniqueUsername(@Validated @ModelAttribute UsernameRequest usernameRequest){
        return ApiResponse.success(memberService.validateUsername(usernameRequest.getUsername()));
    }

    /** [API.2] : nickname 중복성 검사 */
    @GetMapping("/signup/nickname")
    public ApiResponse<Boolean> uniqueNickname(@Validated @ModelAttribute NicknameRequest nicknameRequest){
        return ApiResponse.success(memberService.validateNickname(nicknameRequest.getNickname()));
    }

    /** [API.3] : 회원가입 */
    @PostMapping("/signup")
    public ApiResponse<SignUpResponseDto> signUp(@Validated @RequestBody SignUpRequest signUpRequest){

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .username(signUpRequest.getUsername())
                .encryptPassword(signUpRequest.getEncryptPassword())
                .nickname(signUpRequest.getNickname())
                .townName(signUpRequest.getTownName())
                .build();


        return ApiResponse.success(memberService.signUp(signUpRequestDto));
    }

    /** [API.4] : 로그인 */
    @GetMapping("/login")
    public ApiResponse<LoginMemberDto> login(@Validated @ModelAttribute LoginRequest loginRequest){
        return ApiResponse.success(memberService.login(loginRequest.getUsername(),
                                                       loginRequest.getEncryptPassword()));
    }

}
