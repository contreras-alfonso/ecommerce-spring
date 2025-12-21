package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alfonso.ecommerce.entities.Product;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponse {
    private Page<Product> products;
    private AvailableFilters filters;
}
