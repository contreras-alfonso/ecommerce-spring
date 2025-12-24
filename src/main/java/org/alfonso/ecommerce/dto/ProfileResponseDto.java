package org.alfonso.ecommerce.dto;

import lombok.Data;
import org.alfonso.ecommerce.entities.DocumentType;

@Data
public class ProfileResponseDto {
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private DocumentType documentType;
    private String documentNumber;
}
