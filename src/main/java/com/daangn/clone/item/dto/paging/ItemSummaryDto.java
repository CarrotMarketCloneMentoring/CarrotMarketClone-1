package com.daangn.clone.item.dto.paging;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ItemSummaryDto {

    private Long itemId;
    private String title;
    private String townName;
    private LocalDateTime createdAt;
    private Long price;
    private String itemImageUrl;
    private long numOfWish;
    private long numOfChattingRoomList;

    @QueryProjection
    public ItemSummaryDto(Long itemId, String title, String townName, LocalDateTime createdAt, Long price, String itemImageUrl, long numOfWish, long numOfChattingRoomList) {
        this.itemId = itemId;
        this.title = title;
        this.townName = townName;
        this.createdAt = createdAt;
        this.price = price;
        this.itemImageUrl = itemImageUrl;
        this.numOfWish = numOfWish;
        this.numOfChattingRoomList = numOfChattingRoomList;
    }
}
