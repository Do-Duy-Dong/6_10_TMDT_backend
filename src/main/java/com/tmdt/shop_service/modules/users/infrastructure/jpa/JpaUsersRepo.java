package com.tmdt.shop_service.modules.users.infrastructure.jpa;

import com.tmdt.shop_service.modules.users.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);
}
