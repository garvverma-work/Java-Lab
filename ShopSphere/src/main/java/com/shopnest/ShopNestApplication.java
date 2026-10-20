package com.shopnest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Entry Point for ShopNest — Online Shopping Store Application.
 *
 * Tech Stack:
 * - Spring Boot 3
 * - Spring Data JPA
 * - Thymeleaf
 * - H2 File-Based Database (Works completely offline on your PC)
 *
 * Local Access:
 * - Storefront:   http://localhost:8080
 * - Admin Panel:  http://localhost:8080/admin/login
 * - H2 Console:   http://localhost:8080/h2-console
 */
@SpringBootApplication
public class ShopNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopNestApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  SHOPNEST — ONLINE SHOPPING STORE STARTED SUCCESSFULLY! ");
        System.out.println("==========================================================");
        System.out.println("  Storefront:     http://localhost:8080");
        System.out.println("  Admin Login:    http://localhost:8080/admin/login");
        System.out.println("  Admin User:     admin@shopnest.com / admin123");
        System.out.println("  H2 DB Console:  http://localhost:8080/h2-console");
        System.out.println("  JDBC URL:       jdbc:h2:file:./data/shopnestdb");
        System.out.println("==========================================================");
    }
}
