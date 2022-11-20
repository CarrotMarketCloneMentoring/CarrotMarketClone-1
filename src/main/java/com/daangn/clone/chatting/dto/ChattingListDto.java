package com.daangn.clone.chatting.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingListDto {

    private List<ChattingDto> chattingDtoList;
    private Integer sizeOfChatting;
}
