package config;

import com.nikita.shop.config.ApplicationConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import util.HibernateTestUtil;

@Configuration
@Import(ApplicationConfiguration.class)
public class ApplicationTestConfiguration {

    @Bean
    @Primary
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }

    @Bean
    @Primary
    public Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(
                SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
