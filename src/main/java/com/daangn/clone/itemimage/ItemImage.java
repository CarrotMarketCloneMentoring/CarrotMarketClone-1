package com.daangn.clone.itemimage;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.item.Item;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "item_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ItemImage extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    private String path;

    @JoinColumn(name = "item_id",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(name = "item_id")
    private Long itemId;

    /** [연관관계 편의 메서드]*/

    //ItemImage와 Item
    public void setItem(Item item){
        this.item = item;
        item.getItemImageList().add(this);
    }

    /** [생성자]*/
    public ItemImage(String path, Item item) {
        this.path = path;
        if(item!=null){
            setItem(item);
        }
    }
}
