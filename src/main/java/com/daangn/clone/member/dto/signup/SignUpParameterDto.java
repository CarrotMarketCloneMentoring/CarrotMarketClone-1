package com.daangn.clone.member.dto.signup;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpParameterDto {

    private String username;

    private String encryptPassword;

    private String nickname;

    private String townName;
}
