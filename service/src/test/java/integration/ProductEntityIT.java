package integration;

import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.embeddable.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductEntityIT extends TransactionManager {

    @Test
    void save() {
        ProductEntity productEntity = getProduct();

        session.persist(productEntity);
        session.flush();
        session.clear();

        assertNotNull(productEntity.getId());
    }

    @Test
    void get() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);
        session.flush();
        session.clear();

        ProductEntity retrievedProductEntity = session.get(ProductEntity.class, productEntity.getId());

        assertNotNull(retrievedProductEntity);
    }

    @Test
    void update() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);
        session.flush();
        session.clear();

        var product = session.get(ProductEntity.class, productEntity.getId());
        product.setAvailable(false);
        session.merge(product);
        session.flush();
        session.clear();


        var resultProductEntity = session.get(ProductEntity.class, productEntity.getId());
        Assertions.assertFalse(resultProductEntity.isAvailable());
    }

    @Test
    void delete() {
        ProductEntity productEntity = getProduct();
        session.persist(productEntity);
        session.flush();
        session.clear();

        session.remove(productEntity);
        session.flush();
        session.clear();

        Assertions.assertNull(session.get(ProductEntity.class, productEntity.getId()));

    }

    private ProductEntity getProduct() {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .price(new BigDecimal("100"))
                        .productImage("f")
                        .category("f")
                        .count(4)
                        .description("fa")
                        .build())
                .createTime(Instant.now())
                .available(true)
                .build();
    }
}
