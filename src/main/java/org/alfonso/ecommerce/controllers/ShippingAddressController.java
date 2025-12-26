package org.alfonso.ecommerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alfonso.ecommerce.entities.ShippingAddress;
import org.alfonso.ecommerce.services.ShippingAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class ShippingAddressController {
    private ShippingAddressService shippingAddressService;

    @GetMapping
    public ResponseEntity<List<ShippingAddress>> findAll() {
        return ResponseEntity.ok(shippingAddressService.findAll());
    }

    @PostMapping
    public ResponseEntity<ShippingAddress> save(
            @Valid ShippingAddress address
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shippingAddressService.save(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddress> update(
            @PathVariable String id,
            @Valid ShippingAddress address
    ) {
        return ResponseEntity.ok(shippingAddressService.update(id, address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {
        shippingAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
