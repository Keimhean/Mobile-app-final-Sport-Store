// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/utils/Counters.sol";

/**
 * @title ProductAuthenticityNFT
 * @dev NFT for authentic product certificates
 * Each premium product gets a unique NFT proving authenticity
 */
contract ProductAuthenticityNFT is ERC721URIStorage, Ownable {
    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;
    
    struct ProductCertificate {
        string productId;
        string productName;
        string brand;
        uint256 purchasePrice;
        uint256 purchaseDate;
        string orderId;
        address originalOwner;
        bool isAuthentic;
    }
    
    // Mapping from token ID to product certificate
    mapping(uint256 => ProductCertificate) public certificates;
    
    // Mapping from product ID to token ID
    mapping(string => uint256) public productToToken;
    
    // Mapping to track if product already has certificate
    mapping(string => bool) public productHasCertificate;
    
    // Events
    event CertificateIssued(
        uint256 indexed tokenId,
        string productId,
        address indexed owner,
        string orderId
    );
    event CertificateTransferred(
        uint256 indexed tokenId,
        address indexed from,
        address indexed to
    );
    event CertificateRevoked(uint256 indexed tokenId, string reason);
    
    constructor() ERC721("SportsStore Authenticity Certificate", "SSAC") Ownable(msg.sender) {}
    
    /**
     * @dev Issue authenticity certificate for a product
     * @param customer Address of the customer
     * @param productId Unique product identifier
     * @param productName Name of the product
     * @param brand Product brand
     * @param purchasePrice Purchase price in wei
     * @param orderId Order ID for tracking
     * @param tokenURI Metadata URI (IPFS or centralized)
     */
    function issueCertificate(
        address customer,
        string memory productId,
        string memory productName,
        string memory brand,
        uint256 purchasePrice,
        string memory orderId,
        string memory tokenURI
    ) external onlyOwner returns (uint256) {
        require(customer != address(0), "Invalid customer address");
        require(!productHasCertificate[productId], "Certificate already exists");
        
        _tokenIds.increment();
        uint256 newTokenId = _tokenIds.current();
        
        _mint(customer, newTokenId);
        _setTokenURI(newTokenId, tokenURI);
        
        certificates[newTokenId] = ProductCertificate({
            productId: productId,
            productName: productName,
            brand: brand,
            purchasePrice: purchasePrice,
            purchaseDate: block.timestamp,
            orderId: orderId,
            originalOwner: customer,
            isAuthentic: true
        });
        
        productToToken[productId] = newTokenId;
        productHasCertificate[productId] = true;
        
        emit CertificateIssued(newTokenId, productId, customer, orderId);
        
        return newTokenId;
    }
    
    /**
     * @dev Verify product authenticity by product ID
     * @param productId Product identifier
     */
    function verifyProduct(string memory productId) external view returns (
        bool exists,
        bool isAuthentic,
        string memory productName,
        string memory brand,
        address currentOwner
    ) {
        if (!productHasCertificate[productId]) {
            return (false, false, "", "", address(0));
        }
        
        uint256 tokenId = productToToken[productId];
        ProductCertificate memory cert = certificates[tokenId];
        address owner = ownerOf(tokenId);
        
        return (
            true,
            cert.isAuthentic,
            cert.productName,
            cert.brand,
            owner
        );
    }
    
    /**
     * @dev Get certificate details by token ID
     * @param tokenId NFT token ID
     */
    function getCertificate(uint256 tokenId) external view returns (
        string memory productId,
        string memory productName,
        string memory brand,
        uint256 purchasePrice,
        uint256 purchaseDate,
        string memory orderId,
        address originalOwner,
        address currentOwner,
        bool isAuthentic
    ) {
        require(_ownerOf(tokenId) != address(0), "Certificate does not exist");
        
        ProductCertificate memory cert = certificates[tokenId];
        
        return (
            cert.productId,
            cert.productName,
            cert.brand,
            cert.purchasePrice,
            cert.purchaseDate,
            cert.orderId,
            cert.originalOwner,
            ownerOf(tokenId),
            cert.isAuthentic
        );
    }
    
    /**
     * @dev Revoke certificate (mark as inauthentic)
     * @param tokenId NFT token ID
     * @param reason Reason for revocation
     */
    function revokeCertificate(uint256 tokenId, string memory reason) external onlyOwner {
        require(_ownerOf(tokenId) != address(0), "Certificate does not exist");
        
        certificates[tokenId].isAuthentic = false;
        
        emit CertificateRevoked(tokenId, reason);
    }
    
    /**
     * @dev Get all certificates owned by an address
     * @param owner Address of the owner
     */
    function getCertificatesByOwner(address owner) external view returns (uint256[] memory) {
        uint256 totalSupply = _tokenIds.current();
        uint256 ownerBalance = balanceOf(owner);
        uint256[] memory ownedTokens = new uint256[](ownerBalance);
        uint256 currentIndex = 0;
        
        for (uint256 i = 1; i <= totalSupply; i++) {
            if (_ownerOf(i) == owner) {
                ownedTokens[currentIndex] = i;
                currentIndex++;
            }
        }
        
        return ownedTokens;
    }
    
    /**
     * @dev Override transfer to emit custom event
     */
    function _update(address to, uint256 tokenId, address auth) internal virtual override returns (address) {
        address from = _ownerOf(tokenId);
        address previousOwner = super._update(to, tokenId, auth);
        
        if (from != address(0) && to != address(0)) {
            emit CertificateTransferred(tokenId, from, to);
        }
        
        return previousOwner;
    }
    
    /**
     * @dev Get total certificates issued
     */
    function totalCertificates() external view returns (uint256) {
        return _tokenIds.current();
    }
}
