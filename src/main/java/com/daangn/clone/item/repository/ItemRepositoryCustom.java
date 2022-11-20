package com.daangn.clone.item.repository;

import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.paging.ItemSummaryDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> searchItems(Long townId, Long categoryIdCond, ItemStatus itemStatusCond,
                                     OrderSpecifier specifier, int offset, int limit);

    List<ItemSummaryDto> searchItemSummaryDtos(Long townId, Long categoryIdCond, ItemStatus itemStatusCond,
                                               OrderSpecifier specifier, int offset, int limit);

}
