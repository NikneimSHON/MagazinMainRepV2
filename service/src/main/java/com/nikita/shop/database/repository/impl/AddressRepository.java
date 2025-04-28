package com.nikita.shop.database.repository.impl;

import com.nikita.shop.database.repository.filter.FilterAddressRepository;
import com.nikita.shop.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long>, FilterAddressRepository{

    int deleteAllByUserId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM AddressEntity a WHERE a.addressInfo.country = :country " +
            "AND a.addressInfo.city = :city")
    int deleteByCountryAndCity(@Param("country") String country,
                                @Param("city") String city);

}


