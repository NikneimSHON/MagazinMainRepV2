package com.nikita.shop;

import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApplicationRunner {

    public static void main(String[] args) {
        var context = SpringApplication.run(ApplicationRunner.class, args);
        var userRepository = context.getBean(UserRepository.class);
        userRepository.findAll().forEach(System.out::println);


    }


}