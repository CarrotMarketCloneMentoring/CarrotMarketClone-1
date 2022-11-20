package com.daangn.clone.wish;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.common.enums.Status;
import com.daangn.clone.item.Item;
import com.daangn.clone.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "wish")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Wish extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "wish_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "member_id")
    private Long memberId;

    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "item_id")
    private Long itemId;

    /** [얀관관계 편의 메서드]*/

    //Wish와 Member
    public void setMember(Member member){
        this.member = member;
        member.getWishList().add(this);
    }

    //Wish와 Item
    public void setItem(Item item){
        this.item = item;
        item.getWishList().add(this);
    }

    /** [생성자]*/
    public Wish(Status status, Member member, Item item) {
        this.status = status;
        if(member!=null){
            setMember(member);
        }
        if(item!=null){
            setItem(item);
        }

    }
}
