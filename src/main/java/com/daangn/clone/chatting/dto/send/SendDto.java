package com.daangn.clone.chatting.dto.send;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendDto {

    private Long chattingRoomId;
    private String content;
}
