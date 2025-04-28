package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.database.repository.filter.FilterProductReviewRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.ProductReviewFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.nikita.shop.entity.QProductReviewEntity.productReviewEntity;

public class FilterProductReviewRepositoryImpl implements FilterProductReviewRepository {
    private final JPAQueryFactory queryFactory;

    public FilterProductReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductReviewEntity> findAllByFilter(ProductReviewFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<ProductReviewEntity> resultList = queryFactory
                .select(productReviewEntity)
                .from(productReviewEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(productReviewEntity)
                .from(productReviewEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);
    }

    private Predicate predicateBuilder(ProductReviewFilter filter) {
        return QPredicate.builder()
                .add(filter.getRating(), productReviewEntity.productReviewInfo.rating::eq)
                .add(filter.getBeforeCreateTime(), productReviewEntity.productReviewInfo.createTime::loe)
                .add(filter.getAfterCreateTime(), productReviewEntity.productReviewInfo.createTime::goe)
                .add(filter.getUserId(), productReviewEntity.user.id::eq)
                .buildAnd();

    }
}
