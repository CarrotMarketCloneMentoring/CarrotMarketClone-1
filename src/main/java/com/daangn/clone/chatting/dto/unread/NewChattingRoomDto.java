package com.daangn.clone.chatting.dto.unread;

import com.daangn.clone.chatting.dto.ChattingDto;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewChattingRoomDto {

    /** 새로 생긴 chattingRoomList*/
    private Integer sizeOfChatting;
    private List<ChattingDto> chattingDtoList;


}
