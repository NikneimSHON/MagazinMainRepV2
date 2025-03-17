package com.nikita.shop.dto;

import com.nikita.shop.entity.UserEntity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressFilter {
    String country;
    String city;
    String street;
    int houseNumber;
    int apartmentNumber;
    UserEntity user;
}
