package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alfonso.ecommerce.entities.Brand;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableFilters {
    private Double minPrice;
    private Double maxPrice;
    List<Brand> brands;
}
