package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String>, SlugExistenceRepository {
    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findByName(String name);
}
