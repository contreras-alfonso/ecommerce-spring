package org.alfonso.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alfonso.ecommerce.entities.DocumentType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 a 50 caracteres")
    private String name;

    @NotBlank
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 a 50 caracteres")
    private String lastname;

    private DocumentType documentType;

    @NotBlank(message = "error en document Number")
    @Size(min = 8, max = 50, message = "El número de documento debe tener entre 3 a 50 caracteres")
    private String documentNumber;

    @NotBlank(message = "El correo es requerido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

}
