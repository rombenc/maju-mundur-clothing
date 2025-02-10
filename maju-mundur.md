# MAJU-MUNDUR E-Commerce API

## Overview
MAJU-MUNDUR is an e-commerce application providing a comprehensive set of APIs for managing products, orders, payments, and user authentication.

## Base URL
`http://localhost:8080/api/v1`

## Authentication
- Authentication Type: Bearer JWT
- All endpoints require JWT token authentication
- Obtain token via `/authentication/login` endpoint

## Endpoints

### Authentication
- `POST /authentication/login`: User login
- `POST /authentication/register/customer`: Customer registration
- `POST /authentication/register/merchant`: Merchant registration
- `POST /authentication/verify`: Verify user email
- `PUT /authentication/update-password`: Update user password
- `PUT /authentication/update-email`: Update user email
- `PUT /authentication/update-name`: Update user name
- `DELETE /authentication/{userId}`: Delete user account

### Products
- `GET /products`: Retrieve all products
- `GET /products/search`: Search products by name
- `GET /products/brands`: Search products by brand
- `POST /products/{merchantId}`: Create a new product
- `PUT /products/{id}`: Update product details
- `DELETE /products/{id}`: Delete a product

### Orders
- `GET /orders`: Retrieve all orders
- `GET /orders/{orderId}`: Get specific order details
- `POST /orders`: Create a new order
- `PUT /orders/{orderId}`: Update order status

### Payments
- `GET /payments`: Retrieve all payments
- `GET /payments/{paymentId}`: Get specific payment details
- `POST /payments`: Make a payment
- `PUT /payments/{paymentId}`: Update payment status

### Carts
- `GET /carts`: Retrieve cart items
- `POST /carts`: Add item to cart
- `PUT /carts/{cartId}`: Update cart item quantity
- `DELETE /carts/{cartId}`: Remove cart item

### Merchants
- `GET /merchants`: Retrieve all merchants
- `POST /merchants`: Create a new merchant
- `PUT /merchants/{merchantId}`: Update merchant details

### Customers
- `GET /api/customers`: Retrieve all customers
- `POST /api/customers`: Create a new customer
- `PUT /api/customers/{customerId}`: Update customer details

## Request/Response Formats
- Most endpoints use JSON for request and response bodies
- Common response structure includes:
  ```json
  {
    "statusCode": 200,
    "message": "Success",
    "data": {}
  }
  ```

## Security
- JWT token required for most endpoints
- Token obtained through login endpoint
- Token includes user roles and permissions

## Error Handling
- Endpoints return appropriate HTTP status codes
- Error responses include status code and error message

## Postman/API Testing
- Import the OpenAPI specification for easy API testing
- Ensure to include JWT token in Authorization header

## Changelog
- Version: 0.1
- Last Updated: [Current Date]

## Contact
- Email: dimasnurfaouzi@gmail.com