package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Color;
import org.alfonso.ecommerce.services.ColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(colorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Color> optionalColor = colorService.findById(id);
        return optionalColor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Color color) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colorService.save(color));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Color color, @PathVariable String id) {
        Optional<Color> optionalColor = colorService.update(id, color);
        if (optionalColor.isPresent()) {
            return ResponseEntity.ok(optionalColor.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Color no encontrado"));
    }

}
