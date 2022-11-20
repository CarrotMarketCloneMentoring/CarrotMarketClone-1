package com.daangn.clone.item.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedBuyerDto {

    private int numOfExpectedBuyer;
    private List<Long> expectedBuyerIdList;
    private Long itemId;
}
