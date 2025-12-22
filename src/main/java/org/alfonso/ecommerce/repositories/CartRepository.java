package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
}
