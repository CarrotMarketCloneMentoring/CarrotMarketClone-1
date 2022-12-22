package com.daangn.clone.category.controller;

import com.daangn.clone.category.dto.CategoryDto;
import com.daangn.clone.category.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class CategoryControllerTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void api6(){
        //given

        //when
        List<CategoryDto> categoryDtoList = categoryService.getAll();

        //then
        categoryDtoList.stream()
                .map(c -> c.getCategoryName())
                .forEach(n -> System.out.println(n));
    }

}