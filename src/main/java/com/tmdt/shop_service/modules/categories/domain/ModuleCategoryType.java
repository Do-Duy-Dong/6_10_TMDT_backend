package com.tmdt.shop_service.modules.categories.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum ModuleCategoryType implements HasEnumValue {
    LAP_TOP(1);

    final Integer value;
    ModuleCategoryType(Integer value) {
        this.value = value;
    }

    public static final class CategoryTypeConverter implements EnumConverter<ModuleCategoryType> {

        @Override
        public Integer convertToDatabaseColumn(ModuleCategoryType attribute) {
            return attribute.value;
        }

        @Override
        public ModuleCategoryType convertToEntityAttribute(Integer value) {
            for (ModuleCategoryType categoryType : ModuleCategoryType.values()) {
                if (categoryType.value == value) {
                    return categoryType;
                }
            }
            return null;
        }
    }
}
