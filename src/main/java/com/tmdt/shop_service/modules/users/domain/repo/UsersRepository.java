package com.tmdt.shop_service.modules.users.domain.repo;

import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Users save(Users users);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Users update(Users users);

    Optional<Users> findById(Long userId);

    List<Users> findByIdIn(List<Long> userIds);

    Page<UserDto> getListByParams(
            Pageable pageable,
            String nameCt,
            String emailCt,
            String phoneNumberCt,
            Integer isActive);
}
