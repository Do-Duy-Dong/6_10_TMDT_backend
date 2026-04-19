package com.tmdt.shop_service.modules.users.domain.repo;

import com.tmdt.shop_service.modules.users.domain.model.Users;

import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Users update(Users users);
}
