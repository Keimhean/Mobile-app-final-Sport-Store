# Sports Store Backend API

Node.js/Express REST API for the Sports Store Android App with MongoDB database.

## 📋 Features

- **Authentication**: JWT-based authentication with bcrypt password hashing
- **Products Management**: CRUD operations for sports products
- **Order Management**: Complete order lifecycle handling
- **User Management**: User registration, login, and profile updates
- **Security**: Helmet, CORS, input validation, role-based access control
- **Database**: MongoDB with Mongoose ODM
- **Docker**: Multi-stage build for production deployment
- **Kubernetes**: Full K8s manifests with StatefulSet for MongoDB

## 🚀 Quick Start

### Local Development

1. **Install Dependencies**
```bash
cd backend
npm install
```

2. **Configure Environment**
```bash
cp .env.example .env
# Edit .env with your settings
```

3. **Start MongoDB** (using Docker)
```bash
docker run -d -p 27017:27017 --name mongodb mongo:7.0
```

4. **Run Development Server**
```bash
npm run dev
```

API will be available at `http://localhost:3000`

### Docker Compose

```bash
cd backend
docker-compose up -d
```

This starts both MongoDB and the backend API.

## 📡 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `GET /api/v1/auth/me` - Get current user (requires auth)
- `PUT /api/v1/auth/updateprofile` - Update profile (requires auth)

### Products
- `GET /api/v1/products` - Get all products (with filters)
- `GET /api/v1/products/:id` - Get single product
- `POST /api/v1/products` - Create product (admin only)
- `PUT /api/v1/products/:id` - Update product (admin only)
- `DELETE /api/v1/products/:id` - Delete product (admin only)

### Orders
- `GET /api/v1/orders` - Get orders (user's orders or all for admin)
- `GET /api/v1/orders/:id` - Get single order
- `POST /api/v1/orders` - Create order (requires auth)
- `PUT /api/v1/orders/:id` - Update order status (admin only)
- `DELETE /api/v1/orders/:id` - Cancel order (requires auth)

## 🔐 Authentication

Include JWT token in Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## 📝 Example Requests

### Register User
```bash
curl -X POST http://localhost:3000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "phone": "+1234567890"
  }'
```

### Login
```bash
curl -X POST http://localhost:3000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Get Products
```bash
curl http://localhost:3000/api/v1/products
```

### Get Products with Filters
```bash
curl "http://localhost:3000/api/v1/products?category=Football&minPrice=50&maxPrice=200&page=1&limit=10"
```

### Create Product (Admin)
```bash
curl -X POST http://localhost:3000/api/v1/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Nike Football",
    "description": "Professional football",
    "price": 89.99,
    "category": "Football",
    "brand": "Nike",
    "stock": 50,
    "sizes": ["5"],
    "colors": ["White", "Black"]
  }'
```

### Create Order
```bash
curl -X POST http://localhost:3000/api/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "items": [
      {
        "product": "PRODUCT_ID",
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
  }'
```

## 🐳 Docker

### Build Image
```bash
cd backend
docker build -t sports-store-backend .
```

### Run Container
```bash
docker run -d \
  -p 3000:3000 \
  -e MONGO_URI=mongodb://host.docker.internal:27017/sports_store \
  -e JWT_SECRET=your-secret \
  --name backend-api \
  sports-store-backend
```

## ☸️ Kubernetes Deployment

### Deploy Backend and MongoDB
```bash
kubectl apply -f k8s/backend-deployment.yaml
```

### Update Secrets (Production)
```bash
# Edit secrets
kubectl edit secret mongo-secret -n sports-store
kubectl edit secret backend-secret -n sports-store
```

### Check Deployment
```bash
kubectl get pods -n sports-store
kubectl get services -n sports-store
kubectl logs -f deployment/backend-api -n sports-store
```

### Access API
```bash
# Get LoadBalancer IP
kubectl get service backend-service -n sports-store

# Test API
curl http://EXTERNAL_IP/health
```

## 📦 Database Models

### User
```javascript
{
  name: String,
  email: String (unique),
  password: String (hashed),
  phone: String,
  address: Object,
  role: "user" | "admin",
  isActive: Boolean,
  lastLogin: Date
}
```

### Product
```javascript
{
  name: String,
  description: String,
  price: Number,
  category: String,
  brand: String,
  imageUrl: String,
  stock: Number,
  sizes: [String],
  colors: [String],
  ratings: { average: Number, count: Number },
  featured: Boolean,
  isActive: Boolean
}
```

### Order
```javascript
{
  user: ObjectId,
  items: [{
    product: ObjectId,
    quantity: Number,
    size: String,
    color: String,
    price: Number
  }],
  totalAmount: Number,
  status: "pending" | "processing" | "shipped" | "delivered" | "cancelled",
  shippingAddress: Object,
  paymentMethod: String,
  paymentStatus: String,
  trackingNumber: String,
  deliveredAt: Date
}
```

## 🔧 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Server port | `3000` |
| `NODE_ENV` | Environment | `development` |
| `MONGO_URI` | MongoDB connection string | `mongodb://localhost:27017/sports_store` |
| `JWT_SECRET` | JWT signing secret | Required |
| `JWT_EXPIRE` | JWT expiration time | `7d` |
| `CORS_ORIGINS` | Allowed CORS origins | `*` |
| `API_VERSION` | API version prefix | `v1` |

## 🧪 Testing

```bash
npm test
```

## 📊 CI/CD

GitHub Actions workflow automatically:
- Runs tests on push/PR
- Builds Docker image
- Pushes to Docker Hub (when secrets configured)

Required GitHub Secrets:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

## 🛡️ Security

- Passwords hashed with bcrypt (10 rounds)
- JWT tokens for authentication
- Helmet for security headers
- CORS configured
- Input validation on all routes
- Role-based access control (user/admin)

## 📄 License

MIT
