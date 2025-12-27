require('dotenv').config();
const mongoose = require('mongoose');

// Connect to MongoDB
const connectDB = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI || 'mongodb://admin:password123@localhost:27017/sports_store?authSource=admin', {
      useNewUrlParser: true,
      useUnifiedTopology: true
    });
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

const additionalProducts = [
  // Cycling Products
  {
    name: 'Road Bike 21-Speed',
    description: 'Lightweight aluminum road bike with 21-speed transmission',
    price: 399.99,
    category: 'Cycling',
    brand: 'Trek',
    imageUrl: 'https://via.placeholder.com/300?text=Road+Bike',
    stock: 8,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Red', 'Black', 'Blue'],
    ratings: { average: 4.7, count: 38 },
    featured: true,
    isActive: true
  },
  {
    name: 'Mountain Bike',
    description: 'Full suspension mountain bike for off-road adventures',
    price: 549.99,
    category: 'Cycling',
    brand: 'Giant',
    imageUrl: 'https://via.placeholder.com/300?text=Mountain+Bike',
    stock: 6,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'Green', 'Orange'],
    ratings: { average: 4.8, count: 42 },
    featured: true,
    isActive: true
  },
  {
    name: 'Cycling Helmet',
    description: 'Safety-certified cycling helmet with ventilation',
    price: 79.99,
    category: 'Cycling',
    brand: 'Giro',
    imageUrl: 'https://via.placeholder.com/300?text=Cycling+Helmet',
    stock: 20,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['White', 'Black', 'Red'],
    ratings: { average: 4.9, count: 56 },
    featured: false,
    isActive: true
  },
  {
    name: 'Cycling Shorts',
    description: 'Padded cycling shorts for comfort on long rides',
    price: 69.99,
    category: 'Cycling',
    brand: 'Castelli',
    imageUrl: 'https://via.placeholder.com/300?text=Cycling+Shorts',
    stock: 24,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'Blue', 'Red'],
    ratings: { average: 4.6, count: 31 },
    featured: false,
    isActive: true
  },
  {
    name: 'Bike Lock Premium',
    description: 'U-lock with key for secure bike parking',
    price: 49.99,
    category: 'Cycling',
    brand: 'Kryptonite',
    imageUrl: 'https://via.placeholder.com/300?text=Bike+Lock',
    stock: 35,
    sizes: [],
    colors: ['Black'],
    ratings: { average: 4.8, count: 67 },
    featured: false,
    isActive: true
  },
  {
    name: 'Cycling Gloves',
    description: 'Padded gloves with grip for better control',
    price: 34.99,
    category: 'Cycling',
    brand: 'Pearl Izumi',
    imageUrl: 'https://via.placeholder.com/300?text=Cycling+Gloves',
    stock: 28,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'Gray', 'Red'],
    ratings: { average: 4.5, count: 24 },
    featured: false,
    isActive: true
  },

  // Gym & Training Products
  {
    name: 'Adjustable Dumbbells Set',
    description: '20kg adjustable dumbbell pair with stand - perfect for home gym',
    price: 199.99,
    category: 'Gym',
    brand: 'PowerBlocks',
    imageUrl: 'https://via.placeholder.com/300?text=Dumbbells',
    stock: 12,
    sizes: [],
    colors: ['Black', 'Red'],
    ratings: { average: 4.8, count: 73 },
    featured: true,
    isActive: true
  },
  {
    name: 'Yoga Mat Premium',
    description: '6mm non-slip yoga mat with carrying strap',
    price: 44.99,
    category: 'Gym',
    brand: 'Manduka',
    imageUrl: 'https://via.placeholder.com/300?text=Yoga+Mat',
    stock: 32,
    sizes: [],
    colors: ['Black', 'Purple', 'Blue', 'Green'],
    ratings: { average: 4.7, count: 89 },
    featured: false,
    isActive: true
  },
  {
    name: 'Resistance Bands Set',
    description: '5-piece resistance band set with varying resistance levels',
    price: 29.99,
    category: 'Gym',
    brand: 'TheraBand',
    imageUrl: 'https://via.placeholder.com/300?text=Resistance+Bands',
    stock: 40,
    sizes: [],
    colors: ['Multicolor'],
    ratings: { average: 4.6, count: 54 },
    featured: false,
    isActive: true
  },
  {
    name: 'Kettlebell 16kg',
    description: 'Cast iron kettlebell for functional fitness training',
    price: 59.99,
    category: 'Gym',
    brand: 'Rep Fitness',
    imageUrl: 'https://via.placeholder.com/300?text=Kettlebell',
    stock: 18,
    sizes: [],
    colors: ['Black'],
    ratings: { average: 4.7, count: 42 },
    featured: false,
    isActive: true
  },
  {
    name: 'Weight Lifting Belt',
    description: 'Premium leather weight lifting belt for back support',
    price: 64.99,
    category: 'Gym',
    brand: 'Inzer',
    imageUrl: 'https://via.placeholder.com/300?text=Lifting+Belt',
    stock: 16,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'Brown'],
    ratings: { average: 4.8, count: 38 },
    featured: true,
    isActive: true
  },
  {
    name: 'Gym Bag Sports',
    description: 'Large gym duffel bag with compartments and shoe storage',
    price: 79.99,
    category: 'Gym',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Gym+Bag',
    stock: 22,
    sizes: [],
    colors: ['Black', 'Gray', 'Blue'],
    ratings: { average: 4.5, count: 29 },
    featured: false,
    isActive: true
  },
  {
    name: 'Foam Roller',
    description: 'High-density foam roller for muscle recovery and massage',
    price: 39.99,
    category: 'Gym',
    brand: 'TriggerPoint',
    imageUrl: 'https://via.placeholder.com/300?text=Foam+Roller',
    stock: 26,
    sizes: [],
    colors: ['Black', 'Gray'],
    ratings: { average: 4.6, count: 46 },
    featured: false,
    isActive: true
  },

  // Tennis & Racket Sports Products
  {
    name: 'Professional Tennis Racket',
    description: 'Carbon fiber tennis racket with balanced weight distribution',
    price: 179.99,
    category: 'Tennis',
    brand: 'Wilson',
    imageUrl: 'https://via.placeholder.com/300?text=Tennis+Racket',
    stock: 14,
    sizes: [],
    colors: ['Black', 'White', 'Blue'],
    ratings: { average: 4.8, count: 48 },
    featured: true,
    isActive: true
  },
  {
    name: 'Tennis Ball Canister',
    description: 'Pack of 3 professional tennis balls with pressurized can',
    price: 14.99,
    category: 'Tennis',
    brand: 'Wilson',
    imageUrl: 'https://via.placeholder.com/300?text=Tennis+Balls',
    stock: 60,
    sizes: [],
    colors: ['Yellow'],
    ratings: { average: 4.4, count: 35 },
    featured: false,
    isActive: true
  },
  {
    name: 'Tennis Shoes Court',
    description: 'Specialized court shoes for stability and lateral support',
    price: 119.99,
    category: 'Tennis',
    brand: 'Asics',
    imageUrl: 'https://via.placeholder.com/300?text=Tennis+Shoes',
    stock: 19,
    sizes: ['39', '40', '41', '42', '43', '44', '45'],
    colors: ['White', 'Black', 'Blue', 'Yellow'],
    ratings: { average: 4.7, count: 41 },
    featured: false,
    isActive: true
  },
  {
    name: 'Badminton Racket Set',
    description: 'Professional badminton racket with carrying bag',
    price: 84.99,
    category: 'Tennis',
    brand: 'Yonex',
    imageUrl: 'https://via.placeholder.com/300?text=Badminton+Racket',
    stock: 17,
    sizes: [],
    colors: ['Black', 'Blue', 'Red'],
    ratings: { average: 4.6, count: 28 },
    featured: false,
    isActive: true
  },
  {
    name: 'Tennis Polo Shirt',
    description: 'Breathable tennis polo with moisture-wicking technology',
    price: 54.99,
    category: 'Tennis',
    brand: 'Adidas',
    imageUrl: 'https://via.placeholder.com/300?text=Tennis+Polo',
    stock: 28,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['White', 'Black', 'Blue', 'Red'],
    ratings: { average: 4.5, count: 22 },
    featured: false,
    isActive: true
  },
  {
    name: 'Tennis Wristband',
    description: 'Sweat-absorbing wristband pack of 2',
    price: 19.99,
    category: 'Tennis',
    brand: 'Lotto',
    imageUrl: 'https://via.placeholder.com/300?text=Wristband',
    stock: 45,
    sizes: ['One Size'],
    colors: ['White', 'Black', 'Blue'],
    ratings: { average: 4.3, count: 18 },
    featured: false,
    isActive: true
  },

  // Additional Running & Fitness Products
  {
    name: 'Fitness Tracker Watch',
    description: 'Smart fitness tracker with sleep monitoring and calorie counter',
    price: 149.99,
    category: 'Running',
    brand: 'Fitbit',
    imageUrl: 'https://via.placeholder.com/300?text=Fitness+Tracker',
    stock: 15,
    sizes: [],
    colors: ['Black', 'Blue', 'Pink'],
    ratings: { average: 4.6, count: 63 },
    featured: true,
    isActive: true
  },
  {
    name: 'Running Armband Phone Holder',
    description: 'Adjustable armband for secure phone carrying while running',
    price: 24.99,
    category: 'Running',
    brand: 'VUP',
    imageUrl: 'https://via.placeholder.com/300?text=Phone+Armband',
    stock: 32,
    sizes: [],
    colors: ['Black', 'Blue', 'Red'],
    ratings: { average: 4.5, count: 37 },
    featured: false,
    isActive: true
  },
  {
    name: 'Running Headlamp',
    description: 'LED headlamp for running at night with adjustable brightness',
    price: 29.99,
    category: 'Running',
    brand: 'Black Diamond',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Headlamp',
    stock: 24,
    sizes: [],
    colors: ['Black', 'Red'],
    ratings: { average: 4.7, count: 29 },
    featured: false,
    isActive: true
  },
  {
    name: 'Compression Running Jacket',
    description: 'Lightweight compression jacket for weather protection',
    price: 99.99,
    category: 'Running',
    brand: 'Nike',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Jacket',
    stock: 18,
    sizes: ['XS', 'S', 'M', 'L', 'XL', 'XXL'],
    colors: ['Black', 'Blue', 'Gray', 'Green'],
    ratings: { average: 4.6, count: 33 },
    featured: false,
    isActive: true
  },
  {
    name: 'Running Belt with Pockets',
    description: 'Waist belt with 4 pockets for keys, phone and essentials',
    price: 34.99,
    category: 'Running',
    brand: 'Travelon',
    imageUrl: 'https://via.placeholder.com/300?text=Running+Belt',
    stock: 28,
    sizes: ['S', 'M', 'L', 'XL'],
    colors: ['Black', 'Gray', 'Blue'],
    ratings: { average: 4.4, count: 20 },
    featured: false,
    isActive: true
  }
];

const seedAdditionalProducts = async () => {
  try {
    await connectDB();

    // Insert additional products
    const result = await Product.insertMany(additionalProducts);
    console.log(`✅ Successfully added ${result.length} products!`);
    
    console.log('\n📊 Updated Products by Category:');
    const categories = await Product.aggregate([
      { $group: { _id: '$category', count: { $sum: 1 } } },
      { $sort: { _id: 1 } }
    ]);
    
    const totalProducts = categories.reduce((sum, cat) => sum + cat.count, 0);
    categories.forEach(cat => {
      console.log(`  - ${cat._id}: ${cat.count} products`);
    });
    console.log(`\n  Total Products: ${totalProducts}`);

    mongoose.connection.close();
  } catch (error) {
    console.error('Error seeding database:', error);
    process.exit(1);
  }
};

seedAdditionalProducts();
