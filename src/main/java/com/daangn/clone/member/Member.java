package com.daangn.clone.member;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.common.enums.Status;
import com.daangn.clone.item.Item;
import com.daangn.clone.chatting.chattingmember.ChattingMember;
import com.daangn.clone.town.Town;
import com.daangn.clone.wish.Wish;
import lombok.*;

import java.util.ArrayList;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BasicEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    /** 결국 이 password 필드는 절대 원본 비밀번호가 들어가지 않고 , 해쉬화된 비밀번호가 들어가는 필드라는 점을 명심해야 함 !!! */
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "town_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Town town;

    @Column(name = "town_id")
    private Long townId;

    @OneToMany(mappedBy = "sellerMember")
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ChattingMember> chattingMemberList = new ArrayList<>();


    /** [연관관계 편의 메서드] */

    // Member와 Town
    public void setTown(Town town){
        this.town = town;
        town.getMemberList().add(this);
    }

    /** [생성자]*/
    public Member(String username, String password, String nickname, Status status, Town town) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        if(town != null) {
            setTown(town);
        }
    }


}
