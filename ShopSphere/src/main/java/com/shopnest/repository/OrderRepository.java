package com.shopnest.repository;

import com.shopnest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Retrieve all orders sorted from newest to oldest
    List<Order> findAllByOrderByOrderDateDesc();

    // Look up order by tracking number (e.g. SN-ORD-20261019-XXXX)
    Optional<Order> findByOrderNumber(String orderNumber);

    // Filter orders by status (PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
    List<Order> findByOrderStatusOrderByOrderDateDesc(String orderStatus);

    // Get order count by status for Admin metrics
    long countByOrderStatus(String orderStatus);

    // Sum total revenue from delivered/confirmed orders
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o WHERE o.orderStatus <> 'CANCELLED'")
    Double calculateTotalRevenue();

    // Fetch top 5 recent orders for admin dashboard
    List<Order> findTop5ByOrderByOrderDateDesc();
}
