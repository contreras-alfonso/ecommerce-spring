package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.services.ProductService;
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
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
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

        System.out.println("files asda = " + files);
        Product product = productService.save(productJson, files);
        return ResponseEntity.ok(product);
    }


}
