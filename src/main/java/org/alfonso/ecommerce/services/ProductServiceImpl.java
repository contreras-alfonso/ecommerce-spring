package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.dto.VariantDataDTO;
import org.alfonso.ecommerce.entities.*;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.ResourceConflictException;
import org.alfonso.ecommerce.repositories.BrandRepository;
import org.alfonso.ecommerce.repositories.CategoryRepository;
import org.alfonso.ecommerce.repositories.ProductRepository;
import org.alfonso.ecommerce.services.utils.ProductServiceUtil;
import org.alfonso.ecommerce.utils.GeneralUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorService colorService;
    private final ProductServiceUtil productServiceUtil;


    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(String identifier) {
        boolean isUuid = GeneralUtil.isUuid(identifier);
        if (isUuid) {
            return productRepository.findById(identifier);
        }
        return productRepository.findBySlug(identifier);
    }

    @Override
    @Transactional
    public Product save(String productJson, Map<String, MultipartFile> files) {

        // Parsear el producto a un objeto productDto
        ProductCreationDTO productDto = productServiceUtil.parserStringToProductDto(productJson);

        // Validar que no exista el nombre del producto
        if (productRepository.existsByNameIgnoreCase(productDto.getName())) {
            throw new ResourceConflictException("El nombre del producto ya existe");
        }

        // Buscar y asignar datos al objeto de Product
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        Optional<Brand> optionalBrand = brandRepository.findById(productDto.getBrandId());

        Product productToSave = new Product();
        productToSave.setName(productDto.getName());
        productToSave.setDescription(productDto.getDescription());
        productToSave.setUsesTechnicalVariants(productDto.isUsesTechnicalVariants());
        optionalCategory.ifPresentOrElse(productToSave::setCategory, () -> {
            throw new EntityNotFoundException("La categoría no fue encontrada");
        });
        optionalBrand.ifPresentOrElse(productToSave::setBrand, () -> {
            throw new EntityNotFoundException("La marca no fue encontrada");
        });

        // Crear slug para el producto
        String slug = GeneralUtil.createUniqueSlug(productDto.getName(), productRepository);
        productToSave.setSlug(slug);

        List<ProductVariant> variants = new ArrayList<>();

        // Iterar las variantes y asignar al productToSave
        for (VariantDataDTO variantDataDTO : productDto.getVariants()) {
            colorService.findById(variantDataDTO.getColorId()).ifPresent(colorDb -> {
                ProductVariant productVariant = new ProductVariant();
                productVariant.setColor(colorDb);
                productVariant.setPrice(variantDataDTO.getPrice());
                productVariant.setRam(variantDataDTO.getRam());
                productVariant.setStorage(variantDataDTO.getStorage());
                productVariant.setStock(variantDataDTO.getStock());
                variants.add(productVariant);
            });
        }

        productToSave.setVariants(variants);

        // Guardar el producto
        Product savedProduct = productRepository.save(productToSave);

        //Ordenar imágenes por colorId
        Map<String, List<MultipartFile>> groupFilesByKey = productServiceUtil.groupFilesByKey(files);

        // Subir imágenes a s3 y asignarlas al producto
        String folderPath = "products/" + savedProduct.getId();
        List<ProductColorImage> colorImages = productServiceUtil.uploadFilesToS3(groupFilesByKey, folderPath);
        savedProduct.setColorImages(colorImages);

        // Guardar el producto con las imágenes
        productRepository.save(savedProduct);

        return savedProduct;
    }

}
