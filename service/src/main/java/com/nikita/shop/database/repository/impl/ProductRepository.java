package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.database.repository.filter.FilterProductRepository;
import com.nikita.shop.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, FilterProductRepository {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ProductEntity a where a.productInfo.category = :category")
    int deleteByCategory(@Param("category") ProductCategory category);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE ProductEntity a SET a.productInfo.category = :newValue WHERE a.productInfo.category = :category")
    int updateAllByCategory(@Param("category") ProductCategory category,
                            @Param("newValue") ProductCategory newValue);
}
