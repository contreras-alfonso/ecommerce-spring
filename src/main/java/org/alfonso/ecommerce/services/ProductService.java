package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(String identifier);

    Product save(String product, Map<String, MultipartFile> files);

}
