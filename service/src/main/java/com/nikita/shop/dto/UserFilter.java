package com.nikita.shop.dto;

import lombok.Builder;
import lombok.Value;
import org.hibernate.annotations.Filter;

@Value
@Builder
public class UserFilter {
    String lastName;
    String firstName;
}
