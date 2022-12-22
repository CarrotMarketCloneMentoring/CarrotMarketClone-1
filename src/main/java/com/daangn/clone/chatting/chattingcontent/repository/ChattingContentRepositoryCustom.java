package com.daangn.clone.chatting.chattingcontent.repository;

import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface ChattingContentRepositoryCustom {

    List<ChattingContent> findNotReadMessage(Long chattingRoomId, Long lastReadContentId, int limit);
}
