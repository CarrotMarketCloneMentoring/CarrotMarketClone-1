package com.daangn.clone.item;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.category.Category;
import com.daangn.clone.chatting.chattingroom.ChattingRoom;
import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.itemimage.ItemImage;
import com.daangn.clone.member.Member;
import com.daangn.clone.town.Town;
import com.daangn.clone.wish.Wish;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Item extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String title;
    private String content;
    private Long price; // int값을 초과하는 가격이 입력될 수도 있으니깐

    @Column(name = "visit_count")
    private int visitCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn")
    private DelYn delYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @JoinColumn(name = "seller_member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sellerMember;

    @Column(name = "seller_member_id")
    private Long sellerMemberId;

    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "category_id")
    private Long categoryId;

    @JoinColumn(name = "town_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Town town;

    @Column(name = "town_id")
    private Long townId;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> itemImageList = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ChattingRoom> chattingRoomList = new ArrayList<>();

    /** 어쩔 수 없이 객체가 아닌 Id로 직접 저장*/
    @Column(name = "buyer_member_id")
    private Long buyer_member_id;

    @Column(name = "reserved_member_id")
    private Long reserved_member_id;


    /** [연관관계 편의 메서드]*/

    //Item과 sellerMember
    public void setMember(Member sellerMember){
        this.sellerMember = sellerMember;
        sellerMember.getItemList().add(this);

    }

    //Item과 Category
    public void  setCategory(Category category){
        this.category = category;
        category.getItemList().add(this);
    }

    //Item과 Town
    public void setTown(Town town){
        this.town = town;
        town.getItemList().add(this);
    }

    //Item과 buyerMember간 특수항 경우
    public void seyBuyerMemberId(Long buyer_member_id){
        this.buyer_member_id = buyer_member_id;
    }

    /** [생성자]*/
    public Item(String title, String content, Long price, int visitCount, DelYn delYn, ItemStatus saleSituation, Member sellerMember, Category category, Town town) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.visitCount = visitCount;
        this.delYn = delYn;
        this.itemStatus = saleSituation;

        if(sellerMember!=null){
            setSellerMember(sellerMember);
        }
        if(category!=null){
            setCategory(category);
        }


        if(town!=null){
            setTown(town);
        }
    }

    /**
     * [필드 업데이트]
     * */
    public void increaseVisitCount(){
        visitCount++;
    }

    /**
     * [변경 메서드]
     * */

    /** [DelYn 값 변경] */
    public void changeDelYn(DelYn delYn){
        this.delYn = delYn;
    }

    /** [SaleSituation 값 변경] */
    public void changeItemStatus(ItemStatus itemStatus){
        this.itemStatus = itemStatus;
    }

    /** [buyerMemberId 값 변경] */
    public void changeBuyerMemberId(Long buyerMemberId){
        this.buyer_member_id = buyerMemberId;
    }
}
