package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("product") String productJson,
            @RequestParam Map<String, MultipartFile> files) {

        productService.save(productJson, files);
        return ResponseEntity.ok("Archivos recibidos y procesados correctamente.");
    }


}
