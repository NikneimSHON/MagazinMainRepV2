package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.database.repository.filter.FilterPromoCodeRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.PromoCodeFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.nikita.shop.entity.QPromoCodeEntity.promoCodeEntity;

public class FilterPromoCodeRepositoryImpl implements FilterPromoCodeRepository {
    private final JPAQueryFactory queryFactory;

    public FilterPromoCodeRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<PromoCodeEntity> findAllByFilter(PromoCodeFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<PromoCodeEntity> resultList = queryFactory
                .select(promoCodeEntity)
                .from(promoCodeEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(promoCodeEntity)
                .from(promoCodeEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);
    }

    private Predicate predicateBuilder(PromoCodeFilter filter) {
        return QPredicate.builder()
                .add(filter.getActivityPromo(), promoCodeEntity.activityPromo::eq)
                .add(filter.getMinOrderAmount(), promoCodeEntity.minOrderAmount::goe)
                .add(filter.getValidTo(), promoCodeEntity.validTo::loe)
                .add(filter.getValidFrom(), promoCodeEntity.validFrom::goe)
                .add(filter.getMinDiscountSum(), promoCodeEntity.discountSum::goe)
                .add(filter.getMaxDiscountSum(), promoCodeEntity.discountSum::loe)
                .buildAnd();

    }
}
