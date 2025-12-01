#  Sports Store - Android Mobile App

[![Android CI](https://github.com/Keimhean/Mobile-app-final-Sport-Store/workflows/Android%20CI/badge.svg)](https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![MongoDB Docker](https://img.shields.io/badge/MongoDB_Docker-0db7ed?logo=docker&logoColor=white)](https://hub.docker.com/_/mongo)

A modern Android sports equipment store app built with Kotlin, featuring a beautiful Material Design 3 UI, onboarding flow, category browsing, and full CI/CD pipeline. Complete with Node.js backend API, MongoDB database, and Kubernetes deployment.

##  Architecture

```
 Android App (Kotlin)
        ↓ HTTPS
 Backend API (Node.js/Express) ← Docker Container
        ↓ MongoDB Connection
 Kubernetes Cluster (Production)
        ↓
 MongoDB (Products, Users, Orders)
```

##  Features

### Mobile App
-  **Material Design 3** - Modern, beautiful UI with custom themes
-  **Bottom Navigation** - 5-tab navigation (Home, Categories, Cart, Profile, Search)
-  **Onboarding Flow** - 3-page ViewPager2 onboarding for new users
-  **9 Sport Categories** - Running, Gym, Swimming, Cycling, Tennis, Soccer, Basketball, Yoga, Golf
-  **User Profile** - Profile management with stats (completed, active, wishlist)
-  **Authentication Ready** - Login, Sign Up, Guest access

### Backend API
-  **JWT Authentication** - Secure user authentication with bcrypt
-  **Product Management** - CRUD operations with search and filters
-  **Order Management** - Complete order lifecycle (pending → delivered)
-  **User Management** - Registration, login, profile updates
-  **Role-Based Access** - User/Admin authorization
-  **MongoDB Integration** - Mongoose ODM with validation

### CI/CD Pipeline
-  **Automated Build** - Gradle builds on every push
-  **Unit Tests** - Automated testing on CI
-  **Lint Checks** - Code quality validation
-  **Firebase Distribution** - Auto-deploy to testers
-  **Docker Support** - Containerized builds for Android & Backend
-  **Kubernetes Ready** - Full K8s manifests with MongoDB StatefulSet
-  **Play Store Upload** - Automated releases

##  Tech Stack

### Android
- **Language:** Kotlin 1.9.21
- **Build System:** Gradle 8.5
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Java Version:** 21 (LTS)

### UI Components
- Material Design 3
- ConstraintLayout
- RecyclerView
- ViewPager2
- CardView
- BottomNavigationView

### Backend
- **Runtime:** Node.js 18
- **Framework:** Express.js
- **Database:** MongoDB 7.0 with Mongoose
- **Authentication:** JWT + bcryptjs
- **Security:** Helmet, CORS, input validation

### DevOps
- **CI/CD:** GitHub Actions
- **Containerization:** Docker (multi-stage builds)
- **Orchestration:** Kubernetes (StatefulSets, Deployments, HPA)
- **Distribution:** Firebase App Distribution
- **Store:** Google Play Console

##  Quick Start

### Prerequisites
- Android Studio Hedgehog or later
- JDK 21
- Android SDK 34
- Gradle 8.5+

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Keimhean/Mobile-app-final-Sport-Store.git
   cd Mobile-app-final-Sport-Store
   ```

2. **Setup Backend (Optional but recommended):**
   ```bash
   cd backend
   ./setup.sh
   # Or manually:
   npm install
   cp .env.example .env
   # Edit .env with your MongoDB URI
   ```

3. **Start MongoDB:**
   ```bash
   docker run -d -p 27017:27017 --name mongodb mongo:7.0
   # Or use Docker Compose:
   cd backend && docker-compose up -d
   ```

4. **Run Backend API:**
   ```bash
   cd backend
   npm run dev
   # API available at http://localhost:3000
   ```

5. **Open Android App in Android Studio:**
   - File → Open → Select project folder
   - Wait for Gradle sync

6. **Run the Android app:**
   ```bash
   ./gradlew installDebug
   # or press Run in Android Studio
   ```

### Build Commands

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run lint
./gradlew lintDebug

# Install on device
./gradlew installDebug
```

##  Docker

### Android App Docker Image

```bash
docker build -t sports-store-android:latest .
docker run -p 8080:80 sports-store-android:latest
# Visit http://localhost:8080 to download APK
```

### Backend API Docker Image

```bash
cd backend
docker build -t sports-store-backend:latest .
docker run -d -p 3000:3000 \
  -e MONGO_URI=mongodb://host.docker.internal:27017/sports_store \
  -e JWT_SECRET=your-secret \
  sports-store-backend:latest
```

### Full Stack with Docker Compose

```bash
cd backend
docker-compose up -d
# Backend: http://localhost:3000
# MongoDB: localhost:27017
```

### Push to Docker Hub

```bash
docker login
docker tag sports-store-backend:latest YOUR_USERNAME/sports-store-backend:latest
docker push YOUR_USERNAME/sports-store-backend:latest
```

##  Kubernetes

### Deploy Full Stack to Cluster

```bash
# Create namespace
kubectl create namespace sports-store

# Deploy MongoDB + Backend API
kubectl apply -f k8s/backend-deployment.yaml

# Deploy Android App
kubectl apply -f k8s/deployment.yaml

# Check status
kubectl get all -n sports-store

# View logs
kubectl logs -f deployment/backend-api -n sports-store
kubectl logs -f deployment/sports-store-app -n production

# Get service endpoints
kubectl get svc -n sports-store
```

### Update Secrets (Production)

```bash
# MongoDB credentials
kubectl edit secret mongo-secret -n sports-store

# Backend JWT secret and MongoDB URI
kubectl edit secret backend-secret -n sports-store

# Update Docker Hub username in deployment
kubectl edit deployment backend-api -n sports-store
```

### Access Services

```bash
# Get Backend API URL
kubectl get service backend-service -n sports-store
# API: http://EXTERNAL_IP/api/v1

# Test health endpoint
curl http://EXTERNAL_IP/health
```

##  CI/CD Workflows

### Continuous Integration (CI)
Triggers on push/PR to `main` or `develop`:

**Android CI:**
-  Build APK
-  Run unit tests
-  Lint checks
-  Upload artifacts
-  Firebase distribution
-  Docker build & push (if secrets configured)

**Backend CI:**
-  Install dependencies
-  Run tests
-  Build Docker image
-  Push to Docker Hub (if secrets configured)

### Continuous Deployment (CD)
Triggers on version tags (e.g., `v1.0.0`):
-  Build release APK
-  Sign APK (if keystore configured)
-  Create GitHub release
-  Firebase distribution
-  Play Store upload (if configured)
-  Docker deployment
-  Kubernetes rollout

### Create a Release

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

##  Setup GitHub Secrets

For full CI/CD functionality, add these secrets in GitHub:
**Settings → Secrets and variables → Actions → New repository secret**

### Firebase ( Currently Active)
- `FIREBASE_APP_ID`: `1:411738770294:android:cda52d6d3a08fb88af8857`
- `FIREBASE_SERVICE_CREDENTIALS`: Service account JSON

### Docker (Optional)
- `DOCKERHUB_USERNAME`: Docker Hub username
- `DOCKERHUB_TOKEN`: Docker Hub access token

See [SECRETS_SETUP.md](SECRETS_SETUP.md) and [backend/README.md](backend/README.md) for detailed instructions.

##  Project Structure

```
Mobile-app-final-Sport-Store/
├── app/                          # Android app source
│   ├── src/
│   │   └── main/
│   │       ├── java/com/keimhean/sportsotore/
│   │       │   ├── MainActivity.kt
│   │       │   ├── CategoryActivity.kt
│   │       │   ├── OnboardingActivity.kt
│   │       │   └── ProfileActivity.kt
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   ├── drawable/
│   │       │   └── values/
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
├── backend/                       # Node.js backend API
│   ├── src/
│   │   ├── config/
│   │   │   └── database.js
│   │   ├── models/
│   │   │   ├── Product.js
│   │   │   ├── User.js
│   │   │   └── Order.js
│   │   ├── controllers/
│   │   │   ├── productController.js
│   │   │   ├── authController.js
│   │   │   └── orderController.js
│   │   ├── routes/
│   │   │   ├── products.js
│   │   │   ├── auth.js
│   │   │   └── orders.js
│   │   ├── middleware/
│   │   │   ├── auth.js
│   │   │   └── errorHandler.js
│   │   └── server.js
│   ├── package.json
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── README.md
│   └── API_DOCS.md
├── .github/
│   └── workflows/
│       ├── android-ci.yml         # Android CI pipeline
│       ├── android-cd.yml         # Android CD pipeline
│       └── backend-ci.yml         # Backend CI pipeline
├── k8s/
│   ├── deployment.yaml            # Android app K8s deployment
│   └── backend-deployment.yaml    # Backend + MongoDB K8s
├── gradle/
├── Dockerfile                      # Android app Dockerfile
├── build.gradle.kts
├── settings.gradle.kts
├── CI_CD_SETUP.md
├── SECRETS_SETUP.md
└── README.md
```

##  Screenshots

### Onboarding Flow
- Page 1: Shop Sports Gear Easily
- Page 2: Fast & Secure Checkout
- Page 3: Track Your Orders

### Main Features
- Home screen with featured products
- 9 sport categories with gradient cards
- Bottom navigation with 5 tabs
- User profile with activity stats

##  Configuration

### Package Name
```
com.keimhean.sportsotore
```

### Firebase Project
- Project ID: `finalproject-kotlin-a735b`
- App ID: `1:411738770294:android:cda52d6d3a08fb88af8857`

### Version Info
- Version Code: 1
- Version Name: 1.0

##  Development Guide

### Adding New Category

1. Create gradient drawable in `res/drawable/img_category_*.xml`
2. Add to `CategoryActivity.kt`:
   ```kotlin
   CategoryItem("", "Category Name", R.drawable.img_category_name)
   ```

### Adding New Screen

1. Create layout XML in `res/layout/`
2. Create Activity in `src/main/java/`
3. Register in `AndroidManifest.xml`
4. Add navigation in MainActivity

##  Testing

```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests CategoryActivityTest

# Generate test report
./gradlew test jacocoTestReport
```

##  Documentation

- **[Backend API Documentation](backend/README.md)** - Complete backend setup guide
- **[API Reference](backend/API_DOCS.md)** - REST API endpoint documentation
- **[CI/CD Setup Guide](CI_CD_SETUP.md)** - GitHub Actions workflows
- **[Secrets Configuration](SECRETS_SETUP.md)** - Firebase and Docker secrets
- **[Kubernetes Deployment](k8s/)** - K8s manifests for full stack

##  Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

##  License

This project is licensed under the MIT License.

##  Author

**Keimhean**
- GitHub: [@Keimhean](https://github.com/Keimhean)
- Repository: [Mobile-app-final-Sport-Store](https://github.com/Keimhean/Mobile-app-final-Sport-Store)

##  Acknowledgments

- Material Design 3 components
- Firebase for distribution
- GitHub Actions for CI/CD
- Docker & Kubernetes for deployment

---

 Star this repo if you find it helpful!
