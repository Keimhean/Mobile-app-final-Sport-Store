# Docker CI/CD Setup Guide

## Prerequisites
- Docker Hub account (free): https://hub.docker.com/signup
- GitHub repository (already done ✅)

## Step 1: Create Docker Hub Account

1. Go to https://hub.docker.com/signup
2. Sign up with your email
3. Verify your email address
4. Remember your username (you'll need it)

## Step 2: Generate Docker Hub Access Token

1. Log in to Docker Hub
2. Click your username (top right) → Account Settings
3. Go to **Security** → **New Access Token**
4. Fill in:
   - Description: `GitHub Actions CI/CD`
   - Access permissions: `Read, Write, Delete`
5. Click **Generate**
6. **IMPORTANT:** Copy the token immediately (you can't see it again!)
   - Example: `dckr_pat_abc123xyz789...`

## Step 3: Add Secrets to GitHub

1. Go to your repository: https://github.com/Keimhean/Mobile-app-final-Sport-Store
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**

### Add DOCKER_USERNAME:
- Name: `DOCKERHUB_USERNAME`
- Secret: Your Docker Hub username (e.g., `keimhean`)
- Click **Add secret**

### Add DOCKER_PASSWORD:
- Click **New repository secret** again
- Name: `DOCKERHUB_TOKEN`
- Secret: Paste the access token you copied (e.g., `dckr_pat_abc123xyz789...`)
- Click **Add secret**

## Step 4: Update Kubernetes Deployment (Optional)

Open `k8s/deployment.yaml` and replace `YOUR_DOCKER_USERNAME` with your actual username:

```yaml
image: keimhean/sports-store:latest
```

## Step 5: Test Docker CI/CD

Push a change to trigger the workflow:

```bash
cd /Users/macbook/Desktop/MyKotlinAndroidApp
git commit --allow-empty -m "test: trigger Docker CI/CD"
git push origin main
```

## Step 6: Verify It's Working

1. Go to GitHub Actions: https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions
2. Click on the latest workflow run
3. Expand the **docker-build** job
4. You should see:
   - ✅ Login to Docker Hub
   - ✅ Build and push Docker image
   - Images pushed: `keimhean/sports-store:latest` and `keimhean/sports-store:<commit-sha>`

5. Check Docker Hub: https://hub.docker.com/r/keimhean/sports-store
   - You should see your image listed

## Step 7: Pull and Run Locally

Once the image is pushed, you can pull it anywhere:

```bash
# Pull from Docker Hub
docker pull keimhean/sports-store:latest

# Run the container
docker run -p 8080:80 keimhean/sports-store:latest

# Visit http://localhost:8080 to download the APK
```

## What Happens Automatically

### On Every Push to `main`:
1. GitHub Actions builds your Android APK
2. Creates a Docker image with the APK inside
3. Pushes to Docker Hub with two tags:
   - `latest` (always the newest)
   - `<commit-sha>` (specific version)

### On Version Tag (e.g., `v1.0.0`):
1. Builds release APK
2. Creates Docker image
3. Pushes to Docker Hub with version tag
4. Optionally deploys to Kubernetes

## Docker Image Details

Your Docker image contains:
- **Base:** nginx:alpine (lightweight web server)
- **Content:** Your Android APK
- **Server:** Serves the APK at http://localhost/app-release.apk
- **Size:** ~50-100MB

## Troubleshooting

### "Error: denied: requested access to the resource is denied"
- Check username is correct (case-sensitive)
- Regenerate access token with Read/Write/Delete permissions
- Update GitHub secrets

### "Error: manifest unknown"
- Image doesn't exist yet (wait for first successful build)
- Check repository name matches your username

### Docker build fails
- Check Dockerfile syntax
- Verify Gradle builds locally first: `./gradlew assembleRelease`

## Commands Reference

```bash
# Local build
docker build -t sports-store:local .

# Run locally
docker run -p 8080:80 sports-store:local

# Tag for Docker Hub
docker tag sports-store:local keimhean/sports-store:latest

# Push to Docker Hub
docker login
docker push keimhean/sports-store:latest

# Pull from anywhere
docker pull keimhean/sports-store:latest
```

## Next Steps

After Docker CI/CD works:
1. ✅ Docker images auto-build on every push
2. 🚀 Deploy to Kubernetes cluster
3. 🔄 Set up automatic rollouts
4. 📊 Monitor container health

---

**Need help?** Check the CI/CD workflow logs at:
https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions
