package com.nikita.shop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateRunner {

    public static void main(String[] args) {
        Object connectionPull;
        try (var context = new ClassPathXmlApplicationContext("application.xml")) {
            connectionPull = context.getBean("driver");
            System.out.println(connectionPull);
        }

    }


}
