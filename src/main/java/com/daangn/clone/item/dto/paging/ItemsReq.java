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


    @PositiveOrZero( message = "페이지는 0번 페이지 부터 시작합니다.")
    private int page;


    @Range(min = 1 , max = 30 , message = "한 페이지에 불러올 수 있는 상품의 개수는 최소 10개에서 최대 30개로 제한합니다.")
    private int limit;

    @NotNull(message = "townId는 필수값 입니다.")
    @Positive(message = "townId 값은 양수인 정수 입니다.")
    private Long townId;

    @NotNull(message = "상품 정렬 기준은 필수 입니다.")
    private SortCriteria sortCriteria;


    @Positive(message = "categoryId 값은 양수인 정수 입니다.")
    private Long categoryId;

    private ItemStatus itemStatus;


}
