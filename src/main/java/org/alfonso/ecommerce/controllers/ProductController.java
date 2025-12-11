package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<?> findById(@PathVariable String identifier) {
        Optional<Product> optionalProduct = productService.findById(identifier);
        return optionalProduct
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el producto buscado"));
    }

    @PostMapping
    public ResponseEntity<Product> handleFileUpload(@RequestParam("product") String productJson,
                                                    @RequestParam Map<String, MultipartFile> files) {

        Product product = productService.save(productJson, files);
        return ResponseEntity.ok(product);
    }


}
