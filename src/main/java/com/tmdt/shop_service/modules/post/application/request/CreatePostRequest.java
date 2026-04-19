package com.tmdt.shop_service.modules.post.application.request;

import com.tmdt.shop_service.modules.post.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private PostStatus status;
}
