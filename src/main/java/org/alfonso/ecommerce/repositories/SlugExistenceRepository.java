package org.alfonso.ecommerce.repositories;

public interface SlugExistenceRepository {
    boolean existsBySlugIgnoreCase(String slug);
}
