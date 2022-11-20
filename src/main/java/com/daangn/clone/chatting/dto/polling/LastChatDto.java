package com.daangn.clone.chatting.dto.polling;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LastChatDto {

    private Boolean isLastMessage;
    private LocalDate lastDateAt;
    private LocalTime lastTimeAt;
}
