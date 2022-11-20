package com.daangn.clone.chatting.chattingmember.repository;

import com.daangn.clone.common.enums.Role;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingMemberRepository extends JpaRepository<ChattingMember, Long> {

    @Query("select mc from ChattingMember  mc where mc.id = :memberChattingId")
    ChattingMember findOne(@Param("memberChattingId") Long memberChatingId);

    @Query("select mc from ChattingMember mc join fetch mc.chattingRoom where mc.member.id=:memberId and mc.role=:role")
    List<ChattingMember> findAllWithChattingRoom(@Param("memberId") Long memberId, @Param("role")Role role);

    ChattingMember findByChattingRoomIdAndMemberId(Long chattingRoomId, Long memberId);

    boolean existsByChattingRoomIdAndMemberIdAndRole(Long chattingRoomId, Long memberId, Role role);

    boolean existsByChattingRoomIdAndMemberId(Long chattingRoomId, Long memberId);

    @Query("select cm from ChattingMember cm where cm.chattingRoom.id=:chattingRoomId and " +
            "cm.member.id=:memberId and cm.role=:role")
    ChattingMember findChattingMemberAtRole(@Param("chattingRoomId") Long chattingRoomId,
                                            @Param("memberId") Long memberId, @Param("role") Role role);

    @Query("select cm from ChattingMember cm where cm.chattingRoom.id=:chattingRoomId and cm.member.id=:memberId")
    ChattingMember findChattingMember(@Param("chattingRoomId") Long chattingRoomId, @Param("memberId") Long memberId);

}