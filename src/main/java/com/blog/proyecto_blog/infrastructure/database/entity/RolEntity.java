package com.blog.proyecto_blog.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;
    @Column(unique = true)
    private String rol;
    @OneToMany(mappedBy = "rol")
    private List<UserEntity> users;

}
