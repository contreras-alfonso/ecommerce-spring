package org.alfonso.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alfonso.ecommerce.entities.Brand;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListItem {
    private String id;
    private String name;
    private String slug;
    private String description;
    private boolean usesTechnicalVariants;
    private Brand brand;
    private Double minPrice;
    private Double maxPrice;
    private LocalDateTime createdAt;
}
