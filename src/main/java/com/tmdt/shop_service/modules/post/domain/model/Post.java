package com.tmdt.shop_service.modules.post.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post extends AuditableEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "create_by")
    private Long createBy;

    @Column(name = "update_by")
    private Long updateBy;

    @Column(name = "status")
    @Convert(converter = PostStatus.PostStatusConverter.class)
    private PostStatus status;
}
