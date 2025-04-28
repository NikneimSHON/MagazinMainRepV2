package Integration.repository.impl;

import Integration.annotation.IT;
import Integration.util.ContainerStarter;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.embeddable.ProductCategory;
import com.nikita.shop.entity.embeddable.ProductInfo;
import com.nikita.shop.database.repository.impl.ProductRepository;
import com.nikita.shop.database.filter.ProductFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestConstructor;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@IT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class ProductRepositoryIT extends ContainerStarter {

    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void save() {
        ProductEntity product = getProductEntity(ProductCategory.BABY_PRODUCTS);
        ProductEntity savedProduct = productRepository.save(product);

        entityManager.flush();
        entityManager.clear();

        Optional<ProductEntity> foundProduct = productRepository.findById(savedProduct.getId());

        assertEquals(savedProduct.getId(), foundProduct.get().getId());
        assertEquals("UNIQUE", foundProduct.get().getProductInfo().getName());
    }

    @Test
    void findById() {
        ProductEntity product = getProductEntity(ProductCategory.BABY_PRODUCTS);
        ProductEntity savedProduct = productRepository.save(product);

        entityManager.flush();
        entityManager.clear();

        Optional<ProductEntity> result = productRepository.findById(savedProduct.getId());

        assertTrue(result.isPresent());
        assertEquals(savedProduct.getId(), result.get().getId());
    }

    @Test
    void update() {
        ProductEntity product = getProductEntity(ProductCategory.BABY_PRODUCTS);
        ProductEntity savedProduct = productRepository.save(product);

        entityManager.flush();
        entityManager.clear();

        String newName = "Updated Name";
        ProductEntity toUpdate = productRepository.findById(savedProduct.getId())
                .orElseThrow();
        toUpdate.getProductInfo().setName(newName);
        productRepository.save(toUpdate);

        entityManager.flush();
        entityManager.clear();

        ProductEntity updated = productRepository.findById(savedProduct.getId())
                .orElseThrow();
        assertEquals(newName, updated.getProductInfo().getName());
    }

    @Test
    void delete() {
        ProductEntity product = getProductEntity(ProductCategory.BABY_PRODUCTS);
        ProductEntity savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        entityManager.flush();
        entityManager.clear();

        Optional<ProductEntity> deleted = productRepository.findById(savedProduct.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void deleteByCategory() {
        productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        productRepository.save(getProductEntity(ProductCategory.AUTOMOTIVE));

        entityManager.flush();
        entityManager.clear();

        int deletedCount = productRepository.deleteByCategory(ProductCategory.BABY_PRODUCTS);

        assertEquals(1, deletedCount);
        assertEquals(1, productRepository.count());
    }

    @Test
    void updateAllByCategory() {
        ProductEntity p1 = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        ProductEntity p2 = productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));

        entityManager.flush();
        entityManager.clear();

        ProductCategory newCategory = ProductCategory.FOOD;
        int updatedCount = productRepository.updateAllByCategory(ProductCategory.BABY_PRODUCTS, newCategory);

        entityManager.flush();
        entityManager.clear();

        ProductEntity updated1 = productRepository.findById(p1.getId()).orElseThrow();
        ProductEntity updated2 = productRepository.findById(p2.getId()).orElseThrow();

        assertEquals(2, updatedCount);
        assertEquals(newCategory, updated1.getProductInfo().getCategory());
        assertEquals(newCategory, updated2.getProductInfo().getCategory());
    }

    @Test
    void findAllByFilter_NoFilter() {
        productRepository.save(getProductEntity(ProductCategory.BABY_PRODUCTS));
        productRepository.save(getProductEntity2(ProductCategory.BABY_PRODUCTS));

        ProductFilter filter = ProductFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ProductEntity> page = productRepository.findAllByFilter(filter,pageable);


        assertEquals(2, page.getTotalElements());
    }

    @Test
    void findAllByFilter_WithCategory() {
        productRepository.save(getProductEntity(ProductCategory.GARDEN));
        productRepository.save(getProductEntity2(ProductCategory.GARDEN));

        ProductFilter filter = ProductFilter.builder()
                .category(ProductCategory.BABY_PRODUCTS)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        entityManager.flush();
        entityManager.clear();

        Page<ProductEntity> page = productRepository.findAllByFilter(filter, pageable);

        assertEquals(0, page.getTotalElements());

    }

    private ProductEntity getProductEntity(ProductCategory category) {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .productImage("F")
                        .count(2)
                        .name("UNIQUE")
                        .description("Fake")
                        .price(212)
                        .category(category)
                        .build())
                .available(true)
                .createAt(Instant.now())
                .build();
    }

    private ProductEntity getProductEntity2(ProductCategory category) {
        return ProductEntity.builder()
                .productInfo(ProductInfo.builder()
                        .productImage("Fа")
                        .count(23)
                        .name("fake")
                        .description("Fake")
                        .price(2132)
                        .category(category)
                        .build())
                .available(true)
                .createAt(Instant.now())
                .build();
    }
}
