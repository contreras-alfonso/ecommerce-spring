package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.ProductColorImage;
import org.alfonso.ecommerce.repositories.ProductColorImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductColorImageServiceImpl implements ProductColorImageService {

    private ProductColorImageRepository productColorImageRepository;

    @Override
    public Optional<ProductColorImage> findById(String id) {
        return productColorImageRepository.findById(id);
    }
}
