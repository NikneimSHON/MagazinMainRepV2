package com.nikita.shop.repository.impl;

import com.nikita.shop.dto.AddressFilter;
import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.repository.QPredicate;
import com.nikita.shop.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.nikita.shop.entity.QAddressEntity.addressEntity;


public class AddressRepository extends RepositoryBase<Long, AddressEntity> {

    public AddressRepository(EntityManager entityManager) {
        super(entityManager, AddressEntity.class);
    }

    public List<AddressEntity> findAddressWithFilterUserAndCityAndStreetAndApartAndHouseNumAndCounty(EntityManager entityManager, AddressFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.getCity(), addressEntity.addressInfo.city::eq)
                .add(filter.getStreet(), addressEntity.addressInfo.street::eq)
                .add(filter.getUser(), addressEntity.user::eq)
                .add(filter.getHouseNumber(), addressEntity.addressInfo.houseNumber::eq)
                .add(filter.getApartmentNumber(), addressEntity.addressInfo.apartmentNumber::eq)
                .add(filter.getCountry(), addressEntity.addressInfo.country::eq)
                .buildOr();
        return new JPAQuery<AddressEntity>(entityManager)
                .select(addressEntity)
                .from(addressEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();
    }


}
