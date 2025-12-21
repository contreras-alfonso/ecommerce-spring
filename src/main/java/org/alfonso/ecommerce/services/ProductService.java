package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.dto.ProductSearchResponse;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    ProductSearchResponse findProducts(String categorySlug,
                                       String brandId,
                                       Double minPrice,
                                       Double maxPrice,
                                       String sort,
                                       int page,
                                       int size);

    Page<Product> findByPagination(Pageable pageable);

    List<Product> findAll();

    Optional<Product> findById(String identifier);

    Product save(String product, Map<String, MultipartFile> files);

    Product update(String id, String product, Map<String, MultipartFile> files, List<String> deleteImagesIds);

}
