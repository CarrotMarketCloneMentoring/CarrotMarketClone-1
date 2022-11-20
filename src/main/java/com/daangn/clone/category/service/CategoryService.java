package com.daangn.clone.category.service;

import com.daangn.clone.category.Category;
import com.daangn.clone.category.dto.CategoryDto;
import com.daangn.clone.category.repository.CategoryRepository;
import com.daangn.clone.common.response.ApiException;
import com.daangn.clone.common.response.ApiResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /** [조회] : get 시리즈 */

    public List<CategoryDto> getAll(){
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = categoryList.stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toList());

        return categoryDtoList;
    }

}
