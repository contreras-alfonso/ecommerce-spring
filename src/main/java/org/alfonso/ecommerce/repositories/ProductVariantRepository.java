package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.ProductColorImage;
import org.alfonso.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    @Query("SELECT v.stock FROM ProductVariant v WHERE v.id = :variantId")
    Integer findStockById(String variantId);

    @Query("""
                SELECT v, p
                FROM ProductVariant v
                JOIN v.product p
                WHERE v.id IN :variantIds
            """)
    List<Object[]> findCartVariants(List<String> variantIds);


}
