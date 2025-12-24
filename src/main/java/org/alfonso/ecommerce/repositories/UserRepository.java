package org.alfonso.ecommerce.repositories;

import org.alfonso.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String username);
    Boolean existsByEmail(String username);
}
