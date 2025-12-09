package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(String id);

    Category save(Category category);

    Optional<Category> update(String id, Category category);
}
