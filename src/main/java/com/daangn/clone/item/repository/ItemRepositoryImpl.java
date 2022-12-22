package com.daangn.clone.item.repository;

import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.paging.ItemSummaryDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.daangn.clone.item.QItem.item;
import static com.daangn.clone.member.QMember.member;
import static com.daangn.clone.town.QTown.town;

public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemSummaryDto> searchItemSummaryDtos(Long townId, Long categoryIdCond, ItemStatus situationCond, OrderSpecifier specifier, int offset, int limit) {
        return queryFactory
                .select(Projections.bean(ItemSummaryDto.class,
                        item.id,
                        item.title,
                        item.town.name.as("townName"),
                        item.createdAt,
                        item.price,
                        item.itemImageList.get(0).path.as("itemImageUrl"),
                        item.wishList.size().as("numOfWish"),
                        item.chattingRoomList.size().as("numOfChattingRoomList")))
                .from(item)
                .innerJoin(item.town, town)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(situationCond))
                .orderBy(specifier)
                .offset(offset*limit)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Item> searchItems(Long townId, Long categoryIdCond, ItemStatus itemStatusCond,
                                          OrderSpecifier specifier, int offset, int limit) {
        return queryFactory
                .selectFrom(item)
                .innerJoin(item.town, town)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(itemStatusCond))
                .orderBy(specifier)
                .offset(offset*limit) //주의
                .limit(limit)
                .fetch();
    }

    /** 당연히 삭제되지 않은 아이템들만 가져와야 함 */
    //필수조건
    private Predicate delYnEq(DelYn delYn){
        return item.delYn.eq(delYn);
    }

    private Predicate townIdEq(Long townId){ return item.town.id.eq(townId);}

    //선택조건
    private Predicate categoryIdEq(Long categoryIdCond) {
        return categoryIdCond!=null ? item.category.id.eq(categoryIdCond) : null;
    }

    //선택조건
    private Predicate itemStatusEq(ItemStatus itemStatusCond) {
        return itemStatusCond!=null ? item.itemStatus.eq(itemStatusCond) : null;
    }
}
