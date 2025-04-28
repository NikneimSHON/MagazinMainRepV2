package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.database.filter.ShoppingCartFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterShoppingCartRepository {
    Page<ShoppingCartEntity> findAllByFilter(ShoppingCartFilter filter, Pageable pageable);
}
