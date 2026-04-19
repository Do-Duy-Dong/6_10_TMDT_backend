package com.tmdt.shop_service.modules.post.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.application.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/posts")
@Tag(name = "Public api for post")
public class PostController {
    final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        var result = postService.getPostByIdHasStatusActive(id);
        return ResponseEntity.ok().body(result);
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
            @RequestParam(value = "title:le", required = false) LocalDateTime createAtLe) {

        Page<PostDto> page = postService.getList(pageable, titleCt, createAtGe, createAtLe, null);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<PostDto>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
