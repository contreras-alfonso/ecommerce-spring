package org.alfonso.ecommerce.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Brand;
import org.alfonso.ecommerce.entities.Category;
import org.alfonso.ecommerce.entities.Color;
import org.alfonso.ecommerce.repositories.BrandRepository;
import org.alfonso.ecommerce.repositories.CategoryRepository;
import org.alfonso.ecommerce.repositories.ColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedService {
    private final ColorRepository colorRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public void seed() {
        Color color1 = new Color("Rojo", "#F54927");
        Color color2 = new Color("Azul", "#4359F7");
        Color savedColor1 = colorRepository.save(color1);
        Color savedColor2 = colorRepository.save(color2);

        Brand brand1 = new Brand("xiaomi pro", "xiaomi-pro");
        Brand savedBrand1 = brandRepository.save(brand1);

        Category category1 = new Category("celulares", "nada");
        Category savedCategory1 = categoryRepository.save(category1);


        for (int i = 0; i < 30; i++) {

            String stringProduct = "{\n" +
                    "  \"name\": \"Tablet xiaomi " +  (i + 1) + "\""+ ",\n" +
                    "  \"description\": \"Tablet potente\",\n" +
                    "  \"brandId\": " + "\"" + savedBrand1.getId() + "\"" + ",\n" +
                    "  \"categoryId\": " + "\"" + savedCategory1.getId() + "\"" + ",\n" +
                    "  \"usesTechnicalVariants\": true,\n" +
                    "  \"colors\": [\n" +
                    "    { \"colorId\": " + "\"" +  savedColor1.getId() + "\"" + " },\n" +
                    "    { \"colorId\": " + "\"" + savedColor2.getId() + "\"" + " }\n" +
                    "  ],\n" +
                    "  \"variants\": [\n" +
                    "    { \"colorId\": " + "\"" +  savedColor1.getId() + "\"" + ", \"ram\": \"8GB\", \"storage\": \"256GB\", \"price\": 850, \"stock\": 10 },\n" +
                    "    { \"colorId\": " + "\"" +  savedColor2.getId() + "\"" + ", \"ram\": \"8GB\", \"storage\": \"256GB\", \"price\": 860, \"stock\": 8 }\n" +
                    "  ]\n" +
                    "}\n";

            Map<String, MultipartFile> files = new HashMap<>();
            productService.save(stringProduct, files);
        }


    }
}
