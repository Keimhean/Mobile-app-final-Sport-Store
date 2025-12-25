# CI/CD Setup for Sports Store App

## Overview
This project includes automated CI/CD pipelines using GitHub Actions with Docker/K8s deployment and Firebase App Distribution.

## CI/CD Flow

```
Code Push → GitHub Actions
    ↓
Unit/UI Tests + Static Analysis (Lint)
    ↓
Docker Build & Push to Registry
    ↓
Kubernetes Deployment
    ↓
Firebase App Distribution + Play Console
```

## Continuous Integration (CI)

The CI pipeline runs on every push and pull request to `main` and `develop` branches.

**Jobs:**
1. **Build & Test**: Compiles the app, runs unit tests, and generates debug APK
2. **Lint**: Runs Android lint checks and uploads results
3. **Docker Build**: Builds Docker image and pushes to Docker Hub
4. **Firebase Distribution**: Distributes debug APK to testers

**Triggers:**
- Push to `main` or `develop`
- Pull requests to `main` or `develop`

## Continuous Deployment (CD)

The CD pipeline runs when you create a new version tag.

**Features:**
- Builds release APK
- Signs the APK
- Creates GitHub release with APK
- Distributes to Firebase (testers + production groups)
- Uploads to Google Play Store (internal track)
- Builds and pushes versioned Docker image
- Deploys to Kubernetes cluster

**To trigger a release:**
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## Required Secrets

Add these secrets to your GitHub repository (Settings → Secrets and variables → Actions):

### For Release Signing:
- `SIGNING_KEY`: Base64 encoded keystore file
  ```bash
  base64 your-keystore.jks | pbcopy
  ```
- `ALIAS`: Keystore alias
- `KEY_STORE_PASSWORD`: Keystore password
- `KEY_PASSWORD`: Key password

### For Firebase App Distribution:
- `FIREBASE_APP_ID`: Firebase app ID (from Firebase Console)
- `FIREBASE_SERVICE_CREDENTIALS`: Firebase service account JSON
  ```bash
  cat firebase-service-account.json | jq -c | pbcopy
  ```

### For Play Store Upload:
- `SERVICE_ACCOUNT_JSON`: Google Play Console service account JSON

### For Docker:
- `DOCKER_USERNAME`: Docker Hub username
- `DOCKER_PASSWORD`: Docker Hub password or access token

### For Kubernetes:
- `KUBE_CONFIG`: Base64 encoded kubeconfig file
  ```bash
  cat ~/.kube/config | base64 | pbcopy
  ```

## Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing
3. Add Android app with package name: `com.example.myapp`
4. Download `google-services.json` and place in `app/` directory
5. Go to App Distribution → Create tester groups: `testers`, `production`
6. Generate service account key for GitHub Actions

## Docker Setup

The Dockerfile creates a multi-stage build:
- **Stage 1**: Builds the Android APK using OpenJDK 21
- **Stage 2**: Serves the APK via nginx

Build locally:
```bash
docker build -t sports-store:latest .
docker run -p 8080:80 sports-store:latest
```

## Kubernetes Setup

1. **Create namespace:**
   ```bash
   kubectl create namespace production
   ```

2. **Update deployment.yaml** with your Docker Hub username

3. **Apply configurations:**
   ```bash
   kubectl apply -f k8s/deployment.yaml
   ```

4. **Check deployment:**
   ```bash
   kubectl get pods -n production
   kubectl get service -n production
   ```

5. **Get LoadBalancer IP:**
   ```bash
   kubectl get service sports-store-service -n production
   ```

## Generating Keystore

If you don't have a keystore yet:

```bash
keytool -genkey -v -keystore sports-store.jks -keyalg RSA -keysize 2048 -validity 10000 -alias sports-store
```

## Local Testing

Test the build locally before pushing:

```bash
# Run tests
./gradlew test

# Run lint
./gradlew lintDebug

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build Docker image
docker build -t sports-store:test .

# Test Kubernetes deployment (local)
kubectl apply -f k8s/deployment.yaml --dry-run=client
```

## Workflow Status Badges

Add these badges to your README.md:

```markdown
![Android CI](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Android%20CI/badge.svg)
![Android CD](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Android%20CD/badge.svg)
```
