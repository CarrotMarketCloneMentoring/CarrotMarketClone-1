package com.daangn.clone.category.repository;

import com.daangn.clone.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.id=:id")
    Category findCategory(@Param("id")Long id);

    Category findByName(String name);
}
