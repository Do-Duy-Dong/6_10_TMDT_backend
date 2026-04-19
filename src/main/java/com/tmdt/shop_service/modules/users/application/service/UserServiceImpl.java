package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import com.tmdt.shop_service.modules.users.domain.repo.UsersRepository;
import com.tmdt.shop_service.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UsersRepository usersRepository;

    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Users findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @Override
    public Users save(Users users) {
        try {
            return usersRepository.save(users);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Email or phone number already in use");
        }
    }
}
