package com.tmdt.shop_service.modules.users.infrastructure.repository;

import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.GenderType;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import com.tmdt.shop_service.modules.users.domain.repo.UsersRepository;
import com.tmdt.shop_service.modules.users.infrastructure.jpa.JpaUsersRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {
    final JpaUsersRepo jpaUsersRepo;
    final JdbcTemplate jdbcTemplate;
    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    @Override
    public Optional<Users> findById(Long userId) {
        return jpaUsersRepo.findById(userId);
    }

    @Override
    public List<Users> findByIdIn(List<Long> userIds) {
        return jpaUsersRepo.findByIdIn(userIds);
    }

    @Override
    public Page<UserDto> getListByParams(
            Pageable pageable,
            String nameCt,
            String emailCt,
            String phoneNumberCt,
            Integer isActive) {
        Map<String, Object> params = new HashedMap<>();
        StringBuilder sql = new StringBuilder("select u.id as id,\n" +
                "       u.name as name,\n" +
                "       u.email as email,\n" +
                "       u.gender as gender,\n" +
                "       u.phone_number as phone_number,\n" +
                "       u.is_active as is_active,\n" +
                "       count(u.id) over() as total_element\n" +
                "from users u\n" +
                "where 1 = 1\n");
        if (nameCt != null && !nameCt.isEmpty()) {
            sql.append("and u.name like :nameCt\n");
            params.put("nameCt", StringUtils.likeLowerContainString(nameCt));
        }
        if (emailCt != null && !emailCt.isEmpty()) {
            sql.append("and u.email like :emailCt\n");
            params.put("emailCt", StringUtils.likeLowerContainString(emailCt));
        }
        if (phoneNumberCt != null && !phoneNumberCt.isEmpty()) {
            sql.append("and u.phone_number like :pnCt\n");
            params.put("pnCt", StringUtils.likeLowerContainString(phoneNumberCt));
        }
        if (isActive != null) {
            sql.append("and u.is_active = :isActive\n");
            params.put("isActive", (isActive != 0 && isActive != 1) ? 0 : isActive);
        }
        List<String> sortFields = List.of("id", "email", "phone_number");
        sql.append(StringUtils.genDirection(sortFields, pageable, "u"));
        AtomicLong totalElemts = new AtomicLong();
        GenderType.GenderTypeConverter converter = new GenderType.GenderTypeConverter();
        List<UserDto> userDtos = namedParameterJdbcTemplate.query(sql.toString(), params, (result, rowNumber) -> {
            totalElemts.set(result.getLong("total_element"));
            return new UserDto(
                    result.getLong("id"),
                    result.getString("name"),
                    converter.convertToEntityAttribute(result.getInt("gender")),
                    null,
                    result.getInt("is_active"),
                    result.getString("email"),
                    result.getString("phone_number"),
                    null);
        }).stream().toList();

        return new PageImpl<>(userDtos, pageable, totalElemts.get());
    }
}
