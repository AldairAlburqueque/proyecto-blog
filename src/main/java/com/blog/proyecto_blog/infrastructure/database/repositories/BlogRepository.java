package com.blog.proyecto_blog.infrastructure.database.repositories;

import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByUserIdUser(Long idUser);

    List<BlogEntity> findByCategory_IdCategory(Long categoryId);}
