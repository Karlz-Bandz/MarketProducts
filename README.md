# Products Microservice

## Description
The Products microservice is responsible for managing product data in the market system.  
It provides APIs for creating, reading, updating, and deleting products in the database.

---

## Features
- Create new products
- Retrieve product information
- Update existing products
- Delete products
- RESTful API endpoints

---

## Technology Stack
- Language: Java 21
- Framework: Spring Boot 4.0.2
- Database: PostgreSQL
- Containerization: Docker

---

## API Endpoints
| Method | Endpoint           | Description                  |
|--------|------------------|------------------------------|
| GET    | /products        | Get list of all products     |
| GET    | /products/:id    | Get product by ID            |
| POST   | /products        | Create a new product         |
| PUT    | /products/:id    | Update product by ID         |
| DELETE | /products/:id    | Delete product by ID         |

