package com.daangn.clone.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter     //@ModelAttribute로 값을 받으려면 Setter가 필수
@NoArgsConstructor // @RequestBody로 값을 받으려면 기본생성자가 필수
public class RegistserItemReq {

    @NotBlank(message = "글 제목은 필수로 작성해야 합니다.")
    private String title;
    @NotBlank(message = "글 내용은 필수로 작성해야 합니다.")
    private String content;

    @Min(value = 0, message = "최소 가격은 0원 입니다.")
    private Long price;

    @NotNull // 어차피 어떤 값이든 들어오기만 하면 -> validCheck를 해줄거니깐 , 일단 값이 들어오는지 여부만 check 해 주면 됨
    private Long categoryId;

    @NotNull
    private Long townId;

    @Size(max = 5 , message = "상품 이미지는 최대 5장 까지 업로드 가능합니다.")
    private List<MultipartFile> imageList = new ArrayList<>();

}
