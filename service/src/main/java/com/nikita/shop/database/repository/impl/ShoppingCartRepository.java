package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.database.repository.filter.FilterShoppingCartRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> , FilterShoppingCartRepository {


}
