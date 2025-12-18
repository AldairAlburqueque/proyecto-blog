package com.blog.proyecto_blog.infrastructure.database.repositories;

import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByUserIdUser(Long idUser);
    List<BlogEntity> findByCategory_IdCategory(Long categoryId);
    Page<BlogEntity> findByUser(UserEntity user, Pageable pageable);
    List<BlogEntity> findByTitleContainingIgnoreCase(String title);
}
