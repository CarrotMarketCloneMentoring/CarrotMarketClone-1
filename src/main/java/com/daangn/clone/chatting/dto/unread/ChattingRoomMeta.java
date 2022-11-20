package com.daangn.clone.chatting.dto.unread;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomMeta {

    /** 각 디바이스의 로컬에 저장된 ChattingRoom의 Id*/
    private List<Long> chattingRoomIdList = new ArrayList<>();

}
