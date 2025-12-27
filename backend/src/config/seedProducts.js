/* eslint-disable no-console */
const mongoose = require('mongoose');
const Product = require('../models/Product');

const products = [
  // Football/Soccer
  {
    name: 'Adidas Predator Football Boots',
    description: 'Professional football boots with exceptional ball control and striking power',
    price: 129.99,
    category: 'Football',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 30,
    sizes: ['38', '39', '40', '41', '42', '43', '44'],
    colors: ['Black', 'White', 'Red'],
    featured: true
  },
  {
    name: 'Nike Mercurial Superfly',
    description: 'Speed boots designed for explosive acceleration on the pitch',
    price: 149.99,
    category: 'Football',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 25,
    sizes: ['38', '39', '40', '41', '42', '43'],
    colors: ['Orange', 'Black', 'Blue']
  },
  {
    name: 'Official FIFA Match Ball',
    description: 'Professional quality match ball used in official competitions',
    price: 89.99,
    category: 'Football',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 50,
    colors: ['White', 'Multi']
  },
  {
    name: 'Puma Future Z Football Shoes',
    description: 'Dynamic fit football boots for ultimate agility',
    price: 119.99,
    category: 'Football',
    brand: 'Puma',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 20,
    sizes: ['39', '40', '41', '42', '43'],
    colors: ['Yellow', 'Black']
  },

  // Basketball
  {
    name: 'Nike Air Jordan 1 Mid',
    description: 'Iconic basketball shoes with legendary style and performance',
    price: 139.99,
    category: 'Basketball',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 40,
    sizes: ['39', '40', '41', '42', '43', '44', '45'],
    colors: ['Red', 'Black', 'White'],
    featured: true
  },
  {
    name: 'Spalding NBA Official Game Ball',
    description: 'Official NBA game basketball for professional play',
    price: 69.99,
    category: 'Basketball',
    brand: 'Spalding',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 35,
    colors: ['Orange']
  },
  {
    name: 'Under Armour Curry Flow 10',
    description: 'Signature basketball shoes engineered for elite performance',
    price: 159.99,
    category: 'Basketball',
    brand: 'Under Armour',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 15,
    sizes: ['40', '41', '42', '43', '44'],
    colors: ['Blue', 'White', 'Black']
  },

  // Running
  {
    name: 'Nike Air Zoom Pegasus 40',
    description: 'Responsive cushioning for everyday running comfort',
    price: 89.99,
    category: 'Running',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 60,
    sizes: ['38', '39', '40', '41', '42', '43', '44'],
    colors: ['Black', 'White', 'Blue', 'Pink'],
    featured: true
  },
  {
    name: 'Adidas Ultraboost 23',
    description: 'Ultimate energy return running shoes for long distances',
    price: 179.99,
    category: 'Running',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 45,
    sizes: ['38', '39', '40', '41', '42', '43'],
    colors: ['Black', 'White', 'Gray']
  },
  {
    name: 'Brooks Ghost 15',
    description: 'Smooth and balanced ride for neutral runners',
    price: 139.99,
    category: 'Running',
    brand: 'Brooks',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 30,
    sizes: ['39', '40', '41', '42', '43'],
    colors: ['Blue', 'Black', 'Red']
  },

  // Tennis
  {
    name: 'Wilson Pro Staff Tennis Racket',
    description: 'Professional tennis racket for precision and control',
    price: 199.99,
    category: 'Tennis',
    brand: 'Wilson',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 25,
    colors: ['Black', 'Red']
  },
  {
    name: 'Nike Court Air Zoom Vapor Pro',
    description: 'Lightweight tennis shoes for quick movements on court',
    price: 129.99,
    category: 'Tennis',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 20,
    sizes: ['38', '39', '40', '41', '42', '43'],
    colors: ['White', 'Blue']
  },
  {
    name: 'Head Tennis Ball Can (4 balls)',
    description: 'Premium tennis balls for tournament play',
    price: 9.99,
    category: 'Tennis',
    brand: 'Head',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 100,
    colors: ['Yellow']
  },

  // Gym & Training
  {
    name: 'Nike Metcon 9 Training Shoes',
    description: 'Versatile training shoes for CrossFit and gym workouts',
    price: 149.99,
    category: 'Gym',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 35,
    sizes: ['38', '39', '40', '41', '42', '43', '44'],
    colors: ['Black', 'Red', 'Gray']
  },
  {
    name: 'Adjustable Dumbbell Set (2-24kg)',
    description: 'Space-saving adjustable dumbbells for home gym',
    price: 299.99,
    category: 'Gym',
    brand: 'Bowflex',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 15,
    featured: true
  },
  {
    name: 'Yoga Mat Premium 6mm',
    description: 'Non-slip eco-friendly yoga mat with carrying strap',
    price: 29.99,
    category: 'Gym',
    brand: 'Manduka',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 80,
    colors: ['Purple', 'Blue', 'Black', 'Green']
  },
  {
    name: 'Resistance Bands Set',
    description: '5-piece resistance band set for strength training',
    price: 24.99,
    category: 'Gym',
    brand: 'TheraBand',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 70,
    colors: ['Multi']
  },

  // Cycling
  {
    name: 'Specialized Road Bike Helmet',
    description: 'Aerodynamic cycling helmet with advanced ventilation',
    price: 89.99,
    category: 'Other',
    brand: 'Specialized',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 40,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'White', 'Red']
  },
  {
    name: 'Cycling Gloves Pro',
    description: 'Padded cycling gloves for comfort on long rides',
    price: 34.99,
    category: 'Other',
    brand: 'Pearl Izumi',
    imageUrl: 'https://via.placeholder.com/300',
    stock: 50,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'Blue']
  }
];

const seedProducts = async () => {
  try {
    // Connect to MongoDB
    await mongoose.connect(process.env.MONGO_URI || 'mongodb://mongodb:27017/sports_store');
    console.log('📦 MongoDB Connected for seeding');

    // Clear existing products
    await Product.deleteMany({});
    console.log('🗑️  Cleared existing products');

    // Insert new products
    await Product.insertMany(products);
    console.log(`✅ Successfully seeded ${products.length} products`);

    process.exit(0);
  } catch (error) {
    console.error('❌ Error seeding products:', error);
    process.exit(1);
  }
};

seedProducts();
