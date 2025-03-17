package com.nikita.shop.repository.impl;
import com.nikita.shop.dto.PromoCodeFilter;
import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nikita.shop.entity.QPromoCodeEntity.promoCodeEntity;

public class PromoCodeRepository extends RepositoryBase<Long, PromoCodeEntity> {
    public PromoCodeRepository(EntityManager entityManager) {
        super(entityManager, PromoCodeEntity.class);
    }

    public List<PromoCodeEntity> findPromoCodeWithFilterActivityAndDiscountSumAndMinOrderAmountAndValidDate(EntityManager entityManager, PromoCodeFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.isActivityPromo(), promoCodeEntity.activityPromo::eq)
                .add(filter.getMinOrderAmount(), promoCodeEntity.minOrderAmount::goe)
                .add(filter.getValidTo(), promoCodeEntity.validTo::loe)
                .add(filter.getValidFrom(), promoCodeEntity.validFrom::goe)
                .add(filter.getMinDiscountSum(), promoCodeEntity.discountSum::goe)
                .add(filter.getMaxDiscountSum(), promoCodeEntity.discountSum::loe)
                .buildOr();

        return new JPAQuery<PromoCodeEntity>(entityManager)
                .select(promoCodeEntity)
                .from(promoCodeEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
