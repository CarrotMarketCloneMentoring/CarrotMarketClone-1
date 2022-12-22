package com.daangn.clone.chatting.dto.send;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendRequest {

    @Positive(message = "chattingRoomId는 양수값이어야 합니다.")
    @NotNull(message = "어떤 채팅방에 메세지를 보낼지에 대한 chattingRoomId 값은 필수 입니다.")
    private Long chattingRoomId;


    @NotNull(message = "메세지 내용은 필수 입니다.")
    private String content;
}
