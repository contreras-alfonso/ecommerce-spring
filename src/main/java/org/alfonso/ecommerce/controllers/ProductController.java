package org.alfonso.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProductFilters;
import org.alfonso.ecommerce.dto.ProductSearchResponse;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.services.ProductSearchService;
import org.alfonso.ecommerce.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<ProductSearchResponse> searchProducts(
            @RequestParam(required = false) List<String> brandIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "created_desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProductFilters request = new ProductFilters(
                brandIds, minPrice, maxPrice, sort, page, size
        );
        ProductSearchResponse response = productSearchService.search(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<Product>> findByPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findByPagination(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> save(@RequestParam("product") String productJson,
                                        @RequestParam Map<String, MultipartFile> files) {

        Product product = productService.save(productJson, files);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestParam("product") String productJson,
            @RequestParam(required = false) Map<String, MultipartFile> newFiles,
            @RequestParam(required = false) List<String> deleteImagesIds

    ) {

        Product updatedProduct = productService.update(id, productJson, newFiles, deleteImagesIds);
        return ResponseEntity.ok(updatedProduct);

    }


}
