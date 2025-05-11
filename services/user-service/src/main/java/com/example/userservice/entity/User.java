package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String name;

    private String address;

    private String email;

    private String phone;

    @Column(nullable = false)
    private String role;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}