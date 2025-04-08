package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.PromoCodeFilter;
import com.nikita.shop.database.entity.PromoCodeEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.nikita.shop.database.entity.QPromoCodeEntity.promoCodeEntity;

import java.util.List;


@Repository
public class PromoCodeRepository extends RepositoryBase<Long, PromoCodeEntity> {

    public PromoCodeRepository(EntityManager entityManager) {
        super(entityManager, PromoCodeEntity.class);
    }

    public List<PromoCodeEntity> findPromoCodeWithFilter(PromoCodeFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.isActivityPromo(), promoCodeEntity.activityPromo::eq)
                .add(filter.getMinOrderAmount(), promoCodeEntity.minOrderAmount::goe)
                .add(filter.getValidTo(), promoCodeEntity.validTo::loe)
                .add(filter.getValidFrom(), promoCodeEntity.validFrom::goe)
                .add(filter.getMinDiscountSum(), promoCodeEntity.discountSum::goe)
                .add(filter.getMaxDiscountSum(), promoCodeEntity.discountSum::loe)
                .buildAnd();

        return new JPAQuery<PromoCodeEntity>(super.getEntityManager())
                .select(promoCodeEntity)
                .from(promoCodeEntity)
                .where(predicate)
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
