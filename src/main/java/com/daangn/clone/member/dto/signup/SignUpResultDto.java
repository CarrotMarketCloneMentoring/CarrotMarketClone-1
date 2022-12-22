package com.daangn.clone.member.dto.signup;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResultDto {

    private Long memberId;
    private String username;
    private String nickname;
}
