package com.shopnest.repository;

import com.shopnest.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for OrderItem entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Retrieve all line items belonging to a particular order
    List<OrderItem> findByOrderId(Long orderId);
}
