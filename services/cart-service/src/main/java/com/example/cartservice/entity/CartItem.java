package com.example.cartservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cartitems")
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "shoe_id", nullable = false)
    private Long shoeId;

    @Column(nullable = false)
    private double size;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int quantity;
}