package com.nikita.shop.dao;

import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.entity.ProductEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.nikita.shop.entity.QProductEntity.productEntity;

public final class ProductDao {
    private static ProductDao instance;

    private ProductDao() {

    }

    public static ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDao();

        }
        return instance;
    }

    public List<ProductEntity> findProductByPriceAndCategory(Session session, ProductFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.getMaxPrice(), productEntity.productInfo.price::loe)
                .add(filter.getMinPrice(), productEntity.productInfo.price::goe)
                .add(filter.getCategory(), productEntity.productInfo.category::eq)
                .buildAnd();
        return new JPAQuery<ProductEntity>(session)
                .select(productEntity)
                .from(productEntity)
                .where(predicate)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("WithPriceAndCategory"))
                .fetch();
    }
}
