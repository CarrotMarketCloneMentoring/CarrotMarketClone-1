package com.daangn.clone.item.repository;

import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.paging.ItemPageDto;
import com.daangn.clone.item.dto.paging.ItemSummaryDto;
import com.daangn.clone.item.dto.paging.TestDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ItemRepositoryCustom {
    ItemPageDto searchItems(Long townId, Long categoryIdCond, ItemStatus itemStatusCond,
                            OrderSpecifier specifier, Pageable pageable);

    Page<TestDto> searchItemSummaryDtos(Long townId, Long categoryIdCond, ItemStatus itemStatusCond, OrderSpecifier specifier, Pageable pageable);

}
