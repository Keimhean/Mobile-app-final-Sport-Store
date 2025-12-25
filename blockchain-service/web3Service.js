const { Web3 } = require('web3');
const { getContract } = require('./compiler');

/**
 * Web3 Service for smart contract deployment and interaction
 */
class Web3Service {
    constructor() {
        // Default to local Ganache or testnet
        this.rpcUrl = process.env.ETHEREUM_RPC_URL || process.env.ETH_RPC_URL || 'http://127.0.0.1:8545';
        this.web3 = new Web3(this.rpcUrl);
        this.deployedContracts = {};
        console.log(`🔗 Web3 connected to: ${this.rpcUrl}`);
    }
    
    /**
     * Set Web3 provider (Ganache, Infura, Alchemy, etc.)
     */
    setProvider(rpcUrl) {
        this.rpcUrl = rpcUrl;
        this.web3 = new Web3(rpcUrl);
    }
    
    /**
     * Get account balance
     */
    async getBalance(address) {
        try {
            const balance = await this.web3.eth.getBalance(address);
            return this.web3.utils.fromWei(balance, 'ether');
        } catch (error) {
            throw new Error(`Failed to get balance: ${error.message}`);
        }
    }
    
    /**
     * Deploy smart contract
     */
    async deployContract(contractName, constructorArgs = [], fromAddress, privateKey) {
        try {
            // Get compiled contract
            const { abi, bytecode } = getContract(contractName);
            
            // Create contract instance
            const contract = new this.web3.eth.Contract(abi);
            
            // Prepare deployment
            const deploy = contract.deploy({
                data: '0x' + bytecode,
                arguments: constructorArgs,
            });
            
            // Estimate gas
            const gas = await deploy.estimateGas({ from: fromAddress });
            const gasPrice = await this.web3.eth.getGasPrice();
            
            // Sign and send transaction
            const tx = {
                from: fromAddress,
                data: deploy.encodeABI(),
                gas: gas.toString(),
                gasPrice: gasPrice.toString(),
            };
            
            const signedTx = await this.web3.eth.accounts.signTransaction(tx, privateKey);
            const receipt = await this.web3.eth.sendSignedTransaction(signedTx.rawTransaction);
            
            // Store deployed contract
            const deployedContract = new this.web3.eth.Contract(abi, receipt.contractAddress);
            this.deployedContracts[contractName] = {
                address: receipt.contractAddress,
                abi,
                instance: deployedContract,
            };
            
            return {
                contractName,
                address: receipt.contractAddress,
                transactionHash: receipt.transactionHash,
                blockNumber: receipt.blockNumber,
                gasUsed: receipt.gasUsed.toString(),
            };
        } catch (error) {
            throw new Error(`Deployment failed: ${error.message}`);
        }
    }
    
    /**
     * Call contract method (read-only)
     */
    async callContractMethod(contractAddress, abi, methodName, params = []) {
        try {
            const contract = new this.web3.eth.Contract(abi, contractAddress);
            const result = await contract.methods[methodName](...params).call();
            return result;
        } catch (error) {
            throw new Error(`Method call failed: ${error.message}`);
        }
    }
    
    /**
     * Send contract transaction (write)
     */
    async sendContractTransaction(
        contractAddress,
        abi,
        methodName,
        params,
        fromAddress,
        privateKey,
        value = '0'
    ) {
        try {
            const contract = new this.web3.eth.Contract(abi, contractAddress);
            const method = contract.methods[methodName](...params);
            
            // Estimate gas
            const gas = await method.estimateGas({ from: fromAddress, value });
            const gasPrice = await this.web3.eth.getGasPrice();
            
            // Prepare transaction
            const tx = {
                from: fromAddress,
                to: contractAddress,
                data: method.encodeABI(),
                gas: gas.toString(),
                gasPrice: gasPrice.toString(),
                value: value,
            };
            
            // Sign and send
            const signedTx = await this.web3.eth.accounts.signTransaction(tx, privateKey);
            const receipt = await this.web3.eth.sendSignedTransaction(signedTx.rawTransaction);
            
            return {
                transactionHash: receipt.transactionHash,
                blockNumber: receipt.blockNumber,
                gasUsed: receipt.gasUsed.toString(),
                success: receipt.status,
            };
        } catch (error) {
            throw new Error(`Transaction failed: ${error.message}`);
        }
    }
    
    /**
     * Get transaction receipt
     */
    async getTransactionReceipt(txHash) {
        try {
            return await this.web3.eth.getTransactionReceipt(txHash);
        } catch (error) {
            throw new Error(`Failed to get receipt: ${error.message}`);
        }
    }
    
    /**
     * Create new account
     */
    createAccount() {
        const account = this.web3.eth.accounts.create();
        return {
            address: account.address,
            privateKey: account.privateKey,
        };
    }
    
    /**
     * Get contract instance
     */
    getContractInstance(contractName) {
        return this.deployedContracts[contractName] || null;
    }
    
    /**
     * Check if connected to network
     */
    async isConnected() {
        try {
            await this.web3.eth.net.isListening();
            return true;
        } catch {
            return false;
        }
    }
    
    /**
     * Get network ID
     */
    async getNetworkId() {
        return await this.web3.eth.net.getId();
    }
    
    /**
     * Get block number
     */
    async getBlockNumber() {
        return await this.web3.eth.getBlockNumber();
    }
}

// Singleton instance
const web3Service = new Web3Service();

module.exports = web3Service;
