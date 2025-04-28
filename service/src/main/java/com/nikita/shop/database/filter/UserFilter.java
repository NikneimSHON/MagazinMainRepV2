package com.nikita.shop.database.filter;

import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
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
