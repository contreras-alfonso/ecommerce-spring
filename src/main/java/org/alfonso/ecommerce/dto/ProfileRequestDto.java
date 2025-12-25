package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.alfonso.ecommerce.entities.DocumentType;

@Data
public class ProfileRequestDto {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    @NotBlank(message = "El apellido es requerido")
    private String lastname;
    @NotBlank(message = "El correo es requerido")
    private String email;
    @NotBlank(message = "El celular es requerido")
    private String phone;
    private DocumentType documentType;
    @NotBlank(message = "El número de documento es requerido")
    private String documentNumber;
}
