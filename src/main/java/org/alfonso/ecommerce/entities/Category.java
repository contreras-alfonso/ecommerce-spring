package org.alfonso.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    private String slug;

    @NotBlank(message = "El nombre es requerido")
    @Column(unique = true)
    private String name;

    public Category(String slug, String name) {
        this.slug = slug;
        this.name = name;
    }
}
