package com.nikita.shop.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int countProduct;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ShoppingCartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Override
    public String toString() {
        return "ShoppingCartProductEntity{" +
               "id=" + id +
               ", countProduct=" + countProduct +
               ", cart=" + (cart != null ? cart.getId() : "null") +
               ", product=" + (product != null ? product.getId() : "null") +
               '}';
    }

}
