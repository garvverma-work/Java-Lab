# 🛍️ ShopNest — Online Shopping Store

> **A Complete, Unique, College-Level E-Commerce Web Application built purely with Java & Spring Boot.**  
> *Designed and developed for Final Year B.Tech, BCA, MCA, or M.Tech Academic Project Demonstrations &amp; Viva Evaluations.*

---

## 📌 Badges & Technology Stack

![Java 17](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-3.2.4-brightgreen?style=for-the-badge&logo=springboot)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf)
![H2 Database](https://img.shields.io/badge/Database-H2_File_Based-blue?style=for-the-badge&logo=sqlite)
![Bootstrap 5](https://img.shields.io/badge/Frontend-Bootstrap_5.3-purple?style=for-the-badge&logo=bootstrap)
![Maven](https://img.shields.io/badge/Build-Maven_3.9-C71A36?style=for-the-badge&logo=apachemaven)
![VS Code](https://img.shields.io/badge/IDE-VS_Code-007ACC?style=for-the-badge&logo=visualstudiocode)

---

## 📖 Table of Contents
1. [Project Overview](#-project-overview)
2. [Key Features](#-key-features)
3. [Architecture & Design Pattern](#-architecture--design-pattern)
4. [Database Schema (H2 Relational)](#-database-schema-h2-relational)
5. [Sample Product Catalog (16 Items)](#-sample-product-catalog-16-items)
6. [Project Folder Structure](#-project-folder-structure)
7. [Step-by-Step Local Setup in VS Code](#-step-by-step-local-setup-in-vs-code)
8. [Access URLs & Default Credentials](#-access-urls--default-credentials)
9. [Step-by-Step GitHub Upload Guide](#-step-by-step-github-upload-guide)
10. [Free Cloud Deployment Guide (Render / Railway / Koyeb)](#-free-cloud-deployment-guide)
11. [College Viva & Interview Q&A](#-college-viva--interview-qa)
12. [Future Improvements](#-future-improvements)

---

## 🌟 Project Overview

**ShopNest** is a full-featured, offline-ready e-commerce web platform developed entirely using standard Java technologies. The application avoids external paid APIs or third-party cloud database dependencies by running an embedded **file-based H2 database** (`./data/shopnestdb.mv.db`), ensuring that the website functions 100% offline on any student's laptop or college laboratory computer.

The application serves server-side rendered HTML5 views via **Thymeleaf**, styled with a custom modern vibrant theme over **Bootstrap 5.3**, with responsive layouts for both mobile smartphones and desktop screens.

---

## ✨ Key Features

### 1. 🏠 Modern Storefront & Home Page
- **Logo & Identity:** Custom SVG branding and gradient "ShopNest" typography.
- **Hero Banner:** Eye-catching gradient showcase highlighting next-gen college project features.
- **Search Bar:** Real-time search by product name, SKU, or description keywords.
- **Category Filter Pills:** Quick filters for *Electronics*, *Fashion*, *Home Appliances*, *Books*, and *Accessories*.
- **Featured Product Grid:** Responsive card layout with smooth hover lift micro-animations, stock badges, rating stars, and instant *Add to Cart* action.
- **College Project Footer:** Comprehensive academic credits, college project label, quick links, and system specs.

### 2. 📦 Product Catalog & Details
- **Over 16 Realistic Products:** Pre-populated across 5 diverse categories.
- **Product Attributes:** Product ID, Unique SKU (e.g., `SN-ELEC-001`), Name, Detailed Description, Price in INR (₹), Category, Stock Quantity, Image URL, Rating, and Featured flag.
- **Dedicated Product View (`/products/{id}`):** High-resolution image showcase, interactive quantity stepper (+/-), live inventory counter, product specifications, and related items from the same category.
- **Sorting Options:** Sort by Price (Low to High / High to Low), Product Name (A-Z), and Newest Arrivals.

### 3. 🛒 Persistent Shopping Cart
- **Add to Cart:** Add items directly from the home page, catalog, or product detail page.
- **Quantity Adjuster:** Increase or decrease item quantities with automated stock cap checks.
- **Item Removal & Clear Cart:** Delete individual items or empty the entire cart with one click.
- **Cart Persistence:** Cart items are stored directly in the H2 database keyed by visitor HTTP session IDs, preserving cart data across browser refreshes and tab reloads.
- **Live Navbar Badge:** Automatic badge count updates dynamically on every page.

### 4. 💳 Customer Checkout & Cash on Delivery (COD)
- **Customer Form:** Capture full customer name, email address, 10-digit mobile number, street delivery address, city, and 6-digit postal pincode with validation feedback.
- **Payment Method:** Dedicated **Cash on Delivery (COD)** payment mode with informative delivery instructions.
- **Stock Deduction:** Automatically reduces warehouse inventory upon order placement and prevents overselling.
- **Printable Order Receipt:** Comprehensive invoice showing unique Order Tracking ID (e.g. `SN-ORD-20261019-4821`), date, customer address, itemized product snapshot, and printable packing slip.

### 5. 🛡️ Administrative Control Panel (`/admin`)
- **Admin Authentication:** Simple sign-in with default administrator credentials (`admin@shopnest.com` / `admin123`).
- **Dashboard KPIs:** Real-time business metrics displaying Total Revenue (₹), Total Orders Placed, Products in Catalog, and Low Stock Alerts.
- **Product Management (Full CRUD):**
  - **Create:** Add new products with SKU, category, price, stock, description, and live image URL preview.
  - **Read:** Searchable and filterable data table with image thumbnails and stock badges.
  - **Update:** Edit existing product prices, descriptions, and restock warehouse quantities.
  - **Delete:** Remove discontinued products with confirmation guards.
- **Order Management:** View all placed customer orders, filter by status, inspect itemized order slips, and update order fulfillment stages (**PLACED** ➔ **CONFIRMED** ➔ **SHIPPED** ➔ **DELIVERED** ➔ **CANCELLED**).

---

## 🏛️ Architecture & Design Pattern

ShopNest implements the classic **Model-View-Controller (MVC)** layered enterprise architectural design pattern:

```
[ Browser / Client ]
        │  ▲
        │  │  HTTP GET / POST (HTML + CSS + JS)
        ▼  │
[ Spring MVC Controller Layer ]  ──▶ GlobalControllerAdvice (Cart Badge & Nav Menu)
        │  ▲
        ▼  │
[ Service Layer (Business Logic) ] ──▶ ProductService, CartService, OrderService, UserService
        │  ▲
        ▼  │
[ Spring Data JPA Repository Layer ] ──▶ ProductRepository, OrderRepository, CartItemRepository
        │  ▲
        ▼  │
[ Hibernate ORM Engine ]
        │  ▲
        ▼  │
[ Embedded H2 Relational Database ] ──▶ (File: ./data/shopnestdb.mv.db)
```

---

## 🗄️ Database Schema (H2 Relational)

The application automatically creates and updates relational database tables via Hibernate DDL auto-configuration (`spring.jpa.hibernate.ddl-auto=update`):

| Table Name | Primary Key | Key Columns / Foreign Keys | Description |
| :--- | :--- | :--- | :--- |
| `products` | `id` (BIGINT) | `sku` (UNIQUE), `name`, `description`, `price`, `category`, `stock_quantity`, `image_url`, `rating`, `featured`, `created_at` | Product catalog and warehouse inventory |
| `users` | `id` (BIGINT) | `email` (UNIQUE), `full_name`, `password`, `role`, `phone`, `created_at` | Administrative and customer credentials |
| `cart_items` | `id` (BIGINT) | `session_id`, `product_id` (FK ➔ `products.id`), `quantity`, `added_at` | Persistent visitor shopping carts |
| `orders` | `id` (BIGINT) | `order_number` (UNIQUE), `customer_name`, `customer_email`, `customer_phone`, `shipping_address`, `city`, `postal_code`, `payment_method`, `payment_status`, `order_status`, `total_amount`, `order_date` | Customer orders and billing metadata |
| `order_items` | `id` (BIGINT) | `order_id` (FK ➔ `orders.id`), `product_id` (FK ➔ `products.id`), `product_name`, `product_sku`, `product_image_url`, `price`, `quantity`, `subtotal` | Itemized line snapshots per order |

---

## 📦 Sample Product Catalog (16 Items)

The application automatically seeds 16 high-demand, realistic products into the database on first boot:

| ID | SKU | Product Name | Category | Price (INR) | Stock |
| :-: | :--- | :--- | :--- | :-: | :-: |
| 1 | `SN-ELEC-001` | Sony WH-1000XM5 Wireless Noise-Canceling Headphones | Electronics | ₹26,990.00 | 25 |
| 2 | `SN-ELEC-002` | Apple Watch SE (2nd Gen) GPS 44mm Starlight | Electronics | ₹29,900.00 | 18 |
| 3 | `SN-ELEC-003` | Logitech MX Master 3S Wireless Performance Mouse | Electronics | ₹8,995.00 | 40 |
| 4 | `SN-ELEC-004` | Samsung 27-inch Curved 144Hz Gaming Monitor | Electronics | ₹18,499.00 | 12 |
| 5 | `SN-FASH-001` | Classic Oxford Pure Cotton Slim-Fit Shirt | Fashion | ₹1,899.00 | 55 |
| 6 | `SN-FASH-002` | Levi's 511 Slim Fit Stretch Denim Jeans | Fashion | ₹2,999.00 | 35 |
| 7 | `SN-FASH-003` | Puma Nitro Pro Lightweight Running Shoes | Fashion | ₹4,499.00 | 28 |
| 8 | `SN-FASH-004` | Elegance Floral Summer Chiffon Maxi Dress | Fashion | ₹2,499.00 | 20 |
| 9 | `SN-HOME-001` | Philips Digital Air Fryer HD9252 (4.1 Litre) | Home Appliances | ₹7,499.00 | 15 |
| 10 | `SN-HOME-002` | Morphy Richards Espresso & Cappuccino Coffee Maker | Home Appliances | ₹6,299.00 | 10 |
| 11 | `SN-HOME-003` | Dyson V8 Cordless Stick Vacuum Cleaner | Home Appliances | ₹29,990.00 | 8 |
| 12 | `SN-BOOK-001` | Atomic Habits by James Clear (Paperback) | Books | ₹549.00 | 80 |
| 13 | `SN-BOOK-002` | Clean Code: A Handbook of Agile Software Craftsmanship | Books | ₹1,299.00 | 45 |
| 14 | `SN-BOOK-003` | The Psychology of Money by Morgan Housel | Books | ₹399.00 | 60 |
| 15 | `SN-ACCS-001` | Fossil Vintage Brown Genuine Leather Bifold Wallet | Accessories | ₹2,199.00 | 30 |
| 16 | `SN-ACCS-002` | Ray-Ban Aviator Classic UV-Protected Sunglasses | Accessories | ₹8,290.00 | 16 |

---

## 📁 Project Folder Structure

```
ShopNest/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── shopnest/
│   │   │           ├── ShopNestApplication.java          # Spring Boot main entry point
│   │   │           ├── config/
│   │   │           │   └── DataInitializer.java          # Automatic DB seeder (16 products + admin)
│   │   │           ├── controller/
│   │   │           │   ├── AdminController.java          # Admin dashboard, product CRUD & orders
│   │   │           │   ├── CartController.java           # Shopping cart interactions
│   │   │           │   ├── CheckoutController.java       # Customer details & order confirmation
│   │   │           │   ├── GlobalControllerAdvice.java   # Global cart counter badge injection
│   │   │           │   └── HomeController.java           # Storefront, catalog, & product detail
│   │   │           ├── dto/
│   │   │           │   ├── AdminLoginDto.java            # Admin sign-in validation DTO
│   │   │           │   ├── CartSummaryDto.java           # Shopping cart calculation DTO
│   │   │           │   ├── CheckoutRequestDto.java       # Customer delivery form DTO
│   │   │           │   └── ProductDto.java               # Admin add/edit product DTO
│   │   │           ├── model/
│   │   │           │   ├── CartItem.java                 # 'cart_items' database entity
│   │   │           │   ├── Order.java                    # 'orders' database entity
│   │   │           │   ├── OrderItem.java                # 'order_items' database entity
│   │   │           │   ├── Product.java                  # 'products' database entity
│   │   │           │   └── User.java                     # 'users' database entity
│   │   │           ├── repository/
│   │   │           │   ├── CartItemRepository.java       # Spring Data JPA Cart queries
│   │   │           │   ├── OrderItemRepository.java      # Order item queries
│   │   │           │   ├── OrderRepository.java          # Order & revenue aggregation queries
│   │   │           │   ├── ProductRepository.java        # Product search & filtering queries
│   │   │           │   └── UserRepository.java           # User credentials queries
│   │   │           └── service/
│   │   │               ├── CartService.java              # Cart management & stock checks
│   │   │               ├── OrderService.java             # Checkout & order processing logic
│   │   │               ├── ProductService.java           # Product business logic
│   │   │               └── UserService.java              # Admin authentication logic
│   │   └── resources/
│   │       ├── application.properties                    # H2 DB, server port & Thymeleaf config
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                         # Custom modern responsive stylesheet
│   │       │   ├── js/
│   │       │   │   └── main.js                           # Quantity steppers & interactive UI
│   │       │   └── images/
│   │       │       └── logo.svg                          # ShopNest vector logo
│   │       └── templates/
│   │           ├── about.html                            # College project architecture page
│   │           ├── cart.html                             # Shopping cart page
│   │           ├── checkout.html                         # Checkout & COD form
│   │           ├── contact.html                          # Support & contact page
│   │           ├── index.html                            # Storefront home page
│   │           ├── order-confirmation.html               # Printable invoice receipt
│   │           ├── product-detail.html                   # Product details & specifications
│   │           ├── products.html                         # Filterable product catalog
│   │           ├── admin/
│   │           │   ├── dashboard.html                    # Admin KPI metrics & recent orders
│   │           │   ├── login.html                        # Admin login page
│   │           │   ├── order-detail.html                 # Order inspector & status changer
│   │           │   ├── orders.html                       # Order management table
│   │           │   ├── product-form.html                 # Product add/edit form
│   │           │   └── products.html                     # Product management table
│   │           └── fragments/
│   │               ├── alerts.html                       # Floating flash messages
│   │               ├── footer.html                       # Global college project footer
│   │               └── header.html                       # Global navigation & cart badge
├── .gitignore                                            # Git ignore configuration
├── Dockerfile                                            # Multi-stage Docker deployment config
├── pom.xml                                               # Maven dependency configuration
└── README.md                                             # Complete documentation & setup guide
```

---

## 💻 Step-by-Step Local Setup in VS Code

Follow these clear steps to run ShopNest on Windows, macOS, or Linux:

### Step 1: Install Java JDK 17 or Above
1. Download **Eclipse Temurin JDK 17** (LTS) or **Oracle JDK 17+**:
   - [Adoptium Eclipse Temurin 17 Download](https://adoptium.net/temurin/releases/?version=17)
2. Run the installer and ensure the option **"Add to PATH"** is checked.
3. Verify installation in your terminal / command prompt:
   ```bash
   java -version
   ```
   *Expected output: `openjdk version "17.0.x" ...`*

### Step 2: Install VS Code
- Download and install Visual Studio Code from: [https://code.visualstudio.com/](https://code.visualstudio.com/)

### Step 3: Install Required VS Code Extensions
Open VS Code, press `Ctrl+Shift+X` (or `Cmd+Shift+X` on Mac) to open the Extensions view, and install:
1. **Extension Pack for Java** (by Microsoft) — *Provides Java language support, debugging, code completion, and test runner.*
2. **Spring Boot Extension Pack** (by VMware) — *Provides Spring Boot dashboard, navigation, and properties autocompletion.*
3. **Maven for Java** (by Microsoft) — *Provides project lifecycle and build execution within VS Code.*
4. *(Optional)* **Community Server Connectors** or **Thunder Client** (for API testing).

### Step 4: Open the Project in VS Code
1. Open VS Code.
2. Click **File ➔ Open Folder...**
3. Select the `ShopNest` folder.
4. VS Code will automatically detect the `pom.xml` and initialize the Java Language Server in the background.

### Step 5: Run the Project
You can run the application in either of two easy ways:

#### Option A: Using the VS Code Terminal (Recommended)
1. Open terminal inside VS Code (`Ctrl+` `~` or **Terminal ➔ New Terminal**).
2. If you have Apache Maven installed:
   ```bash
   mvn spring-boot:run
   ```
3. Or using the Maven Wrapper:
   - On Windows (Command Prompt / PowerShell):
     ```cmd
     mvnw.cmd spring-boot:run
     ```
   - On Mac / Linux:
     ```bash
     chmod +x mvnw && ./mvnw spring-boot:run
     ```

#### Option B: Using the VS Code Run Button
1. In the file explorer, navigate to:
   `src/main/java/com/shopnest/ShopNestApplication.java`
2. Click the **Run** button (or press `F5` / click `Run Java` above the `main` method).

When the console prints:
```text
==========================================================
  SHOPNEST — ONLINE SHOPPING STORE STARTED SUCCESSFULLY! 
==========================================================
  Storefront:     http://localhost:8080
  Admin Login:    http://localhost:8080/admin/login
  Admin User:     admin@shopnest.com / admin123
  H2 DB Console:  http://localhost:8080/h2-console
  JDBC URL:       jdbc:h2:file:./data/shopnestdb
==========================================================
```
Your application is fully live and running!

---

## 🌐 Access URLs & Default Credentials

| Portal / Page | URL | Purpose / Credentials |
| :--- | :--- | :--- |
| **Storefront Home** | [http://localhost:8080](http://localhost:8080) | Browse products, search, categories, and shopping cart |
| **Product Catalog** | [http://localhost:8080/products](http://localhost:8080/products) | Filter products by category, sort by price, add to cart |
| **Shopping Cart** | [http://localhost:8080/cart](http://localhost:8080/cart) | View selected items, adjust quantities, calculate total |
| **Checkout** | [http://localhost:8080/checkout](http://localhost:8080/checkout) | Enter shipping address and choose Cash on Delivery |
| **Admin Login** | [http://localhost:8080/admin/login](http://localhost:8080/admin/login) | **Email:** `admin@shopnest.com`<br>**Password:** `admin123` |
| **Admin Dashboard** | [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard) | Revenue, total orders, product CRUD & order management |
| **H2 Database Web Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | Inspect relational SQL tables directly in browser |

### Connecting to the H2 Database Console:
1. Open [http://localhost:8080/h2-console](http://localhost:8080/h2-console) in your browser.
2. Ensure the following connection parameters are filled:
   - **Driver Class:** `org.h2.Driver`
   - **JDBC URL:** `jdbc:h2:file:./data/shopnestdb`
   - **User Name:** `sa`
   - **Password:** *(leave blank)*
3. Click **Connect**. You will see all 5 relational tables (`PRODUCTS`, `USERS`, `CART_ITEMS`, `ORDERS`, `ORDER_ITEMS`) and can run standard SQL queries like:
   ```sql
   SELECT * FROM PRODUCTS;
   SELECT * FROM ORDERS;
   ```

---

## 🚀 Step-by-Step GitHub Upload Guide

Follow these steps to upload your ShopNest project to your personal GitHub account:

### Step 1: Create a New GitHub Repository
1. Log in to [GitHub.com](https://github.com/).
2. In the top-right corner, click **`+`** ➔ **New repository**.
3. Name your repository: `ShopNest-Ecommerce` (or `ShopNest`).
4. Set visibility to **Public** (or **Private**).
5. **Do NOT** check "Initialize this repository with a README" or ".gitignore" (as they are already created).
6. Click **Create repository**.

### Step 2: Initialize Git in Project Folder & Push
Open your terminal inside the `ShopNest` folder and run:

```bash
# 1. Initialize local git repository
git init

# 2. Add all files to staging (respecting .gitignore)
git add .

# 3. Create initial commit
git commit -m "Initial commit: Complete ShopNest E-Commerce Spring Boot project"

# 4. Rename default branch to main
git branch -M main

# 5. Link local repository to your remote GitHub repo
# Replace USERNAME with your actual GitHub username!
git remote add origin https://github.com/USERNAME/ShopNest-Ecommerce.git

# 6. Push your project to GitHub
git push -u origin main
```

---

## ☁️ Free Cloud Deployment Guide

You can deploy ShopNest online for free so your college professor and external examiner can test it live on the web:

### Option 1: Deploy on Render.com (Recommended - Free Web Service)
1. Push your project to your GitHub account as described above.
2. Sign up / Log in to [Render.com](https://render.com/).
3. Click **New + ➔ Web Service**.
4. Select **Build and deploy from a Git repository** and connect your `ShopNest-Ecommerce` repository.
5. In the settings:
   - **Name:** `shopnest-ecommerce`
   - **Environment:** `Docker` (Render will automatically detect the provided `Dockerfile`!)
   - **Region:** Singapore / Frankfurt / Oregon (closest to you)
   - **Plan:** **Free**
6. Click **Create Web Service**.
7. Render will build the Maven Docker container and give you a free live URL (e.g. `https://shopnest-ecommerce.onrender.com`).

### Option 2: Deploy on Railway.app
1. Go to [Railway.app](https://railway.app/) and sign in with GitHub.
2. Click **New Project ➔ Deploy from GitHub repo**.
3. Select `ShopNest-Ecommerce`.
4. Railway will automatically detect the Dockerfile or Java Maven environment and deploy it.
5. Under service settings, click **Generate Domain** to get a public URL.

### Option 3: Deploy on Koyeb
1. Sign up at [Koyeb.com](https://www.koyeb.com/).
2. Choose **Create App ➔ GitHub**.
3. Pick your repo and select the Docker builder.
4. Click **Deploy**.

---

## 🎓 College Viva & Interview Q&A

Here are common viva questions examiners ask about Spring Boot E-Commerce projects and how to answer them:

#### Q1: Why did you choose Spring Boot for this project?
> **Answer:** Spring Boot provides rapid application development with convention-over-configuration, embedded Tomcat server support (no manual `.war` deployment needed), dependency injection, and automatic configuration for Spring Data JPA and Thymeleaf.

#### Q2: What is the difference between In-Memory H2 and File-Based H2?
> **Answer:** An in-memory H2 database (`jdbc:h2:mem:testdb`) loses all product and order records the moment the application stops or restarts. In ShopNest, we configured **file-based H2** (`jdbc:h2:file:./data/shopnestdb`), which writes data directly to a local disk file (`shopnestdb.mv.db`). This ensures 100% offline persistence across server restarts.

#### Q3: How does Thymeleaf work with Spring Boot?
> **Answer:** Thymeleaf is a modern server-side Java template engine. When a browser requests a page, the Spring MVC Controller populates data into a `Model` object. Thymeleaf processes the HTML template on the server, parses expressions such as `th:text="${product.name}"` and `th:each="p : ${products}"`, and generates standard HTML5 sent to the browser.

#### Q4: How is the Shopping Cart persisted for guest users?
> **Answer:** ShopNest uses the HTTP Session ID (`session.getId()`). When a visitor adds an item to the cart, the record is stored in the `cart_items` table mapped to their unique session token. This allows guest shopping without forcing immediate user registration.

#### Q5: How do you prevent overselling when placing an order?
> **Answer:** In `OrderService.placeOrder()`, we perform a transactional inventory check (`@Transactional`). If the requested quantity exceeds `product.getStockQuantity()`, an exception is thrown. If sufficient stock exists, the stock quantity is immediately decremented and persisted before order completion.

---

## 🔮 Future Improvements

1. **Online Payment Gateway:** Integration with Razorpay, Stripe, or PayPal for credit card / UPI payments.
2. **User Authentication (Spring Security):** Customer registration, profile dashboard, and password hashing using BCrypt.
3. **Email Notification Service:** Integrating JavaMailSender to dispatch PDF invoices upon order placement.
4. **Product Reviews & Comments:** Allowing verified buyers to post star reviews and feedback.
5. **REST API & Mobile App:** Exposing `@RestController` endpoints for React Native / Flutter Android app integration.

---

## 👨‍💻 Project Developer Information

- **Project Title:** ShopNest — Online Shopping Store
- **Domain:** E-Commerce Web Application / Enterprise Java
- **Target Audience:** College Examination & Final Year Project Evaluation
- **License:** Open Source for Academic & Educational Purposes

---
*Developed with ❤️ using pure Java and Spring Boot.*
