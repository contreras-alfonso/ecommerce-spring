package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {
    long countByUserIdAndIsDeletedFalse(String userId);
    boolean existsByIsDefaultTrueAndUserIdAndIsDeletedFalse(String userId);
    Optional<ShippingAddress> getByIsDefaultTrueAndUserIdAndIsDeletedFalse(String userId);
    List<ShippingAddress> getAllByUserIdAndIsDeletedFalse(String userId);
    Optional<ShippingAddress> findByIdAndUserIdAndIsDeletedFalse(String addressId, String userId);
}
