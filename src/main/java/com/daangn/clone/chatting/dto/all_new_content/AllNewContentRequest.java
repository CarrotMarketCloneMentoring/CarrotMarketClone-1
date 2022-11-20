package com.daangn.clone.chatting.dto.all_new_content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllNewContentRequest {

    @NotNull(message = "어떤 채팅룸에 대해 새로운 content가 왔는지를 확인하기 위해, chattingRoomId 값인 필수로 넘겨져야 합니다.")
    private List<Long> chattingRoomId;

    @NotNull(message = "로컬이 가지고 있는 마지막으로 읽은 contentId값은 필수 입니다.")
    private List<Long> lastReadContentId;
}
