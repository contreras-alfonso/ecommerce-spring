package org.alfonso.ecommerce.services;

import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.Category;
import org.alfonso.ecommerce.exceptions.ResourceConflictException;
import org.alfonso.ecommerce.repositories.CategoryRepository;
import org.alfonso.ecommerce.repositories.SlugExistenceRepository;
import org.alfonso.ecommerce.utils.GeneralUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Category save(Category category) {

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new ResourceConflictException("El nombre de la categoría ya existe.");
        }

        String slug = GeneralUtil.createUniqueSlug(category.getName(), categoryRepository);
        category.setSlug(slug);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Optional<Category> update(String id, Category category) {

        return categoryRepository.findById(id).map(categoryDb -> {
            if (!category.getName().equals(categoryDb.getName())) {

                // Si el nombre cambió, buscar category por el nombre y comparar los ids
                categoryRepository.findByName(category.getName()).ifPresent(existCategory -> {
                    if (!existCategory.getId().equals(id)) {
                        throw new ResourceConflictException("El nombre de la categoría ya existe.");
                    }
                });

                String slug = GeneralUtil.createUniqueSlug(category.getName(), categoryRepository);
                System.out.println("slug = " + slug);
                categoryDb.setSlug(slug);
            }
            categoryDb.setName(category.getName());
            return categoryRepository.save(categoryDb);
        });
    }


}
