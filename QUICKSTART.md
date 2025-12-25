# 🚀 Quick Start Guide - Sports Store Full Stack

## What You Got

A complete **Sports Store** application with:
- 📱 **Android App** (Kotlin) - Mobile interface with categories, profile, navigation
- 🔵 **Backend API** (Node.js/Express) - REST API with authentication
- 🍃 **MongoDB** - Database for products, users, orders
- 🐳 **Docker** - Containerization for both Android and Backend
- ☸️ **Kubernetes** - Production deployment manifests
- ✅ **CI/CD** - GitHub Actions workflows

---

## 📋 Step-by-Step Setup

### **Option 1: Quick Local Testing (Fastest)**

#### 1. Start Backend + MongoDB
```bash
cd backend
npm install
docker-compose up -d
```
✅ Backend API: `http://localhost:3000`  
✅ MongoDB: `localhost:27017`

#### 2. Test API
```bash
# Health check
curl http://localhost:3000/health

# Register user
curl -X POST http://localhost:3000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'

# Get products
curl http://localhost:3000/api/v1/products
```

#### 3. Run Android App
```bash
cd ..
./gradlew installDebug
# or open in Android Studio and press Run
```

---

### **Option 2: Docker Everything**

#### 1. Build and Run Backend
```bash
cd backend
docker-compose up -d
```

#### 2. Build Android Docker Image
```bash
cd ..
docker build -t sports-store-android .
docker run -p 8080:80 sports-store-android
# Download APK at http://localhost:8080
```

---

### **Option 3: Kubernetes Production Deployment**

#### Prerequisites
- Kubernetes cluster (minikube, GKE, EKS, AKS)
- kubectl configured

#### 1. Update Docker Hub Username
Edit these files and replace `YOUR_DOCKERHUB_USERNAME`:
- `k8s/backend-deployment.yaml` (line 85)
- `k8s/deployment.yaml` (line 20)

#### 2. Build and Push Images
```bash
# Backend
cd backend
docker build -t YOUR_USERNAME/sports-store-backend:latest .
docker push YOUR_USERNAME/sports-store-backend:latest

# Android
cd ..
docker build -t YOUR_USERNAME/mobile-app-final-sport-store:latest .
docker push YOUR_USERNAME/mobile-app-final-sport-store:latest
```

#### 3. Deploy to Kubernetes
```bash
# Create namespace
kubectl create namespace sports-store

# Deploy backend + MongoDB
kubectl apply -f k8s/backend-deployment.yaml

# Deploy Android app
kubectl apply -f k8s/deployment.yaml

# Check everything
kubectl get all -n sports-store
kubectl get all -n production
```

#### 4. Access Services
```bash
# Get backend API URL
kubectl get svc backend-service -n sports-store
# API: http://EXTERNAL_IP

# Get Android APK download URL
kubectl get svc sports-store-service -n production
# APK: http://EXTERNAL_IP
```

---

## 🔐 Setup GitHub CI/CD

### 1. Firebase Secrets (Already Configured ✅)
- `FIREBASE_APP_ID`: ✅ Already set
- `FIREBASE_SERVICE_CREDENTIALS`: ✅ Already set

### 2. Docker Hub Secrets (Optional)
Go to: [GitHub Secrets Settings](https://github.com/Keimhean/Mobile-app-final-Sport-Store/settings/secrets/actions)

Add:
- `DOCKERHUB_USERNAME`: Your Docker Hub username
- `DOCKERHUB_TOKEN`: Docker Hub access token

**How to create Docker Hub token:**
1. Visit https://hub.docker.com/settings/security
2. Click "New Access Token"
3. Name: `GitHub Actions`
4. Copy the token
5. Add to GitHub Secrets

### 3. Push Code
```bash
git add .
git commit -m "Your changes"
git push origin main
```

GitHub Actions will automatically:
- ✅ Build Android APK
- ✅ Run tests
- ✅ Distribute to Firebase
- 🐳 Build and push Docker images (if secrets configured)

---

## 📡 Backend API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register
- `POST /api/v1/auth/login` - Login
- `GET /api/v1/auth/me` - Get profile (requires token)
- `PUT /api/v1/auth/updateprofile` - Update profile (requires token)

### Products
- `GET /api/v1/products` - Get all products (with filters)
- `GET /api/v1/products/:id` - Get single product
- `POST /api/v1/products` - Create product (admin only)
- `PUT /api/v1/products/:id` - Update product (admin only)
- `DELETE /api/v1/products/:id` - Delete product (admin only)

### Orders
- `GET /api/v1/orders` - Get orders (user's own or all for admin)
- `GET /api/v1/orders/:id` - Get single order
- `POST /api/v1/orders` - Create order (requires token)
- `PUT /api/v1/orders/:id` - Update order status (admin only)
- `DELETE /api/v1/orders/:id` - Cancel order (requires token)

**Full API Documentation:** See `backend/API_DOCS.md`

---

## 🧪 Test the Full Flow

### 1. Register User
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

Response includes `token` - save it!

### 2. Create Product (You'll need admin role)
```bash
curl -X POST http://localhost:3000/api/v1/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "Nike Football",
    "description": "Professional quality football",
    "price": 89.99,
    "category": "Football",
    "brand": "Nike",
    "stock": 50,
    "sizes": ["5"],
    "colors": ["White", "Black"]
  }'
```

### 3. Get Products
```bash
curl http://localhost:3000/api/v1/products
```

### 4. Create Order
```bash
curl -X POST http://localhost:3000/api/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "items": [{
      "product": "PRODUCT_ID",
      "quantity": 2,
      "size": "5",
      "color": "White",
      "price": 89.99
    }],
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

---

## 📦 Project Files Overview

```
Mobile-app-final-Sport-Store/
├── app/                    # Android app (Kotlin)
├── backend/                # Node.js API
│   ├── src/
│   │   ├── models/         # MongoDB schemas
│   │   ├── controllers/    # Business logic
│   │   ├── routes/         # API routes
│   │   └── middleware/     # Auth, error handling
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── README.md
├── .github/workflows/      # CI/CD pipelines
│   ├── android-ci.yml
│   ├── android-cd.yml
│   └── backend-ci.yml
├── k8s/                    # Kubernetes manifests
│   ├── backend-deployment.yaml  # Backend + MongoDB
│   └── deployment.yaml          # Android app
└── Dockerfile              # Android app Docker
```

---

## 🎯 What's Next?

### Connect Android App to Backend
1. **Add Retrofit to Android app:**
   ```kotlin
   // In app/build.gradle.kts
   implementation("com.squareup.retrofit2:retrofit:2.9.0")
   implementation("com.squareup.retrofit2:converter-gson:2.9.0")
   ```

2. **Create API service:**
   ```kotlin
   interface SportsStoreApi {
       @GET("api/v1/products")
       suspend fun getProducts(): Response<ProductsResponse>
       
       @POST("api/v1/auth/login")
       suspend fun login(@Body credentials: LoginRequest): Response<AuthResponse>
   }
   ```

3. **Update base URL:**
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:3000/" // For emulator
   // or "http://localhost:3000/" for device with adb reverse
   ```

### Add More Features
- 🔍 Search products
- ⭐ Product ratings
- 📸 Image upload
- 💳 Payment integration
- 📧 Email notifications
- 📊 Admin dashboard

---

## 🆘 Troubleshooting

### Backend won't start
```bash
# Check MongoDB is running
docker ps | grep mongo

# Check logs
cd backend && docker-compose logs -f
```

### Android app can't connect to backend
```bash
# For emulator, use 10.0.2.2 instead of localhost
# or set up adb reverse:
adb reverse tcp:3000 tcp:3000
```

### Kubernetes pods not starting
```bash
# Check pod status
kubectl describe pod POD_NAME -n sports-store

# Check logs
kubectl logs POD_NAME -n sports-store

# Update secrets
kubectl edit secret backend-secret -n sports-store
```

### Docker build fails
```bash
# Clean Docker cache
docker system prune -a

# Rebuild
docker-compose build --no-cache
```

---

## 📚 Documentation Links

- **[Backend README](backend/README.md)** - Complete backend guide
- **[API Documentation](backend/API_DOCS.md)** - All endpoints with examples
- **[CI/CD Setup](CI_CD_SETUP.md)** - GitHub Actions configuration
- **[Secrets Setup](SECRETS_SETUP.md)** - Firebase and Docker secrets
- **[Main README](README.md)** - Project overview

---

## ✅ Quick Checklist

- [ ] Backend running locally (`docker-compose up -d`)
- [ ] API health check works (`curl http://localhost:3000/health`)
- [ ] Android app builds (`./gradlew assembleDebug`)
- [ ] Firebase configured (✅ already done)
- [ ] Docker Hub secrets added (optional)
- [ ] Code pushed to GitHub
- [ ] CI/CD workflows running

---

**Need help?** Check the documentation files or open an issue on GitHub!

🎉 **You're all set! Happy coding!**
