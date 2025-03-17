package com.nikita.shop.entity;

import com.nikita.shop.entity.embeddable.ProductInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "WithPriceAndCategoryAndAvailable"
        , attributeNodes = {@NamedAttributeNode("productInfo"), @NamedAttributeNode("available")}

)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = {"shoppingCartProducts", "productReviews"})
@Table(name = "product")
public class ProductEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createTime;

    private boolean available;

    @Embedded
    private ProductInfo productInfo;

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShoppingCartProductEntity> shoppingCartProducts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProductReviewEntity> productReviews = new ArrayList<>();

    @Override
    public String toString() {
        return "ProductEntity{" +
               "id=" + id +
               ", shoppingCartProduct=" + (shoppingCartProducts != null ? shoppingCartProducts.stream()
                .map(ShoppingCartProductEntity::getId)
                .toList() : "null") +
               ", productReview=" + (productReviews != null ? productReviews.stream()
                .map(ProductReviewEntity::getId)
                .toList() : "null") +
               '}';
    }
}
