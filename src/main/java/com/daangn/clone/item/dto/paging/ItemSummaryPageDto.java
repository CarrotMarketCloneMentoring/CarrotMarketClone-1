package com.daangn.clone.item.dto.paging;


import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSummaryPageDto {

    private List<ItemSummaryDto> itemSummaryDtoList;
    private long totalCount;
}
