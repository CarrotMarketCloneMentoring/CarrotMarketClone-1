package com.daangn.clone.member.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "아이디는 필수입니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Length(min = 0, max = 48, message = "비밀번호는 AES128로 암호화 된 , 비밀번호를 넘겨야 합니다.")
    private String encryptPassword;
}
