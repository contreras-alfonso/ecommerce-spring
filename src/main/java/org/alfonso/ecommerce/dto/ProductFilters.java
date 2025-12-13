package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductFilters {
    private List<String> brandIds;
    private Double minPrice;
    private Double maxPrice;
    private String sort;
    private int page;
    private int size;

}
