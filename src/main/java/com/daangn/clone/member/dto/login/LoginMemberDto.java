package com.daangn.clone.member.dto.login;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginMemberDto {

    private Long memberId;
    private String token;
}
