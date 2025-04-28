package com.nikita.shop.database.repository.impl;

import com.nikita.shop.entity.Activity;
import com.nikita.shop.entity.UserEntity;
import com.nikita.shop.database.repository.filter.FilterUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, FilterUserRepository {

    @Query("SELECT u FROM UserEntity u WHERE u.credentials.email = :email AND u.phoneNumber = :phoneNumber")
    Page<UserEntity> findAllByEmailAndPhoneNumber(
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            Pageable pageable
    );

    @Query("select u from UserEntity u where u.userActivity.activity = :activity")
    Page<UserEntity> findAllByActivity(@Param("activity") Activity activity, Pageable pageable);


}
