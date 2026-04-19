package com.tmdt.shop_service.modules.post.infrastructure.jpa;

import com.tmdt.shop_service.modules.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostRepo extends JpaRepository<Post, Long> {

}
