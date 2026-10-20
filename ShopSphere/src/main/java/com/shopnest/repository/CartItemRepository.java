package com.shopnest.repository;

import com.shopnest.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for CartItem entity.
 * Handles database-backed persistence of visitor/customer shopping carts.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Retrieve all cart items associated with a visitor's session ID
    List<CartItem> findBySessionIdOrderByAddedAtDesc(String sessionId);

    // Find specific item in a session's cart
    Optional<CartItem> findBySessionIdAndProductId(String sessionId, Long productId);

    // Count distinct items in a session's cart
    int countBySessionId(String sessionId);

    // Delete single item from session's cart
    @Transactional
    @Modifying
    void deleteBySessionIdAndProductId(String sessionId, Long productId);

    // Empty entire cart for a session after order completion
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.sessionId = :sessionId")
    void deleteAllBySessionId(@Param("sessionId") String sessionId);
}
