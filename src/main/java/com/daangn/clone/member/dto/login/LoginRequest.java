package com.daangn.clone.member.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    //@Length(min = 0, max = 48, message = "비밀번호는 AES128로 암호화 된 , 비밀번호를 넘겨야 합니다.")
    // 어차피 해쉬화 해서 비교했을 때 -> (암호화를 하든 안하든)그 값이 일치하지 않으면 로그인 fail 이니깐 / 굳이 이 검사가 들어가야 하나? 라는 생각이 듦
    private String encryptPassword;
}
