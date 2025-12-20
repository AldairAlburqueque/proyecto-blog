package com.blog.proyecto_blog.infrastructure.database.entity;

import com.blog.proyecto_blog.domain.enun.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="blog")
public class BlogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBlog;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToOne
    @JoinColumn(name = "idUser")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "idCategory")
    private CategoryEntity category;
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnore
    private List<CommentEntity> comments = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}
