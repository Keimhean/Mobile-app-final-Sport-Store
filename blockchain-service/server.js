const express = require('express');
const cors = require('cors');
const crypto = require('crypto');
const web3Service = require('./web3Service');
const { getContract } = require('./compiler');

const app = express();
app.use(cors());
app.use(express.json());

// Blockchain Implementation
class Block {
    constructor(index, timestamp, transactions, previousHash = '') {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = this.calculateHash();
    }

    calculateHash() {
        return crypto
            .createHash('sha256')
            .update(
                this.index +
                this.previousHash +
                this.timestamp +
                JSON.stringify(this.transactions) +
                this.nonce
            )
            .digest('hex');
    }

    mineBlock(difficulty) {
        while (this.hash.substring(0, difficulty) !== Array(difficulty + 1).join('0')) {
            this.nonce++;
            this.hash = this.calculateHash();
        }
        console.log(`Block mined: ${this.hash}`);
    }
}

class Blockchain {
    constructor() {
        this.chain = [this.createGenesisBlock()];
        this.difficulty = 2;
        this.pendingTransactions = [];
        this.miningReward = 100;
    }

    createGenesisBlock() {
        return new Block(0, Date.now(), [{ type: 'genesis', data: 'Genesis Block' }], '0');
    }

    getLatestBlock() {
        return this.chain[this.chain.length - 1];
    }

    minePendingTransactions(miningRewardAddress) {
        const block = new Block(
            this.chain.length,
            Date.now(),
            this.pendingTransactions,
            this.getLatestBlock().hash
        );
        block.mineBlock(this.difficulty);

        console.log('Block successfully mined!');
        this.chain.push(block);

        this.pendingTransactions = [
            {
                type: 'mining_reward',
                to: miningRewardAddress,
                amount: this.miningReward,
            },
        ];
    }

    createTransaction(transaction) {
        this.pendingTransactions.push(transaction);
    }

    isChainValid() {
        for (let i = 1; i < this.chain.length; i++) {
            const currentBlock = this.chain[i];
            const previousBlock = this.chain[i - 1];

            if (currentBlock.hash !== currentBlock.calculateHash()) {
                return false;
            }

            if (currentBlock.previousHash !== previousBlock.hash) {
                return false;
            }
        }
        return true;
    }

    getTransactionsByOrderId(orderId) {
        const transactions = [];
        for (const block of this.chain) {
            for (const tx of block.transactions) {
                if (tx.orderId === orderId) {
                    transactions.push({
                        ...tx,
                        blockIndex: block.index,
                        blockHash: block.hash,
                        timestamp: block.timestamp,
                    });
                }
            }
        }
        return transactions;
    }
}

// Initialize blockchain
const sportStoreBlockchain = new Blockchain();

// Routes
app.get('/health', (req, res) => {
    res.json({
        status: 'healthy',
        service: 'Blockchain Service',
        timestamp: new Date().toISOString(),
        chainLength: sportStoreBlockchain.chain.length,
    });
});

app.post('/api/v1/blockchain/order/create', (req, res) => {
    try {
        const { orderId, userId, products, totalAmount } = req.body;

        if (!orderId || !userId) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields: orderId, userId',
            });
        }

        const transaction = {
            type: 'order_created',
            orderId,
            userId,
            products,
            totalAmount,
            status: 'pending',
            timestamp: Date.now(),
        };

        sportStoreBlockchain.createTransaction(transaction);
        sportStoreBlockchain.minePendingTransactions('system');

        res.json({
            success: true,
            message: 'Order registered on blockchain',
            orderId,
            blockIndex: sportStoreBlockchain.chain.length - 1,
        });
    } catch (error) {
        console.error('Order creation error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.post('/api/v1/blockchain/order/update', (req, res) => {
    try {
        const { orderId, status, updatedBy } = req.body;

        if (!orderId || !status) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields: orderId, status',
            });
        }

        const transaction = {
            type: 'order_updated',
            orderId,
            status,
            updatedBy: updatedBy || 'system',
            timestamp: Date.now(),
        };

        sportStoreBlockchain.createTransaction(transaction);
        sportStoreBlockchain.minePendingTransactions('system');

        res.json({
            success: true,
            message: 'Order status updated on blockchain',
            orderId,
            newStatus: status,
        });
    } catch (error) {
        console.error('Order update error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.post('/api/v1/blockchain/payment/verify', (req, res) => {
    try {
        const { orderId, paymentId, amount, method } = req.body;

        if (!orderId || !paymentId || !amount) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields: orderId, paymentId, amount',
            });
        }

        const transaction = {
            type: 'payment_verified',
            orderId,
            paymentId,
            amount,
            method: method || 'card',
            timestamp: Date.now(),
            verified: true,
        };

        sportStoreBlockchain.createTransaction(transaction);
        sportStoreBlockchain.minePendingTransactions('system');

        const receiptHash = crypto
            .createHash('sha256')
            .update(`${orderId}-${paymentId}-${amount}`)
            .digest('hex');

        res.json({
            success: true,
            message: 'Payment verified on blockchain',
            orderId,
            paymentId,
            receiptHash,
            blockIndex: sportStoreBlockchain.chain.length - 1,
        });
    } catch (error) {
        console.error('Payment verification error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/order/:orderId/history', (req, res) => {
    try {
        const { orderId } = req.params;
        const history = sportStoreBlockchain.getTransactionsByOrderId(orderId);

        res.json({
            success: true,
            orderId,
            history,
            totalEvents: history.length,
        });
    } catch (error) {
        console.error('Order history error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/chain', (req, res) => {
    try {
        res.json({
            success: true,
            chain: sportStoreBlockchain.chain,
            length: sportStoreBlockchain.chain.length,
            isValid: sportStoreBlockchain.isChainValid(),
        });
    } catch (error) {
        console.error('Chain retrieval error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/validate', (req, res) => {
    try {
        const isValid = sportStoreBlockchain.isChainValid();
        res.json({
            success: true,
            isValid,
            message: isValid ? 'Blockchain is valid' : 'Blockchain has been tampered with',
            chainLength: sportStoreBlockchain.chain.length,
        });
    } catch (error) {
        console.error('Validation error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/stats', (req, res) => {
    try {
        let totalTransactions = 0;
        for (const block of sportStoreBlockchain.chain) {
            totalTransactions += block.transactions.length;
        }

        res.json({
            success: true,
            stats: {
                totalBlocks: sportStoreBlockchain.chain.length,
                totalTransactions,
                difficulty: sportStoreBlockchain.difficulty,
                isValid: sportStoreBlockchain.isChainValid(),
                latestBlockHash: sportStoreBlockchain.getLatestBlock().hash,
            },
        });
    } catch (error) {
        console.error('Stats error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// ========== SMART CONTRACT ROUTES ==========

app.post('/api/v1/blockchain/contract/deploy', async (req, res) => {
    try {
        const { contractName, constructorArgs, fromAddress, privateKey, rpcUrl } = req.body;

        if (!contractName || !fromAddress || !privateKey) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields: contractName, fromAddress, privateKey',
            });
        }

        // Set RPC provider if specified
        if (rpcUrl) {
            web3Service.setProvider(rpcUrl);
        }

        // Deploy contract
        const result = await web3Service.deployContract(
            contractName,
            constructorArgs || [],
            fromAddress,
            privateKey
        );

        res.json({
            success: true,
            message: 'Contract deployed successfully',
            ...result,
        });
    } catch (error) {
        console.error('Contract deployment error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.post('/api/v1/blockchain/contract/call', async (req, res) => {
    try {
        const { contractAddress, contractName, methodName, params } = req.body;

        if (!contractAddress || !contractName || !methodName) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields: contractAddress, contractName, methodName',
            });
        }

        // Get contract ABI
        const { abi } = getContract(contractName);

        // Call method
        const result = await web3Service.callContractMethod(
            contractAddress,
            abi,
            methodName,
            params || []
        );

        res.json({
            success: true,
            result,
        });
    } catch (error) {
        console.error('Contract call error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.post('/api/v1/blockchain/contract/send', async (req, res) => {
    try {
        const {
            contractAddress,
            contractName,
            methodName,
            params,
            fromAddress,
            privateKey,
            value,
        } = req.body;

        if (!contractAddress || !contractName || !methodName || !fromAddress || !privateKey) {
            return res.status(400).json({
                success: false,
                error: 'Missing required fields',
            });
        }

        // Get contract ABI
        const { abi } = getContract(contractName);

        // Send transaction
        const result = await web3Service.sendContractTransaction(
            contractAddress,
            abi,
            methodName,
            params || [],
            fromAddress,
            privateKey,
            value || '0'
        );

        res.json({
            success: true,
            message: 'Transaction sent successfully',
            ...result,
        });
    } catch (error) {
        console.error('Contract transaction error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.post('/api/v1/blockchain/account/create', (req, res) => {
    try {
        const account = web3Service.createAccount();
        res.json({
            success: true,
            account,
            warning: 'Store private key securely - it cannot be recovered!',
        });
    } catch (error) {
        console.error('Account creation error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/account/:address/balance', async (req, res) => {
    try {
        const { address } = req.params;
        const balance = await web3Service.getBalance(address);
        res.json({
            success: true,
            address,
            balance,
            unit: 'ETH',
        });
    } catch (error) {
        console.error('Balance error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/network/status', async (req, res) => {
    try {
        const isConnected = await web3Service.isConnected();
        const networkId = isConnected ? await web3Service.getNetworkId() : null;
        const blockNumber = isConnected ? await web3Service.getBlockNumber() : null;

        res.json({
            success: true,
            isConnected,
            networkId: networkId ? networkId.toString() : null,
            blockNumber: blockNumber ? blockNumber.toString() : null,
            rpcUrl: web3Service.rpcUrl,
        });
    } catch (error) {
        console.error('Network status error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

app.get('/api/v1/blockchain/contract/:name/abi', (req, res) => {
    try {
        const { name } = req.params;
        const { abi, bytecode } = getContract(name);
        res.json({
            success: true,
            contractName: name,
            abi,
            bytecodeSize: bytecode.length,
        });
    } catch (error) {
        console.error('ABI retrieval error:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
    console.log(`🔗 Blockchain Service running on port ${PORT}`);
    console.log(`✅ Genesis block created: ${sportStoreBlockchain.chain[0].hash}`);
    console.log(`🔐 Smart contracts ready: LoyaltyToken, ProductNFT, PaymentEscrow`);
});
