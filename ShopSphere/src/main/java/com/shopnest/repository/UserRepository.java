package com.shopnest.repository;

import com.shopnest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Retrieve user by email (used during Admin login)
    Optional<User> findByEmail(String email);

    // Verify whether an email is already registered
    boolean existsByEmail(String email);
}
