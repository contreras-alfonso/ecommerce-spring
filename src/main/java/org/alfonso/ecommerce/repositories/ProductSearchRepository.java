package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.Product;
import org.alfonso.ecommerce.projection.FiltersListProjection;
import org.alfonso.ecommerce.projection.ProductListItemProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductSearchRepository extends JpaRepository<Product, String> {
    @Query("""
            SELECT
               p.id AS id,
               p.name AS name,
               p.slug AS slug,
               p.description AS description,
               p.usesTechnicalVariants AS usesTechnicalVariants,
               p.brand.id AS brandId,
               p.brand.name AS brandName,
               p.brand.slug AS brandSlug,
               MIN(v.price) AS minPrice,
               MAX(v.price) AS maxPrice,
               p.createdAt AS createdAt
            FROM Product p
            JOIN p.variants v
            WHERE (:brandIds IS NULL OR p.brand.id IN :brandIds)
            AND (:minPrice IS NULL OR v.price >= :minPrice)
            AND (:maxPrice IS NULL OR v.price <= :maxPrice)
            AND (p.category.slug = :categorySlug)
            GROUP BY p.id, p.brand.id
            ORDER BY
               CASE WHEN :sort = 'price_asc' THEN MIN(v.price) END ASC,
               CASE WHEN :sort = 'price_desc' THEN MIN(v.price) END DESC,
               CASE WHEN :sort = 'created_desc' OR :sort IS NULL THEN p.createdAt END DESC
            
            """)
    Page<ProductListItemProjection> findProducts(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice,
            String sort,
            Pageable pageable
    );

    @Query("""
            SELECT
                MIN(v.price) AS minPrice,
                MAX(v.price) AS maxPrice,
                p.brand.id AS brandId,
                p.brand.name AS brandName,
                p.brand.slug AS brandSlug
            FROM Product p
            JOIN p.variants v
            WHERE (:brandIds IS NULL OR p.brand.id IN :brandIds)
            AND (p.category.slug = :categorySlug)
            AND (:minPrice IS NULL OR v.price >= :minPrice)
            GROUP BY p.brand.id, p.brand.name
            """)
    List<FiltersListProjection> findAvailableFilters(
            String categorySlug,
            List<String> brandIds,
            Double minPrice,
            Double maxPrice
    );

}
