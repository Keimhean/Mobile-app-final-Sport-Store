# 🏃‍♂️ Sports Store - Android Mobile App

[![Android CI](https://github.com/Keimhean/Mobile-app-final-Sport-Store/workflows/Android%20CI/badge.svg)](https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions)
[![Android CD](https://github.com/Keimhean/Mobile-app-final-Sport-Store/workflows/Android%20CD/badge.svg)](https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions)

A modern Android sports equipment store app built with Kotlin, featuring a beautiful Material Design 3 UI, onboarding flow, category browsing, and full CI/CD pipeline.

## ✨ Features

### Mobile App
- 🎨 **Material Design 3** - Modern, beautiful UI with custom themes
- 🏠 **Bottom Navigation** - 5-tab navigation (Home, Categories, Cart, Profile, Search)
- 📱 **Onboarding Flow** - 3-page ViewPager2 onboarding for new users
- 🏃 **9 Sport Categories** - Running, Gym, Swimming, Cycling, Tennis, Soccer, Basketball, Yoga, Golf
- 👤 **User Profile** - Profile management with stats (completed, active, wishlist)
- 🔐 **Authentication Ready** - Login, Sign Up, Guest access

### CI/CD Pipeline
- ✅ **Automated Build** - Gradle builds on every push
- ✅ **Unit Tests** - Automated testing on CI
- ✅ **Lint Checks** - Code quality validation
- ✅ **Firebase Distribution** - Auto-deploy to testers
- 🐳 **Docker Support** - Containerized builds
- ☸️ **Kubernetes Ready** - K8s deployment manifests
- 🚀 **Play Store Upload** - Automated releases

## 🛠️ Tech Stack

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

### Backend Integration (Ready)
- Firebase (App Distribution, Analytics)
- Clean Architecture pattern
- Dependency Injection (Hilt ready)
- Ktor/Retrofit for networking

### DevOps
- **CI/CD:** GitHub Actions
- **Containerization:** Docker
- **Orchestration:** Kubernetes
- **Distribution:** Firebase App Distribution
- **Store:** Google Play Console

## 🚀 Quick Start

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

2. **Open in Android Studio:**
   - File → Open → Select project folder
   - Wait for Gradle sync

3. **Run the app:**
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

## 🐳 Docker

### Build Docker Image

```bash
docker build -t sports-store:latest .
```

### Run Container

```bash
docker run -p 8080:80 sports-store:latest
# Visit http://localhost:8080 to download APK
```

### Push to Docker Hub

```bash
docker login
docker tag sports-store:latest YOUR_USERNAME/sports-store:latest
docker push YOUR_USERNAME/sports-store:latest
```

## ☸️ Kubernetes

### Deploy to Cluster

```bash
# Create namespace
kubectl create namespace production

# Apply deployment
kubectl apply -f k8s/deployment.yaml

# Check status
kubectl get pods -n production
kubectl get svc -n production

# View logs
kubectl logs -f deployment/sports-store-app -n production
```

## 📦 CI/CD Workflows

### Continuous Integration (CI)
Triggers on push/PR to `main` or `develop`:
- ✅ Build APK
- ✅ Run unit tests
- ✅ Lint checks
- ✅ Upload artifacts
- ✅ Firebase distribution
- 🐳 Docker build & push (if secrets configured)

### Continuous Deployment (CD)
Triggers on version tags (e.g., `v1.0.0`):
- ✅ Build release APK
- ✅ Sign APK (if keystore configured)
- ✅ Create GitHub release
- ✅ Firebase distribution
- ✅ Play Store upload (if configured)
- 🐳 Docker deployment
- ☸️ Kubernetes rollout

### Create a Release

```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## 🔐 Setup GitHub Secrets

For full CI/CD functionality, add these secrets in GitHub:
**Settings → Secrets and variables → Actions → New repository secret**

### Firebase (✅ Currently Active)
- `FIREBASE_APP_ID`: `1:411738770294:android:cda52d6d3a08fb88af8857`
- `FIREBASE_SERVICE_CREDENTIALS`: Service account JSON

### Docker (Optional)
- `DOCKER_USERNAME`: Docker Hub username
- `DOCKER_PASSWORD`: Docker Hub access token

### App Signing (Optional)
- `SIGNING_KEY`: Base64 encoded keystore
- `ALIAS`: Keystore alias
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_PASSWORD`: Key password

### Google Play (Optional)
- `SERVICE_ACCOUNT_JSON`: Play Console service account

### Kubernetes (Optional)
- `KUBE_CONFIG`: Base64 encoded kubeconfig

See [SECRETS_SETUP.md](SECRETS_SETUP.md) for detailed instructions.

## 📁 Project Structure

```
Mobile-app-final-Sport-Store/
├── app/
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
├── .github/
│   └── workflows/
│       ├── android-ci.yml
│       └── android-cd.yml
├── k8s/
│   └── deployment.yaml
├── gradle/
├── Dockerfile
├── build.gradle.kts
├── settings.gradle.kts
├── CI_CD_SETUP.md
├── SECRETS_SETUP.md
└── README.md
```

## 🎨 Screenshots

### Onboarding Flow
- Page 1: Shop Sports Gear Easily
- Page 2: Fast & Secure Checkout
- Page 3: Track Your Orders

### Main Features
- Home screen with featured products
- 9 sport categories with gradient cards
- Bottom navigation with 5 tabs
- User profile with activity stats

## 🔧 Configuration

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

## 📝 Development Guide

### Adding New Category

1. Create gradient drawable in `res/drawable/img_category_*.xml`
2. Add to `CategoryActivity.kt`:
   ```kotlin
   CategoryItem("⚽", "Category Name", R.drawable.img_category_name)
   ```

### Adding New Screen

1. Create layout XML in `res/layout/`
2. Create Activity in `src/main/java/`
3. Register in `AndroidManifest.xml`
4. Add navigation in MainActivity

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run specific test
./gradlew test --tests CategoryActivityTest

# Generate test report
./gradlew test jacocoTestReport
```

## 📚 Documentation

- [CI/CD Setup Guide](CI_CD_SETUP.md)
- [Secrets Configuration](SECRETS_SETUP.md)
- [Kubernetes Deployment](k8s/deployment.yaml)

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👤 Author

**Keimhean**
- GitHub: [@Keimhean](https://github.com/Keimhean)
- Repository: [Mobile-app-final-Sport-Store](https://github.com/Keimhean/Mobile-app-final-Sport-Store)

## 🌟 Acknowledgments

- Material Design 3 components
- Firebase for distribution
- GitHub Actions for CI/CD
- Docker & Kubernetes for deployment

---

**⭐ Star this repo if you find it helpful!**
