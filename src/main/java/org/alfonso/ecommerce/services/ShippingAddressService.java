package org.alfonso.ecommerce.services;

import org.alfonso.ecommerce.entities.ShippingAddress;

import java.util.List;

public interface ShippingAddressService {
    List<ShippingAddress> findAll();
    ShippingAddress save(ShippingAddress shippingAddress);
    ShippingAddress update(String id, ShippingAddress address);
    void delete(String id);
}
