# Microservices Integration Guide

## 🚀 Services Overview

Your Sports Store now has 3 microservices running:

1. **Backend API** (Port 3000) - Main application server
2. **AI Service** (Port 5001) - Product recommendations using ML
3. **Blockchain Service** (Port 4000) - Order tracking & payment verification

## ✅ Services Status

All services are running and healthy:
- Backend: http://localhost:3000/health
- AI: http://localhost:5001/health  
- Blockchain: http://localhost:4000/health

## 🧠 AI Service - Product Recommendations

### Content-Based Recommendations
Get similar products based on product features:
```bash
curl http://localhost:5001/api/v1/ai/recommendations/content/PRODUCT_ID?limit=5
```

### User-Based Recommendations
Get personalized recommendations for a user:
```bash
curl http://localhost:5001/api/v1/ai/recommendations/user/USER_ID?limit=5
```

### Track User Interactions
Record user behavior for better recommendations:
```bash
curl -X POST http://localhost:5001/api/v1/ai/train \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "productId": "prod456",
    "type": "view"
  }'
```

### Update Product Embeddings
Sync product catalog with AI service:
```bash
curl -X POST http://localhost:5001/api/v1/ai/embeddings \
  -H "Content-Type: application/json" \
  -d '{
    "products": [
      {
        "_id": "prod1",
        "category": "Running",
        "brand": "Nike",
        "price": 129.99
      }
    ]
  }'
```

## 🔗 Blockchain Service - Order Tracking

### Create Order on Blockchain
Register new order immutably:
```bash
curl -X POST http://localhost:4000/api/v1/blockchain/order/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD123",
    "userId": "user123",
    "products": [{"id": "prod1", "qty": 2}],
    "totalAmount": 259.98
  }'
```

### Update Order Status
Track order lifecycle:
```bash
curl -X POST http://localhost:4000/api/v1/blockchain/order/update \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD123",
    "status": "shipped",
    "updatedBy": "admin"
  }'
```

### Verify Payment
Create blockchain-verified payment receipt:
```bash
curl -X POST http://localhost:4000/api/v1/blockchain/payment/verify \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD123",
    "paymentId": "PAY789",
    "amount": 259.98,
    "method": "card"
  }'
```

### Get Order History
View complete audit trail:
```bash
curl http://localhost:4000/api/v1/blockchain/order/ORD123/history
```

### View Blockchain
Inspect the entire chain:
```bash
curl http://localhost:4000/api/v1/blockchain/chain
```

### Validate Chain Integrity
Check for tampering:
```bash
curl http://localhost:4000/api/v1/blockchain/validate
```

## 📊 Service Statistics

### AI Stats
```bash
curl http://localhost:5001/api/v1/ai/stats
```

### Blockchain Stats
```bash
curl http://localhost:4000/api/v1/blockchain/stats
```

## 🔧 Docker Commands

### View Logs
```bash
# All services
docker-compose -f backend/docker-compose.yml logs -f

# Specific service
docker-compose -f backend/docker-compose.yml logs -f ai-service
docker-compose -f backend/docker-compose.yml logs -f blockchain-service
```

### Restart Services
```bash
cd backend
docker-compose restart ai-service
docker-compose restart blockchain-service
```

### Stop All Services
```bash
cd backend
docker-compose down
```

### Rebuild Services
```bash
cd backend
docker-compose up --build -d
```

## 📱 Android Integration

Next steps to integrate with your Android app:

1. **Add API calls in AuthRepository**
2. **Create AIService.kt for recommendations**
3. **Create BlockchainService.kt for order tracking**
4. **Update ProductDetailActivity to show "Similar Products"**
5. **Update OrderActivity to show blockchain verification**

Would you like me to implement the Android integration next?
