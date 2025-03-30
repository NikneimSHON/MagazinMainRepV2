package com.nikita.shop.config;

import com.nikita.shop.database.repository.Repository;
import com.nikita.shop.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.nikita.shop",
        useDefaultFilters = false,
        includeFilters = {
                @Filter(type = FilterType.ANNOTATION, value = Component.class),
                @Filter(type = FilterType.ASSIGNABLE_TYPE, value = Repository.class),
                @Filter(type = FilterType.REGEX, pattern = "com\\..+Repository"),

        }
)

public class ApplicationConfiguration {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean
    public Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(
                SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }

}
