package com.shopnest.controller;

import com.shopnest.dto.CartSummaryDto;
import com.shopnest.dto.CheckoutRequestDto;
import com.shopnest.model.Order;
import com.shopnest.service.CartService;
import com.shopnest.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller managing checkout process, customer delivery data capture,
 * Cash on Delivery selection, and order receipt rendering.
 */
@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    /**
     * Display checkout page with address form and order summary sidebar.
     */
    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        CartSummaryDto cart = cartService.getCartSummary(session.getId());

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new CheckoutRequestDto());
        }

        model.addAttribute("cart", cart);
        return "checkout";
    }

    /**
     * Process checkout form, validate customer inputs, place order, and redirect to confirmation.
     */
    @PostMapping("/checkout/place-order")
    public String placeOrder(
            @Valid @ModelAttribute("checkoutForm") CheckoutRequestDto form,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        CartSummaryDto cart = cartService.getCartSummary(session.getId());

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "checkout";
        }

        try {
            Order order = orderService.placeOrder(form, session.getId());
            return "redirect:/order-confirmation/" + order.getOrderNumber();
        } catch (Exception e) {
            model.addAttribute("cart", cart);
            model.addAttribute("errorMessage", "Failed to place order: " + e.getMessage());
            return "checkout";
        }
    }

    /**
     * Display order confirmation receipt page with order details and COD summary.
     */
    @GetMapping("/order-confirmation/{orderNumber}")
    public String orderConfirmation(@PathVariable("orderNumber") String orderNumber, Model model) {
        Order order = orderService.getOrderByNumber(orderNumber)
                .orElse(null);

        if (order == null) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
