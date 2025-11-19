# Sports Store - Full Stack Mobile Application

## Project Overview
A complete e-commerce mobile application for sports equipment with Android frontend and Node.js backend, featuring user authentication, product catalog, shopping cart, and order management.

---

## 🏗️ Architecture

### **Tech Stack**

**Frontend (Android)**
- **Language:** Kotlin 1.9.21
- **Build System:** Gradle 8.5, Android Gradle Plugin 8.2.0
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **JDK:** 21 LTS

**Backend (Node.js)**
- **Runtime:** Node.js 18 (Alpine)
- **Framework:** Express 4.18
- **Database:** MongoDB 7.0
- **Authentication:** JWT (JSON Web Tokens)
- **Password Hashing:** bcrypt

**Infrastructure**
- **Containerization:** Docker & Docker Compose
- **Orchestration:** Kubernetes (K8s manifests provided)
- **CI/CD:** GitHub Actions workflows

---

## 📱 Android Application

### **Key Features**
1. **User Authentication**
   - Login with email/password
   - User registration with validation
   - JWT token storage (SharedPreferences)
   - Network security config for local development

2. **UI Components**
   - Splash screen with app branding
   - Onboarding flow for new users
   - Login/Register screens
   - Home activity with product listings
   - Category browsing
   - Product detail view
   - Shopping cart
   - Checkout flow
   - User profile management

3. **Networking**
   - Retrofit 2.11.0 for REST API calls
   - Gson converter for JSON parsing
   - OkHttp logging interceptor for debugging
   - Coroutines for async operations

### **Project Structure**
```
app/src/main/java/com/example/myapp/
├── data/
│   ├── model/
│   │   └── AuthModels.kt          # Data models for auth responses
│   ├── network/
│   │   ├── ApiClient.kt           # Retrofit singleton configuration
│   │   └── AuthService.kt         # Auth API endpoints
│   └── repository/
│       └── AuthRepository.kt      # Data layer abstraction
├── util/
│   └── TokenStore.kt              # JWT token persistence
├── LoginActivity.kt               # Login screen
├── RegisterActivity.kt            # Registration screen
├── SplashActivity.kt              # App entry point
├── OnboardingActivity.kt          # First-time user flow
├── HomeActivity.kt                # Main product listing
├── CategoryActivity.kt            # Category browsing
├── ProductDetailActivity.kt       # Product details
├── CartActivity.kt                # Shopping cart
├── CheckoutActivity.kt            # Order checkout
├── ProfileActivity.kt             # User profile
└── MainActivity.kt                # Main navigation hub
```

### **Dependencies**
```kotlin
// UI & Material Design
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.10.0
androidx.constraintlayout:constraintlayout:2.1.4
androidx.recyclerview:recyclerview:1.3.0
androidx.viewpager2:viewpager2:1.0.0

// Lifecycle & Coroutines
androidx.lifecycle:lifecycle-runtime-ktx:2.6.1
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1

// Networking
com.squareup.retrofit2:retrofit:2.11.0
com.squareup.retrofit2:converter-gson:2.11.0
com.squareup.okhttp3:logging-interceptor:4.12.0
```

### **Network Configuration**
- **Base URL (Emulator):** `http://10.0.2.2:3000/api/v1/`
- **Base URL (Physical Device):** Update to your local IP
- **Cleartext Traffic:** Enabled for `10.0.2.2` and `localhost`

---

## 🖥️ Backend API

### **Features**
1. **Authentication**
   - User registration with email/password
   - Login with JWT token generation
   - Password hashing with bcrypt (10 rounds)
   - Protected routes with JWT middleware
   - Role-based access control (admin/user)

2. **Product Management**
   - CRUD operations for products
   - Product filtering by category, brand, price
   - Featured products
   - Stock management
   - Admin-only product creation/updates

3. **Order Management**
   - Create orders with line items
   - Order status tracking (pending → processing → shipped → delivered)
   - Payment method tracking
   - Payment status (pending, paid, failed, refunded)
   - Shipping address management
   - Tracking number support

4. **Security**
   - Helmet.js for security headers
   - CORS configuration
   - Rate limiting considerations
   - Input validation
   - MongoDB injection prevention

### **API Endpoints**

#### **Authentication** (`/api/v1/auth`)
```
POST   /register    - Create new user account
POST   /login       - Authenticate and get JWT token
GET    /me          - Get current user profile (requires auth)
```

#### **Products** (`/api/v1/products`)
```
GET    /            - List all products (public)
GET    /:id         - Get single product (public)
POST   /            - Create product (admin only)
PUT    /:id         - Update product (admin only)
DELETE /:id         - Delete product (admin only)
```

#### **Orders** (`/api/v1/orders`)
```
GET    /            - Get user's orders (or all if admin)
GET    /:id         - Get single order
POST   /            - Create new order
PUT    /:id         - Update order status (admin only)
DELETE /:id         - Cancel order
```

### **Data Models**

**User Schema**
```javascript
{
  name: String (required),
  email: String (required, unique),
  password: String (required, hashed),
  phone: String,
  role: String (enum: ['user', 'admin'], default: 'user'),
  isActive: Boolean (default: true),
  lastLogin: Date,
  timestamps: true
}
```

**Product Schema**
```javascript
{
  name: String (required),
  description: String (required),
  price: Number (required),
  category: String (required),
  brand: String,
  stock: Number (default: 0),
  imageUrl: String,
  colors: [String],
  sizes: [String],
  featured: Boolean (default: false),
  rating: Number (0-5),
  reviews: Number (default: 0),
  timestamps: true
}
```

**Order Schema**
```javascript
{
  user: ObjectId (ref: User),
  items: [{
    product: ObjectId (ref: Product),
    quantity: Number (min: 1),
    price: Number,
    size: String,
    color: String
  }],
  totalAmount: Number (auto-calculated),
  status: String (enum: pending/processing/shipped/delivered/cancelled),
  shippingAddress: {
    street, city, state, zipCode, country
  },
  paymentMethod: String (credit_card/debit_card/paypal/cash_on_delivery),
  paymentStatus: String (pending/paid/failed/refunded),
  trackingNumber: String,
  deliveredAt: Date,
  timestamps: true
}
```

### **Environment Variables**
```bash
NODE_ENV=production
PORT=3000
MONGO_URI=mongodb://admin:password123@mongodb:27017/sports_store?authSource=admin
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRE=7d
CORS_ORIGINS=http://localhost:3000,http://localhost:8080
```

---

## 🐳 Docker & Deployment

### **Docker Compose Services**

**MongoDB**
- Image: `mongo:7.0`
- Port: `27017`
- Persistent volume: `mongodb_data`
- Root credentials: admin/password123

**Backend API**
- Multi-stage build (Node 18 Alpine)
- Port: `3000`
- Health check configured
- Auto-restart enabled

### **Kubernetes Deployment**
Located in `k8s/` directory:
- `deployment.yaml` - Android app (if containerized)
- `backend-deployment.yaml` - Backend API + MongoDB StatefulSet

---

## 🚀 Getting Started

### **Prerequisites**
- Android Studio (latest stable)
- JDK 21
- Docker Desktop
- Node.js 18+ (for local development)
- MongoDB (via Docker or local)

### **Backend Setup**

1. **Using Docker Compose (Recommended)**
```bash
cd backend
docker compose up -d

# Verify health
curl http://localhost:3000/health
```

2. **Local Development**
```bash
cd backend
npm install
npm start
```

### **Android App Setup**

1. **Open in Android Studio**
```bash
# Open the project root directory
open -a "Android Studio" /path/to/MyKotlinAndroidApp
```

2. **Sync Gradle**
- Wait for Gradle sync to complete
- Resolve any SDK/dependency issues

3. **Configure Emulator**
```bash
# List available AVDs
emulator -list-avds

# Start emulator
emulator -avd Pixel_9_Pro_XL -no-boot-anim
```

4. **Build and Install**
```bash
./gradlew clean installDebug
```

5. **Launch App**
```bash
adb shell am start -n com.example.myapp/.SplashActivity
```

---

## 🧪 Testing

### **Backend API Tests**
```bash
cd backend
chmod +x test-api.sh
./test-api.sh

# Test Results:
# - 15 comprehensive tests
# - Auth, Products, Orders coverage
# - 80%+ pass rate
```

### **Manual Testing Flow**

1. **User Registration**
   - Open app → Login screen
   - Tap "Sign Up"
   - Fill: Name, Email, Phone, Password
   - Tap Register
   - Verify success message

2. **User Login**
   - Enter registered email/password
   - Tap Sign In
   - Verify navigation to HomeActivity

3. **Browse Products**
   - Scroll through product listings
   - Filter by category
   - View product details

4. **Test Credentials**
   - Email: `newtest@example.com`
   - Password: `pass123`

---

## 📊 API Response Formats

### **Success Response**
```json
{
  "success": true,
  "data": {
    "id": "691dad603d57b25762f61fae",
    "name": "New Test User",
    "email": "newtest@example.com",
    "role": "user",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### **Error Response**
```json
{
  "success": false,
  "error": "Invalid credentials"
}
```

---

## 🔒 Security Considerations

### **Current Implementation**
✅ JWT-based authentication  
✅ Password hashing with bcrypt  
✅ HTTPS-ready (cleartext only for local dev)  
✅ Role-based access control  
✅ Input validation on backend  
✅ Secure headers (Helmet.js)  

### **Production Recommendations**
⚠️ Change JWT_SECRET to strong random value  
⚠️ Enable HTTPS/TLS for all communication  
⚠️ Implement rate limiting  
⚠️ Add refresh token mechanism  
⚠️ Enable Firebase authentication (currently disabled)  
⚠️ Implement proper session management  
⚠️ Add password strength requirements  
⚠️ Enable 2FA for admin accounts  

---

## 📝 Known Issues & Limitations

1. **Payment Processing**
   - No real payment gateway integration (Stripe/PayPal)
   - Payment status tracked manually
   - Recommend: Integrate Stripe SDK for production

2. **Image Handling**
   - Product images stored as URLs (not uploaded)
   - No CDN integration
   - Recommend: Add image upload + AWS S3/Cloudinary

3. **Push Notifications**
   - Not implemented
   - Recommend: Firebase Cloud Messaging

4. **Offline Support**
   - No local caching
   - Recommend: Room database for offline mode

5. **Firebase**
   - Google Services plugin disabled due to package mismatch
   - Need to regenerate `google-services.json` for `com.example.myapp`

---

## 🛠️ Troubleshooting

### **"Network Security Policy" Error**
✅ Fixed with `network_security_config.xml` allowing cleartext to `10.0.2.2`

### **"Login Failed" Error**
✅ Fixed by correcting AuthResponse model to match backend structure

### **Backend Not Accessible from Emulator**
- Use `http://10.0.2.2:3000` (not `localhost`)
- For physical device, use your local IP (e.g., `http://192.168.1.x:3000`)

### **Build Failures**
```bash
# Clean build
./gradlew clean build --refresh-dependencies

# Check Gradle daemon
./gradlew --stop
```

---

## 📈 Future Enhancements

### **Phase 1: Core Features**
- [ ] Product search functionality
- [ ] Wishlist/favorites
- [ ] Order history with details
- [ ] User reviews and ratings
- [ ] Password reset flow

### **Phase 2: Advanced Features**
- [ ] Real-time order tracking
- [ ] Push notifications
- [ ] Social login (Google, Facebook)
- [ ] Payment gateway integration
- [ ] Image upload for products
- [ ] Admin dashboard (web)

### **Phase 3: Optimization**
- [ ] Pagination for products/orders
- [ ] Image caching and optimization
- [ ] Offline mode with Room
- [ ] Analytics integration
- [ ] Performance monitoring

---

## 🤝 Contributing

### **Git Workflow**
```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "Add: your feature description"

# Push to GitHub
git push origin feature/your-feature-name

# Create Pull Request on GitHub
```

### **Code Style**
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep functions small and focused

---

## 📄 License
This project is for educational purposes.

---

## 👥 Team
- **Developer:** Keimhean
- **Repository:** https://github.com/Keimhean/Mobile-app-final-Sport-Store

---

## 📞 Support
For issues or questions:
1. Check this documentation
2. Review API_DOCS.md in backend/
3. Check GitHub Issues
4. Review backend logs: `docker logs sports-store-backend`

---

**Last Updated:** November 19, 2025  
**Version:** 1.0.0
