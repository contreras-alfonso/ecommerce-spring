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
    private final ShippingAddressService shippingAddressService;

    @GetMapping
    public ResponseEntity<List<ShippingAddress>> findAll() {
        return ResponseEntity.ok(shippingAddressService.findAll());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAll() {
        return ResponseEntity.ok(shippingAddressService.countAll());
    }

    @PostMapping
    public ResponseEntity<ShippingAddress> save(
            @Valid @RequestBody ShippingAddress address
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shippingAddressService.save(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddress> update(
            @PathVariable String id,
            @Valid @RequestBody ShippingAddress address
    ) {
        return ResponseEntity.ok(shippingAddressService.update(id, address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ShippingAddress>> delete(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(shippingAddressService.delete(id));
    }
}
