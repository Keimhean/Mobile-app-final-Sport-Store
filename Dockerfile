# Multi-stage build for Android app
FROM openjdk:21-slim AS builder

# Install Android SDK dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Set up Android SDK
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Download and install Android command line tools
RUN mkdir -p $ANDROID_HOME/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/cmdtools.zip && \
    unzip -q /tmp/cmdtools.zip -d $ANDROID_HOME/cmdline-tools && \
    mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && \
    rm /tmp/cmdtools.zip

# Accept licenses and install build tools
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Grant execute permission to gradlew
RUN chmod +x ./gradlew

# Build the app
RUN ./gradlew assembleRelease --no-daemon

# Final lightweight image
FROM nginx:alpine

# Copy built APK
COPY --from=builder /app/app/build/outputs/apk/release/app-release-unsigned.apk /usr/share/nginx/html/

# Copy a simple HTML page to serve the APK
RUN echo '<html><body><h1>Sports Store App</h1><a href="/app-release-unsigned.apk">Download APK</a></body></html>' > /usr/share/nginx/html/index.html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
