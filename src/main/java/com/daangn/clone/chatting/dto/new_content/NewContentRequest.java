package com.daangn.clone.chatting.dto.new_content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewContentRequest {

    @Positive(message = "chattingRoomId 값은 양수여야 합니다.")
    @NotNull(message = "어떤 채팅룸에 대해 새로운 content가 왔는지를 확인하기 위해, chattingRoomId 값인 필수로 넘겨져야 합니다.")
    private Long chattingRoomId;

}
