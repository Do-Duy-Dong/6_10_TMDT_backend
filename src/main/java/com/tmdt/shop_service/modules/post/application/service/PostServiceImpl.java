package com.tmdt.shop_service.modules.post.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.application.mapper.PostMapper;
import com.tmdt.shop_service.modules.post.application.request.CreatePostRequest;
import com.tmdt.shop_service.modules.post.application.request.UpdatePostRequest;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import com.tmdt.shop_service.modules.post.domain.model.Post;
import com.tmdt.shop_service.modules.post.domain.repo.PostRepo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    final PostRepo postRepo;

    @Override
    public PostDto create(@NotNull CreatePostRequest request, @NotNull Long userId) {
        Post post = new Post(
                request.getTitle(),
                request.getDescription(),
                userId,
                null,
                request.getStatus());

        post = postRepo.save(post);

        return PostMapper.INSTANCE.toDto(post);
    }

    @Override
    public PostDto update(
            @NotNull Long postId,
            @NotNull UpdatePostRequest request,
            @NotNull Long userId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post Not Found"));
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setUpdateBy(userId);
        post.setStatus(request.getStatus());
        post  = postRepo.save(post);
        return PostMapper.INSTANCE.toDto(post);
    }

    @Override
    public void updateStatus(Long postId, PostStatus status) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post Not Found"));
        post.setStatus(status);
        postRepo.save(post);
    }

    @Override
    public void deletePost(@NotNull Long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post Not Found"));
        postRepo.delete(id);
    }

    @Override
    public PostDto getById(Long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post Not Found"));
        return PostMapper.INSTANCE.toDto(post);
    }

    @Override
    public Page<PostDto> getList(
            Pageable pageable,
            String titleCt,
            LocalDateTime createAtGe,
            LocalDateTime createAtLe,
            List<PostStatus> statuses) {
        List<Integer> listStatus = new ArrayList<>();
        if  (statuses != null && !statuses.isEmpty()) {
            listStatus = statuses.stream().map(PostStatus::getValue).toList();
        } else {
            listStatus.add(PostStatus.ACTIVE.getValue());
        }
        return postRepo.getList(pageable, titleCt, createAtGe, createAtLe, listStatus);
    }

    @Override
    public PostDto getPostByIdHasStatusActive(Long id) {
        PostDto postDto = getById(id);
        if (PostStatus.ACTIVE.equals(postDto.getStatus())) {
            throw new ResourceNotFoundException("Post Not Found");
        }
        return postDto;
    }
}
