package org.alfonso.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")
public class Brand extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    public Brand(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
}
