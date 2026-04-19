package com.tmdt.shop_service.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity extends BaseEntity {
    @CreatedDate
    @Column(name = "create_at", updatable = false)
    protected LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "update_at")
    protected LocalDateTime updateAt;
}
