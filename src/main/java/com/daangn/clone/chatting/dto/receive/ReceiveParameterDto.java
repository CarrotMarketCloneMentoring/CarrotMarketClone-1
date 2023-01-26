package com.daangn.clone.chatting.dto.receive;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiveParameterDto {

    private Long chattingRoomId;
    private Long lastReadContentId;
    private int limit;

}
