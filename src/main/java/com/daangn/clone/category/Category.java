package com.daangn.clone.category;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.item.Item;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Item> itemList = new ArrayList<>();

    /** [생성자]*/
    public Category(String name) {
        this.name = name;
    }
}
