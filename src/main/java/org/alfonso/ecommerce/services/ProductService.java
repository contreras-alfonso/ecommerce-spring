package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(String identifier);

    Product save(String product, Map<String, MultipartFile> files);

}
