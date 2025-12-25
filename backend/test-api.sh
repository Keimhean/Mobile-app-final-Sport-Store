#!/bin/bash

# Backend API Testing Script
# Tests all endpoints with proper flow

echo "🧪 Sports Store Backend API Tests"
echo "=================================="
echo ""

BASE_URL="http://localhost:3000"
API_URL="$BASE_URL/api/v1"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

# Function to test endpoint
test_endpoint() {
    local name="$1"
    local expected_code="$2"
    local response="$3"
    
    if [[ $response == *"\"success\":true"* ]] && [[ $expected_code == "2"* ]]; then
        echo -e "${GREEN}✓${NC} $name"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗${NC} $name"
        echo "   Response: $response"
        ((TESTS_FAILED++))
    fi
}

echo "1️⃣  Testing Health Check..."
RESPONSE=$(curl -s "$BASE_URL/health")
test_endpoint "Health check" "200" "$RESPONSE"
echo ""

echo "2️⃣  Testing User Registration..."
RESPONSE=$(curl -s -X POST "$API_URL/auth/register" \
    -H "Content-Type: application/json" \
    -d '{
        "name": "John Doe",
        "email": "john.doe@example.com",
        "password": "securepass123",
        "phone": "+1234567890"
    }')
test_endpoint "User registration" "201" "$RESPONSE"

# Extract token
USER_TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo -e "   ${BLUE}Token:${NC} ${USER_TOKEN:0:50}..."
echo ""

echo "3️⃣  Testing User Login..."
RESPONSE=$(curl -s -X POST "$API_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "email": "john.doe@example.com",
        "password": "securepass123"
    }')
test_endpoint "User login" "200" "$RESPONSE"
USER_TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo ""

echo "4️⃣  Testing Get Current User..."
RESPONSE=$(curl -s "$API_URL/auth/me" \
    -H "Authorization: Bearer $USER_TOKEN")
test_endpoint "Get current user" "200" "$RESPONSE"
USER_ID=$(echo $RESPONSE | grep -o '"_id":"[^"]*' | cut -d'"' -f4)
echo ""

echo "5️⃣  Testing Get Products (Public)..."
RESPONSE=$(curl -s "$API_URL/products")
test_endpoint "Get all products" "200" "$RESPONSE"
echo ""

echo "6️⃣  Testing Product Filters..."
RESPONSE=$(curl -s "$API_URL/products?category=Football&featured=true")
test_endpoint "Filter products" "200" "$RESPONSE"
echo ""

echo "7️⃣  Testing Create Product (Should Fail - Not Admin)..."
RESPONSE=$(curl -s -X POST "$API_URL/products" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $USER_TOKEN" \
    -d '{
        "name": "Test Product",
        "description": "Test",
        "price": 99.99,
        "category": "Football",
        "brand": "Test Brand",
        "stock": 10
    }')
if [[ $response == *"not authorized"* ]]; then
    echo -e "${GREEN}✓${NC} Authorization check works (403)"
    ((TESTS_PASSED++))
else
    echo -e "${RED}✗${NC} Authorization check failed"
    ((TESTS_FAILED++))
fi
echo ""

echo "8️⃣  Setting up Admin User..."
docker exec -i sports-store-mongodb mongosh -u admin -p password123 --authenticationDatabase admin sports_store --quiet --eval "db.users.updateOne({ email: 'john.doe@example.com' }, { \$set: { role: 'admin' } })" > /dev/null 2>&1
echo -e "${GREEN}✓${NC} User promoted to admin"

# Login again to get admin token
RESPONSE=$(curl -s -X POST "$API_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "email": "john.doe@example.com",
        "password": "securepass123"
    }')
ADMIN_TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo ""

echo "9️⃣  Testing Create Product (Admin)..."
RESPONSE=$(curl -s -X POST "$API_URL/products" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d '{
        "name": "Premium Basketball",
        "description": "Professional indoor/outdoor basketball",
        "price": 59.99,
        "category": "Basketball",
        "brand": "Spalding",
        "stock": 100,
        "colors": ["Orange"],
        "featured": true
    }')
test_endpoint "Create product (admin)" "201" "$RESPONSE"
PRODUCT_ID=$(echo $RESPONSE | grep -o '"_id":"[^"]*' | cut -d'"' -f4)
echo -e "   ${BLUE}Product ID:${NC} $PRODUCT_ID"
echo ""

echo "🔟 Testing Get Single Product..."
RESPONSE=$(curl -s "$API_URL/products/$PRODUCT_ID")
test_endpoint "Get single product" "200" "$RESPONSE"
echo ""

echo "1️⃣1️⃣  Testing Update Product..."
RESPONSE=$(curl -s -X PUT "$API_URL/products/$PRODUCT_ID" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d '{
        "price": 49.99,
        "stock": 150
    }')
test_endpoint "Update product" "200" "$RESPONSE"
echo ""

echo "1️⃣2️⃣  Testing Create Order..."
RESPONSE=$(curl -s -X POST "$API_URL/orders" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d "{
        \"items\": [
            {
                \"product\": \"$PRODUCT_ID\",
                \"quantity\": 2,
                \"color\": \"Orange\",
                \"price\": 49.99
            }
        ],
        \"shippingAddress\": {
            \"street\": \"456 Oak Ave\",
            \"city\": \"Los Angeles\",
            \"state\": \"CA\",
            \"zipCode\": \"90001\",
            \"country\": \"USA\"
        },
        \"paymentMethod\": \"credit_card\"
    }")
test_endpoint "Create order" "201" "$RESPONSE"
ORDER_ID=$(echo $RESPONSE | grep -o '"_id":"[^"]*' | cut -d'"' -f4)
echo -e "   ${BLUE}Order ID:${NC} $ORDER_ID"
echo ""

echo "1️⃣3️⃣  Testing Get Orders..."
RESPONSE=$(curl -s "$API_URL/orders" \
    -H "Authorization: Bearer $ADMIN_TOKEN")
test_endpoint "Get all orders" "200" "$RESPONSE"
echo ""

echo "1️⃣4️⃣  Testing Get Single Order..."
RESPONSE=$(curl -s "$API_URL/orders/$ORDER_ID" \
    -H "Authorization: Bearer $ADMIN_TOKEN")
test_endpoint "Get single order" "200" "$RESPONSE"
echo ""

echo "1️⃣5️⃣  Testing Update Order Status..."
RESPONSE=$(curl -s -X PUT "$API_URL/orders/$ORDER_ID" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d '{
        "status": "shipped",
        "trackingNumber": "TRACK123456"
    }')
test_endpoint "Update order status" "200" "$RESPONSE"
echo ""

echo "1️⃣6️⃣  Testing Delete Product..."
RESPONSE=$(curl -s -X DELETE "$API_URL/products/$PRODUCT_ID" \
    -H "Authorization: Bearer $ADMIN_TOKEN")
test_endpoint "Delete product" "200" "$RESPONSE"
echo ""

echo "=================================="
echo "📊 Test Results"
echo "=================================="
echo -e "${GREEN}Passed:${NC} $TESTS_PASSED"
echo -e "${RED}Failed:${NC} $TESTS_FAILED"
echo -e "Total:  $((TESTS_PASSED + TESTS_FAILED))"
echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}✅ All tests passed!${NC}"
    exit 0
else
    echo -e "${RED}❌ Some tests failed${NC}"
    exit 1
fi
