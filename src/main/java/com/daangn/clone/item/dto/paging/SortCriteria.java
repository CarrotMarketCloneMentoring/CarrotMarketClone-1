package com.daangn.clone.item.dto.paging;

import com.daangn.clone.item.QItem;
import com.querydsl.core.types.OrderSpecifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import static com.daangn.clone.item.QItem.item;

@Getter
@AllArgsConstructor
public enum SortCriteria {

    /**
     * MAX_PRICE : 가격 높은순
     * MIN_PRICE : 가격 낮은순
     * NEW : 최신 등록순
     * OLD : 오래된 등록 순
     * */

    MAX_PRICE(item.price.desc()),
    MIN_PRICE(item.price.asc()),
    NEW(item.createdAt.desc()),
    OLD(item.createdAt.asc());


    private OrderSpecifier specifier;

}
