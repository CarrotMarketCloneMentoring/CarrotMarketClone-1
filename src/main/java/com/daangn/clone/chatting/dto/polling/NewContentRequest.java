package com.daangn.clone.chatting.dto.polling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewContentRequest {

    @NotNull(message = "새 메세지가 왔는지의 여부를 알기 위해선, 어느 채팅방의 새 메세지 여부를 확인할지에 대한 chattingRoomId 값이 필수 입니다.")
    private Long chattingRoomId;

}
