package com.daangn.clone.item.repository;

import com.daangn.clone.chatting.chattingroom.QChattingRoom;
import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.item.Item;
import com.daangn.clone.item.dto.paging.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.daangn.clone.chatting.chattingroom.QChattingRoom.chattingRoom;
import static com.daangn.clone.item.QItem.item;
import static com.daangn.clone.itemimage.QItemImage.itemImage;
import static com.daangn.clone.member.QMember.member;
import static com.daangn.clone.town.QTown.town;
import static com.daangn.clone.wish.QWish.wish;

public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TestDto> searchItemSummaryDtos(Long townId, Long categoryIdCond, ItemStatus itemStatusCond, OrderSpecifier specifier, Pageable pageable) {
        List<TestDto > content = queryFactory
                .select(new QTestDto(item.id, item.title, town.name, item.createdAt, item.price, item.itemImageList.get(0).path))
                .from(item)
                .innerJoin(item.town, town)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(itemStatusCond))
                .orderBy(specifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCountQuery = queryFactory
                .select(item.count())
                .from(item)
                .innerJoin(item.town, town)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(itemStatusCond));

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCountQuery.fetchOne());

    }

    @Override
    public ItemPageDto searchItems(Long townId, Long categoryIdCond, ItemStatus itemStatusCond,
                                   OrderSpecifier specifier, Pageable pageable) {

        List<Item> itemList = queryFactory
                .selectFrom(item)
                .innerJoin(item.town, town)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(itemStatusCond))
                .orderBy(specifier)
                .offset(pageable.getOffset()) //주의
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(item.count())
                .from(item)
                .where(delYnEq(DelYn.N), townIdEq(townId), categoryIdEq(categoryIdCond), itemStatusEq(itemStatusCond))
                .fetchOne();

        return ItemPageDto.builder()
                .itemList(itemList)
                .totalCount(totalCount)
                .build();

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
