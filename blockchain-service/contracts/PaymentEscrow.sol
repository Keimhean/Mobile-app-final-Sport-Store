// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title PaymentEscrow
 * @dev Escrow contract for secure payments
 * Holds payment until order is confirmed delivered
 */
contract PaymentEscrow {
    enum PaymentStatus { Pending, Completed, Refunded, Disputed }
    
    struct Payment {
        address buyer;
        address seller;
        uint256 amount;
        string orderId;
        PaymentStatus status;
        uint256 createdAt;
        uint256 releaseTime;
        bool buyerConfirmed;
        bool sellerConfirmed;
    }
    
    // Owner of the contract (platform)
    address public owner;
    
    // Platform fee percentage (e.g., 2.5%)
    uint256 public platformFeePercent = 25; // 2.5% (25/1000)
    
    // Mapping from payment ID to Payment struct
    mapping(bytes32 => Payment) public payments;
    
    // Track active payments by buyer
    mapping(address => bytes32[]) public buyerPayments;
    
    // Track active payments by seller
    mapping(address => bytes32[]) public sellerPayments;
    
    // Events
    event PaymentCreated(
        bytes32 indexed paymentId,
        address indexed buyer,
        address indexed seller,
        uint256 amount,
        string orderId
    );
    event PaymentReleased(bytes32 indexed paymentId, uint256 amount, uint256 platformFee);
    event PaymentRefunded(bytes32 indexed paymentId, uint256 amount);
    event PaymentDisputed(bytes32 indexed paymentId, address indexed initiator);
    event DisputeResolved(bytes32 indexed paymentId, bool refundToBuyer);
    
    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can call this");
        _;
    }
    
    modifier onlyBuyer(bytes32 paymentId) {
        require(msg.sender == payments[paymentId].buyer, "Only buyer can call this");
        _;
    }
    
    modifier onlySeller(bytes32 paymentId) {
        require(msg.sender == payments[paymentId].seller, "Only seller can call this");
        _;
    }
    
    constructor() {
        owner = msg.sender;
    }
    
    /**
     * @dev Create new escrow payment
     * @param seller Address of the seller
     * @param orderId Order ID for tracking
     * @param releaseDelay Time in seconds before automatic release
     */
    function createPayment(
        address seller,
        string memory orderId,
        uint256 releaseDelay
    ) external payable returns (bytes32) {
        require(msg.value > 0, "Payment amount must be positive");
        require(seller != address(0), "Invalid seller address");
        require(seller != msg.sender, "Buyer and seller cannot be the same");
        
        bytes32 paymentId = keccak256(
            abi.encodePacked(msg.sender, seller, orderId, block.timestamp)
        );
        
        require(payments[paymentId].amount == 0, "Payment already exists");
        
        payments[paymentId] = Payment({
            buyer: msg.sender,
            seller: seller,
            amount: msg.value,
            orderId: orderId,
            status: PaymentStatus.Pending,
            createdAt: block.timestamp,
            releaseTime: block.timestamp + releaseDelay,
            buyerConfirmed: false,
            sellerConfirmed: false
        });
        
        buyerPayments[msg.sender].push(paymentId);
        sellerPayments[seller].push(paymentId);
        
        emit PaymentCreated(paymentId, msg.sender, seller, msg.value, orderId);
        
        return paymentId;
    }
    
    /**
     * @dev Buyer confirms delivery
     * @param paymentId Payment identifier
     */
    function confirmDelivery(bytes32 paymentId) external onlyBuyer(paymentId) {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        
        payment.buyerConfirmed = true;
        
        // If both parties confirmed or just buyer confirmed, release payment
        _releasePayment(paymentId);
    }
    
    /**
     * @dev Seller confirms shipment
     * @param paymentId Payment identifier
     */
    function confirmShipment(bytes32 paymentId) external onlySeller(paymentId) {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        
        payment.sellerConfirmed = true;
    }
    
    /**
     * @dev Release payment to seller (internal)
     * @param paymentId Payment identifier
     */
    function _releasePayment(bytes32 paymentId) internal {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        
        payment.status = PaymentStatus.Completed;
        
        // Calculate platform fee
        uint256 platformFee = (payment.amount * platformFeePercent) / 1000;
        uint256 sellerAmount = payment.amount - platformFee;
        
        // Transfer to seller
        payable(payment.seller).transfer(sellerAmount);
        
        // Transfer platform fee to owner
        payable(owner).transfer(platformFee);
        
        emit PaymentReleased(paymentId, sellerAmount, platformFee);
    }
    
    /**
     * @dev Automatic release after delay
     * @param paymentId Payment identifier
     */
    function autoReleasePayment(bytes32 paymentId) external {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        require(block.timestamp >= payment.releaseTime, "Release time not reached");
        
        _releasePayment(paymentId);
    }
    
    /**
     * @dev Initiate dispute
     * @param paymentId Payment identifier
     */
    function initiateDispute(bytes32 paymentId) external {
        Payment storage payment = payments[paymentId];
        require(
            msg.sender == payment.buyer || msg.sender == payment.seller,
            "Not authorized"
        );
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        
        payment.status = PaymentStatus.Disputed;
        
        emit PaymentDisputed(paymentId, msg.sender);
    }
    
    /**
     * @dev Resolve dispute (owner only)
     * @param paymentId Payment identifier
     * @param refundToBuyer True to refund buyer, false to release to seller
     */
    function resolveDispute(bytes32 paymentId, bool refundToBuyer) external onlyOwner {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Disputed, "Payment not disputed");
        
        if (refundToBuyer) {
            payment.status = PaymentStatus.Refunded;
            payable(payment.buyer).transfer(payment.amount);
            emit PaymentRefunded(paymentId, payment.amount);
        } else {
            _releasePayment(paymentId);
        }
        
        emit DisputeResolved(paymentId, refundToBuyer);
    }
    
    /**
     * @dev Refund payment (owner only, emergency)
     * @param paymentId Payment identifier
     */
    function emergencyRefund(bytes32 paymentId) external onlyOwner {
        Payment storage payment = payments[paymentId];
        require(payment.status == PaymentStatus.Pending, "Payment not pending");
        
        payment.status = PaymentStatus.Refunded;
        payable(payment.buyer).transfer(payment.amount);
        
        emit PaymentRefunded(paymentId, payment.amount);
    }
    
    /**
     * @dev Get payment details
     * @param paymentId Payment identifier
     */
    function getPayment(bytes32 paymentId) external view returns (
        address buyer,
        address seller,
        uint256 amount,
        string memory orderId,
        PaymentStatus status,
        uint256 createdAt,
        uint256 releaseTime,
        bool buyerConfirmed,
        bool sellerConfirmed
    ) {
        Payment memory payment = payments[paymentId];
        return (
            payment.buyer,
            payment.seller,
            payment.amount,
            payment.orderId,
            payment.status,
            payment.createdAt,
            payment.releaseTime,
            payment.buyerConfirmed,
            payment.sellerConfirmed
        );
    }
    
    /**
     * @dev Update platform fee (owner only)
     * @param newFeePercent New fee percentage (e.g., 25 = 2.5%)
     */
    function updatePlatformFee(uint256 newFeePercent) external onlyOwner {
        require(newFeePercent <= 100, "Fee too high"); // Max 10%
        platformFeePercent = newFeePercent;
    }
    
    /**
     * @dev Withdraw platform fees (owner only)
     */
    function withdrawFees() external onlyOwner {
        uint256 balance = address(this).balance;
        payable(owner).transfer(balance);
    }
}
