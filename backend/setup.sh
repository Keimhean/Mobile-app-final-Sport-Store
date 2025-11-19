#!/bin/bash

# Sports Store Backend Setup Script

echo "🚀 Sports Store Backend Setup"
echo "=============================="
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18 or higher."
    exit 1
fi

echo "✅ Node.js version: $(node --version)"
echo ""

# Navigate to backend directory
cd "$(dirname "$0")"

# Install dependencies
echo "📦 Installing dependencies..."
npm install

# Check if .env exists
if [ ! -f .env ]; then
    echo "⚙️  Creating .env file from template..."
    cp .env.example .env
    echo "✅ .env file created. Please update it with your configuration."
else
    echo "✅ .env file already exists."
fi

echo ""
echo "=============================="
echo "✅ Backend setup complete!"
echo ""
echo "Next steps:"
echo "1. Edit backend/.env with your configuration"
echo "2. Start MongoDB: docker run -d -p 27017:27017 mongo:7.0"
echo "3. Run development server: npm run dev"
echo "4. API will be available at http://localhost:3000"
echo ""
echo "Or use Docker Compose:"
echo "  docker-compose up -d"
echo ""
