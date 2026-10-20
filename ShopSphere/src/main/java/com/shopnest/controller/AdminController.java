package com.shopnest.controller;

import com.shopnest.dto.AdminLoginDto;
import com.shopnest.dto.ProductDto;
import com.shopnest.model.Order;
import com.shopnest.model.Product;
import com.shopnest.model.User;
import com.shopnest.service.OrderService;
import com.shopnest.service.ProductService;
import com.shopnest.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller managing the Administrative Panel:
 * - Authentication (admin@shopnest.com / admin123)
 * - Dashboard metrics
 * - Product catalog CRUD (add, edit, delete)
 * - Order fulfillment and status tracking
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    // Helper method to verify admin session
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("adminUser") != null;
    }

    /**
     * Admin login page.
     */
    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        if (isAdminLoggedIn(session)) {
            return "redirect:/admin/dashboard";
        }
        if (!model.containsAttribute("loginDto")) {
            model.addAttribute("loginDto", new AdminLoginDto());
        }
        return "admin/login";
    }

    /**
     * Process admin login POST.
     */
    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginDto") AdminLoginDto loginDto,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/login";
        }

        Optional<User> adminOpt = userService.authenticateAdmin(loginDto.getEmail(), loginDto.getPassword());

        if (adminOpt.isPresent()) {
            session.setAttribute("adminUser", adminOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, Administrator!");
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid credentials. Use admin@shopnest.com / admin123");
            return "redirect:/admin/login";
        }
    }

    /**
     * Admin logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("adminUser");
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully.");
        return "redirect:/admin/login";
    }

    /**
     * Admin redirect root.
     */
    @GetMapping
    public String adminRoot(HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Admin Dashboard with overview KPIs and recent orders.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("totalProducts", productService.getTotalProductCount());
        model.addAttribute("totalOrders", orderService.getTotalOrdersCount());
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("lowStockCount", productService.getLowStockProducts(5).size());
        model.addAttribute("recentOrders", orderService.getRecentOrders());

        return "admin/dashboard";
    }

    // ===================================================================
    // PRODUCT MANAGEMENT (CRUD)
    // ===================================================================

    /**
     * View all products in a management table.
     */
    @GetMapping("/products")
    public String listProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("products", productService.filterAndSearch(category, keyword));
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("selectedCategory", category != null ? category : "All");
        model.addAttribute("keyword", keyword);

        return "admin/products";
    }

    /**
     * Show form to add a new product.
     */
    @GetMapping("/products/new")
    public String showAddProductForm(HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (!model.containsAttribute("productDto")) {
            model.addAttribute("productDto", new ProductDto());
        }
        model.addAttribute("isEdit", false);
        model.addAttribute("categories", productService.getAllCategories());

        return "admin/product-form";
    }

    /**
     * Show form to edit an existing product.
     */
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Product product = productService.getProductById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setImageUrl(product.getImageUrl());
        dto.setRating(product.getRating());
        dto.setFeatured(product.getFeatured());

        model.addAttribute("productDto", dto);
        model.addAttribute("isEdit", true);
        model.addAttribute("categories", productService.getAllCategories());

        return "admin/product-form";
    }

    /**
     * Save product (Create or Update).
     */
    @PostMapping("/products/save")
    public String saveProduct(
            @Valid @ModelAttribute("productDto") ProductDto productDto,
            BindingResult bindingResult,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", productDto.getId() != null);
            model.addAttribute("categories", productService.getAllCategories());
            return "admin/product-form";
        }

        try {
            productService.saveProductFromDto(productDto);
            String action = productDto.getId() != null ? "updated" : "added";
            redirectAttributes.addFlashAttribute("successMessage", "Product successfully " + action + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    /**
     * Delete a product by ID.
     */
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete product: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    // ===================================================================
    // ORDER MANAGEMENT
    // ===================================================================

    /**
     * View all orders with status filtering.
     */
    @GetMapping("/orders")
    public String listOrders(
            @RequestParam(value = "status", required = false) String status,
            HttpSession session,
            Model model) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("orders", orderService.getOrdersByStatus(status));
        model.addAttribute("selectedStatus", status != null ? status : "ALL");

        return "admin/orders";
    }

    /**
     * View order details inspection page.
     */
    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Order order = orderService.getOrderById(id).orElse(null);
        if (order == null) {
            return "redirect:/admin/orders";
        }

        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    /**
     * Update order status.
     */
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated to " + status + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating status: " + e.getMessage());
        }

        return "redirect:/admin/orders/" + id;
    }
}
