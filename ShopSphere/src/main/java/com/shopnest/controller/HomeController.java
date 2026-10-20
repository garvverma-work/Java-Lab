package com.shopnest.controller;

import com.shopnest.model.Product;
import com.shopnest.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller handling public storefront pages: Home, Product Catalog,
 * Product Details, About, and Contact pages.
 */
@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Home page with hero banner, category pills, search bar, and featured product grid.
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Product> featuredProducts = productService.getFeaturedProducts();
        List<Product> allProducts = productService.getAllProducts();
        List<String> categories = productService.getAllCategories();

        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("categories", categories);
        return "index";
    }

    /**
     * Full product catalog with category filtering, keyword searching, and price/name sorting.
     */
    @GetMapping("/products")
    public String products(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "default") String sort,
            Model model) {

        List<Product> products = productService.filterAndSearch(category, keyword);

        // Apply sorting
        if ("price_asc".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .collect(Collectors.toList());
        } else if ("name_asc".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toList());
        } else if ("newest".equalsIgnoreCase(sort)) {
            products = products.stream()
                    .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category != null ? category : "All");
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentSort", sort);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("totalFound", products.size());

        return "products";
    }

    /**
     * Single product detail view with full specifications, SKU, stock status,
     * and related products from the same category.
     */
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        // Fetch related products in the same category excluding the current product
        List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory())
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "product-detail";
    }

    /**
     * College Project Info & Tech Stack overview page.
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("totalProducts", productService.getTotalProductCount());
        return "about";
    }

    /**
     * Customer support and contact page.
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}
