package org.alfonso.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDto {
    private String cartId;
    private List<CartItemDto> items;
    private Double subtotal;
    private int itemsCount;
}
