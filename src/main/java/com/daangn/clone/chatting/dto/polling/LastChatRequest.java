package com.daangn.clone.chatting.dto.polling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LastChatRequest {

    @NotNull(message = "어떤 채팅룸에서 마지막까지 읽은 메세지를 알고싶은지에 대한 , ChattingRoomId 값은 필수 입니다.")
    private Long chattingRoomId;
    @NotNull(message = "특정 채팅룸에서 , 어떤 상대방이 마지막까지 읽은 메세지가 무엇인지에 대해 알고싶은지, 그 TargetMemberId 값은 필수 입니다.")
    private Long targetMemberId;
}
