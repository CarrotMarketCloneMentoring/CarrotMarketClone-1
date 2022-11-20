package com.daangn.clone.category.controller;

import com.daangn.clone.category.dto.CategoryDto;
import com.daangn.clone.category.service.CategoryService;
import com.daangn.clone.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    /** [API 6.] : 모든 카테고리 조회 */
    @GetMapping("/category/all")
    public ApiResponse<List<CategoryDto>> getCategoryAll(){


        //0. 모든 카테고리 목록을 확인할 때는 굳이 유효성 검사를 하지 않아도 됨.

        //1. CategoryService를 통해 - 모든 Category를 꺼내와서 - 각 Category를 CategoryDto로 변환하여 - 리스트에 모두 담은 채 반환
        return ApiResponse.success(categoryService.getAll());

    }
}
