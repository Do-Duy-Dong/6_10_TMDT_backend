package com.tmdt.shop_service.modules.post.infrastructure.repo;

import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import com.tmdt.shop_service.modules.post.domain.model.Post;
import com.tmdt.shop_service.modules.post.domain.repo.PostRepo;
import com.tmdt.shop_service.modules.post.infrastructure.jpa.JpaPostRepo;
import com.tmdt.shop_service.utils.StringUtils;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PostRepoImpl implements PostRepo {
    private final List<String> sortFields = List.of("title", "create_at", "id", "status");
    final JpaPostRepo jpaPostRepo;
    final NamedParameterJdbcTemplate paramterJdbcTemplate;

    @Override
    public Post save(Post post) {
        return jpaPostRepo.save(post);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return jpaPostRepo.findById(postId);
    }

    @Override
    public void delete(Long id) {
        jpaPostRepo.deleteById(id);
    }

    @Override
    public Page<PostDto> getList(Pageable pageable, String titleCt, LocalDateTime createAtGe, LocalDateTime createAtLe, List<Integer> status) {
        try {
            Map<String, Object> params = new HashMap<>();
            String selectSql = "select * from posts\n";
            String countSql = "select count(*) from posts\n";

            String condition = "where status in (:statuses) \n";

            params.put("statuses", status);
            if (titleCt != null) {
                condition += "  and lower(title) like :titleCt \n";
                params.put("titleCt", StringUtils.likeLowerContentString(titleCt));
            }
            if  (createAtGe != null) {
                condition += "  and create_at >= :createAtGe \n";
                params.put("createAtGe" , createAtGe);
            }
            if (createAtLe != null) {
                condition += "  and create_at <= :createAtLe \n";
                params.put("createAtLe", createAtLe);
            }

            countSql += condition;
            selectSql += condition + genDirection(pageable);

            Long total = paramterJdbcTemplate.queryForObject(countSql, params, Long.class);

            PostStatus.PostStatusConverter postStatusConverter = new PostStatus.PostStatusConverter();
            List<PostDto> postDtoList = paramterJdbcTemplate.query(selectSql, params, (rs, rowNumber) -> {
                Timestamp createAt = rs.getTimestamp("create_at");
                Timestamp updateAt = rs.getTimestamp("update_at");
                PostDto postDto = PostDto.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                        .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                        .status(postStatusConverter.convertToEntityAttribute(rs.getInt("status")))
                        .build();
                return postDto;
            });

            return new PageImpl<>(postDtoList, pageable, total);
        } catch (Exception e) {
            throw e;
        }
    }

    public String genDirection(Pageable pageable) {
        String paging = "order by ";
        for (Sort.Order order : pageable.getSort()) {
            if (sortFields.contains(order.getProperty())) {
                paging += order.getProperty() + " " + order.getDirection() + ", ";
            }
        }
        paging = paging.substring(0, paging.length() - 2) + "\n";
        paging = "limit " + pageable.getPageSize() + " offset " + pageable.getOffset() + "\n";
        return paging;
    }
}
