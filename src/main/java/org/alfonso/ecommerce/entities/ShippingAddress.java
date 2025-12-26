package org.alfonso.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "shipping_addresses")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @NotBlank(message = "La dirección es requerida")
    private String address;

    private String reference;

    @NotBlank(message = "El celular es requerido")
    private String phone;

    @Column(name = "is_default")
    private boolean isDefault;

    @NotBlank(message = "El ubigeo es requerido")
    private String ubigeo;

    @NotNull(message = "La latitud es requerida")
    private BigDecimal lat;

    @NotNull(message = "La longitud es requerida.")
    private BigDecimal lng;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
