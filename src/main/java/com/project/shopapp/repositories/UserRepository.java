package com.project.shopapp.repositories;

import com.project.shopapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT o from User o where o.active = true AND (:keyword is null or :keyword = '' or " +
            "o.fullName like %:keyword% " +
            "or o.address like %:keyword% " +
            "or o.phoneNumber like %:keyword%) " +
            "and lower(o.role.name) = 'user'"
    )
    Page<User> findAll(@Param("keyword") String keyword, Pageable pageable);

}
