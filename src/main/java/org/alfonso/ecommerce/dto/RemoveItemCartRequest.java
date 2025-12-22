package org.alfonso.ecommerce.dto;

import lombok.Data;

@Data
public class RemoveItemCartRequest {
    private String cartId;
    private String variantId;
}
