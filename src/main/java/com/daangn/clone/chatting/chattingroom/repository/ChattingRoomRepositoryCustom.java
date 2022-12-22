package com.daangn.clone.chatting.chattingroom.repository;

import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface ChattingRoomRepositoryCustom {


    List<ChattingRoom> findBySellerMemberIdOrBuyerMemberId(Long sellerMemberId, Long buyerMemberId);
}
