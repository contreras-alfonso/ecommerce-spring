package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreationDTO {

    private String id;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    private String description;

    private boolean usesTechnicalVariants;

    @NotBlank(message = "La marca es requerida")
    private String brandId;

    @NotBlank(message = "La categoría es requerida")
    private String categoryId;

    @NotNull(message = "Se debe incluir el mapeo de imágenes.")
    private List<ColorImageMappingDTO> colors;

    @NotNull(message = "Las variantes de producto son requeridas.")
    private List<VariantDataDTO> variants;

}
