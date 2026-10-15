package com.blog.proyecto_blog.infrastructure.database.repositories;

import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByUserIdUser(Long idUser);
    @EntityGraph(attributePaths = {"user", "category"})
    @Override
    Page<BlogEntity> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"user", "category"})
    Page<BlogEntity> findByCategory_IdCategory(Long categoryId, Pageable pageable);
    @EntityGraph(attributePaths = {"user", "category"})
    Page<BlogEntity> findByUser(UserEntity user, Pageable pageable);
    @EntityGraph(attributePaths = {"user", "category"})
    Page<BlogEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    @EntityGraph(attributePaths = {"user", "category"})
    Optional<BlogEntity> findByIdBlog(Long idBlog);
}
