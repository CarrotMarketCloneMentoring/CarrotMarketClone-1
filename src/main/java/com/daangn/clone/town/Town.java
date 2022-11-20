package com.daangn.clone.town;

import com.daangn.clone.common.BasicEntity;
import com.daangn.clone.item.Item;
import com.daangn.clone.member.Member;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "town")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Town extends BasicEntity{

    @Id @GeneratedValue
    @Column(name = "town_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "town")
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "town")
    private List<Item> itemList = new ArrayList<>();

    /** [생성자]*/
    public Town(String name) {
        this.name = name;
    }
}

