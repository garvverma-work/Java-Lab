package com.shopnest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an Order placed by a customer.
 * Maps directly to the 'orders' table in the H2 database.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique tracking order number (e.g. SN-ORD-20261019-4821)
    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 100)
    private String customerEmail;

    @Column(nullable = false, length = 20)
    private String customerPhone;

    @Column(nullable = false, length = 255)
    private String shippingAddress;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 20)
    private String postalCode;

    // Fixed to "Cash on Delivery" as required
    @Column(nullable = false, length = 50)
    private String paymentMethod = "Cash on Delivery";

    // Payment status: Pending (for COD until delivered) / Paid / Refunded
    @Column(nullable = false, length = 30)
    private String paymentStatus = "Pending";

    // Order lifecycle: PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    @Column(nullable = false, length = 30)
    private String orderStatus = "PLACED";

    // Grand total in INR (₹)
    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    // List of purchased items in this order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    public Order(String orderNumber, String customerName, String customerEmail, 
                 String customerPhone, String shippingAddress, String city, 
                 String postalCode, Double totalAmount) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.paymentMethod = "Cash on Delivery";
        this.paymentStatus = "Pending";
        this.orderStatus = "PLACED";
        this.totalAmount = totalAmount;
        this.orderDate = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }

    // Helper to add order item
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // Helper to format order date for display in Thymeleaf
    public String getFormattedDate() {
        if (orderDate != null) {
            return orderDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        }
        return "";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
