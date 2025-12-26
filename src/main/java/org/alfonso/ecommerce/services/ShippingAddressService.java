package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.ShippingAddress;

import java.util.List;

public interface ShippingAddressService {
    long countAll();
    List<ShippingAddress> findAll();
    ShippingAddress save(ShippingAddress shippingAddress);
    ShippingAddress update(String id, ShippingAddress address);
    List<ShippingAddress> delete(String id);
}
