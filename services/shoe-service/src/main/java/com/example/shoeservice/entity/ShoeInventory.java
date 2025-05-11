package com.example.shoeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shoe_inventory")
@Getter
@Setter
public class ShoeInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoe_id", nullable = false)
    private Shoe shoe;

    @Column(nullable = false)
    private double size;

    @Column(nullable = false)
    private String color;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;
} 