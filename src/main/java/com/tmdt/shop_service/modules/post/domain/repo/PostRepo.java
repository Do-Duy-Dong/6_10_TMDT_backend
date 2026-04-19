package com.tmdt.shop_service.modules.post.domain.repo;

import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepo {
    Post save(Post post);

    Optional<Post> findById(Long postId);

    void delete(Long id);

    Page<PostDto> getList(
            Pageable pageable,
            String titleCt,
            LocalDateTime createAtGe,
            LocalDateTime createAtLe,
            List<Integer> statuses);
}
