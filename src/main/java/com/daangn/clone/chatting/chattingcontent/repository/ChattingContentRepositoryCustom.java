package com.daangn.clone.chatting.chattingcontent.repository;

import com.daangn.clone.chatting.dto.ChattingContentDto;

import java.util.List;

public interface ChattingContentRepositoryCustom {

    List<ChattingContentDto> findByChattingRoomIdAndLastReadContentIdOver(Long chattingRoomId, Long lastReadContentId, int limit);
}
