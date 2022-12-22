package com.daangn.clone.chatting.chattingroom.repository;

import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> , ChattingRoomRepositoryCustom {

    @Query("select distinct cr from ChattingRoom cr join fetch cr.chattingMemberList mc join fetch mc.member where cr.id=:id")
    ChattingRoom findOneWithMember(@Param("id")Long id);


    Optional<ChattingRoom> findByItemIdAndBuyerMemberId(Long itemId, Long buyerMemberId);

    @Query("select cr from ChattingRoom  cr where cr.id=:chattingRoomId")
    ChattingRoom findOne(@Param("chattingRoomId") Long chattingRoomId);


    boolean existsBySellerMemberId (Long sellerMemberId);
    boolean existsByBuyerMemberId  (Long buyerMemberId);
}
