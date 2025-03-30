package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.database.entity.ProductEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import static com.nikita.shop.database.entity.QProductEntity.productEntity;

import java.util.List;

@Repository
public class ProductRepository extends RepositoryBase<Long, ProductEntity> {

    public ProductRepository(EntityManager entityManager) {
        super(entityManager, ProductEntity.class);
    }

    public List<ProductEntity> findProductWithFilter(EntityManager entityManager, ProductFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getMaxPrice(), productEntity.productInfo.price::loe)
                .add(filter.getMinPrice(), productEntity.productInfo.price::goe)
                .add(filter.getMaxCount(), productEntity.productInfo.count::loe)
                .add(filter.getMinCount(), productEntity.productInfo.count::goe)
                .add(filter.getCategory(), productEntity.productInfo.category::eq)
                .add(filter.isAvailable(), productEntity.available::eq)
                .add(filter.getDescription().toLowerCase(), productEntity.productInfo.description.lower()::like)
                .add(filter.getProductName(), productEntity.productInfo.name::like)
                .buildAnd();
        return new JPAQuery<ProductEntity>(entityManager)
                .select(productEntity)
                .from(productEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityManager.getEntityGraph("WithPriceAndCategoryAndAvailable"))
                .fetch();
    }

}
