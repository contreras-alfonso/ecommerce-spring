package org.alfonso.ecommerce.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String name;

    @Column(unique = true)
    private String slug;

    private String description;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "uses_technical_variants")
    private boolean usesTechnicalVariants;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductVariant> variants;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductColorImage> colorImages = new ArrayList<>();

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", description=" + description + ", basePrice=" + basePrice
                + ", usesTechnicalVariants=" + usesTechnicalVariants + ", category=" + category + ", variants="
                + variants + ", colorImages=" + colorImages + "}";
    }

}
