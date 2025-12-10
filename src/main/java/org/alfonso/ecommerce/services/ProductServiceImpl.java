package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.*;
import org.alfonso.ecommerce.repositories.BrandRepository;
import org.alfonso.ecommerce.repositories.CategoryRepository;
import org.alfonso.ecommerce.repositories.ColorRepository;
import org.alfonso.ecommerce.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;
    private final S3Service s3Service;
    private final UploadFileService uploadFileService;

    @Override
    @Transactional
    public Product save(String productJson, Map<String, MultipartFile> files) {


        Map<String, List<MultipartFile>> groupFilesByKey = this.groupFilesByKey(files);
        ProductCreationDTO productDto = this.parserStringToProductDto(productJson);

        List<ProductColorImage> colorImages = this.uploadFilesToS3(groupFilesByKey);

        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        Optional<Brand> optionalBrand = brandRepository.findById(productDto.getBrandId());

        Product productToSave = new Product();
        productToSave.setName(productDto.getName());
        productToSave.setDescription(productDto.getDescription());
        productToSave.setUsesTechnicalVariants(productDto.isUsesTechnicalVariants());
        optionalCategory.ifPresent(productToSave::setCategory);
        optionalBrand.ifPresent(productToSave::setBrand);
        productToSave.setColorImages(colorImages);

        //Product savedProduct = productRepository.save(productToSave);

        //String folderPath = "products/" + savedProduct.getId();


        //productRepository.save()

        return null;
    }

    public List<ProductColorImage> uploadFilesToS3(Map<String, List<MultipartFile>> files) {
        List<ProductColorImage> colorImages = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : files.entrySet()) {
            Optional<Color> optionalColor = colorRepository.findById(entry.getKey());
            if (optionalColor.isPresent()) {
                Color colorDb = optionalColor.get();

                for (MultipartFile file : entry.getValue()) {
                    String pathS3 = uploadFileService.uploadImage(file, "products");
                    ProductColorImage productColorImage = new ProductColorImage(null, pathS3, colorDb);
                    colorImages.add(productColorImage);
                }
            }
        }

        return colorImages;

    }

    public Map<String, List<MultipartFile>> groupFilesByKey(Map<String, MultipartFile> files) {
        if (files.isEmpty()) {
            //TODO: manejar esto
            //return ResponseEntity.badRequest().body("No se enviaron archivos.");
        }

        Map<String, List<MultipartFile>> filesGroupedByColorId = new HashMap<>();

        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            String fullKey = entry.getKey();
            MultipartFile file = entry.getValue();

            String[] contentParts = fullKey.split("__");
            String prefix = contentParts[0];
            String colorId = contentParts[1];


            if (!filesGroupedByColorId.containsKey(colorId)) {
                filesGroupedByColorId.put(colorId, new ArrayList<>());
            }
            filesGroupedByColorId.get(colorId).add(file);
        }
        return filesGroupedByColorId;

    }

    public ProductCreationDTO parserStringToProductDto(String productString) {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductCreationDTO productCreationDTO;

        productCreationDTO = objectMapper.readValue(productString, ProductCreationDTO.class);

        return productCreationDTO;

    }
}
