package com.tmdt.shop_service.modules.users.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.users.domain.GenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"phone_number"}),
})
public class Users extends AuditableEntity {
    @Column(name = "name")
    private String fullName;

    @Column(name = "gender")
    @Convert(converter = GenderType.GenderTypeConverter.class)
    private GenderType gender;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active", nullable = false)
    private Integer isActive;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "salt", nullable = false)
    private String salt;
}
