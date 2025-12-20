package org.alfonso.ecommerce.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @Lob
    private String description;

    @Column(name = "uses_technical_variants")
    private boolean usesTechnicalVariants;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductVariant> variants;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductColorImage> colorImages = new ArrayList<>();

}
