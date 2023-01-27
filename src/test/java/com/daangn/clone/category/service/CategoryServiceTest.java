package com.daangn.clone.category.service;

import com.daangn.clone.category.Category;
import com.daangn.clone.category.dto.CategoryDto;
import com.daangn.clone.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void 모든Category이름조회(){
        //given
        Category category1 = new Category("중고차");
        Category category2 = new Category("디지털기기");
        Category category3 = new Category("생활가전");
        Category category4 = new Category("가구/인테리어");
        Category category5 = new Category("유아동");
        Category category6 = new Category("유아도서");
        Category category7 = new Category("생활/가공식품");
        Category category8 = new Category("스포츠/레저");
        Category category9 = new Category("여성잡화");
        Category category10 = new Category("여성의류");
        Category category11 = new Category("남성패션/잡화");
        Category category12 = new Category("게임/취미");
        Category category13 = new Category("뷰티/미용");
        Category category14 = new Category("반려동물용품");
        Category category15 = new Category("도서/티켓/음반");
        Category category16 = new Category("식물");
        Category category17 = new Category("기타 중고물품");
        Category category18 = new Category("삽니다");

        List<Category> categoryList = List.of(category1, category2, category3, category4, category5, category6, category7, category8, category9,
                category10, category11, category12, category13, category14, category15, category16, category17, category18);

        when(categoryRepository.findAll()).thenReturn(categoryList);

        //when
        List<CategoryDto> categoryDtoList = categoryService.getAll();

        //then
        assertThat(categoryDtoList).extracting("categoryName").containsExactly(
                "중고차", "디지털기기", "생활가전", "가구/인테리어", "유아동", "유아도서", "생활/가공식품", "스포츠/레저", "여성잡화",
                "여성의류", "남성패션/잡화", "게임/취미", "뷰티/미용", "반려동물용품", "도서/티켓/음반", "식물", "기타 중고물품", "삽니다"
        );
    }

}
