package com.shopnest.controller;

import com.shopnest.dto.CartSummaryDto;
import com.shopnest.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller managing customer shopping cart interactions.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * View current shopping cart.
     */
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartSummaryDto cartSummary = cartService.getCartSummary(session.getId());
        model.addAttribute("cart", cartSummary);
        return "cart";
    }

    /**
     * Add product to cart with specified quantity.
     */
    @PostMapping("/add")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            @RequestParam(value = "redirect", required = false) String redirectUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            cartService.addToCart(session.getId(), productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Item successfully added to your cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/cart";
    }

    /**
     * Update quantity for a specific product in the cart.
     */
    @PostMapping("/update")
    public String updateQuantity(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            cartService.updateQuantity(session.getId(), productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Remove product from shopping cart.
     */
    @GetMapping("/remove/{productId}")
    public String removeFromCart(
            @PathVariable("productId") Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            cartService.removeFromCart(session.getId(), productId);
            redirectAttributes.addFlashAttribute("successMessage", "Product removed from cart.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart";
    }

    /**
     * Clear all items in cart.
     */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(session.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Your cart has been cleared.");
        return "redirect:/cart";
    }
}
