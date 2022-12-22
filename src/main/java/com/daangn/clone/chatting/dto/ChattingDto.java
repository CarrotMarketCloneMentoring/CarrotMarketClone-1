package com.daangn.clone.chatting.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingDto {
    private Long memberId;
    private Long targetMemberId;
    private Long itemId;
    private Long chattingRoomId;
    private Long chattingMemberId;
    private Long targetChattingMemberId;

}
