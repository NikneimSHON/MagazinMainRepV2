package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.database.repository.filter.FilterShoppingCartRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.ShoppingCartFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.nikita.shop.entity.QShoppingCartEntity.shoppingCartEntity;

public class FilterShoppingCartRepositoryImpl implements FilterShoppingCartRepository {
    private final JPAQueryFactory queryFactory;

    public FilterShoppingCartRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ShoppingCartEntity> findAllByFilter(ShoppingCartFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<ShoppingCartEntity> resultList = queryFactory
                .select(shoppingCartEntity)
                .from(shoppingCartEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(shoppingCartEntity)
                .from(shoppingCartEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);
    }

    private Predicate predicateBuilder(ShoppingCartFilter filter) {
        return QPredicate.builder()
                .add(filter.getStatus(), shoppingCartEntity.orderStatus::eq)
                .add(filter.getBeforeCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::loe)
                .add(filter.getAfterCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::goe)
                .add(filter.getUserId(), shoppingCartEntity.user.id::eq)
                .buildAnd();

    }

}

