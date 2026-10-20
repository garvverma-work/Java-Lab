package com.shopnest.service;

import com.shopnest.dto.CheckoutRequestDto;
import com.shopnest.model.CartItem;
import com.shopnest.model.Order;
import com.shopnest.model.OrderItem;
import com.shopnest.model.Product;
import com.shopnest.repository.CartItemRepository;
import com.shopnest.repository.OrderRepository;
import com.shopnest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service managing customer checkout, inventory deduction, order placement,
 * and order lifecycle tracking for administrators.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, 
                        ProductRepository productRepository, 
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Places a new order from current session's cart items.
     * Validates stock, deducts inventory, creates Order and OrderItem records,
     * and clears the shopping cart.
     */
    @Transactional
    public Order placeOrder(CheckoutRequestDto dto, String sessionId) {
        List<CartItem> cartItems = cartItemRepository.findBySessionIdOrderByAddedAtDesc(sessionId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Please add products before placing an order.");
        }

        // Generate unique readable order reference (e.g. SN-ORD-20261019-7492)
        String orderNumber = generateOrderNumber();

        // Calculate total amount
        double totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }

        // Create main Order entity
        Order order = new Order(
                orderNumber,
                dto.getCustomerName().trim(),
                dto.getEmail().trim(),
                dto.getPhone().trim(),
                dto.getShippingAddress().trim(),
                dto.getCity().trim(),
                dto.getPostalCode().trim(),
                totalAmount
        );

        // Convert cart items to order items and update warehouse stock
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // Check stock availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName() 
                        + ". Available: " + product.getStockQuantity());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Create OrderItem line snapshot
            OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity());
            order.addItem(orderItem);
        }

        // Persist order with cascade to all items
        Order savedOrder = orderRepository.save(order);

        // Clear cart for this session after successful placement
        cartItemRepository.deleteAllBySessionId(sessionId);

        return savedOrder;
    }

    // Retrieve order by unique orderNumber
    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    // Retrieve order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Get all orders sorted newest first
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    // Filter orders by status
    public List<Order> getOrdersByStatus(String status) {
        if (status == null || status.trim().isEmpty() || "ALL".equalsIgnoreCase(status)) {
            return orderRepository.findAllByOrderByOrderDateDesc();
        }
        return orderRepository.findByOrderStatusOrderByOrderDateDesc(status.toUpperCase());
    }

    // Get 5 most recent orders for Admin Dashboard
    public List<Order> getRecentOrders() {
        return orderRepository.findTop5ByOrderByOrderDateDesc();
    }

    // Update order status (PLACED -> CONFIRMED -> SHIPPED -> DELIVERED -> CANCELLED)
    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        order.setOrderStatus(newStatus.toUpperCase());
        if ("DELIVERED".equalsIgnoreCase(newStatus)) {
            order.setPaymentStatus("Paid");
        }
        orderRepository.save(order);
    }

    // Get total order count
    public long getTotalOrdersCount() {
        return orderRepository.count();
    }

    // Get total revenue
    public Double getTotalRevenue() {
        Double revenue = orderRepository.calculateTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    // Helper to generate unique order reference
    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomPart = 1000 + new Random().nextInt(9000);
        return "SN-ORD-" + datePart + "-" + randomPart;
    }
}
