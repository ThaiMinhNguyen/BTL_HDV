package com.example.shoeservice.controller;

import com.example.shoeservice.entity.Shoe;
import com.example.shoeservice.entity.ShoeInventory;
import com.example.shoeservice.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/shoes")
@RequiredArgsConstructor
public class ShoeController {

    private final ShoeService shoeService;

    @GetMapping("/{id}")
    public ResponseEntity<Shoe> getShoeById(@PathVariable Long id) {
        Optional<Shoe> shoe = shoeService.findById(id);
        return shoe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Shoe>> getAllShoes() {
        List<Shoe> shoes = shoeService.findAll();
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<List<Shoe>> getShoesByBrand(@PathVariable Long brandId) {
        List<Shoe> shoes = shoeService.findByBrandId(brandId);
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<Shoe>> getShoesByCategory(@PathVariable Long categoryId) {
        List<Shoe> shoes = shoeService.findByCategoryId(categoryId);
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<Shoe>> getShoesByGender(@PathVariable Shoe.Gender gender) {
        List<Shoe> shoes = shoeService.findByGender(gender);
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Shoe>> getShoesByPriceRange(
            @RequestParam double min,
            @RequestParam double max) {
        List<Shoe> shoes = shoeService.findByPriceRange(min, max);
        return ResponseEntity.ok(shoes);
    }

    @GetMapping("/{id}/inventory")
    public ResponseEntity<List<ShoeInventory>> getShoeInventory(@PathVariable Long id) {
        List<ShoeInventory> inventory = shoeService.getShoeInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkShoeAvailability(
            @RequestParam Long shoeId,
            @RequestParam double size,
            @RequestParam String color,
            @RequestParam int quantity) {
        boolean isAvailable = shoeService.checkAvailability(shoeId, size, color, quantity);
        return ResponseEntity.ok(Map.of("isAvailable", isAvailable));
    }

    @PutMapping("/{id}/updateInventory")
    public ResponseEntity<Void> updateInventoryQuantity(
            @PathVariable Long id,
            @RequestParam double size,
            @RequestParam String color,
            @RequestParam int quantity) {
        shoeService.updateInventoryQuantity(id, size, color, quantity);
        return ResponseEntity.ok().build();
    }
} 