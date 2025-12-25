const fs = require('fs');
const path = require('path');
const solc = require('solc');

/**
 * Compile Solidity contracts
 */
function compileContract(contractName) {
    const contractPath = path.resolve(__dirname, 'contracts', `${contractName}.sol`);
    const source = fs.readFileSync(contractPath, 'utf8');
    
    // Prepare compiler input
    const input = {
        language: 'Solidity',
        sources: {
            [`${contractName}.sol`]: {
                content: source,
            },
        },
        settings: {
            outputSelection: {
                '*': {
                    '*': ['abi', 'evm.bytecode'],
                },
            },
            optimizer: {
                enabled: true,
                runs: 200,
            },
        },
    };
    
    // Find imports (OpenZeppelin contracts)
    function findImports(importPath) {
        try {
            const nodeModulesPath = path.resolve(__dirname, 'node_modules', importPath);
            const contents = fs.readFileSync(nodeModulesPath, 'utf8');
            return { contents };
        } catch (error) {
            return { error: 'File not found' };
        }
    }
    
    // Compile
    const output = JSON.parse(
        solc.compile(JSON.stringify(input), { import: findImports })
    );
    
    // Check for errors
    if (output.errors) {
        console.log('Compilation errors/warnings:', JSON.stringify(output.errors, null, 2));
        const errors = output.errors.filter(error => error.severity === 'error');
        if (errors.length > 0) {
            throw new Error(JSON.stringify(errors, null, 2));
        }
    }
    
    // Debug: log output structure
    console.log('Output contracts:', Object.keys(output.contracts || {}));
    if (output.contracts && output.contracts[`${contractName}.sol`]) {
        console.log('Contract keys:', Object.keys(output.contracts[`${contractName}.sol`]));
    }
    
    // Extract contract data - get the first (and typically only) contract from the file
    const contractFile = output.contracts[`${contractName}.sol`];
    if (!contractFile) {
        throw new Error(`Contract file ${contractName}.sol not found in compilation output`);
    }
    
    // Get the first contract name from the file (usually the main contract)
    const actualContractName = Object.keys(contractFile)[0];
    const contract = contractFile[actualContractName];
    
    if (!contract) {
        throw new Error(`No contract found in ${contractName}.sol`);
    }
    
    return {
        abi: contract.abi,
        bytecode: contract.evm.bytecode.object,
        contractName: actualContractName,
    };
}

/**
 * Get compiled contract
 */
function getContract(contractName) {
    try {
        return compileContract(contractName);
    } catch (error) {
        console.error(`Error compiling ${contractName}:`, error.message);
        throw error;
    }
}

module.exports = {
    compileContract,
    getContract,
};
