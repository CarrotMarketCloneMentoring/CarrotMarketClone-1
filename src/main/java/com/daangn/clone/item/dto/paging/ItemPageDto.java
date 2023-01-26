package com.daangn.clone.item.dto.paging;

import com.daangn.clone.item.Item;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPageDto {

    private List<Item> itemList;
    private long totalCount;
}
