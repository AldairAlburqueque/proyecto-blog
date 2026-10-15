package com.blog.proyecto_blog.infrastructure.database.repositories;

import com.blog.proyecto_blog.infrastructure.database.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<CommentEntity> findByBlog_IdBlog(
            Long blogId,
            Pageable pageable
    );
}
