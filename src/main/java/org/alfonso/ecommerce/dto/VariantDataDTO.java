package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariantDataDTO {

    private String id;

    @NotBlank(message = "El ID del color de la variante es requerido.")
    private String colorId;

    @NotBlank(message = "La RAM es requerida para la variante.")
    private String ram;

    @NotBlank(message = "El almacenamiento es requerido para la variante.")
    private String storage;

    @NotNull(message = "El precio de la variante es requerido.")
    private Double price;

    @NotNull(message = "El stock inicial es requerido.")
    private Integer stock;
}
