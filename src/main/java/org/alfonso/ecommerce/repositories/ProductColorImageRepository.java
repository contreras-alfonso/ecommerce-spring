package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.ProductColorImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductColorImageRepository extends JpaRepository<ProductColorImage, String> {
    @Query("""
                SELECT img
                FROM ProductColorImage img
                WHERE img.id IN (
                    SELECT MIN(i.id)
                    FROM ProductColorImage i
                    WHERE i.color.id IN :colorIds
                    GROUP BY i.color.id
                )
            """)
    List<ProductColorImage> findFirstImageByColorIds(List<String> colorIds);
}
