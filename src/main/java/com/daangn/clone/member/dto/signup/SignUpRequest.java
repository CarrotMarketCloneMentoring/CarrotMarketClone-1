package com.daangn.clone.member.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,10}$", message = "아이디는 6자 이상 10자 이하로 구성된, 영어와 숫자의 조합이어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    //@Size(min = 1, max = 48, message = "비밀번호는 AES128로 암호화 된 , 비밀번호를 넘겨야 합니다.")
    // AES 암호화 되지 않은 비밀번호가 들어오면 -> 어차피 복호화에서 터지니까 -> 굳이 @Size로 암호화 여부를 확인할 필요는 없다 (중복 check니까까
    private String encryptPassword;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message = "닉네임은 2자 이상 16자 이하의, 영어 또는 숫자 또는 한글로 구성되어야 합니다.")
    private String nickname;

    @NotBlank(message = "행정동 이름은 필수입니다.")
    private String townName;

}
