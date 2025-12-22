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

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedService {
    private final ColorRepository colorRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void seed() {
        Color color1 = new Color("Negro", "#454545");
        Color color2 = new Color("Blanco", "#FAFAFA");
        Color color3 = new Color("Menta", "#EAF5E1");
        colorRepository.save(color1);
        colorRepository.save(color2);
        colorRepository.save(color3);

        Brand brand1 = new Brand("Xiaomi", "xiaomi");
        brandRepository.save(brand1);

        Brand apple = new Brand("Apple", "apple");
        brandRepository.save(apple);

        Brand asus = new Brand("Asus", "asus");
        brandRepository.save(asus);

        Brand brand2 = new Brand("Samsung", "samsung");
        brandRepository.save(brand2);

        Brand brand3 = new Brand("OnePlus", "one-plus");
        brandRepository.save(brand3);

        Category category1 = new Category("Celulares", "celulares");
        categoryRepository.save(category1);

        Category category2 = new Category("Audífonos", "audifonos");
        categoryRepository.save(category2);

        Category category3 = new Category("Relojes", "relojes");
        categoryRepository.save(category3);

    }
}
