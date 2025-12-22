package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    @Query("SELECT v.stock FROM ProductVariant v WHERE v.id = :variantId")
    Integer findStockById(String variantId);
}
