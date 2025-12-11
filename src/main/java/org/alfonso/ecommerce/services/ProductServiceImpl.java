package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.dto.VariantDataDTO;
import org.alfonso.ecommerce.entities.*;
import org.alfonso.ecommerce.exceptions.EntityNotFoundException;
import org.alfonso.ecommerce.exceptions.ResourceConflictException;
import org.alfonso.ecommerce.repositories.ProductRepository;
import org.alfonso.ecommerce.services.utils.ProductServiceUtil;
import org.alfonso.ecommerce.utils.GeneralUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.alfonso.ecommerce.entities.ProductColorImage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ColorService colorService;
    private final ProductColorImageService productColorImageService;
    private final ProductServiceUtil productServiceUtil;


    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable);
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
        Optional<Category> optionalCategory = categoryService.findById(productDto.getCategoryId());
        Optional<Brand> optionalBrand = brandService.findById(productDto.getBrandId());

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

    @Override
    public Product update(String id, String productJson, Map<String, MultipartFile> files, List<String> deleteImagesIds) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el producto buscado."));

        // Parsear el producto a un objeto productDto
        ProductCreationDTO productDto = productServiceUtil.parserStringToProductDto(productJson);

        // Actualizar campos simples

        if (!existingProduct.getName().equals(productDto.getName())) {
            // Si el nombre cambio validar si el nombre ya está siendo usado
            productRepository.findByName(productDto.getName()).ifPresent(existProduct -> {
                if (!existProduct.getId().equals(id)) {
                    throw new ResourceConflictException("El nombre del producto ya está siendo usado");
                }
            });

            existingProduct.setName(productDto.getName());
            String slug = GeneralUtil.generateSlug(productDto.getName());
            existingProduct.setSlug(slug);
        }

        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setUsesTechnicalVariants(productDto.isUsesTechnicalVariants());

        // Validar si la marca cambió
        if (existingProduct.getBrand().getId().equals(productDto.getBrandId())) {
            brandService.findById(productDto.getBrandId()).
                    ifPresentOrElse(existingProduct::setBrand, () -> {
                        throw new EntityNotFoundException("No se encontró la marca seleccionada.");
                    });
        }

        // Validar si la categoría cambió
        if (existingProduct.getCategory().getId().equals(productDto.getCategoryId())) {
            categoryService.findById(productDto.getCategoryId()).
                    ifPresentOrElse(existingProduct::setCategory, () -> {
                        throw new EntityNotFoundException("No se encontró la categoría seleccionada.");
                    });
        }

        // Obtener variantes actuales del producto en la DB
        List<ProductVariant> currentVariants = existingProduct.getVariants();
        // Mapear las variantes por Id -> Data
        Map<String, ProductVariant> currentVariantsMap = currentVariants.stream()
                .collect(Collectors.toMap(
                        ProductVariant::getId,
                        variant -> variant
                ));

        // Crear nueva lista de variantes
        List<ProductVariant> updatedVariants = new ArrayList<>();

        // Iterar las variantes del productDto
        for (VariantDataDTO variantDto : productDto.getVariants()) {

            // Si el id no es null, es un registro existente en la db
            if (variantDto.getId() != null) {
                ProductVariant variantToUpdate = currentVariantsMap.get(variantDto.getId());

                variantToUpdate.setRam(variantDto.getRam());
                variantToUpdate.setStorage(variantDto.getStorage());
                variantToUpdate.setPrice(variantDto.getPrice());
                variantToUpdate.setStock(variantDto.getStock());

                // Si el color no es igual al de la db, actualizar
                if (!variantToUpdate.getColor().getId().equals(variantDto.getColorId())) {
                    colorService.findById(variantDto.getColorId())
                            .ifPresentOrElse(variantToUpdate::setColor,
                                    () -> {
                                        throw new EntityNotFoundException("El color no fue encontrado" + variantDto.getColorId());
                                    });
                }

                updatedVariants.add(variantToUpdate);

            } else {

                // Si el id es null, crear nueva variante
                ProductVariant newVariant = new ProductVariant();
                colorService.findById(variantDto.getColorId())
                        .ifPresentOrElse(newVariant::setColor,
                                () -> {
                                    throw new EntityNotFoundException("El color no fue encontrado: " + variantDto.getColorId());
                                }
                        );

                newVariant.setRam(variantDto.getRam());
                newVariant.setStorage(variantDto.getStorage());
                newVariant.setPrice(variantDto.getPrice());
                newVariant.setStock(variantDto.getStock());

                updatedVariants.add(newVariant);
            }
        }

        existingProduct.setVariants(updatedVariants);

        //Obtener las imágenes antiguas
        List<ProductColorImage> currentProductColorImages = existingProduct.getColorImages();
        //Crear nueva lista de imágenes
        List<ProductColorImage> updatedProductColorImages = currentProductColorImages;

        // Tratar las imágenes nuevas si es que vienen
        if (files != null && !files.isEmpty()) {


            //Ordenar imágenes por colorId
            Map<String, List<MultipartFile>> groupFilesByKey = productServiceUtil.groupFilesByKey(files);

            // Subir imágenes a s3 y asignarlas al producto
            String folderPath = "products/" + existingProduct.getId();
            List<ProductColorImage> colorImages = productServiceUtil.uploadFilesToS3(groupFilesByKey, folderPath);

            // Agregar las nuevas imágenes
            updatedProductColorImages.addAll(colorImages);

            // Actualizar las imágenes del producto de la db
            existingProduct.setColorImages(updatedProductColorImages);
        }

        if (!deleteImagesIds.isEmpty()) {
            for (String deleteId : deleteImagesIds) {
                ProductColorImage productColorImage = productColorImageService.findById(deleteId)
                        .orElseThrow(() -> new EntityNotFoundException("No se encontró la imagen"));

                // Eliminar imagen del listado
                updatedProductColorImages.remove(productColorImage);
            }

            // Actualizar las imágenes del producto de la db
            existingProduct.setColorImages(updatedProductColorImages);
        }

        return productRepository.save(existingProduct);
    }
}
