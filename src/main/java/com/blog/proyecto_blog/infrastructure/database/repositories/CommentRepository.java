package com.blog.proyecto_blog.infrastructure.database.repositories;

import com.blog.proyecto_blog.infrastructure.database.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBlog_IdBlog(Long blogId);
}
