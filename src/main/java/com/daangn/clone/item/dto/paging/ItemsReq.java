package com.daangn.clone.item.dto.paging;

import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
public class ItemsReq {


    @NotNull(message = "townId는 필수값 입니다.")
    @Positive(message = "townId 값은 양수인 정수 입니다.")
    private Long townId;

    @Positive(message = "categoryId 값은 양수인 정수 입니다.")
    private Long categoryId;

    private ItemStatus itemStatus;


    @NotNull(message = "상품 정렬 기준은 필수 입니다.")
    private SortCriteria sortCriteria;




}
