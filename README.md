# Multi-Tenant E-Commerce 

A Spring Boot based multi-tenant e-commerce backend implementing authentication, product catalog, dynamic pricing, cart, and order management.

---

## Features Implemented

### 1. Multi-Tenant Architecture
- Tenant resolved via `x-tenant-id` header
- Strict logical isolation
- No cross-tenant access

### 2. Authentication System
- JWT-based authentication (RS256)
- Access Token: 15 minutes
- Refresh Token: 7 days
- Refresh token rotation
- Logout invalidates refresh token

APIs:
- POST /auth/register
- POST /auth/login
- POST /auth/refresh
- POST /auth/logout

### 3. Product Catalog
- SKU unique per tenant
- Pagination and filtering
- Inventory validation (cannot go below zero)

### 4. Dynamic Pricing Engine
- Percentage discount
- Flat discount
- Inventory-based pricing
- Strategy pattern
- Rules evaluated at runtime

### 5. Cart System
- One active cart per user per tenant
- Dynamic pricing applied
- Inventory validation

### 6. Order Creation
- Cart to order flow
- Atomic inventory reservation
- Price snapshot stored per order

---


## Running the Application


- mvn clean spring-boot:run



---


## Running Tests
- mvn test



--- 

## Coverage Report

- mvn test
- mvn clean verify

Coverage Output
- target/site/jacoco/index.html
- Open index.html in a browser
