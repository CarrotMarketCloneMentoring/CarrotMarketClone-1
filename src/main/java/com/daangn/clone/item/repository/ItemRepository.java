package com.daangn.clone.item.repository;

import com.daangn.clone.common.enums.DelYn;
import com.daangn.clone.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> , ItemRepositoryCustom{


    /** [id를 가지고 유효한 item인지 검증하는 로직]
     * : 비단 해당 id값하고 같은 Item일 뿐만 아니라 - 삭제되지 않은 Item이어야 한다.
     * (delYn이 DELETED인 삭제된 아이템은 관리자만 다루는 상품이기 때문에 - 일반 사용자 입장에서는 유효한 상품으로 취급하지 않는다)*/
    boolean existsByIdAndDelYnNot(Long id, DelYn delYn);

    @Query("select i from Item i where i.id=:id")
    Item findOne(@Param("id") Long id);

    /** [이미 유효하다고 검증을 받은 ItemId를 가지고 Item을 조회하는 로직 - 페치조인을 사용하여 연관된 엔티티들을 최대한 함께 조회]*/
    @Query("select i from Item i join fetch i.town  join fetch i.category join fetch i.sellerMember  where i.id=:id and i.delYn='N' ")
    Optional<Item> findItemById(@Param("id") Long id);

    /** [아이템 조회] : 연관된 하나의 Seller Member를 함께 조회해 오도록 Fetch Join
     * 단 아직 한번도 채팅이 진행되지 않은 상품인 경우 -> 연관된 ChattingRoomList가 존재하지 않을 수 있고 -> 그럼에도 무조건 fetch join으로
     * ChattingRoomList까지 같이 조회하려고 하면 -> 오히려 inner join의 결과 row 가 없게 되어 에러가 터진다.
     * => 따라서 반드시 존재한다고 보장되는 SellerMember를 제외하고는 fetch join을 하지 않아야 한다. */
    @Query("select i from Item i join fetch i.sellerMember m  where i.id = :id")
    Optional<Item> findOneWithSellerMember(@Param("id") Long id);


    @Query("select distinct i from Item i join fetch i.itemImageList where i.id = :id")
    Optional<Item> findItemWithImages(@Param("id") Long id);







    /** (1) Town이나 Category나 where문의 대상이 되므로 어차피 이들을 대상으로는 fetch join을 못씀
     *      fetch join 대신에 inner join을 사용하여 , 해당 Item과 연관된 Town과 Category 정보를 함께 가져올 수는 O(100프로 확신은 x - 확인 필요)
     *  (2) 그렇다면 아쉬운 대로 itemImages or wishList or chattingRoomList 들중 하나라도 fetch join해오자 라고 생각할 수 있지만,
     *      컬렉션 페치조인을 하면서 페이징을 하면 실제 sql level에서 데이터 뻥튀기문제 때문에 페이징이 제대로 되지 않으므로,
     *      [결론적으로] 페이징을 할때는 컬렉션 페치조인을 사용하면 안됨
     *
     *      => 즉 이렇게 inner join만을 써서 필터링 하면소 && 동시에 페이징 하는게 JPQL 레벨에서는 최선
     *
     *      => 근데 결론적으로 검색 조건에 따른 동적 쿼리를 만들어야 했으므로, 쿼리 DSL을 이용함 */












}
