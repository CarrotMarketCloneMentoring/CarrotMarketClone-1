package com.daangn.clone.item.dto;

import com.daangn.clone.common.enums.ItemStatus;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangedSituationDto {

    private ItemStatus changedItemStatus;
    private Long buyerMemberId;
    private Long changedItemId;
}
