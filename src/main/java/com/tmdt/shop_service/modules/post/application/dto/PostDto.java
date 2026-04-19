package com.tmdt.shop_service.modules.post.application.dto;

import com.tmdt.shop_service.modules.post.domain.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private PostStatus status;
}
