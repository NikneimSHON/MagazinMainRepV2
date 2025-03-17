package integration.entity;

import com.nikita.shop.entity.OrderStatus;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.embeddable.ShoppingCartDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TransactionManager;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShoppingCartEntityIT extends TransactionManager {

    @Test
    void save() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();

        session.persist(shoppingCartEntity);
        session.flush();
        session.clear();

        assertNotNull(shoppingCartEntity.getId());
    }

    @Test
    void get() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();

        session.persist(shoppingCartEntity);
        session.flush();
        session.clear();

        assertNotNull(shoppingCartEntity.getId());
    }

    @Test
    void update() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();
        session.persist(shoppingCartEntity);

        var shoppingCart = session.get(ShoppingCartEntity.class, shoppingCartEntity.getId());
        shoppingCart.setOrderStatus(OrderStatus.ASSEMBLY);
        session.merge(shoppingCart);
        session.flush();
        session.clear();

       Assertions.assertEquals("topFAKE", shoppingCart.getOrderStatus());
    }

    @Test
    void delete() {
        ShoppingCartEntity shoppingCartEntity = getShoppingCart();
        session.persist(shoppingCartEntity);
        session.flush();
        session.clear();

        session.remove(shoppingCartEntity);
        session.flush();
        session.clear();

        Assertions.assertNull(session.get(ShoppingCartEntity.class, shoppingCartEntity.getId()));
    }


    private ShoppingCartEntity getShoppingCart() {
        return ShoppingCartEntity.builder()
                .orderStatus(OrderStatus.ASSEMBLY)
                .shoppingCartDate(ShoppingCartDate.builder()
                        .updateTime(Instant.now())
                        .creatTime(Instant.now())
                        .build())
                .build();
    }
}
