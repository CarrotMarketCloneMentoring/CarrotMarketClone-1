package com.daangn.clone.item.dto;

import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter @Setter
@AllArgsConstructor // 빌드패턴이 적용되려면 , 모든 필드를 가지고 조합 가능한 모든 생성자가 있어야 함. 따라서 @AllArgsConstructor가  필수
@Builder
public class RegisterItemDto {

    //빌드패턴 사용시 , 필수로 넘어와야 하는 값은 final 필드로 선언하고 , 그렇지 않은 필드는 일반 필드로 선언한다.
    private final String title;
    private final String content;
    private final Long price;
    private final Long categoryId;
    private final Long townId;
    private List<MultipartFile> imageList;

}
