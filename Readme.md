# second-round-assignm-final-19622-prathmesh

## E-Commerce Backend API

## Overview

RESTful API built with Spring Boot, serving as the core backend for an e-commerce platform. The system manages the complete user checkout lifecycle, including secure authentication, product browsing, dynamic cart calculations, order persistence, and third-party payment processing.

## Technology Stack

- **Framework:** Spring Boot 3.x (Spring Web, Spring Security, Spring Data JPA)
- **Language:** Java 21
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT) & BCrypt
- **External Integrations:** Stripe API (Payment Processing)
- **Testing:** JUnit 5, Mockito

## Core Modules

- **Security & Authorization:** Role-based access control (RBAC).
- **Product Management:** Full CRUD operations for maintaining the product catalog.
- **Cart System:** State management for user shopping sessions, including dynamic total calculations, item removal, and inventory validation.
- **Order Management:** Conversion of active carts into immutable order records.
- **Payment Gateway:** Secure integration with Stripe's REST API.

## Setup and Installation

### Prerequisites

- Java Development Kit (JDK) 21
- MySQL Server (Local or Dockerized)
- Maven 3.x

### 1. Database Configuration

Execute the following SQL command in your local database environment to create the required schema:

```sql
CREATE DATABASE ecommerce;
```

### 2. Environment Variables

Replace the following placeholders with your local credentials within application.properties:

```sql
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
jwt.secret=YOUR_JWT_SECRET_STRING
stripe.api.secretKey=YOUR_STRIPE_TEST_SECRET_KEY
```

### 3. API Reference

- **Authentication**

| Method | Endpoint           | Description                   | Access |
| ------ | ------------------ | ----------------------------- | ------ |
| POST   | /api/auth/register | Register a new user           | Public |
| POST   | /api/auth/login    | Authenticate and retrieve JWT | Public |

- **Products**

| Method | Endpoint      | Description           | Access |
| ------ | ------------- | --------------------- | ------ |
| GET    | /api/products | Retrieve all products | Public |
| POST   | /api/products | Create a new product  | Admin  |

- **Cart**

| Method | Endpoint              | Description                    | Access             |
| ------ | --------------------- | ------------------------------ | ------------------ |
| GET    | /api/cart             | Retrieve current user's cart   | Authenticated User |
| POST   | /api/cart/add         | Add product to cart            | Authenticated User |
| DELETE | /api/cart/remove/{id} | Remove specific item from cart | Authenticated User |

- **Orders & Payments**

| Method | Endpoint             | Description                          | Access             |
| ------ | -------------------- | ------------------------------------ | ------------------ |
| POST   | /api/orders/checkout | Convert active cart to pending order | Authenticated User |
| GET    | /api/orders          | Retrieve user's order history        | Authenticated User |
| POST   | /api/orders/{id}/pay | Process Stripe payment for order     | Authenticated User |
