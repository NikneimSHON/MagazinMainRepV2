package com.nikita.shop.database.repository.filter.Impl;

import com.nikita.shop.entity.AddressEntity;
import com.nikita.shop.database.repository.filter.FilterAddressRepository;
import com.nikita.shop.database.repository.filter.QPredicate;
import com.nikita.shop.database.filter.AddressFilter;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.nikita.shop.entity.QAddressEntity.addressEntity;

public class FilterAddressRepositoryImpl implements FilterAddressRepository {

    private final JPAQueryFactory queryFactory;

    public FilterAddressRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<AddressEntity> findAllByFilter(AddressFilter filter, Pageable pageable) {
        var predicate = predicateBuilder(filter);

        List<AddressEntity> resultList = queryFactory
                .select(addressEntity)
                .from(addressEntity)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long countElement = queryFactory
                .select(addressEntity)
                .from(addressEntity)
                .where(predicate)
                .fetch().size();

        return new PageImpl<>(resultList, pageable, countElement);

    }


    private Predicate predicateBuilder(AddressFilter filter) {
        return QPredicate.builder()
                .add(filter.getCity(), addressEntity.addressInfo.city::eq)
                .add(filter.getStreet(), addressEntity.addressInfo.street::eq)
                .add(filter.getUserId(), addressEntity.user.id::eq)
                .add(filter.getHouseNumber(), addressEntity.addressInfo.houseNumber::eq)
                .add(filter.getApartmentNumber(), addressEntity.addressInfo.apartmentNumber::eq)
                .add(filter.getCountry(), addressEntity.addressInfo.country::eq)
                .buildAnd();
    }

}
