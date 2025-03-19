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
    Integer houseNumber;
    Integer apartmentNumber;
    Long userId;
}
