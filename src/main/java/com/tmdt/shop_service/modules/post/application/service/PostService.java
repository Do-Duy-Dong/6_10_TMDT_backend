package com.tmdt.shop_service.modules.post.application.service;

import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.application.request.CreatePostRequest;
import com.tmdt.shop_service.modules.post.application.request.UpdatePostRequest;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    PostDto create(@NotNull CreatePostRequest request, @NotNull Long userId);

    PostDto update(
            @NotNull Long postId,
            @NotNull UpdatePostRequest request,
            @NotNull Long userId);

    void updateStatus(Long postId, PostStatus status);

    void deletePost(@NotNull Long id);

    PostDto getById(Long id);

    Page<PostDto> getList(
            Pageable pageable,
            String titleCt,
            LocalDateTime createAtGe,
            LocalDateTime createAtLe,
            List<PostStatus> statuses);

    PostDto getPostByIdHasStatusActive(Long id);
}
