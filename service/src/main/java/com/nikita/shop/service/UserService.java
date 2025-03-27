package com.nikita.shop.service;

import com.nikita.shop.database.repository.impl.AddressRepository;
import com.nikita.shop.database.repository.impl.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }
}
