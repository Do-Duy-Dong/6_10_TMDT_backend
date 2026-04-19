package com.tmdt.shop_service.modules.attaches.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attach")
public class Attaches extends AuditableEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Integer is_active;


    @Column(name = "type")
    @Convert(converter = AttachType.AttachTypeConverter.class)
    private AttachType type;

    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "owner_id")
    private Long ownerId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attach_metadata")
    private AttachMetadata attachMetadata;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attach_storage_metadata")
    private AttachStorageMetadata attachStorageMetadata;
}
