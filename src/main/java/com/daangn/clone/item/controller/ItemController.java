package com.daangn.clone.item.controller;


import com.daangn.clone.common.enums.ItemStatus;
import com.daangn.clone.item.dto.*;
import com.daangn.clone.item.dto.paging.ItemSummaryDto;
import com.daangn.clone.item.dto.paging.ItemSummaryPageDto;
import com.daangn.clone.item.dto.paging.ItemsReq;
import com.daangn.clone.item.dto.paging.TestDto;
import com.daangn.clone.item.dto.update.ChangeRequest;
import com.daangn.clone.item.service.ItemService;
import com.daangn.clone.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daangn.clone.common.enums.ItemStatus.FOR_SALE;
import static com.daangn.clone.common.enums.ItemStatus.RESERVED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    /** [API 7] : 상품 등록 */

    @PostMapping("/item")
    public ApiResponse<Long> registerItem (@RequestAttribute  Long memberId,
                                           @Validated @ModelAttribute RegistserItemReq request){

        /** POST 전송 방식이지만 , 이미지라는 binary data를 전송해야 하고 - 그러기위해서는 multi part form data 전송방식으로 데이터를
         * 전송해야 하므로 -> multi part form data 전송방식으로 전송되는 데이터를 받을 수 있는 @ModelAttribute를 사용 */

        //  Item 및 ItemImage들을 기반으로 Item과 ItemImage 엔티티를 같은 트랜젝션 안에서 생성하여 save 해야 한다
        // (핵심은 Item과 ItemImage를 같은 트랜젝션 안에서 생성해야 - 만약에 DB에 save하는게 실패해서 rollback이 되더라도 , Item과 ItemImage부분이 모두 rollback이 되므로 , 추후 문제가 없게 됨.)
        // (또한 DB관련된 작업을 먼저 다 수행한 후 - 파일작업을 수행해야 문제가 생겨도 롤백이 가능함)
        // (만약 파일작업을 먼저 수행 후 - DB를 건드리다가 DB에 문제가 터지면 이는 복구 불가)
        /** request로 받은 {title, content, price, categoryId} 를 -> Service계층으로 넘길 때에는 -> 별도의 dto로 변환해서 넘김 */

        return ApiResponse.success(
                itemService.register(memberId,
                        RegisterItemDto.builder()
                                .title(request.getTitle())
                                .content(request.getContent())
                                .price(request.getPrice())
                                .categoryId(request.getCategoryId())
                                .townId(request.getTownId())
                                .imageList(request.getImageList())
                                .build())
        );
    }

    /** [API 8] : 특정 상품 조회 */
    @GetMapping("/item/{itemId}")
    public ApiResponse<ItemDto> getItem(@PathVariable Long itemId){

        //1. 이후 itemId를 가지고 해당 Item 정보를 가져와 반환
        return ApiResponse.success(itemService.getItem(itemId));
    }

    /** [API 9] : 상품 이미지 조회
     *  png 이미지 파일과 jpeg 이미지 파일만 요청할 수 있고 - png 이미지 파일과 jpeg 이미지 파일만 로드해서 보내준다.
     *  단 , 어차피 실제 Item과 연관된 상품 이미지인지의 여부는 확인하지 않을 것 이고, 단순히 복호화하여 byte값을 보내줄 것이다.*/
    @GetMapping(value = "/itemImage")
    public InputStreamResource getItemImage(@RequestParam String path){

        //1) 해당 경로의 이미지 파일을 응답으로 넘김
        return itemService.getItemImage(path);
    }



    /** [API 10] : 최신 상품 목록 조회 - 페이징 사용 , 이때 어떤 town의 Item을 볼지는 외부 요청에 의해 받는걸로! */
    @GetMapping("/items")
    public ApiResponse<ItemSummaryPageDto> getItemList(@RequestAttribute Long memberId,
                                                       @Validated @ModelAttribute ItemsReq itemsReq,
                                                       Pageable pageable){

         //아이템 목록 조회하여 반환
        return ApiResponse.success(itemService.getItemList(
                memberId,
                itemsReq.getTownId(),
                itemsReq.getCategoryId(), itemsReq.getItemStatus(),
                itemsReq.getSortCriteria(),
                pageable));


    }




    /** [API 11] : 해당 Item에 채팅을 시도한 EXPECTED_BUYER들을 조회하는 기능 */
    @GetMapping("/item/buyers/{itemId}")
    public ApiResponse<ExpectedBuyerDto> getExpectedBuyers(@RequestAttribute Long memberId, @PathVariable Long itemId){
        return ApiResponse.success(itemService.getExpectedBuyers(memberId, itemId));
    }

    /**
     * [API 12] : 상품의 ItemStatus 변경
     *
     *  1. 해당 아이템에 대해 채팅 요청을 한 사용자들 중에서 구매자를 선택하고
     *      -> 만약 그 안에서 구매자가 선택되지 않는다면 예외
     *  2. 해당 아이템의 SaleSituation 값을 SOLD_OUT 으로 변경한다.
     *
     * */
    @PatchMapping("/item/itemstatus")
    public ApiResponse<ChangedSituationDto> changeSaleSituation(@RequestAttribute Long memberId,
                                                                @Validated @RequestBody ChangeRequest changeRequest){
        if(changeRequest.getItemStatus() == FOR_SALE){
            return ApiResponse.success(itemService.changeToFOR_SALE(memberId, changeRequest.getItemId()));
        } else if(changeRequest.getItemStatus() == RESERVED){
            return ApiResponse.success(itemService.changeToRESERVED(memberId, changeRequest.getItemId(), changeRequest.getBuyerMemberId()));
        } else{
            return ApiResponse.success(itemService.changeToSOLD_OUT(memberId, changeRequest.getItemId(), changeRequest.getBuyerMemberId()));
        }

    }





}
