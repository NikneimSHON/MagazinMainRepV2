package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.database.repository.filter.FilterProductRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.ProductFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.nikita.shop.entity.QProductEntity.productEntity;

public class FilterProductRepositoryImpl implements FilterProductRepository {
    private final JPAQueryFactory queryFactory;

    public FilterProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductEntity> findAllByFilter(ProductFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<ProductEntity> resultList = queryFactory
                .select(productEntity)
                .from(productEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(productEntity)
                .from(productEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);
    }

    private Predicate predicateBuilder(ProductFilter filter) {
        return QPredicate.builder()
                .add(filter.getMaxPrice(), productEntity.productInfo.price::loe)
                .add(filter.getMinPrice(), productEntity.productInfo.price::goe)
                .add(filter.getMaxCount(), productEntity.productInfo.count::loe)
                .add(filter.getMinCount(), productEntity.productInfo.count::goe)
                .add(filter.getAvailable(), productEntity.available::eq)
                .add(filter.getProductName(), productEntity.productInfo.name::like)
                .add(filter.getCategory(), productEntity.productInfo.category::eq)
                .buildAnd();

    }
}
