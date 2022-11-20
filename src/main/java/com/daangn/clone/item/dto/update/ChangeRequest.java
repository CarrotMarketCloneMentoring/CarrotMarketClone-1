package com.daangn.clone.item.dto.update;

import com.daangn.clone.common.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequest {

    @Positive(message = "itemId 값은 반드시 양수여야 합니다.")
    @NotNull(message = "어떤 상품의 상태를 변경할지에 대한 itemId 값은 필수입니다.")
    private Long itemId;

    @Positive(message = "buyerMemberId 값은 반드시 양수여야 합니다.")
    @NotNull(message = "상품을 예약 또는 구매하려고 하는 Member의 Id 값은 필수입니다.")
    private Long buyerMemberId;

    @NotNull(message = "어떤 상태로 변경할지에 대한 itemStatus 값은 필수입니다.")
    private ItemStatus itemStatus;
}
