package com.shopnest.service;

import com.shopnest.dto.ProductDto;
import com.shopnest.model.Product;
import com.shopnest.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class handling all catalog, inventory, and product management operations.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieve single product by primary key ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Retrieve product by SKU code
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    // Filter products by category
    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "All".equalsIgnoreCase(category)) {
            return productRepository.findAll();
        }
        return productRepository.findByCategoryIgnoreCase(category);
    }

    // Search products by keyword
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchProducts(keyword.trim());
    }

    // Combined category and keyword search
    public List<Product> filterAndSearch(String category, String keyword) {
        boolean hasCategory = category != null && !category.trim().isEmpty() && !"All".equalsIgnoreCase(category);
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasCategory && hasKeyword) {
            return productRepository.searchByCategoryAndKeyword(category.trim(), keyword.trim());
        } else if (hasCategory) {
            return productRepository.findByCategoryIgnoreCase(category.trim());
        } else if (hasKeyword) {
            return productRepository.searchProducts(keyword.trim());
        }
        return productRepository.findAll();
    }

    // Get featured products for home page banner
    public List<Product> getFeaturedProducts() {
        List<Product> featured = productRepository.findByFeaturedTrue();
        if (featured.isEmpty()) {
            // Fallback to top 6 products if no featured flag
            return productRepository.findAll().stream().limit(6).toList();
        }
        return featured;
    }

    // Get all available distinct categories
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    // Get count of products
    public long getTotalProductCount() {
        return productRepository.count();
    }

    // Get low-stock products (inventory threshold <= 5)
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThanEqual(threshold);
    }

    // Save or update an existing Product entity
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Save or update product from ProductDto (Admin form submission)
    @Transactional
    public Product saveProductFromDto(ProductDto dto) {
        Product product;
        if (dto.getId() != null) {
            product = productRepository.findById(dto.getId())
                    .orElse(new Product());
        } else {
            product = new Product();
        }

        product.setSku(dto.getSku().trim().toUpperCase());
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription().trim());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory().trim());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(dto.getImageUrl().trim());
        product.setRating(dto.getRating() != null ? dto.getRating() : 4.5);
        product.setFeatured(dto.getFeatured() != null ? dto.getFeatured() : false);

        return productRepository.save(product);
    }

    // Delete product by ID
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
