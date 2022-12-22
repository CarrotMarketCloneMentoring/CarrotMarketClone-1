package com.daangn.clone.chatting.dto.receive;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiveDto {

    private Long chattingRoomId;
    private Long lastReadContentId;
    private int limit;

}
