package com.daangn.clone.chatting.dto.polling;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewContentDto {

    private Boolean isNewContent;
    private Integer cntOfNewMessage;
}
