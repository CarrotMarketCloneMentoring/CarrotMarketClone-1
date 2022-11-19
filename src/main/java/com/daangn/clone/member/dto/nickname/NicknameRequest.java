package com.daangn.clone.member.dto.nickname;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameRequest {

    @NotNull(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message = "닉네임은 2자 이상 16자 이하의, 영어 또는 숫자 또는 한글로 구성되어야 합니다.")
    private String nickname;
}
