package com.daangn.clone.chatting.dto.new_content;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewContentDto {

    private Boolean isNewMessage;
    private int numOfNewMessage;
}
