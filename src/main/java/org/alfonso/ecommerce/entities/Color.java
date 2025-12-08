package org.alfonso.ecommerce.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    @NotBlank(message = "El color hexadecimal es requerido")
    private String hex;

    @Embedded
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Audit audit = new Audit();

    public Color(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }
}
