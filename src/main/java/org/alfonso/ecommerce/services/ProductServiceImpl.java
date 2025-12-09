package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Category;
import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.repositories.CategoryRepository;
import org.alfonso.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product save(ProductCreationDTO productDto, List<MultipartFile> files) {
        Map<String, MultipartFile> fileMap = files.stream().collect(
                Collectors.toMap(
                        file -> file.getName(),
                        file -> file
                )
        );

        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());

        Product productToSave = new Product();
        productToSave.setName(productDto.getName());
        productToSave.setDescription(productDto.getDescription());
        productToSave.setUsesTechnicalVariants(productDto.isUsesTechnicalVariants());
        //productToSave.setCategory(optionalCategory.get());

        //productRepository.save()

        return null;
    }
}
