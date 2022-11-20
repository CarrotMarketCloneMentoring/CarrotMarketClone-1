package com.daangn.clone.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryDto {

    private Long categoryId;
    private String categoryName;

    public CategoryDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
