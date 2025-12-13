package org.alfonso.ecommerce.projection;

import java.time.LocalDateTime;

public interface ProductListItemProjection {
    String getId();

    String getName();

    String getSlug();

    String getDescription();

    boolean isUsesTechnicalVariants();

    // Brand
    String getBrandId();

    String getBrandName();

    String getBrandSlug();

    // Pricing
    Double getMinPrice();

    Double getMaxPrice();

    LocalDateTime getCreatedAt();
}
