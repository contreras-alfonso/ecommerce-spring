package org.alfonso.ecommerce.projection;

public interface FiltersListProjection {
    Double getMinPrice();

    Double getMaxPrice();

    String getBrandId();

    String getBrandName();

    String getBrandSlug();
}
