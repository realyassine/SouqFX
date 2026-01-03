package service;

import model.Product;
import model.Electronics;
import model.Clothing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * CART SERVICE - MANAGES THE SHOPPING CART
 * =============================================================================
 * 
 * CONCEPT: SERVICE CLASS
 * ----------------------
 * A service class contains business logic and operations.
 * It separates the "what to do" from the "how it looks" (UI).
 * 
 * CONCEPT: COLLECTIONS - MAP
 * --------------------------
 * We use a Map<Integer, Product> where:
 * - Key = Product ID (Integer)
 * - Value = Product object
 * 
 * Map is useful when you need fast lookup by a key.
 * 
 * CONCEPT: STREAM API
 * -------------------
 * This class demonstrates various Stream operations:
 * - Filtering
 * - Mapping
 * - Reducing (sum)
 * - Collecting
 * 
 * =============================================================================
 */
public class CartService {

    // -------------------------------------------------------------------------
    // ATTRIBUTES
    // -------------------------------------------------------------------------

    /**
     * CONCEPT: MAP COLLECTION
     * -----------------------
     * HashMap provides O(1) average time for get/put operations.
     * We map Product ID to Product object for fast lookup.
     */
    private Map<Integer, Product> cartItems;

    /**
     * Stores the quantity of each product in the cart.
     * Key = Product ID, Value = Quantity
     */
    private Map<Integer, Integer> quantities;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    public CartService() {
        // Initialize empty maps
        this.cartItems = new HashMap<>();
        this.quantities = new HashMap<>();
    }

    // -------------------------------------------------------------------------
    // CART OPERATIONS
    // -------------------------------------------------------------------------

    /**
     * Add a product to the cart.
     * 
     * CONCEPT: UPCASTING
     * ------------------
     * The parameter is Product type, but we can pass Electronics or Clothing.
     * The object is implicitly upcasted to Product.
     * 
     * @param product The product to add (Electronics or Clothing)
     */
    public void addToCart(Product product) {
        int productId = product.getId();

        if (cartItems.containsKey(productId)) {
            // Product already in cart, increase quantity
            quantities.put(productId, quantities.get(productId) + 1);
        } else {
            // New product, add to cart
            cartItems.put(productId, product);
            quantities.put(productId, 1);
        }

        System.out.println("Added to cart: " + product.getName());
    }

    /**
     * Remove a product from the cart completely.
     * 
     * @param productId The ID of the product to remove
     */
    public void removeFromCart(int productId) {
        if (cartItems.containsKey(productId)) {
            Product removed = cartItems.remove(productId);
            quantities.remove(productId);
            System.out.println("Removed from cart: " + removed.getName());
        }
    }

    /**
     * Decrease quantity of a product by 1.
     * If quantity becomes 0, remove the product.
     * 
     * @param productId The ID of the product
     */
    public void decreaseQuantity(int productId) {
        if (quantities.containsKey(productId)) {
            int currentQty = quantities.get(productId);
            if (currentQty > 1) {
                quantities.put(productId, currentQty - 1);
            } else {
                removeFromCart(productId);
            }
        }
    }

    /**
     * Clear all items from the cart.
     */
    public void clearCart() {
        cartItems.clear();
        quantities.clear();
        System.out.println("Cart cleared.");
    }

    // -------------------------------------------------------------------------
    // STREAM API DEMONSTRATIONS
    // -------------------------------------------------------------------------

    /**
     * Calculate the total price of all items in the cart.
     * 
     * CONCEPT: STREAM API - mapToDouble and sum
     * -----------------------------------------
     * 1. cartItems.values().stream() - Create a stream from cart products
     * 2. mapToDouble(...) - Transform each product to its total price (price *
     * quantity)
     * 3. sum() - Add all the prices together
     */
    public double calculateTotal() {
        return cartItems.values().stream()
                .mapToDouble(product -> product.getPrice() * quantities.get(product.getId()))
                .sum();
    }

    /**
     * Get all Electronics products in the cart.
     * 
     * CONCEPT: STREAM API - filter
     * ----------------------------
     * filter() keeps only elements that match the condition.
     * 
     * CONCEPT: DOWNCASTING with instanceof
     * ------------------------------------
     * After filtering, we can safely downcast Product to Electronics
     * because we've verified the type with instanceof.
     */
    public List<Electronics> getElectronicsItems() {
        return cartItems.values().stream()
                .filter(product -> product instanceof Electronics) // Check type
                .map(product -> (Electronics) product) // DOWNCAST safely
                .collect(Collectors.toList());
    }

    /**
     * Get all Clothing products in the cart.
     * 
     * Same concept as above, but for Clothing.
     */
    public List<Clothing> getClothingItems() {
        return cartItems.values().stream()
                .filter(product -> product instanceof Clothing)
                .map(product -> (Clothing) product)
                .collect(Collectors.toList());
    }

    /**
     * Search for products by name (case-insensitive).
     * 
     * CONCEPT: STREAM API - filter with String methods
     * ------------------------------------------------
     * We use toLowerCase() for case-insensitive search.
     */
    public List<Product> searchByName(String searchTerm) {
        return cartItems.values().stream()
                .filter(product -> product.getName().toLowerCase()
                        .contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get products within a price range.
     * 
     * CONCEPT: STREAM API - multiple conditions in filter
     */
    public List<Product> getProductsInPriceRange(double minPrice, double maxPrice) {
        return cartItems.values().stream()
                .filter(product -> product.getPrice() >= minPrice &&
                        product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Get cart items as a list.
     * 
     * CONCEPT: Converting Map values to List
     */
    public List<Product> getCartItemsList() {
        return new ArrayList<>(cartItems.values());
    }

    /**
     * Get quantity for a specific product.
     * 
     * @param productId The product ID
     * @return The quantity, or 0 if not in cart
     */
    public int getQuantity(int productId) {
        return quantities.getOrDefault(productId, 0);
    }

    /**
     * Get total number of items in cart (counting quantities).
     */
    public int getTotalItemCount() {
        return quantities.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Check if cart is empty.
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Get the cart items map (for UI display).
     */
    public Map<Integer, Product> getCartItems() {
        return new HashMap<>(cartItems); // Return a copy
    }

    /**
     * Get quantities map.
     */
    public Map<Integer, Integer> getQuantities() {
        return new HashMap<>(quantities);
    }

    /**
     * Display cart contents.
     * 
     * CONCEPT: POLYMORPHISM
     * ---------------------
     * When we call product.toString(), the actual type's method is called.
     */
    public void displayCart() {
        System.out.println("\n========== SHOPPING CART ==========");
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (Product product : cartItems.values()) {
                int qty = quantities.get(product.getId());
                // POLYMORPHISM: The correct toString() is called
                System.out.println(product.toString() + " x " + qty);
            }
            System.out.println("-----------------------------------");
            System.out.println("Total: " + String.format("%.2f", calculateTotal()) + " DH");
        }
        System.out.println("====================================\n");
    }
}
