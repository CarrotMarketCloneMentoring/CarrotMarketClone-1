package com.daangn.clone.item.dto.paging;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSummaryDto {

    private Long itemId;
    private String title;
    private String townName;
    private LocalDateTime createdAt;
    private Long price;
    private String itemImageUrl;
    private int numOfWish;
    private int numOfChattingRoomList;

}
