package com.daangn.clone.item.dto.paging;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TestDto {
    private Long itemId;
    private String title;
    private String townName;
    private LocalDateTime createdAt;
    private Long price;
    private String itemImageUrl;


    @QueryProjection
    public TestDto(Long itemId, String title, String townName, LocalDateTime createdAt, Long price, String itemImageUrl) {
        this.itemId = itemId;
        this.title = title;
        this.townName = townName;
        this.createdAt = createdAt;
        this.price = price;
        this.itemImageUrl = itemImageUrl;
    }
}
