package com.nikita.shop.dto;

import com.nikita.shop.database.entity.Activity;
import com.nikita.shop.database.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilter {
    String email;
    String phoneNumber;
    LocalDate beforeBirthday;
    LocalDate afterBirthday;
    String firstName;
    String lastName;
    Activity activity;
    Role role;
}
