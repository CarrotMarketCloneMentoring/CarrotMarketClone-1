package com.daangn.clone.itemimage.repository;

import com.daangn.clone.itemimage.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    boolean existsByItemIdAndPath(Long itemId, String path);
}
