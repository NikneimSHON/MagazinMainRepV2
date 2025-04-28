package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.database.filter.ProductReviewFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterProductReviewRepository {
    Page<ProductReviewEntity> findAllByFilter(ProductReviewFilter filter, Pageable pageable);
}
