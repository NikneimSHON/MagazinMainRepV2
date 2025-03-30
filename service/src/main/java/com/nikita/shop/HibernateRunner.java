package com.nikita.shop;

import com.nikita.shop.config.ApplicationConfiguration;
import com.nikita.shop.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class HibernateRunner {

    public static void main(String[] args) {
        Object service;
        try (var context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            Arrays.stream(context.getBeanDefinitionNames())
                    .map(name -> "Bean: " + name + " : " + context.getType(name))
                    .sorted()
                    .forEach(System.out::println);
        }


    }


}
