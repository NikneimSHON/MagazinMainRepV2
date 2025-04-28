package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.database.repository.filter.FilterPromoCodeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCodeEntity, Long>, FilterPromoCodeRepository {


}
