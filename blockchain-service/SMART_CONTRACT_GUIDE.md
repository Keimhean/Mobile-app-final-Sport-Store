# Smart Contract Integration Guide

This guide explains how to use the Ethereum smart contracts in the Sports Store blockchain service.

## Overview

The blockchain service includes three production-ready smart contracts:

1. **LoyaltyToken** (ERC-20) - Loyalty points system
2. **ProductNFT** (ERC-721) - Product authenticity certificates  
3. **PaymentEscrow** - Secure payment escrow with dispute resolution

## Prerequisites

### 1. Run Local Ethereum Node (Ganache)

Install Ganache for local blockchain testing:

```bash
npm install -g ganache
```

Start Ganache:

```bash
ganache --port 8545 --networkId 1337
```

This will create 10 test accounts with 100 ETH each.

### 2. Get Test Account

When Ganache starts, it displays test accounts like:

```
Available Accounts
==================
(0) 0x627306090abaB3A6e1400e9345bC60c78a8BEf57 (100 ETH)
(1) 0xf17f52151EbEF6C7334FAD080c5704D77216b732 (100 ETH)
...

Private Keys
==================
(0) 0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3
(1) 0xae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f
...
```

Copy an account address and its private key for testing.

## API Endpoints

### Network Status

Check if connected to Ethereum network:

```bash
curl http://localhost:4000/api/v1/blockchain/network/status
```

Response:
```json
{
  "success": true,
  "isConnected": true,
  "networkId": "1337",
  "blockNumber": "15",
  "rpcUrl": "http://127.0.0.1:8545"
}
```

### Create Account

Generate a new Ethereum account:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/account/create
```

Response:
```json
{
  "success": true,
  "account": {
    "address": "0x1234...",
    "privateKey": "0xabcd..."
  },
  "warning": "Store private key securely - it cannot be recovered!"
}
```

### Get Account Balance

```bash
curl http://localhost:4000/api/v1/blockchain/account/0x627306090abaB3A6e1400e9345bC60c78a8BEf57/balance
```

### Get Contract ABI

Retrieve compiled contract ABI:

```bash
curl http://localhost:4000/api/v1/blockchain/contract/LoyaltyToken/abi
curl http://localhost:4000/api/v1/blockchain/contract/ProductNFT/abi
curl http://localhost:4000/api/v1/blockchain/contract/PaymentEscrow/abi
```

## Contract Deployment

### Deploy Loyalty Token Contract

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/deploy \
  -H "Content-Type: application/json" \
  -d '{
    "contractName": "LoyaltyToken",
    "constructorArgs": [],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

Response:
```json
{
  "success": true,
  "message": "Contract deployed successfully",
  "contractAddress": "0x8f0483125FCb9aaAEFA9209D8E9d7b9C8B9Fb90F",
  "transactionHash": "0x123...",
  "gasUsed": "1234567"
}
```

**Save the `contractAddress`** - you'll need it for all contract interactions!

### Deploy Product NFT Contract

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/deploy \
  -H "Content-Type: application/json" \
  -d '{
    "contractName": "ProductNFT",
    "constructorArgs": [],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

### Deploy Payment Escrow Contract

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/deploy \
  -H "Content-Type: application/json" \
  -d '{
    "contractName": "PaymentEscrow",
    "constructorArgs": [],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

## Loyalty Token Usage

### Award Points to Customer

Award 500 points (5 dollars spent):

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x8f0483125FCb9aaAEFA9209D8E9d7b9C8B9Fb90F",
    "contractName": "LoyaltyToken",
    "methodName": "awardPoints",
    "params": [
      "0xf17f52151EbEF6C7334FAD080c5704D77216b732",
      500
    ],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

### Check Customer Points

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/call \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x8f0483125FCb9aaAEFA9209D8E9d7b9C8B9Fb90F",
    "contractName": "LoyaltyToken",
    "methodName": "getCustomerPoints",
    "params": ["0xf17f52151EbEF6C7334FAD080c5704D77216b732"]
  }'
```

### Redeem Points

Redeem 100 points for discount:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x8f0483125FCb9aaAEFA9209D8E9d7b9C8B9Fb90F",
    "contractName": "LoyaltyToken",
    "methodName": "redeemPoints",
    "params": [
      "0xf17f52151EbEF6C7334FAD080c5704D77216b732",
      100
    ],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

## Product NFT Usage

### Issue Product Certificate

Issue authenticity certificate for a product:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "ProductNFT",
    "methodName": "issueCertificate",
    "params": [
      "0xf17f52151EbEF6C7334FAD080c5704D77216b732",
      "SKU-12345",
      "Nike Air Max",
      "2024-12-25"
    ],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

Returns `tokenId` which is used to reference the certificate.

### Verify Product Authenticity

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/call \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "ProductNFT",
    "methodName": "verifyProduct",
    "params": ["SKU-12345"]
  }'
```

Returns:
```json
{
  "success": true,
  "result": {
    "0": true,
    "1": "1"
  }
}
```
Where `0` is `isValid` (true/false) and `1` is the `tokenId`.

### Get Certificate Details

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/call \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "ProductNFT",
    "methodName": "getCertificate",
    "params": [1]
  }'
```

Returns full certificate data including SKU, product name, issue date, owner.

## Payment Escrow Usage

### Create Escrow Payment

Create escrow for order #123:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "PaymentEscrow",
    "methodName": "createPayment",
    "params": [
      "ORDER-123",
      "0xf17f52151EbEF6C7334FAD080c5704D77216b732"
    ],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3",
    "value": "1000000000000000000"
  }'
```

Note: `value` is in Wei (1 ETH = 10^18 Wei). Example sends 1 ETH.

### Confirm Delivery

After customer receives product:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "PaymentEscrow",
    "methodName": "confirmDelivery",
    "params": ["ORDER-123"],
    "fromAddress": "0xf17f52151EbEF6C7334FAD080c5704D77216b732",
    "privateKey": "0xae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f"
  }'
```

This releases payment to seller (minus 2.5% platform fee).

### Initiate Dispute

If there's a problem:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "PaymentEscrow",
    "methodName": "initiateDispute",
    "params": ["ORDER-123"],
    "fromAddress": "0xf17f52151EbEF6C7334FAD080c5704D77216b732",
    "privateKey": "0xae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f"
  }'
```

### Resolve Dispute (Owner Only)

Platform owner can resolve disputes:

```bash
curl -X POST http://localhost:4000/api/v1/blockchain/contract/send \
  -H "Content-Type: application/json" \
  -d '{
    "contractAddress": "0x...",
    "contractName": "PaymentEscrow",
    "methodName": "resolveDispute",
    "params": [
      "ORDER-123",
      true
    ],
    "fromAddress": "0x627306090abaB3A6e1400e9345bC60c78a8BEf57",
    "privateKey": "0xc87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3"
  }'
```

`true` = refund buyer, `false` = pay seller

## Integration with Android App

### Example: Award Points After Purchase

```kotlin
// In OrderService.kt
suspend fun completeOrder(orderId: String, totalAmount: Double) {
    // ... existing order completion logic ...
    
    // Award loyalty points via blockchain
    val points = (totalAmount * 100).toInt() // 100 points per dollar
    
    val requestBody = JSONObject().apply {
        put("contractAddress", LOYALTY_CONTRACT_ADDRESS)
        put("contractName", "LoyaltyToken")
        put("methodName", "awardPoints")
        put("params", JSONArray().apply {
            put(customerAddress)
            put(points)
        })
        put("fromAddress", PLATFORM_ADDRESS)
        put("privateKey", PLATFORM_PRIVATE_KEY)
    }
    
    val response = blockchainService.sendTransaction(requestBody)
    if (response.success) {
        Log.d("Blockchain", "Awarded $points points to customer")
    }
}
```

### Example: Verify Product Authenticity

```kotlin
// In ProductDetailsActivity.kt
suspend fun verifyAuthenticity(sku: String) {
    val requestBody = JSONObject().apply {
        put("contractAddress", NFT_CONTRACT_ADDRESS)
        put("contractName", "ProductNFT")
        put("methodName", "verifyProduct")
        put("params", JSONArray().put(sku))
    }
    
    val response = blockchainService.callContract(requestBody)
    val isAuthentic = response.result["0"] as Boolean
    
    if (isAuthentic) {
        showAuthenticityBadge()
    }
}
```

## Testing Workflow

1. **Start Ganache**
   ```bash
   ganache --port 8545
   ```

2. **Deploy Contracts**
   ```bash
   # Save the returned contract addresses!
   curl -X POST ... # Deploy LoyaltyToken
   curl -X POST ... # Deploy ProductNFT
   curl -X POST ... # Deploy PaymentEscrow
   ```

3. **Test Loyalty Flow**
   ```bash
   # Award points
   curl -X POST ... awardPoints ...
   
   # Check balance
   curl -X POST ... getCustomerPoints ...
   
   # Redeem points
   curl -X POST ... redeemPoints ...
   ```

4. **Test NFT Flow**
   ```bash
   # Issue certificate
   curl -X POST ... issueCertificate ...
   
   # Verify product
   curl -X POST ... verifyProduct ...
   ```

5. **Test Escrow Flow**
   ```bash
   # Create payment
   curl -X POST ... createPayment ... (with value in Wei)
   
   # Confirm delivery
   curl -X POST ... confirmDelivery ...
   ```

## Troubleshooting

### "isConnected: false"

Make sure Ganache is running:
```bash
ganache --port 8545
```

### "insufficient funds"

Ensure your account has enough ETH. Ganache accounts start with 100 ETH.

### "invalid address"

Addresses must be checksummed Ethereum addresses (0x...). Use the addresses from Ganache or the account creation endpoint.

### "Contract deployment failed"

Check:
- Ganache is running
- Private key is correct
- Account has sufficient ETH for gas

## Security Notes

⚠️ **NEVER commit private keys to git!**

- Store private keys in environment variables
- Use `.env` file (add to `.gitignore`)
- For production, use hardware wallets or key management services

⚠️ **Use Testnet for Development**

- Ganache for local testing
- Sepolia or Goerli for public testnet
- Never deploy to mainnet without thorough testing

## Next Steps

1. Deploy contracts to test environment
2. Integrate blockchain calls into Android app
3. Add blockchain transaction history UI
4. Implement automatic point awards on purchase
5. Add QR code scanning for product verification
6. Deploy to Ethereum testnet (Sepolia)
7. Audit smart contracts before mainnet deployment

## Support

For issues or questions:
- Check blockchain service logs: `docker logs sports-store-blockchain`
- Verify Ganache is running: `netstat -an | grep 8545`
- Test network connection: `curl http://localhost:4000/api/v1/blockchain/network/status`
