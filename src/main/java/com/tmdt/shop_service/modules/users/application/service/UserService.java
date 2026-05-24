package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Users findByEmail(String email);

    Users findByPhoneNumber(String phoneNumber);

    Users save(Users users);

    Optional<Users> findById(Long userId);

    List<UserDto> findByIdIn(List<Long> userIds);

    Page<UserDto> getListByParams(
            Pageable pageable,
            String nameCt,
            String emailCt,
            String phoneNumberCt,
            Integer isActive);
}
