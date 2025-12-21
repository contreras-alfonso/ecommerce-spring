package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrandCountDto {
    private String id;
    private String name;
    private String slug;
    private Long count;
}
