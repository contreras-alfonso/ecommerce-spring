package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Category;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Category> optionalCategory = categoryService.findById(id);
        return optionalCategory.map(ResponseEntity::ok).orElseThrow(()-> new EntityNotFoundException("Categoría no encontrada"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@Valid @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Valid @RequestBody Category category, @PathVariable String id) {
       return categoryService.update(id, category).map(ResponseEntity::ok).orElseThrow(()-> new EntityNotFoundException("Categoría no encontrada"));
    }

}
