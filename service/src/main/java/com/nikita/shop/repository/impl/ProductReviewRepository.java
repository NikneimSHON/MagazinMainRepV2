package com.nikita.shop.repository.impl;

import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.dto.ProductReviewFilter;
import com.nikita.shop.dto.UserFilter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nikita.shop.entity.QProductReviewEntity.productReviewEntity;

public class ProductReviewRepository extends RepositoryBase<Long, ProductReviewEntity> {
    public ProductReviewRepository(EntityManager entityManager) {
        super(entityManager, ProductReviewEntity.class);
    }

    public List<ProductReviewEntity> findReviewWithUserAndFilerWithRatingAndTimeAndDescriptionAndProductNameAndCategory(EntityManager entityManager, ProductReviewFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getRating(), productReviewEntity.productReviewInfo.rating::eq)
                .add(filter.getDescription().toLowerCase(), productReviewEntity.productReviewInfo.description.lower()::like)
                .add(filter.getBeforeCreateTime(), productReviewEntity.productReviewInfo.createTime::loe)
                .add(filter.getAfterCreateTime(), productReviewEntity.productReviewInfo.createTime::goe)
                .add(filter.getProductFilter().getProductName(), productReviewEntity.product.productInfo.name::eq)
                .add(filter.getProductFilter().getCategory(), productReviewEntity.product.productInfo.category::eq)
                .add(filter.getUser(), productReviewEntity.user::eq)
                .buildOr();
        return new JPAQuery<ProductReviewEntity>(entityManager)
                .select(productReviewEntity)
                .from(productReviewEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .fetch();
    }


}
