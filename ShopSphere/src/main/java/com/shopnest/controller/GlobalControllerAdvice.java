package com.shopnest.controller;

import com.shopnest.service.CartService;
import com.shopnest.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Controller Advice that injects global variables (cart item count, category list)
 * into the model for every Thymeleaf page render.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private final CartService cartService;
    private final ProductService productService;

    public GlobalControllerAdvice(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Injects the dynamic cart item count into the header badge across all pages.
     */
    @ModelAttribute("cartItemCount")
    public int getCartItemCount(HttpSession session) {
        if (session == null) {
            return 0;
        }
        return cartService.getCartItemCount(session.getId());
    }

    /**
     * Injects available product categories into navigation menus across all pages.
     */
    @ModelAttribute("navCategories")
    public List<String> getNavCategories() {
        return productService.getAllCategories();
    }
}
