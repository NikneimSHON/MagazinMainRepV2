package com.nikita.shop.service;

import com.nikita.shop.database.filter.UserFilter;
import com.nikita.shop.database.repository.impl.UserRepository;
import com.nikita.shop.dto.UserReadDto;
import com.nikita.shop.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;

    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userReadMapper::map);
    }

}
