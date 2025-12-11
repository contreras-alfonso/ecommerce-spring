package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.ProductColorImage;

import java.util.Optional;

public interface ProductColorImageService {
    Optional<ProductColorImage> findById(String id);
}
