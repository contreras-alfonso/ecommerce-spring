package org.alfonso.ecommerce.repositories;

import jakarta.validation.constraints.NotBlank;
import org.alfonso.ecommerce.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String>, SlugExistenceRepository {
    boolean existsByNameIgnoreCase(String name);

    Optional<Brand> findByName(String name);
}
