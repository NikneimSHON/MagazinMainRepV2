package com.nikita.shop.database.repository.impl;

import com.nikita.shop.dto.AddressFilter;
import com.nikita.shop.database.entity.AddressEntity;
import com.nikita.shop.database.repository.QPredicate;
import com.nikita.shop.database.repository.RepositoryBase;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nikita.shop.database.entity.QAddressEntity.addressEntity;

@Repository
public class AddressRepository extends RepositoryBase<Long, AddressEntity> {

    public AddressRepository(EntityManager entityManager) {
        super(entityManager, AddressEntity.class);
    }

    public List<AddressEntity> findAddressWithFilter(EntityManager entityManager, AddressFilter filter, int limit, int offset) {
        var predicate = QPredicate.builder()
                .add(filter.getCity(), addressEntity.addressInfo.city::eq)
                .add(filter.getStreet(), addressEntity.addressInfo.street::eq)
                .add(filter.getUserId(), addressEntity.user.id::eq)
                .add(filter.getHouseNumber(), addressEntity.addressInfo.houseNumber::eq)
                .add(filter.getApartmentNumber(), addressEntity.addressInfo.apartmentNumber::eq)
                .add(filter.getCountry(), addressEntity.addressInfo.country::eq)
                .buildAnd();
        return new JPAQuery<AddressEntity>(entityManager)
                .select(addressEntity)
                .from(addressEntity)
                .where(predicate)
                .limit(limit)
                .offset(offset)
                .fetch();
    }


}
