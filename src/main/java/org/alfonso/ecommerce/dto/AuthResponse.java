package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alfonso.ecommerce.entities.DocumentType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String email;
    private String name;
    private String lastname;
    private DocumentType documentType;
    private String documentNumber;
    private List<String> roles;
    private TokenPair token;
}
