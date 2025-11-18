# GitHub Secrets Setup Guide

## Your Firebase App ID
```
1:411738770294:android:cda52d6d3a08fb88af8857
```

## Required GitHub Secrets

Go to: https://github.com/Keimhean/Mobile-app-final-Sport-Store/settings/secrets/actions

Click "New repository secret" and add each of these:

### 1. Firebase App Distribution

**FIREBASE_APP_ID**
```
1:411738770294:android:cda52d6d3a08fb88af8857
```

**FIREBASE_SERVICE_CREDENTIALS**
- Go to Firebase Console: https://console.firebase.google.com/project/finalproject-kotlin-a735b/settings/serviceaccounts/adminsdk
- Click "Generate new private key"
- Download the JSON file
- Copy the entire JSON content and paste as the secret value

### 2. Docker Hub (Optional)

**DOCKER_USERNAME**
```
your-dockerhub-username
```

**DOCKER_PASSWORD**
- Go to https://hub.docker.com
- Account Settings → Security → New Access Token
- Copy the token and paste as secret value

### 3. App Signing (Optional - for releases)

Generate a keystore:
```bash
cd /Users/macbook/Desktop/MyKotlinAndroidApp
keytool -genkey -v -keystore sports-store.jks -keyalg RSA -keysize 2048 -validity 10000 -alias sports-store
```

**SIGNING_KEY**
```bash
base64 -i sports-store.jks | pbcopy
# Paste the copied base64 string as the secret value
```

**ALIAS**
```
sports-store
```

**KEY_STORE_PASSWORD**
```
your-keystore-password
```

**KEY_PASSWORD**
```
your-key-password
```

### 4. Google Play Console (Optional)

**SERVICE_ACCOUNT_JSON**
- Go to Google Play Console
- Setup → API access → Create service account
- Download JSON key
- Copy entire JSON content and paste as secret value

### 5. Kubernetes (Optional)

**KUBE_CONFIG**
```bash
cat ~/.kube/config | base64 | pbcopy
# Paste the copied base64 string as the secret value
```

## Testing

After adding secrets, push a change to trigger CI:
```bash
git commit --allow-empty -m "test: trigger CI with Firebase"
git push origin main
```

Check workflow: https://github.com/Keimhean/Mobile-app-final-Sport-Store/actions
