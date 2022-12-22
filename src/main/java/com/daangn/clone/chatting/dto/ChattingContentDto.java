package com.daangn.clone.chatting.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingContentDto {
    private Long chattingContentId;
    private Long chattingRoomId;
    private String content;
    private Long senderMemberId;
    private LocalDateTime createdAt;

}
