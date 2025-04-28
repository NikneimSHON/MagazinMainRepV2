package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.database.filter.AddressFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterAddressRepository{

    Page<AddressEntity> findAllByFilter(AddressFilter filter, Pageable pageable);

}
