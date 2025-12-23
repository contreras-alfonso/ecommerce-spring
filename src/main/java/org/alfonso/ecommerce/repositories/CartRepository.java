package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.Cart;
import org.alfonso.ecommerce.entities.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.status = :status AND c.userId = :userId")
    Optional<Cart> findByStatusAndUserId(CartStatus status, String userId);
}
