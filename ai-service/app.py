from flask import Flask, jsonify, request
from flask_cors import CORS
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from datetime import datetime
import logging

app = Flask(__name__)
CORS(app)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# In-memory storage for demo (use Redis/MongoDB in production)
product_embeddings = {}
user_interactions = {}
user_profiles = {}

class RecommendationEngine:
    def __init__(self):
        self.product_features = {}
        self.user_history = {}
        
    def generate_product_embedding(self, product):
        """Generate embedding vector for a product"""
        # Simple feature extraction (extend with more features)
        features = []
        
        # Category encoding (one-hot style)
        categories = ['Running', 'Football', 'Basketball', 'Tennis', 'Gym', 'Cycling']
        category_vector = [1.0 if product.get('category', '') == cat else 0.0 for cat in categories]
        features.extend(category_vector)
        
        # Price normalization (0-1 range)
        price = float(product.get('price', 0))
        normalized_price = min(price / 1000, 1.0)
        features.append(normalized_price)
        
        # Brand encoding
        brands = ['Nike', 'Adidas', 'Puma', 'Under Armour', 'Reebok', 'New Balance']
        brand = product.get('brand', 'Other')
        brand_vector = [1.0 if brand == b else 0.0 for b in brands]
        features.extend(brand_vector)
        
        return np.array(features)
    
    def content_based_recommendations(self, product_id, top_n=5):
        """Find similar products using cosine similarity"""
        if product_id not in product_embeddings:
            return []
        
        target_embedding = product_embeddings[product_id]
        similarities = []
        
        for pid, embedding in product_embeddings.items():
            if pid != product_id:
                similarity = cosine_similarity(
                    target_embedding.reshape(1, -1),
                    embedding.reshape(1, -1)
                )[0][0]
                similarities.append((pid, similarity))
        
        # Sort by similarity and return top N
        similarities.sort(key=lambda x: x[1], reverse=True)
        return [pid for pid, _ in similarities[:top_n]]
    
    def collaborative_filtering(self, user_id, top_n=5):
        """Recommend based on similar users' preferences"""
        if user_id not in user_profiles:
            return self.popular_products(top_n)
        
        user_vector = user_profiles[user_id]
        similar_users = []
        
        for uid, profile in user_profiles.items():
            if uid != user_id:
                similarity = cosine_similarity(
                    user_vector.reshape(1, -1),
                    profile.reshape(1, -1)
                )[0][0]
                if similarity > 0.3:  # Threshold
                    similar_users.append(uid)
        
        # Aggregate recommendations from similar users
        recommendations = {}
        for uid in similar_users:
            if uid in user_interactions:
                for product_id in user_interactions[uid]:
                    if product_id not in user_interactions.get(user_id, []):
                        recommendations[product_id] = recommendations.get(product_id, 0) + 1
        
        # Sort and return top N
        sorted_recs = sorted(recommendations.items(), key=lambda x: x[1], reverse=True)
        return [pid for pid, _ in sorted_recs[:top_n]]
    
    def popular_products(self, top_n=5):
        """Fallback: return most popular products"""
        product_counts = {}
        for interactions in user_interactions.values():
            for pid in interactions:
                product_counts[pid] = product_counts.get(pid, 0) + 1
        
        sorted_products = sorted(product_counts.items(), key=lambda x: x[1], reverse=True)
        return [pid for pid, _ in sorted_products[:top_n]]

engine = RecommendationEngine()

@app.route('/health', methods=['GET'])
def health():
    return jsonify({
        'status': 'healthy',
        'service': 'AI Recommendation Service',
        'timestamp': datetime.utcnow().isoformat()
    })

@app.route('/api/v1/ai/recommendations/content/<product_id>', methods=['GET'])
def content_recommendations(product_id):
    """Get content-based recommendations for a product"""
    try:
        # Validate input
        if not product_id or not isinstance(product_id, str) or len(product_id.strip()) == 0:
            return jsonify({'success': False, 'error': 'Invalid product_id'}), 400
        
        product_id = product_id.strip()
        
        # Validate limit parameter
        try:
            top_n = int(request.args.get('limit', 5))
            if top_n < 1 or top_n > 50:
                top_n = 5  # Default to 5 if out of range
        except ValueError:
            top_n = 5
        
        # Check if product exists
        if product_id not in product_embeddings:
            logger.warning(f"Product not found: {product_id}")
            return jsonify({
                'success': True,
                'productId': product_id,
                'recommendations': [],
                'type': 'content-based',
                'count': 0,
                'message': 'Product not yet in system'
            })
        
        recommendations = engine.content_based_recommendations(product_id, top_n)
        
        return jsonify({
            'success': True,
            'productId': product_id,
            'recommendations': recommendations,
            'type': 'content-based',
            'count': len(recommendations)
        })
    except Exception as e:
        logger.error(f"Content recommendation error: {str(e)}")
        return jsonify({'success': False, 'error': 'Failed to generate recommendations'}), 500

@app.route('/api/v1/ai/recommendations/user/<user_id>', methods=['GET'])
def user_recommendations(user_id):
    """Get personalized recommendations for a user"""
    try:
        # Validate input
        if not user_id or not isinstance(user_id, str) or len(user_id.strip()) == 0:
            return jsonify({'success': False, 'error': 'Invalid user_id'}), 400
        
        user_id = user_id.strip()
        
        # Validate limit parameter
        try:
            top_n = int(request.args.get('limit', 5))
            if top_n < 1 or top_n > 50:
                top_n = 5  # Default to 5 if out of range
        except ValueError:
            top_n = 5
        
        recommendations = engine.collaborative_filtering(user_id, top_n)
        
        return jsonify({
            'success': True,
            'userId': user_id,
            'recommendations': recommendations,
            'type': 'collaborative-filtering',
            'count': len(recommendations)
        })
    except Exception as e:
        logger.error(f"User recommendation error: {str(e)}")
        return jsonify({'success': False, 'error': 'Failed to generate recommendations'}), 500

@app.route('/api/v1/ai/train', methods=['POST'])
def train_interaction():
    """Record user interaction for learning"""
    try:
        data = request.json
        
        # Validate request body
        if not data:
            return jsonify({'success': False, 'error': 'Request body is required'}), 400
        
        user_id = data.get('userId')
        product_id = data.get('productId')
        interaction_type = data.get('type', 'view')
        
        # Validate required fields
        if not user_id or not isinstance(user_id, str) or len(user_id.strip()) == 0:
            return jsonify({'success': False, 'error': 'Invalid or missing userId'}), 400
        
        if not product_id or not isinstance(product_id, str) or len(product_id.strip()) == 0:
            return jsonify({'success': False, 'error': 'Invalid or missing productId'}), 400
        
        user_id = user_id.strip()
        product_id = product_id.strip()
        
        # Validate interaction type
        valid_types = ['view', 'purchase', 'cart', 'wishlist']
        if interaction_type not in valid_types:
            interaction_type = 'view'
        
        # Store interaction
        if user_id not in user_interactions:
            user_interactions[user_id] = []
        user_interactions[user_id].append(product_id)
        
        # Update user profile (simple bag-of-products)
        if user_id not in user_profiles:
            user_profiles[user_id] = np.zeros(len(product_embeddings))
        
        if product_id in product_embeddings:
            idx = list(product_embeddings.keys()).index(product_id)
            user_profiles[user_id][idx] += 1.0
        
        logger.info(f"Recorded {interaction_type} interaction: user={user_id}, product={product_id}")
        
        return jsonify({
            'success': True,
            'message': 'Interaction recorded',
            'userId': user_id,
            'productId': product_id,
            'type': interaction_type
        })
    except Exception as e:
        logger.error(f"Training error: {str(e)}")
        return jsonify({'success': False, 'error': 'Failed to record interaction'}), 500

@app.route('/api/v1/ai/embeddings', methods=['POST'])
def update_embeddings():
    """Update product embeddings"""
    try:
        data = request.json
        
        # Validate request body
        if not data:
            return jsonify({'success': False, 'error': 'Request body is required'}), 400
        
        products = data.get('products', [])
        
        # Validate products is a list
        if not isinstance(products, list):
            return jsonify({'success': False, 'error': 'products must be a list'}), 400
        
        if len(products) == 0:
            return jsonify({
                'success': True,
                'message': 'No products to update',
                'totalEmbeddings': len(product_embeddings)
            })
        
        update_count = 0
        for product in products:
            try:
                product_id = product.get('_id') or product.get('id')
                if product_id and isinstance(product_id, str):
                    embedding = engine.generate_product_embedding(product)
                    product_embeddings[product_id] = embedding
                    update_count += 1
            except Exception as product_error:
                logger.warning(f"Failed to process product {product.get('_id')}: {str(product_error)}")
                continue
        
        logger.info(f"Updated embeddings for {update_count}/{len(products)} products")
        
        return jsonify({
            'success': True,
            'message': f'Updated {update_count} product embeddings',
            'totalEmbeddings': len(product_embeddings),
            'processedCount': update_count
        })
    except Exception as e:
        logger.error(f"Embedding update error: {str(e)}")
        return jsonify({'success': False, 'error': 'Failed to update embeddings'}), 500

@app.route('/api/v1/ai/stats', methods=['GET'])
def get_stats():
    """Get AI service statistics"""
    return jsonify({
        'success': True,
        'stats': {
            'totalProducts': len(product_embeddings),
            'totalUsers': len(user_profiles),
            'totalInteractions': sum(len(interactions) for interactions in user_interactions.values())
        }
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
