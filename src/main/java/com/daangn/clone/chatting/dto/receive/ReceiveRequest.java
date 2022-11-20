package com.daangn.clone.chatting.dto.receive;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveRequest {

    @NotNull(message = "어떤 채팅방의 메세지를 조회할지에 대한 chattingRoomId 값은 필수 입니다.")
    private Long chattingRoomId;

    @PositiveOrZero(message = "lastReadContentId 값은 양수여야 합니다.")
    @NotNull(message = "수신시에는 마지막 contentId값을 필수로 넘겨받아야 합니다.")
    private Long lastReadContentId;

    @PositiveOrZero( message = "페이지는 0번 페이지 부터 시작합니다.")
    private int page;
    @Range(min = 1 , max = 20 , message = "한 페이지에 불러올 수 있는 메세지 개수는 최소 1개에서 최대 20개로 제한합니다.")
    private int limit;



}
