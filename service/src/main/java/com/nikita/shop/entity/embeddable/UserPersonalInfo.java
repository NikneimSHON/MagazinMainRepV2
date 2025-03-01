package com.nikita.shop.entity.embeddable;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class UserPersonalInfo {
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private String image;
}
