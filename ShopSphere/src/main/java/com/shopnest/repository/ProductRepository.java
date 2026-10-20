package com.shopnest.repository;

import com.shopnest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Product entity.
 * Provides out-of-the-box CRUD operations, category filtering, and keyword search.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find product by unique SKU code
    Optional<Product> findBySku(String sku);

    // Check if a product SKU already exists
    boolean existsBySku(String sku);

    // Filter products by specific category
    List<Product> findByCategoryIgnoreCase(String category);

    // Find featured products for the home page showcase
    List<Product> findByFeaturedTrue();

    // Search products by name or description keyword (case-insensitive)
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    // Filter by category AND search keyword
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.category) = LOWER(:category) AND (" +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword);

    // Get list of distinct product categories in the system
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category ASC")
    List<String> findDistinctCategories();

    // Alert query for low stock inventory (for Admin dashboard)
    List<Product> findByStockQuantityLessThanEqual(Integer threshold);
}
