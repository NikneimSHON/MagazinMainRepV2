package com.nikita.shop.database.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class AddressInfo {
    private String country;
    private String city;
    private String street;
    private Integer houseNumber;
    private Integer apartmentNumber;
}
