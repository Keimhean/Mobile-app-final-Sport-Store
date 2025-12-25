// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

/**
 * @title SportsStoreLoyaltyToken
 * @dev ERC-20 token for Sports Store loyalty points
 * Customers earn tokens for purchases and can redeem for discounts
 */
contract SportsStoreLoyaltyToken is ERC20, Ownable {
    // Mapping to track customer balances
    mapping(address => uint256) public customerPoints;
    
    // Mapping to track redeemed points
    mapping(address => uint256) public redeemedPoints;
    
    // Points per dollar spent (100 points = $1)
    uint256 public pointsPerDollar = 100;
    
    // Minimum points required for redemption
    uint256 public minRedemptionPoints = 1000;
    
    // Events
    event PointsEarned(address indexed customer, uint256 amount, string orderId);
    event PointsRedeemed(address indexed customer, uint256 amount, string orderId);
    event PointsTransferred(address indexed from, address indexed to, uint256 amount);
    
    constructor() ERC20("SportsStore Loyalty Points", "SSLP") Ownable(msg.sender) {
        // Mint initial supply to owner (10 million tokens)
        _mint(msg.sender, 10000000 * 10 ** decimals());
    }
    
    /**
     * @dev Award loyalty points to customer for purchase
     * @param customer Address of the customer
     * @param purchaseAmount Amount spent in dollars (with 2 decimals)
     * @param orderId Order ID for tracking
     */
    function awardPoints(
        address customer,
        uint256 purchaseAmount,
        string memory orderId
    ) external onlyOwner {
        require(customer != address(0), "Invalid customer address");
        require(purchaseAmount > 0, "Purchase amount must be positive");
        
        uint256 points = purchaseAmount * pointsPerDollar;
        customerPoints[customer] += points;
        
        // Mint new tokens to customer
        _mint(customer, points);
        
        emit PointsEarned(customer, points, orderId);
    }
    
    /**
     * @dev Redeem loyalty points for discount
     * @param amount Number of points to redeem
     * @param orderId Order ID for tracking
     */
    function redeemPoints(uint256 amount, string memory orderId) external {
        require(amount >= minRedemptionPoints, "Below minimum redemption amount");
        require(customerPoints[msg.sender] >= amount, "Insufficient points");
        require(balanceOf(msg.sender) >= amount, "Insufficient token balance");
        
        customerPoints[msg.sender] -= amount;
        redeemedPoints[msg.sender] += amount;
        
        // Burn redeemed tokens
        _burn(msg.sender, amount);
        
        emit PointsRedeemed(msg.sender, amount, orderId);
    }
    
    /**
     * @dev Get customer's available points
     * @param customer Address of the customer
     */
    function getCustomerPoints(address customer) external view returns (uint256) {
        return customerPoints[customer];
    }
    
    /**
     * @dev Get customer's redeemed points history
     * @param customer Address of the customer
     */
    function getRedeemedPoints(address customer) external view returns (uint256) {
        return redeemedPoints[customer];
    }
    
    /**
     * @dev Update points per dollar rate (admin only)
     * @param newRate New rate (100 = 1:1 ratio)
     */
    function updatePointsRate(uint256 newRate) external onlyOwner {
        require(newRate > 0, "Rate must be positive");
        pointsPerDollar = newRate;
    }
    
    /**
     * @dev Update minimum redemption points (admin only)
     * @param newMin New minimum points
     */
    function updateMinRedemption(uint256 newMin) external onlyOwner {
        require(newMin > 0, "Minimum must be positive");
        minRedemptionPoints = newMin;
    }
    
    /**
     * @dev Transfer points between customers
     * @param to Recipient address
     * @param amount Number of points to transfer
     */
    function transferPoints(address to, uint256 amount) external {
        require(to != address(0), "Invalid recipient");
        require(customerPoints[msg.sender] >= amount, "Insufficient points");
        
        customerPoints[msg.sender] -= amount;
        customerPoints[to] += amount;
        
        transfer(to, amount);
        
        emit PointsTransferred(msg.sender, to, amount);
    }
}
