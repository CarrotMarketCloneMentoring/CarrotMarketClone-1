package com.daangn.clone.chatting.chattingcontent.repository;

import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import com.daangn.clone.chatting.chattingcontent.QChattingContent;
import com.daangn.clone.chatting.dto.ChattingContentDto;
import com.daangn.clone.chatting.dto.QChattingContentDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.daangn.clone.chatting.chattingcontent.QChattingContent.chattingContent;


public class ChattingContentRepositoryImpl implements ChattingContentRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ChattingContentRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChattingContentDto> findByChattingRoomIdAndLastReadContentIdOver(Long chattingRoomId, Long lastReadContentId, int limit) {
        return queryFactory
                .select(new QChattingContentDto(chattingContent.id, chattingContent.chattingRoom.id, chattingContent.content, chattingContent.senderMemberId, chattingContent.createdAt))
                .from(chattingContent)
                .where(chattingRoomIdEq(chattingRoomId), lastReadContentIdOver(lastReadContentId))
                .orderBy(chattingContent.createdAt.asc())
                .limit(limit)
                .fetch();
    }

    private Predicate chattingRoomIdEq(Long chattingRoomId){
        return chattingContent.chattingRoom.id.eq(chattingRoomId);
    }

    private Predicate lastReadContentIdOver(Long lastReadContentId){
        return chattingContent.id.gt(lastReadContentId);
    }
}
