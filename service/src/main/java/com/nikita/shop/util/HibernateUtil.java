package com.nikita.shop.util;

import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.entity.ProductEntity;
import com.nikita.shop.entity.ProductReviewEntity;
import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.entity.Role;
import com.nikita.shop.entity.ShoppingCartEntity;
import com.nikita.shop.entity.ShoppingCartProductEntity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.entity.UserPromoCodeEntity;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(PromoCodeEntity.class);
        configuration.addAnnotatedClass(UserPromoCodeEntity.class);
        configuration.addAnnotatedClass(AddressEntity.class);
        configuration.addAnnotatedClass(ProductEntity.class);
        configuration.addAnnotatedClass(ProductReviewEntity.class);
        configuration.addAnnotatedClass(ShoppingCartEntity.class);
        configuration.addAnnotatedClass(ShoppingCartProductEntity.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Activity.class);

        return configuration;
    }

}
