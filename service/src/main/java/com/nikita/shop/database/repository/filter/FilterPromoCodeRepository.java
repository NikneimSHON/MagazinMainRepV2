package com.nikita.shop.database.repository.filter;

import com.nikita.shop.entity.PromoCodeEntity;
import com.nikita.shop.database.filter.PromoCodeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterPromoCodeRepository {
    Page<PromoCodeEntity> findAllByFilter(PromoCodeFilter filter, Pageable pageable);
}
