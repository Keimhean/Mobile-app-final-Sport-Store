# Firebase Quick Fix Guide

## 🔴 CRITICAL: Your Firebase is not fully configured

### Your SHA-1 Fingerprint:
```
80:A4:91:66:CE:8D:90:8D:DB:5A:DD:DC:01:EA:79:0C:C2:9D:C0:C6
```

## ⚡ Quick Steps (5 minutes):

### 1. Open Firebase Console
Go to: https://console.firebase.google.com/project/finalproject-kotlin-a735b/settings/general

### 2. Add Android App (if not exists)
- Click "Add app" → Android icon
- **Package name:** `com.example.myapp`
- **App nickname:** Sports Store
- **SHA-1 certificate:** `80:A4:91:66:CE:8D:90:8D:DB:5A:DD:DC:01:EA:79:0C:C2:9D:C0:C6`
- Click "Register app"
- Download `google-services.json`

### 3. Enable Google Sign-In
- Go to: https://console.firebase.google.com/project/finalproject-kotlin-a735b/authentication/providers
- Click **Google** provider
- Toggle **Enable**
- Set support email (your email)
- Click **Save**

### 4. Get Web Client ID
After enabling Google Sign-In:
- Go back to Project Settings
- Download **NEW** `google-services.json` (it will now have OAuth clients)
- Replace `app/google-services.json` with the new file
- Open the new file and find this section:
```json
"oauth_client": [
  {
    "client_id": "xxxxx-xxxxxxxx.apps.googleusercontent.com",
    "client_type": 3
  }
]
```
- Copy the `client_id` value (the one with `client_type: 3`)

### 5. Update Your Code
Edit `app/src/main/java/com/example/myapp/auth/FirebaseAuthHelper.kt` line 43:

**Change from:**
```kotlin
.requestIdToken("411738770294-YOUR_WEB_CLIENT_ID.apps.googleusercontent.com")
```

**Change to:**
```kotlin
.requestIdToken("YOUR_ACTUAL_WEB_CLIENT_ID_FROM_STEP_4")
```

### 6. Replace google-services.json
```bash
# Replace the file in your project
cp ~/Downloads/google-services.json app/google-services.json
```

### 7. Rebuild and Test
```bash
./gradlew clean assembleDebug
./gradlew installDebug
```

### 8. Commit Changes
```bash
git add app/google-services.json app/src/main/java/com/example/myapp/auth/FirebaseAuthHelper.kt
git commit -m "fix: update Firebase configuration with proper OAuth client"
git push
```

---

## 🔍 Verify Configuration

After completing the steps, your `google-services.json` should have:
- ✅ Package name: `com.example.myapp`
- ✅ `oauth_client` array with at least one entry
- ✅ Web client (client_type: 3)

Check with:
```bash
cat app/google-services.json | jq '.client[0].oauth_client'
```

If it shows `[]` (empty), you need to enable Google Sign-In in Firebase Console.

---

## 📱 Test Google Sign-In

1. Launch app on emulator
2. Go to Login screen
3. Tap **Google** button
4. Should see Google account picker
5. Select account
6. Should log in successfully and navigate to Home

---

## ❌ Common Errors

### "API not enabled"
- Solution: Enable Google Sign-In in Firebase Console

### "Invalid client ID"
- Solution: Make sure you copied the Web Client ID (client_type: 3), not Android client

### "Sign-in failed: 10"
- Solution: SHA-1 fingerprint not added to Firebase Console
- Add: `80:A4:91:66:CE:8D:90:8D:DB:5A:DD:DC:01:EA:79:0C:C2:9D:C0:C6`

### "No matching client found"
- Solution: Package name mismatch
- Verify package in AndroidManifest.xml matches Firebase (com.example.myapp)

---

## 🎯 Current Status

❌ **OAuth clients:** Missing (oauth_client: [])  
❌ **Web Client ID:** Using placeholder  
❌ **Google Sign-In:** Not enabled in Firebase  
⚠️ **SHA-1:** Not added to Firebase project  

**After following steps above:**
✅ All should be configured correctly

---

**Need help?** Check the full guide: `FIREBASE_SETUP.md`
