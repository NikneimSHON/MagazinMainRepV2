package com.nikita.shop.repository.impl;

import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.nikita.shop.entity.QProductEntity.productEntity;

public class ProductRepository extends RepositoryBase<Long, ProductEntity> {
    public ProductRepository(EntityManager entityManager) {
        super(entityManager, ProductEntity.class);
    }

    public List<ProductEntity> findProductByPriceAndCategoryAndPriceAndAvailableAndCountAndDescriptionAndName(Session session, ProductFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getMaxPrice(), productEntity.productInfo.price::loe)
                .add(filter.getMinPrice(), productEntity.productInfo.price::goe)
                .add(filter.getMaxCount(), productEntity.productInfo.count::loe)
                .add(filter.getMinCount(), productEntity.productInfo.count::goe)
                .add(filter.getCategory(), productEntity.productInfo.category::eq)
                .add(filter.isAvailable(), productEntity.available::eq)
                .add(filter.getDescription().toLowerCase(), productEntity.productInfo.description.lower()::like)
                .add(filter.getProductName(), productEntity.productInfo.name::like)
                .buildOr();
        return new JPAQuery<ProductEntity>(session)
                .select(productEntity)
                .from(productEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("WithPriceAndCategoryAndAvailable"))
                .fetch();
    }


}
