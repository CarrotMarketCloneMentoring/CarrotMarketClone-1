package com.daangn.clone.chatting.chattingroom.repository;

import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.chatting.chattingroom.QChattingRoom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;


import javax.persistence.EntityManager;
import java.util.List;

import static com.daangn.clone.chatting.chattingroom.QChattingRoom.chattingRoom;
import static com.daangn.clone.item.QItem.item;


public class ChattingRoomRepositoryImpl implements ChattingRoomRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ChattingRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChattingRoom> findBySellerMemberIdOrBuyerMemberId(Long sellerMemberId, Long buyerMemberId) {
        return queryFactory.selectFrom(chattingRoom)
                .innerJoin(chattingRoom.item, item)
                .where(chattingRoom.sellerMemberId.eq(sellerMemberId).or(chattingRoom.buyerMemberId.eq(buyerMemberId)))
                .orderBy(chattingRoom.createdAt.desc())
                .fetch();
    }
}
