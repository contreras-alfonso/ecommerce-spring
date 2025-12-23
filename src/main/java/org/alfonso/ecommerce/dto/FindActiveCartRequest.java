package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindActiveCartRequest {
    @NotBlank(message = "El Id del carrito es obligatorio")
    private String cartId;
}
