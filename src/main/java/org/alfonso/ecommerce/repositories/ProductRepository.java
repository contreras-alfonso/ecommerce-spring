package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, SlugExistenceRepository {
    boolean existsByNameIgnoreCase(String name);

    Optional<Product> findBySlug(String slug);
}
