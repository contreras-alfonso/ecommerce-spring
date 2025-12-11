package org.alfonso.ecommerce.services.utils;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.dto.ProductCreationDTO;
import org.alfonso.ecommerce.entities.Color;
import org.alfonso.ecommerce.entities.ProductColorImage;
import org.alfonso.ecommerce.exceptions.MissingFilesException;
import org.alfonso.ecommerce.exceptions.ObjectMappingException;
import org.alfonso.ecommerce.repositories.ColorRepository;
import org.alfonso.ecommerce.services.UploadFileService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductServiceUtil {

    private final ColorRepository colorRepository;
    private final UploadFileService uploadFileService;
    private final ObjectMapper objectMapper;

    public List<ProductColorImage> uploadFilesToS3(Map<String, List<MultipartFile>> files, String folderPath) {
        List<ProductColorImage> colorImages = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : files.entrySet()) {
            Optional<Color> optionalColor = colorRepository.findById(entry.getKey());
            if (optionalColor.isPresent()) {
                Color colorDb = optionalColor.get();

                for (MultipartFile file : entry.getValue()) {
                    String pathS3 = uploadFileService.uploadImage(file, folderPath);
                    ProductColorImage productColorImage = new ProductColorImage(null, pathS3, colorDb);
                    colorImages.add(productColorImage);
                }
            }
        }

        return colorImages;

    }

    public Map<String, List<MultipartFile>> groupFilesByKey(Map<String, MultipartFile> files) {
        if (files.isEmpty()) {
            throw new MissingFilesException("No se enviaron imágenes");
        }

        Map<String, List<MultipartFile>> filesGroupedByColorId = new HashMap<>();

        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            String fullKey = entry.getKey();
            MultipartFile file = entry.getValue();

            String[] contentParts = fullKey.split("__");
            String colorId = contentParts[1];

            if (!filesGroupedByColorId.containsKey(colorId)) {
                filesGroupedByColorId.put(colorId, new ArrayList<>());
            }
            filesGroupedByColorId.get(colorId).add(file);
        }
        return filesGroupedByColorId;

    }

    public ProductCreationDTO parserStringToProductDto(String productString) {

        ProductCreationDTO productCreationDTO;

        try {
            productCreationDTO = objectMapper.readValue(productString, ProductCreationDTO.class);
        } catch (Exception e) {
            throw new ObjectMappingException("Error al deserializar el JSON del Producto");
        }

        return productCreationDTO;

    }
}
