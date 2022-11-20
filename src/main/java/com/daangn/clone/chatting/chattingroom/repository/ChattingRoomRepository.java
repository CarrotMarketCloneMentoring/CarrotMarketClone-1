package com.daangn.clone.chatting.chattingroom.repository;

import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    @Query("select distinct cr from ChattingRoom cr join fetch cr.chattingMemberList mc join fetch mc.member where cr.id=:id")
    ChattingRoom findOneWithMember(@Param("id")Long id);

    @Query("select cr.id from ChattingRoom cr where cr.itemId = :itemId")
    List<Long> findId(@Param("itemId")Long itemId);

    @Query("select cr from ChattingRoom cr where cr.id=:chattingRoomId")
    ChattingRoom findByChattingRoomId(@Param("chattingRoomId") Long chattingRoomId);

    Optional<ChattingRoom> findByItemIdAndBuyerMemberId(Long itemId, Long buyerMemberId);

    List<ChattingRoom> findBySellerMemberId(Long sellerMemberId);
    List<ChattingRoom> findByBuyerMemberId(Long buyerMemberId);

    boolean existsByIdAndStatus(Long id, Status status);
    boolean existsBySellerMemberId (Long sellerMemberId);
    boolean existsByBuyerMemberId  (Long buyerMemberId);
}
