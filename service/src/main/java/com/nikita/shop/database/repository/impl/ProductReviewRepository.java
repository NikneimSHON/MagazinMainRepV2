package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.ProductReviewFilter;
import com.nikita.shop.database.entity.ProductReviewEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.nikita.shop.database.entity.QProductReviewEntity.productReviewEntity;

import java.util.List;

@Repository
public class ProductReviewRepository extends RepositoryBase<Long, ProductReviewEntity> {

    public ProductReviewRepository(EntityManager entityManager) {
        super(entityManager, ProductReviewEntity.class);
    }

    public List<ProductReviewEntity> findProductReviewWithFilter(EntityManager entityManager, ProductReviewFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getRating(), productReviewEntity.productReviewInfo.rating::eq)
                .add(filter.getDescription().toLowerCase(), productReviewEntity.productReviewInfo.description.lower()::like)
                .add(filter.getBeforeCreateTime(), productReviewEntity.productReviewInfo.createTime::loe)
                .add(filter.getAfterCreateTime(), productReviewEntity.productReviewInfo.createTime::goe)
                .add(filter.getUserId(), productReviewEntity.user.id::eq)
                .buildAnd();
        return new JPAQuery<ProductReviewEntity>(entityManager)
                .select(productReviewEntity)
                .from(productReviewEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .fetch();
    }


}
