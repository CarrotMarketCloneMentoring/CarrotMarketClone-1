package com.daangn.clone.chatting.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ChattingContentDto {
    private Long chattingContentId;
    private Long chattingRoomId;
    private String content;
    private Long senderMemberId;
    private LocalDateTime createdAt;

    @QueryProjection
    public ChattingContentDto(Long chattingContentId, Long chattingRoomId, String content, Long senderMemberId, LocalDateTime createdAt) {
        this.chattingContentId = chattingContentId;
        this.chattingRoomId = chattingRoomId;
        this.content = content;
        this.senderMemberId = senderMemberId;
        this.createdAt = createdAt;
    }
}
