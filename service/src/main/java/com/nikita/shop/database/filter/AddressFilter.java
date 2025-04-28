package com.nikita.shop.database.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressFilter {
    String country;
    String city;
    String street;
    Integer houseNumber;
    Integer apartmentNumber;
    Long userId;
}
