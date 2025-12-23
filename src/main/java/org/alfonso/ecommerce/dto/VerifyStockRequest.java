package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.alfonso.ecommerce.entities.StockUpdateMode;

@Data
@AllArgsConstructor
public class VerifyStockRequest {
    private StockUpdateMode mode;
    @NotBlank(message = "El Id del carrito es obligatorio")
    private String cartId;
    @NotBlank(message = "El Id de la variante es obligatoria")
    private String variantId;
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer quantity;
}
