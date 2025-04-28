package com.nikita.shop.mapper;

import com.nikita.shop.dto.UserReadDto;
import com.nikita.shop.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<UserEntity, UserReadDto> {

    @Override
    public UserReadDto map(UserEntity object) {
        return UserReadDto.builder()
                .firstname(object.getPersonalInfo().getFirstname())
                .lastname(object.getPersonalInfo().getLastname())
                .phoneNumber(object.getPhoneNumber())
                .role(object.getRole())
                .activity(object.getUserActivity().getActivity())
                .email(object.getCredentials().getEmail())
                .birthDate(object.getPersonalInfo().getBirthDate())
                .build();
    }

}
