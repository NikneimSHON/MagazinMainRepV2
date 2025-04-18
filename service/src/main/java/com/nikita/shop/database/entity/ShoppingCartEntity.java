package com.nikita.shop.database.entity;

import com.nikita.shop.database.entity.embeddable.ShoppingCartDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = {"shoppingCartProducts"})
@Table(name = "shopping_cart")
public class ShoppingCartEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private ShoppingCartDate shoppingCartDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<ShoppingCartProductEntity> shoppingCartProducts = new ArrayList<>();

    @Override
    public String toString() {
        return "ShoppingCartEntity{" +
               "id=" + id +
               ", orderStatus='" + orderStatus + '\'' +
               ", shoppingCartDate=" + shoppingCartDate +
               ", user=" + (user != null ? user.getId() : "null") +
               ", shoppingCartProducts=" + (shoppingCartProducts != null ? shoppingCartProducts.stream()
                .map(ShoppingCartProductEntity::getId).toList() : "null") +
               '}';
    }
}
