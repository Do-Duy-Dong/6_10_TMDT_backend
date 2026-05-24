package com.tmdt.shop_service.modules.users.domain.repo;

import com.tmdt.shop_service.modules.users.domain.model.Users;
import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Users update(Users users);

    Optional<Users> findById(Long userId);

    List<Users> findByIdIn(List<Long> userIds);
}
