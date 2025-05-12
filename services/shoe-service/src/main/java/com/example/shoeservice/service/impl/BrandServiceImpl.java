package com.example.shoeservice.service.impl;

import com.example.shoeservice.entity.Brand;
import com.example.shoeservice.repository.BrandRepository;
import com.example.shoeservice.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return brandRepository.findByName(name);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }
} 