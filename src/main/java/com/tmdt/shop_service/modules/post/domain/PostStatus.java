package com.tmdt.shop_service.modules.post.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum PostStatus implements HasEnumValue {
    DELETED(-1),
    DRAFT(0),
    ACTIVE(1);

    private final Integer value;
    PostStatus(Integer value) {
        this.value = value;
    }

    public static final class PostStatusConverter implements EnumConverter<PostStatus> {

        @Override
        public Integer convertToDatabaseColumn(PostStatus attribute) {
            return attribute.value;
        }

        @Override
        public PostStatus convertToEntityAttribute(Integer value) {
            for (PostStatus postStatus : PostStatus.values()) {
                if (postStatus.value.equals(value)) {
                    return postStatus;
                }
            }
            return null;
        }
    }
}
