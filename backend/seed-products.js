require('dotenv').config();
const mongoose = require('mongoose');

// Connect to MongoDB
const connectDB = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI || 'mongodb://admin:password123@localhost:27017/sports_store?authSource=admin');
    console.log('MongoDB connected');
  } catch (error) {
    console.error('Error connecting to MongoDB:', error.message);
    process.exit(1);
  }
};

const productSchema = new mongoose.Schema({
  name: String,
  description: String,
  price: Number,
  category: String,
  brand: String,
  imageUrl: String,
  stock: Number,
  sizes: [String],
  colors: [String],
  ratings: {
    average: { type: Number, default: 0 },
    count: { type: Number, default: 0 }
  },
  featured: { type: Boolean, default: false },
  isActive: { type: Boolean, default: true }
}, { timestamps: true });

const Product = mongoose.model('Product', productSchema);

const products = [
  // Football/Soccer Products
  {
    name: 'Professional Soccer Ball',
    description: 'Official match-quality soccer ball with superior grip and control',
    price: 89.99,
    category: 'Football',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Soccer+Ball',
    stock: 15,
    sizes: [],
    colors: ['Black', 'White', 'Red'],
    ratings: { average: 4.8, count: 45 },
    featured: true,
    isActive: true
  },
  {
    name: 'Soccer Cleats Pro',
    description: 'Lightweight soccer cleats with superior ball control and comfort',
    price: 129.99,
    category: 'Football',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300?text=Soccer+Cleats',
    stock: 20,
    sizes: ['39', '40', '41', '42', '43', '44', '45'],
    colors: ['Black', 'White', 'Blue'],
    ratings: { average: 4.6, count: 32 },
    featured: false,
    isActive: true
  },
  {
    name: 'Football Jersey Set',
    description: 'Professional football jersey with moisture-wicking technology',
    price: 59.99,
    category: 'Football',
    brand: 'Puma',
    imageUrl: 'https://via.placeholder.com/300?text=Football+Jersey',
    stock: 30,
    sizes: ['S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Red', 'Blue', 'Green', 'White'],
    ratings: { average: 4.5, count: 28 },
    featured: true,
    isActive: true
  },
  {
    name: 'Soccer Shin Guards',
    description: 'Protective shin guards with foam padding for maximum comfort',
    price: 34.99,
    category: 'Football',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Shin+Guards',
    stock: 25,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'White'],
    ratings: { average: 4.3, count: 18 },
    featured: false,
    isActive: true
  },
  {
    name: 'Football Goalkeeper Gloves',
    description: 'Premium goalkeeper gloves with latex grip and wrist support',
    price: 79.99,
    category: 'Football',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300?text=Goalie+Gloves',
    stock: 12,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Yellow', 'Black', 'Red', 'Green'],
    ratings: { average: 4.7, count: 22 },
    featured: false,
    isActive: true
  },

  // Basketball Products
  {
    name: 'Professional Basketball',
    description: 'Official NBA-size basketball with superior bounce and control',
    price: 69.99,
    category: 'Basketball',
    brand: 'Spalding',
    imageUrl: 'https://via.placeholder.com/300?text=Basketball',
    stock: 18,
    sizes: [],
    colors: ['Orange', 'Black'],
    ratings: { average: 4.9, count: 52 },
    featured: true,
    isActive: true
  },
  {
    name: 'Basketball High-Top Shoes',
    description: 'High-performance basketball shoes with ankle support and cushioning',
    price: 149.99,
    category: 'Basketball',
    brand: 'Air Jordan',
    imageUrl: 'https://via.placeholder.com/300?text=Basketball+Shoes',
    stock: 22,
    sizes: ['39', '40', '41', '42', '43', '44', '45'],
    colors: ['Red', 'Black', 'White', 'Blue'],
    ratings: { average: 4.7, count: 48 },
    featured: true,
    isActive: true
  },
  {
    name: 'Basketball Jersey',
    description: 'Breathable basketball jersey with mesh panels for ventilation',
    price: 49.99,
    category: 'Basketball',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Basketball+Jersey',
    stock: 35,
    sizes: ['S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Purple', 'Gold', 'Black', 'White', 'Blue'],
    ratings: { average: 4.4, count: 31 },
    featured: false,
    isActive: true
  },
  {
    name: 'Basketball Shorts',
    description: 'Lightweight basketball shorts with side pockets and sweat-wicking',
    price: 39.99,
    category: 'Basketball',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300?text=Basketball+Shorts',
    stock: 28,
    sizes: ['S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'White', 'Red', 'Blue', 'Green'],
    ratings: { average: 4.5, count: 25 },
    featured: false,
    isActive: true
  },
  {
    name: 'Basketball Socks Pack',
    description: 'Pack of 3 professional basketball socks with arch support',
    price: 24.99,
    category: 'Basketball',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Basketball+Socks',
    stock: 40,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'White', 'Gray'],
    ratings: { average: 4.6, count: 19 },
    featured: false,
    isActive: true
  },

  // Running Products
  {
    name: 'Professional Running Shoes',
    description: 'Latest cushioned running shoes with responsive foam technology',
    price: 139.99,
    category: 'Running',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Shoes',
    stock: 25,
    sizes: ['39', '40', '41', '42', '43', '44', '45'],
    colors: ['Black', 'White', 'Blue', 'Red'],
    ratings: { average: 4.8, count: 67 },
    featured: true,
    isActive: true
  },
  {
    name: 'Running Compression Tights',
    description: 'Compression running tights with moisture-wicking fabric',
    price: 89.99,
    category: 'Running',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Tights',
    stock: 20,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'Navy', 'Gray'],
    ratings: { average: 4.5, count: 34 },
    featured: false,
    isActive: true
  },
  {
    name: 'Running T-Shirt',
    description: 'Lightweight running shirt with breathable mesh fabric',
    price: 44.99,
    category: 'Running',
    brand: 'Under Armour',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Shirt',
    stock: 32,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'White', 'Blue', 'Red', 'Gray'],
    ratings: { average: 4.4, count: 28 },
    featured: true,
    isActive: true
  },
  {
    name: 'Running Shorts',
    description: 'Lightweight shorts with built-in brief and reflective details',
    price: 49.99,
    category: 'Running',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Shorts',
    stock: 28,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'White', 'Green'],
    ratings: { average: 4.6, count: 21 },
    featured: false,
    isActive: true
  },
  {
    name: 'Running Socks Set',
    description: 'Pack of 3 moisture-wicking running socks with arch support',
    price: 29.99,
    category: 'Running',
    brand: 'Puma',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Socks',
    stock: 45,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'White', 'Blue'],
    ratings: { average: 4.5, count: 16 },
    featured: false,
    isActive: true
  },
  {
    name: 'Sports Water Bottle',
    description: '750ml hydration bottle with temperature control',
    price: 34.99,
    category: 'Running',
    brand: 'Hydro Flask',
    imageUrl: 'https://via.placeholder.com/300?text=Water+Bottle',
    stock: 50,
    sizes: [],
    colors: ['Black', 'Blue', 'Red', 'Green', 'Pink'],
    ratings: { average: 4.7, count: 43 },
    featured: false,
    isActive: true
  },
  {
    name: 'Running Smartwatch',
    description: 'GPS smartwatch with heart rate monitor and fitness tracking',
    price: 249.99,
    category: 'Running',
    brand: 'Garmin',
    imageUrl: 'https://via.placeholder.com/300?text=Smartwatch',
    stock: 10,
    sizes: [],
    colors: ['Black', 'Silver'],
    ratings: { average: 4.8, count: 55 },
    featured: true,
    isActive: true
  }
];

const seedDatabase = async () => {
  try {
    await connectDB();

    // Clear existing products
    await Product.deleteMany({});
    console.log('Cleared existing products');

    // Insert new products
    const result = await Product.insertMany(products);
    console.log(`✅ Successfully added ${result.length} products!`);

    console.log('\n📊 Products by Category:');
    const categories = await Product.aggregate([
      { $group: { _id: '$category', count: { $sum: 1 } } }
    ]);
    categories.forEach(cat => {
      console.log(`  - ${cat._id}: ${cat.count} products`);
    });

    mongoose.connection.close();
  } catch (error) {
    console.error('Error seeding database:', error);
    process.exit(1);
  }
};

seedDatabase();
