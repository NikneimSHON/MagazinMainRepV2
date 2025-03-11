package integration.dao;

import com.nikita.shop.dao.ProductDao;
import com.nikita.shop.dto.ProductFilter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.entity.embeddable.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;
import java.util.List;

import static com.mysema.commons.lang.Assert.assertThat;

public class ProductDaoIT extends TransactionManager {
    private final ProductDao productDao = ProductDao.getInstance();

    @Test
    void findProductByPriceAndCategory() {
        ProductFilter filter = ProductFilter.builder()
                .maxPrice(120)
                .minPrice(90)
                .category(ProductCategory.BABY_PRODUCTS)
                .build();
        session.persist(getProduct1());
        session.persist(getProduct2());
        session.clear();
        List<ProductEntity> productResult = productDao.findProductByPriceAndCategory(session, filter);

        for (ProductEntity productEntity : productResult) {
            Assertions.assertEquals(productEntity.getProductInfo().getCategory(), ProductCategory.BABY_PRODUCTS);
            Assertions.assertTrue(productEntity.getProductInfo().getPrice() >= 90);
            Assertions.assertTrue(productEntity.getProductInfo().getPrice() <= 120);
        }

    }

    private ProductEntity getProduct1() {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .price(100)
                        .productImage("f")
                        .category(ProductCategory.BABY_PRODUCTS)
                        .count(4)
                        .description("fa")
                        .build())
                .createTime(Instant.now())
                .available(true)
                .build();
    }

    private ProductEntity getProduct2() {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .price(30)
                        .productImage("f")
                        .category(ProductCategory.BABY_PRODUCTS)
                        .count(4)
                        .description("fa")
                        .build())
                .createTime(Instant.now())
                .available(true)
                .build();
    }


}
