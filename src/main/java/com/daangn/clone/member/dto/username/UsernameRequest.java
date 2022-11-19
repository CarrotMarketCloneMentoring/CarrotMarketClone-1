package com.daangn.clone.member.dto.username;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameRequest {

    @NotNull(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}$", message = "아이디는 6자 이상 10자 이하로 구성된, 영어와 숫자의 조합이어야 합니다.")
    private String username;
}
