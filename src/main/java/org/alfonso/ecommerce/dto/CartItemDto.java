package org.alfonso.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private String productSlug;

    private String variantId;

    private Double price;
    private String imageUrl;
    private Integer quantity;

    private Boolean usesTechnicalVariants;

    private String ram;
    private String storage;
}
