package com.daangn.clone.chatting.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingContentListDto {

    private Integer sizeOfChttingContent;
    private List<ChattingContentDto> chattingContentDtoList;

}
