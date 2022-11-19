package com.daangn.clone.chatting.dto.last_read;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class LastReadRequest {

    @Positive(message = "chattingRoomId 값은 양수여야 합니다.")
    @NotNull(message = "어떤 채팅룸에서 상대가 마지막으로 읽은 content는 무엇인지를 알기 위해서는, 그 chattingRoomId 값은 필수 입니다.")
    private Long chattingRoomId;

    @Positive(message = "targetMemberId 값은 양수여야 합니다.")
    @NotNull(message = "어떤 채팅룸에서 상대가 마지막으로 읽은 content는 무엇인지를 알기 위해서는, 그 targetMemberId 값은 필수 입니다.")
    private Long targetMemberId;
}
