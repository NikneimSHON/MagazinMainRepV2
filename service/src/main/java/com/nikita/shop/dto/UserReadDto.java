package com.nikita.shop.dto;

import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserReadDto {
    String firstname;
    String lastname;
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Role role;

    @Enumerated(EnumType.STRING)
    Activity activity;

    String email;
    LocalDate birthDate;
}
