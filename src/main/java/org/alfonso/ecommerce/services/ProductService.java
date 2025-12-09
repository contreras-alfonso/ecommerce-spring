package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product save(ProductCreationDTO productDto, List<MultipartFile> files);
}
