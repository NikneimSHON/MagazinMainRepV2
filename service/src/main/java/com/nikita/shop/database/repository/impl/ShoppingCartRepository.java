package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.ShoppingCartFilter;
import com.nikita.shop.database.entity.ProductEntity;
import com.nikita.shop.database.entity.ShoppingCartEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nikita.shop.database.entity.QProductEntity.productEntity;
import static com.nikita.shop.database.entity.QShoppingCartEntity.shoppingCartEntity;
import static com.nikita.shop.database.entity.QShoppingCartProductEntity.shoppingCartProductEntity;
import static com.nikita.shop.database.entity.QUserEntity.userEntity;

@Repository
public class ShoppingCartRepository extends RepositoryBase<Long, ShoppingCartEntity> {

    public ShoppingCartRepository(EntityManager entityManager) {
        super(entityManager, ShoppingCartEntity.class);
    }

    public List<ShoppingCartEntity> findShoppingCartWithFilter(ShoppingCartFilter filter, int offset, int limit) {
        var predicate = QPredicate.builder()
                .add(filter.getStatus(), shoppingCartEntity.orderStatus::eq)
                .add(filter.getBeforeCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::loe)
                .add(filter.getAfterCreateTime(), shoppingCartEntity.shoppingCartDate.creatTime::goe)
                .add(filter.getUserId(), shoppingCartEntity.user.id::eq)
                .buildAnd();
        return new JPAQuery<ShoppingCartEntity>(super.getEntityManager())
                .select(shoppingCartEntity)
                .from(shoppingCartEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();
    }


    public List<ProductEntity> findProductWithShoppingCart(ShoppingCartEntity shoppingCartEntity) {
        return new JPAQuery<ProductEntity>(super.getEntityManager())
                .select(productEntity)
                .from(shoppingCartProductEntity)
                .where(shoppingCartProductEntity.cart.id.eq(shoppingCartEntity.getId()))
                .fetch();
    }

    public Map<Long, Long> findUserAndShoppingCartByProductID(ProductEntity productEntity) {
        List<Tuple> results = new JPAQuery<Tuple>(super.getEntityManager())
                .select(userEntity.id, shoppingCartEntity.id)
                .from(shoppingCartProductEntity)
                .join(shoppingCartProductEntity.cart, shoppingCartEntity)
                .join(shoppingCartEntity.user, userEntity)
                .where(shoppingCartProductEntity.product.id.eq(productEntity.getId()))
                .fetch();

        if (!results.isEmpty()) {
            return results.stream()
                    .collect(Collectors.toMap(
                            tuple -> tuple.get(userEntity.id),
                            tuple -> tuple.get(shoppingCartEntity.id)
                    ));
        }
        return null;


    }


}
