package com.shopnest.config;

import com.shopnest.model.Product;
import com.shopnest.repository.ProductRepository;
import com.shopnest.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Automatically seeds default sample products and the administrator account
 * on application startup into the H2 file-based database.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserService userService;

    public DataInitializer(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // 1. Initialize default administrator account (admin@shopnest.com / admin123)
        userService.initDefaultAdmin();

        // 2. Initialize sample products if catalog is empty or has fewer than 12 products
        if (productRepository.count() < 12) {
            seedSampleProducts();
        }
    }

    private void seedSampleProducts() {
        List<Product> products = Arrays.asList(
                // ==================== ELECTRONICS ====================
                new Product(
                        "SN-ELEC-001",
                        "Sony WH-1000XM5 Wireless Noise-Canceling Headphones",
                        "Industry-leading noise canceling with two processors and 8 microphones. Up to 30-hour battery life with quick charging, ultra-comfortable lightweight design with soft fit leather.",
                        26990.0,
                        "Electronics",
                        25,
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        true
                ),
                new Product(
                        "SN-ELEC-002",
                        "Apple Watch SE (2nd Gen) GPS 44mm Starlight",
                        "Essential features to help you stay connected, active, healthy, and safe. Crash detection, enhanced workout metrics, heart rate tracking, and water resistance up to 50 meters.",
                        29900.0,
                        "Electronics",
                        18,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        true
                ),
                new Product(
                        "SN-ELEC-003",
                        "Logitech MX Master 3S Wireless Performance Mouse",
                        "Quiet clicks with 8000 DPI any-surface tracking including glass. MagSpeed electromagnetic scrolling, ergonomic silhouette, and customizable gesture buttons.",
                        8995.0,
                        "Electronics",
                        40,
                        "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        false
                ),
                new Product(
                        "SN-ELEC-004",
                        "Samsung 27-inch Curved 144Hz Gaming Monitor",
                        "1800R curved screen with 144Hz refresh rate and AMD FreeSync for tear-free gaming. Full HD resolution with 3000:1 contrast ratio and Eye Saver mode.",
                        18499.0,
                        "Electronics",
                        12,
                        "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        true
                ),

                // ==================== FASHION ====================
                new Product(
                        "SN-FASH-001",
                        "Classic Oxford Pure Cotton Slim-Fit Shirt",
                        "Timeless button-down shirt crafted from 100% premium long-staple combed cotton. Breathable, easy to iron, suitable for office wear and casual evening outings.",
                        1899.0,
                        "Fashion",
                        55,
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop&q=80",
                        4.5,
                        true
                ),
                new Product(
                        "SN-FASH-002",
                        "Levi's 511 Slim Fit Stretch Denim Jeans",
                        "A modern slim fit with room to move. Premium denim infused with stretch for all-day flexibility and iconic 5-pocket styling in dark indigo wash.",
                        2999.0,
                        "Fashion",
                        35,
                        "https://images.unsplash.com/photo-1542272604-780c96856592?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        false
                ),
                new Product(
                        "SN-FASH-003",
                        "Puma Nitro Pro Lightweight Running Shoes",
                        "Advanced NITRO foam cushioning providing superior responsiveness in a feather-light package. High-traction rubber outsole engineered for road and gym workouts.",
                        4499.0,
                        "Fashion",
                        28,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        true
                ),
                new Product(
                        "SN-FASH-004",
                        "Elegance Floral Summer Chiffon Maxi Dress",
                        "Graceful tiered maxi dress featuring romantic floral prints, breathable chiffon fabric, comfortable elasticated waistline, and ruffled cap sleeves.",
                        2499.0,
                        "Fashion",
                        20,
                        "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        false
                ),

                // ==================== HOME APPLIANCES ====================
                new Product(
                        "SN-HOME-001",
                        "Philips Digital Air Fryer HD9252 (4.1 Litre)",
                        "Rapid Air Technology lets you fry, bake, grill, and roast with up to 90% less fat. Touch screen with 7 presets and keep-warm function for healthy family cooking.",
                        7499.0,
                        "Home Appliances",
                        15,
                        "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        true
                ),
                new Product(
                        "SN-HOME-002",
                        "Morphy Richards Espresso & Cappuccino Coffee Maker",
                        "15-bar powerful pressure pump for rich taste and authentic crema. Includes milk frothing wand for creamy lattes and cappuccinos right at home.",
                        6299.0,
                        "Home Appliances",
                        10,
                        "https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop&q=80",
                        4.5,
                        false
                ),
                new Product(
                        "SN-HOME-003",
                        "Dyson V8 Cordless Stick Vacuum Cleaner",
                        "Engineered for homes with pets. Up to 40 minutes of run time, de-tangling Motorbar cleaner head, and converts easily to a handheld vacuum for car and upholstery.",
                        29990.0,
                        "Home Appliances",
                        8,
                        "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        false
                ),

                // ==================== BOOKS ====================
                new Product(
                        "SN-BOOK-001",
                        "Atomic Habits by James Clear (Paperback)",
                        "An Easy & Proven Way to Build Good Habits & Break Bad Ones. Over 15 million copies sold globally, providing practical strategies to transform daily routines.",
                        549.0,
                        "Books",
                        80,
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        true
                ),
                new Product(
                        "SN-BOOK-002",
                        "Clean Code: A Handbook of Agile Software Craftsmanship",
                        "Robert C. Martin's legendary software engineering guide detailing best practices for writing clean, readable, maintainable, and refactorable Java code.",
                        1299.0,
                        "Books",
                        45,
                        "https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=600&auto=format&fit=crop&q=80",
                        4.9,
                        false
                ),
                new Product(
                        "SN-BOOK-003",
                        "The Psychology of Money by Morgan Housel",
                        "Timeless lessons on wealth, greed, and happiness. Explores 19 short stories demonstrating how people think about money and practical behavioral finance.",
                        399.0,
                        "Books",
                        60,
                        "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        true
                ),

                // ==================== ACCESSORIES ====================
                new Product(
                        "SN-ACCS-001",
                        "Fossil Vintage Brown Genuine Leather Bifold Wallet",
                        "Handcrafted from 100% genuine full-grain leather with RFID blocking protection. Features 8 credit card slots, 2 slip pockets, and dual currency compartments.",
                        2199.0,
                        "Accessories",
                        30,
                        "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600&auto=format&fit=crop&q=80",
                        4.7,
                        true
                ),
                new Product(
                        "SN-ACCS-002",
                        "Ray-Ban Aviator Classic UV-Protected Sunglasses",
                        "Originally designed for U.S. aviators in 1937. Iconic gold metal frame with G-15 green mineral glass lenses providing 100% UV400 protection.",
                        8290.0,
                        "Accessories",
                        16,
                        "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&auto=format&fit=crop&q=80",
                        4.8,
                        false
                ),
                new Product(
                        "SN-ACCS-003",
                        "Nordic Water-Resistant Laptop Backpack (30L)",
                        "Ergonomic travel and college backpack with dedicated 15.6-inch padded laptop sleeve, external USB charging port, anti-theft back pocket, and breathable shoulder straps.",
                        1799.0,
                        "Accessories",
                        40,
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
                        4.6,
                        true
                )
        );

        for (Product product : products) {
            if (!productRepository.existsBySku(product.getSku())) {
                productRepository.save(product);
            }
        }
    }
}
