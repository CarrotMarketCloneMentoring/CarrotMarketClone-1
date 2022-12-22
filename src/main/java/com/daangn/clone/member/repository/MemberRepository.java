package com.daangn.clone.member.repository;


import com.daangn.clone.common.enums.Status;
import com.daangn.clone.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /** 어차피 username은 컨트롤러 이전인 인터셉터에서 유효성 검사를 하므로 - 굳이 Optional로 감쌀 필요가 없다*/
    @Query("select m from Member m join fetch m.town where m.username=:username")
    Member findWithTown(@Param("username") String username);

    boolean existsByIdAndStatus(Long id, Status status);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    boolean existsByNicknameAndStatus(String nickname, Status status);
    boolean existsByUsernameAndPassword(String username, String password);
    boolean existsByUsernameAndStatus(String username, Status status);




    @Query("select m from Member m where m.id = :memberId")
    Member findOne(@Param("memberId") Long memberId);

    /** status에 관계 없이, 오직 아이디와 비밀번호 만으로 조회 */
    @Query("select m from Member m where m.username=:username and m.password=:password")
    Optional<Member> findOne(@Param("username")String username, @Param("password") String password);

    /** 가장 최신에 생성된 채팅방이 가장 위에 보이도록 하기 위해 createdAt 값에 대한 DESC */
    @Query("select distinct m from Member m join fetch m.chattingMemberList mc join fetch mc.chattingRoom c where m.id = :memberId order by c.createdAt desc")
    Member findMemberWithChatting(@Param("memberId") Long memberId);

    @Query("select distinct m from Member m join fetch m.chattingMemberList where m.username = :username")
    Member findWithMemberChattingList(@Param("username")String username);






}
