# API Documentation - Sports Store Backend

Base URL: `http://localhost:3000/api/v1`

## Response Format

All API responses follow this structure:

```json
{
  "success": true,
  "data": { ... }
}
```

Error responses:
```json
{
  "success": false,
  "error": "Error message"
}
```

## Authentication APIs

### Register User
**POST** `/auth/register`

Request:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+1234567890"
}
```

Response:
```json
{
  "success": true,
  "data": {
    "id": "user_id",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "user",
    "token": "jwt_token_here"
  }
}
```

### Login
**POST** `/auth/login`

Request:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

Response: Same as register

### Get Current User
**GET** `/auth/me`

Headers: `Authorization: Bearer <token>`

Response:
```json
{
  "success": true,
  "data": {
    "id": "user_id",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "address": { ... },
    "role": "user"
  }
}
```

### Update Profile
**PUT** `/auth/updateprofile`

Headers: `Authorization: Bearer <token>`

Request:
```json
{
  "name": "John Updated",
  "phone": "+9876543210",
  "address": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  }
}
```

## Product APIs

### Get All Products
**GET** `/products`

Query Parameters:
- `category` - Filter by category (Football, Basketball, Tennis, etc.)
- `search` - Text search in name and description
- `minPrice` - Minimum price filter
- `maxPrice` - Maximum price filter
- `featured` - Filter featured products (true/false)
- `sort` - Sort field (e.g., price, -price, createdAt)
- `page` - Page number (default: 1)
- `limit` - Items per page (default: 10)

Example: `/products?category=Football&minPrice=50&maxPrice=200&page=1&limit=10`

Response:
```json
{
  "success": true,
  "count": 10,
  "total": 45,
  "page": 1,
  "pages": 5,
  "data": [
    {
      "_id": "product_id",
      "name": "Nike Football",
      "description": "Professional football",
      "price": 89.99,
      "category": "Football",
      "brand": "Nike",
      "imageUrl": "https://...",
      "stock": 50,
      "sizes": ["5"],
      "colors": ["White", "Black"],
      "ratings": {
        "average": 4.5,
        "count": 120
      },
      "featured": true,
      "isActive": true,
      "createdAt": "2024-01-01T00:00:00.000Z"
    }
  ]
}
```

### Get Single Product
**GET** `/products/:id`

Response:
```json
{
  "success": true,
  "data": { ... }
}
```

### Create Product (Admin Only)
**POST** `/products`

Headers: `Authorization: Bearer <admin_token>`

Request:
```json
{
  "name": "Nike Football",
  "description": "Professional quality football",
  "price": 89.99,
  "category": "Football",
  "brand": "Nike",
  "imageUrl": "https://example.com/image.jpg",
  "stock": 50,
  "sizes": ["5"],
  "colors": ["White", "Black"],
  "featured": true
}
```

### Update Product (Admin Only)
**PUT** `/products/:id`

Headers: `Authorization: Bearer <admin_token>`

Request: Same fields as create (all optional)

### Delete Product (Admin Only)
**DELETE** `/products/:id`

Headers: `Authorization: Bearer <admin_token>`

## Order APIs

### Get Orders
**GET** `/orders`

Headers: `Authorization: Bearer <token>`

- Regular users see only their orders
- Admins see all orders

Response:
```json
{
  "success": true,
  "count": 5,
  "data": [
    {
      "_id": "order_id",
      "user": {
        "_id": "user_id",
        "name": "John Doe",
        "email": "john@example.com"
      },
      "items": [
        {
          "product": {
            "_id": "product_id",
            "name": "Nike Football",
            "price": 89.99,
            "imageUrl": "https://..."
          },
          "quantity": 2,
          "size": "5",
          "color": "White",
          "price": 89.99
        }
      ],
      "totalAmount": 179.98,
      "status": "processing",
      "paymentStatus": "paid",
      "shippingAddress": { ... },
      "paymentMethod": "credit_card",
      "trackingNumber": "TRACK123",
      "createdAt": "2024-01-01T00:00:00.000Z"
    }
  ]
}
```

### Get Single Order
**GET** `/orders/:id`

Headers: `Authorization: Bearer <token>`

Response: Same as above (single order object)

### Create Order
**POST** `/orders`

Headers: `Authorization: Bearer <token>`

Request:
```json
{
  "items": [
    {
      "product": "product_id",
      "quantity": 2,
      "size": "M",
      "color": "Blue",
      "price": 89.99
    }
  ],
  "shippingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "paymentMethod": "credit_card"
}
```

Response:
```json
{
  "success": true,
  "data": { ... }
}
```

### Update Order Status (Admin Only)
**PUT** `/orders/:id`

Headers: `Authorization: Bearer <admin_token>`

Request:
```json
{
  "status": "shipped",
  "paymentStatus": "paid",
  "trackingNumber": "TRACK123"
}
```

Status values: `pending`, `processing`, `shipped`, `delivered`, `cancelled`
Payment status: `pending`, `paid`, `failed`, `refunded`

### Cancel Order
**DELETE** `/orders/:id`

Headers: `Authorization: Bearer <token>`

- Users can cancel their own orders
- Admins can cancel any order
- Only orders with status `pending` or `processing` can be cancelled

## Error Codes

- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid/missing token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `500` - Server Error

## Rate Limiting

Currently no rate limiting implemented. Consider adding in production.

## Pagination

All list endpoints support pagination:
- Default page size: 10
- Max page size: 100
- Use `page` and `limit` query parameters

## Categories

Valid product categories:
- Football
- Basketball
- Tennis
- Running
- Gym
- Other

## Sizes

Valid sizes:
- Clothing: XS, S, M, L, XL, XXL
- Shoes: 36, 37, 38, 39, 40, 41, 42, 43, 44, 45
