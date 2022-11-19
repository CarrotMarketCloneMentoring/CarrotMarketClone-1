package com.daangn.clone.chatting.chattingmember;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.common.enums.Role;
import com.daangn.clone.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "chatting_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChattingMember extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "chatting_member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;


    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @JoinColumn(name = "chatting_room_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ChattingRoom chattingRoom;

    @Column(name = "chatting_room_id")
    private Long chattingRoomId;

    @Column(name = "last_read_content_id")
    private Long lastReadContentId;

    /** [연관관계 편의 메서드]*/

    //MemberChatting과 Member
    public void setMember(Member member){
        this.member = member;
        member.getChattingMemberList().add(this);
    }

    //MemberChatting과 ChattingRoom
    public void setChattingRoom(ChattingRoom chattingRoom){
        this.chattingRoom = chattingRoom;
        chattingRoom.getChattingMemberList().add(this);
    }

    /** [생성자]*/
    public ChattingMember(Role role, Member member, ChattingRoom chattingRoom) {
        this.role = role;

        if(member != null){
            setMember(member);
        }

        if(chattingRoom!=null){
            setChattingRoom(chattingRoom);
        }

    }

}
