package com.nikita.shop.service;

import com.nikita.shop.database.repository.Repository;
import com.nikita.shop.database.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Repository<Long, UserEntity> userRepository;

    public UserService(Repository<Long, UserEntity> userRepository) {
        this.userRepository = userRepository;
    }
}
