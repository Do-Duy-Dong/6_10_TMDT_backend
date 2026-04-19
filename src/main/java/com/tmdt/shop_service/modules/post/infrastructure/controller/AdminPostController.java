package com.tmdt.shop_service.modules.post.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.application.request.CreatePostRequest;
import com.tmdt.shop_service.modules.post.application.request.UpdatePostRequest;
import com.tmdt.shop_service.modules.post.application.service.PostService;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Post Controller")
@RequestMapping("/v1/admin/posts")
public class AdminPostController {
    final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        PostDto postDto = postService.create(request, userDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        PostDto postDto = postService.update(id, request, userDetail.getId());
        return ResponseEntity.ok(postDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Api thay đổi trạng thái của bài viết")
    public ResponseEntity updateStatusForPost(
            @PathVariable Long id,
            @RequestParam(value = "status") PostStatus status) {
        postService.updateStatus(id, status);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Api xóa cứng")
    public ResponseEntity deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public CollectionResponse<PostDto> getList(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "title:ct", required = false) String titleCt,
            @RequestParam(value = "createAt:ge", required = false) LocalDateTime createAtGe,
            @RequestParam(value = "title:le", required = false) LocalDateTime createAtLe,
            @RequestParam(value = "status:in", required = false) List<PostStatus> statuses) {

        Page<PostDto> page = postService.getList(pageable, titleCt, createAtGe, createAtLe, statuses);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<PostDto>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
