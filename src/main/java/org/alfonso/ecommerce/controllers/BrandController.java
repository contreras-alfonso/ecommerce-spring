package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Brand;
import org.alfonso.ecommerce.services.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Brand> optionalBrand = brandService.findById(id);
        return optionalBrand.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Brand brand) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.save(brand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Brand brand, @PathVariable String id) {
        Optional<Brand> optionalBrand = brandService.update(id, brand);
        if (optionalBrand.isPresent()) {
            return ResponseEntity.ok(optionalBrand.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Color no encontrado"));
    }

}
