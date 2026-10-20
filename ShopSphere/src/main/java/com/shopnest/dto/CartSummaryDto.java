package com.shopnest.dto;

import com.shopnest.model.CartItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object summarizing cart content, subtotal, shipping, and grand total.
 */
public class CartSummaryDto {

    private List<CartItem> items = new ArrayList<>();
    private Double subtotal = 0.0;
    private Double shippingFee = 0.0; // Free delivery across India
    private Double totalAmount = 0.0;
    private int totalItemCount = 0;

    public CartSummaryDto() {
    }

    public CartSummaryDto(List<CartItem> items) {
        if (items != null) {
            this.items = items;
            double sum = 0.0;
            int count = 0;
            for (CartItem item : items) {
                sum += item.getSubtotal();
                count += item.getQuantity();
            }
            this.subtotal = sum;
            this.shippingFee = 0.0; // Free shipping promotion
            this.totalAmount = this.subtotal + this.shippingFee;
            this.totalItemCount = count;
        }
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    // Getters and Setters
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
}
