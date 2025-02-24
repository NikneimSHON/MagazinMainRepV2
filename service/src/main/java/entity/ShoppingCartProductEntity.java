package entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cart","product"})
public class ShoppingCartProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int countProduct;

    @ManyToOne
    private ShoppingCartEntity cart;

    @ManyToOne
    private ProductEntity product;

    public void setCart(ShoppingCartEntity cart) {
        this.cart = cart;
        this.cart.getShoppingCartProducts().add(this);
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
        this.product.getShoppingCartProducts().add(this);
    }



}
