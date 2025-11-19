# Firebase Authentication Setup Guide
## Google & Facebook Sign-In Integration

---

## ⚠️ Important: Firebase Configuration Required

Your current `google-services.json` is configured for package `com.keimhean.sportsotore` but your app uses `com.example.myapp`. You need to update this.

---

## 🔧 Step 1: Update Firebase Project

### Option A: Add New App to Existing Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: `finalproject-kotlin-a735b`
3. Click **Add App** → **Android**
4. Enter package name: `com.example.myapp`
5. Download the new `google-services.json`
6. Replace `app/google-services.json` with the downloaded file

### Option B: Change Package Name in Your App

1. Rename all `com.example.myapp` to `com.keimhean.sportsotore` in your codebase
2. Update `namespace` in `app/build.gradle.kts`
3. Refactor directory structure

**Recommendation:** Use Option A (easier)

---

## 🔥 Step 2: Enable Authentication Methods in Firebase

### Enable Google Sign-In

1. Firebase Console → **Authentication** → **Sign-in method**
2. Click **Google** → **Enable**
3. Set support email
4. Click **Save**

### Enable Facebook Sign-In

1. Firebase Console → **Authentication** → **Sign-in method**
2. Click **Facebook** → **Enable**
3. You'll need Facebook App ID and App Secret (see Step 3)
4. Add OAuth redirect URI to Facebook App settings
5. Click **Save**

---

## 📘 Step 3: Create Facebook App

### Create Facebook Developer App

1. Go to [Facebook Developers](https://developers.facebook.com/)
2. Click **My Apps** → **Create App**
3. Choose **Consumer** app type
4. Fill in app details:
   - **App Name:** Sports Store
   - **App Contact Email:** your-email@example.com
5. Click **Create App**

### Get App ID and Client Token

1. Dashboard → **Settings** → **Basic**
2. Copy **App ID**
3. Copy **App Secret** (you may need to show it)

### Configure Facebook Login

1. Dashboard → **Add Product** → **Facebook Login** → **Set Up**
2. Choose **Android**
3. Follow the setup wizard:
   - **Package Name:** `com.example.myapp`
   - **Default Activity Class:** `com.example.myapp.LoginActivity`
   - **Key Hashes:** Generate using the command below

### Generate Key Hash

Run this command in terminal:
```bash
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
```

Password (if prompted): `android`

Copy the output hash and paste into Facebook Login settings.

### Add OAuth Redirect URI

1. Facebook App Dashboard → **Facebook Login** → **Settings**
2. Add to **Valid OAuth Redirect URIs**:
   ```
   https://finalproject-kotlin-a735b.firebaseapp.com/__/auth/handler
   ```
3. Save changes

---

## 🔑 Step 4: Update Your App Configuration

### Update `strings.xml`

Replace placeholders in `app/src/main/res/values/strings.xml`:

```xml
<string name="facebook_app_id">YOUR_FACEBOOK_APP_ID</string>
<string name="facebook_client_token">YOUR_FACEBOOK_CLIENT_TOKEN</string>
```

With your actual values from Facebook Dashboard.

### Update Firebase Web Client ID

In `app/src/main/java/com/example/myapp/auth/FirebaseAuthHelper.kt`, line 28:

```kotlin
.requestIdToken("411738770294-YOUR_WEB_CLIENT_ID.apps.googleusercontent.com")
```

**How to get Web Client ID:**

1. Firebase Console → **Project Settings**
2. Scroll down to **Your apps**
3. Click on your Android app
4. Expand **SDK setup and configuration**
5. Find **Web client ID** under **OAuth 2.0 Client IDs**
6. Copy the entire client ID (format: `xxxxx-xxxxxxxx.apps.googleusercontent.com`)
7. Replace in the code

Alternatively, check `google-services.json` after downloading:
```json
"oauth_client": [
  {
    "client_id": "COPY_THIS_VALUE",
    "client_type": 3
  }
]
```

---

## 🛠️ Step 5: Build and Test

### Update google-services.json

After creating the new app in Firebase:

1. Download `google-services.json` for package `com.example.myapp`
2. Replace `app/google-services.json`

### Build the App

```bash
./gradlew clean build
./gradlew installDebug
```

### Test Google Sign-In

1. Open app → Login screen
2. Tap **Google** button
3. Choose Google account
4. Should see "Google login success" and navigate to HomeActivity

### Test Facebook Sign-In

1. Open app → Login screen
2. Tap **Facebook** button
3. Enter Facebook credentials (or use test user)
4. Approve permissions
5. Should see "Facebook login success" and navigate to HomeActivity

---

## 🧪 Testing with Test Users

### Create Firebase Test Users

1. Firebase Console → **Authentication** → **Users**
2. Click **Add User**
3. Create test accounts

### Create Facebook Test Users

1. Facebook App Dashboard → **Roles** → **Test Users**
2. Click **Add Test Users**
3. Use these credentials for testing

---

## 🔍 Troubleshooting

### Google Sign-In Fails

**Error:** "API key not found"
- **Solution:** Make sure `google-services.json` package name matches your app

**Error:** "Sign-in failed"
- **Solution:** Enable Google Sign-In in Firebase Console
- **Solution:** Check Web Client ID is correct

### Facebook Sign-In Fails

**Error:** "App ID not found"
- **Solution:** Verify `facebook_app_id` in `strings.xml`

**Error:** "Invalid key hash"
- **Solution:** Regenerate key hash and add to Facebook Dashboard
- **Solution:** For release builds, use release keystore

**Error:** "Invalid redirect URI"
- **Solution:** Add Firebase OAuth redirect URI to Facebook Login settings

### Build Errors

**Error:** "Duplicate class found"
- **Solution:** Clean and rebuild:
  ```bash
  ./gradlew clean
  ./gradlew build
  ```

**Error:** "google-services.json not found"
- **Solution:** Ensure file is in `app/` directory
- **Solution:** Sync Gradle files

---

## 📋 Checklist

Before testing, ensure:

- [ ] Downloaded new `google-services.json` for `com.example.myapp`
- [ ] Replaced old `google-services.json` file
- [ ] Enabled Google Sign-In in Firebase Console
- [ ] Enabled Facebook Login in Firebase Console
- [ ] Created Facebook Developer App
- [ ] Got Facebook App ID and Client Token
- [ ] Updated `strings.xml` with Facebook credentials
- [ ] Updated `FirebaseAuthHelper.kt` with Web Client ID
- [ ] Generated and added Facebook key hash
- [ ] Added OAuth redirect URI to Facebook
- [ ] Built and installed app
- [ ] Tested Google sign-in
- [ ] Tested Facebook sign-in

---

## 🔐 Security Notes

### For Production:

1. **Never commit sensitive keys:**
   - Add `google-services.json` to `.gitignore`
   - Use environment variables for secrets
   - Store Facebook credentials securely

2. **Use Release Key Hash:**
   ```bash
   keytool -exportcert -alias YOUR_RELEASE_KEY_ALIAS -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64
   ```

3. **Enable App Check** (Firebase Console → App Check)

4. **Review OAuth scopes** (only request what you need)

5. **Implement proper error handling**

---

## 📱 What's Implemented

✅ Firebase Authentication integration  
✅ Google Sign-In with OAuth 2.0  
✅ Facebook Login with Graph API  
✅ Token management (Firebase ID Token)  
✅ Seamless navigation after auth  
✅ Error handling and user feedback  
✅ Sign-out functionality  

---

## 🔄 How It Works

### Google Sign-In Flow

1. User taps **Google** button
2. `FirebaseAuthHelper.startGoogleSignIn()` launches Google Sign-In Intent
3. User selects Google account
4. `onActivityResult()` receives the result
5. Exchange Google token for Firebase credential
6. Sign in to Firebase
7. Get Firebase ID token
8. Save token locally
9. Navigate to HomeActivity

### Facebook Sign-In Flow

1. User taps **Facebook** button
2. `FirebaseAuthHelper.startFacebookSignIn()` launches Facebook Login
3. User authorizes app
4. Callback receives Facebook access token
5. Exchange Facebook token for Firebase credential
6. Sign in to Firebase
7. Get Firebase ID token
8. Save token locally
9. Navigate to HomeActivity

---

## 📞 Support Resources

- [Firebase Auth Docs](https://firebase.google.com/docs/auth)
- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android)
- [Facebook Login for Android](https://developers.facebook.com/docs/facebook-login/android)
- [Firebase Console](https://console.firebase.google.com/)
- [Facebook Developers](https://developers.facebook.com/)

---

**Last Updated:** November 19, 2025  
**Status:** Setup Required - Follow steps above to complete integration
