package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.application.mapper.UserMapper;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import com.tmdt.shop_service.modules.users.domain.repo.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Users> findById(Long userId) {
        return usersRepository.findById(userId);
    }

    @Override
    public List<UserDto> findByIdIn(List<Long> userIds) {
        return UserMapper.INSTANCE.toDtoList(usersRepository.findByIdIn(userIds));
    }

    @Override
    public Page<UserDto> getListByParams(
            Pageable pageable,
            String nameCt,
            String emailCt,
            String phoneNumberCt,
            Integer isActive) {
        return usersRepository.getListByParams(pageable, nameCt, emailCt, phoneNumberCt, isActive);
    }
}
