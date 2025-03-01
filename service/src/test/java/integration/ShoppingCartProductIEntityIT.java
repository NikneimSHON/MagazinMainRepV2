package integration;

import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.ShoppingCartProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;


public class ShoppingCartProductIEntityIT extends TransactionManager {

    @Test
    void save() {
        var shoppingCartProductEntity = getShoppingCartProduct();

        session.persist(shoppingCartProductEntity);
        session.flush();
        session.clear();

        Assertions.assertNotNull(shoppingCartProductEntity.getId());
    }

    @Test
    void update() {
        var shoppingCartProductEntity = getShoppingCartProduct();
        session.persist(shoppingCartProductEntity);
        session.flush();
        session.clear();

        var shoppingCartProduct = session.get(ShoppingCartProductEntity.class, shoppingCartProductEntity.getId());
        shoppingCartProduct.setCountProduct(19999);
        session.merge(shoppingCartProduct);
        session.flush();
        session.clear();

        Assertions.assertEquals(19999, session.get(ShoppingCartProductEntity.class, shoppingCartProductEntity.getId()).getCountProduct());
    }

    @Test
    void delete() {
        var shoppingCartProductEntity = getShoppingCartProduct();
        session.persist(shoppingCartProductEntity);
        session.flush();
        session.clear();

        session.remove(shoppingCartProductEntity);
        session.flush();
        session.clear();

        Assertions.assertNull(session.get(ShoppingCartProductEntity.class, shoppingCartProductEntity.getId()));
    }

    @Test
    void get() {
        var shoppingCartProductEntity = getShoppingCartProduct();

        session.persist(shoppingCartProductEntity);
        session.flush();
        session.clear();

        Assertions.assertNotNull(shoppingCartProductEntity.getId());
    }


    private ShoppingCartProductEntity getShoppingCartProduct() {
        return ShoppingCartProductEntity.builder()
                .cart(new ShoppingCartEntity())
                .countProduct(10)
                .product(new ProductEntity())
                .build();
    }
}
