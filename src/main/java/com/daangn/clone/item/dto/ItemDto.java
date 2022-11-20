package com.daangn.clone.item.dto;

import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@AllArgsConstructor
@Builder
public class ItemDto {


    private Long itemeId;
    private String title;
    private String content;
    private Long price;
    private int visitCount;
    private String categoryName;
    private String townName;
    private String sellerMemberName;
    private LocalDateTime createdAt;
    private DelYn delYn; // 겉으로는 표시가 안되지만 , 상품을 구분하는 중요한 정보
    private ItemStatus itemStatus;
    private int numOfWish;
    private int numOfChattingRoom;
    private List<String> itemImagePathList = new ArrayList<>();


}
