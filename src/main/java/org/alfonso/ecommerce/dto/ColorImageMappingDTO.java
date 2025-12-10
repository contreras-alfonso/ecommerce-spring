package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColorImageMappingDTO {
    @NotBlank(message = "El ID del color es requerido.")
    private String colorId;

}
