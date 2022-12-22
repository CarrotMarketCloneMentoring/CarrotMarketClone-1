package com.daangn.clone.chatting.chattingcontent.repository;


import com.daangn.clone.chatting.chattingcontent.ChattingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> , ChattingContentRepositoryCustom{

    @Query("select cc from ChattingContent cc where cc.id = :chattingContentId")
    ChattingContent findOne(@Param("chattingContentId") Long chattingContentId);



    @Query("select cc from ChattingContent cc where cc.chattingRoom.id=:chattingRoomId ORDER BY cc.createdAt DESC")
    List<ChattingContent> findNewContent(@Param("chattingRoomId") Long chattingRoomId);




    /**------------------------------------------------------------------------------------------------------------ */



    boolean existsByChattingRoomIdAndIdAfter(Long chattingRoomId, Long id);

    @Query("select count(cc) from ChattingContent cc where cc.chattingRoom.id=:chattingRoomId and cc.id>:id")
    int findNumOfNewMessage(@Param("chattingRoomId") Long chattingRoomId, @Param("id") Long id);





}


