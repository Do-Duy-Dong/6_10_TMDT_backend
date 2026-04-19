package com.tmdt.shop_service.modules.users.infrastructure.repository;

import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import com.tmdt.shop_service.modules.users.domain.repo.UsersRepository;
import com.tmdt.shop_service.modules.users.infrastructure.jpa.JpaUsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {
    final JpaUsersRepo jpaUsersRepo;
    final JdbcTemplate jdbcTemplate;

    @Override
    public Users save(Users users) {
        return jpaUsersRepo.save(users);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return jpaUsersRepo.findByEmail(email);
    }

    @Override
    public Optional<Users> findByPhoneNumber(String phoneNumber) {
        return jpaUsersRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Users update(Users users) {
        return jpaUsersRepo.save(users);
    }

//    @Override
//    public List<Role> getAllRolesActiveOfUser(Long userId) {
//        String sql =
//    }
}
