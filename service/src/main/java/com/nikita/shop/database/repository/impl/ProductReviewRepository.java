package com.nikita.shop.database.repository.impl;

import com.nikita.shop.database.repository.filter.FilterProductReviewRepository;
import com.nikita.shop.entity.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, Long>, FilterProductReviewRepository {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    List<ProductReviewEntity> deleteProductReviewByUserId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    List<ProductReviewEntity> deleteProductReviewByProductId(Long productId);

}
