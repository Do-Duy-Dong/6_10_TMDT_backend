package com.tmdt.shop_service.modules.categories.domain.model;

import com.tmdt.shop_service.core.entity.BaseEntity;
import com.tmdt.shop_service.modules.categories.domain.ModuleCategoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "module_categories")
public class ModuleCategory extends BaseEntity {
    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "type")
    @Convert(converter = ModuleCategoryType.CategoryTypeConverter.class)
    private ModuleCategoryType type;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "is_active")
    private Integer isActive;
}
