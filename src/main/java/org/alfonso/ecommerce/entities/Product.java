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

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String name;

    private String description;

    @Column(name = "base_price")
    private Double basePrice;

    @Column(name = "uses_technical_variants")
    private boolean usesTechnicalVariants;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductVariant> variants;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductColorImage> colorImages = new ArrayList<>();

    public Product() {
    }

    public Product(String name, String description, Double basePrice, boolean usesTechnicalVariants, Category category,
            List<ProductVariant> variants, List<ProductColorImage> colorImages) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.usesTechnicalVariants = usesTechnicalVariants;
        this.category = category;
        this.variants = variants;
        this.colorImages = colorImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isUsesTechnicalVariants() {
        return usesTechnicalVariants;
    }

    public void setUsesTechnicalVariants(boolean usesTechnicalVariants) {
        this.usesTechnicalVariants = usesTechnicalVariants;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<ProductColorImage> getColorImages() {
        return colorImages;
    }

    public void setColorImages(List<ProductColorImage> colorImages) {
        this.colorImages = colorImages;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", description=" + description + ", basePrice=" + basePrice
                + ", usesTechnicalVariants=" + usesTechnicalVariants + ", category=" + category + ", variants="
                + variants + ", colorImages=" + colorImages + "}";
    }

}
