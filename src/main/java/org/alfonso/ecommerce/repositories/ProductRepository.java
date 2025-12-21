package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.dto.BrandCountDto;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product>, SlugExistenceRepository {
    boolean existsByNameIgnoreCase(String name);

    Optional<Product> findBySlug(String slug);

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Product> findByName(String name);

    @Query("""
                SELECT MIN(v.price), MAX(v.price)
                FROM Product p
                JOIN p.variants v
                WHERE (:categorySlug IS NULL OR p.category.slug = :categorySlug)
                  AND (:minPrice IS NULL OR v.price >= :minPrice)
                  AND (:maxPrice IS NULL OR v.price <= :maxPrice)
                  AND (:brandIds IS NULL OR p.brand.id IN :brandIds)
            """)
    Object[] findMinMaxPrice(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice
    );

    @Query("""
                SELECT new org.alfonso.ecommerce.dto.BrandCountDto(
                    b.id,
                    b.name,
                    b.slug,
                    COUNT(DISTINCT p.id)
                )
                FROM Product p
                JOIN p.brand b
                JOIN p.variants v
                WHERE (:categorySlug IS NULL OR p.category.slug = :categorySlug)
                  AND (:minPrice IS NULL OR v.price >= :minPrice)
                  AND (:maxPrice IS NULL OR v.price <= :maxPrice)
                GROUP BY b.id, b.name, b.slug
                ORDER BY b.name
            """)
    List<BrandCountDto> findAvailableBrands(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice
    );


}
