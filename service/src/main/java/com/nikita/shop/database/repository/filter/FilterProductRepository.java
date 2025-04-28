package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.database.filter.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterProductRepository {

    Page<ProductEntity> findAllByFilter(ProductFilter filter, Pageable pageable);
}
