package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.ShoppingCartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProductEntity, Long> {


}
