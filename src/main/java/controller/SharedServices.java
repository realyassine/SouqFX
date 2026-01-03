package controller;

import service.CartService;
import service.FileService;
import util.OrderProcessor;

/**
 * =============================================================================
 * SHARED SERVICES - SINGLETON PATTERN
 * =============================================================================
 * 
 * CONCEPT: SINGLETON
 * ------------------
 * This class ensures all controllers share the same service instances.
 * This is important for:
 * - CartService: Cart contents must be shared across views
 * - FileService: Single point for file operations
 * - OrderProcessor: Single thread pool for order processing
 * 
 * =============================================================================
 */
public class SharedServices {

    private static CartService cartService;
    private static FileService fileService;
    private static OrderProcessor orderProcessor;

    /**
     * Get the shared CartService instance.
     * 
     * @return The CartService singleton
     */
    public static CartService getCartService() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

    /**
     * Get the shared FileService instance.
     * 
     * @return The FileService singleton
     */
    public static FileService getFileService() {
        if (fileService == null) {
            fileService = new FileService();
        }
        return fileService;
    }

    /**
     * Get the shared OrderProcessor instance.
     * 
     * @return The OrderProcessor singleton
     */
    public static OrderProcessor getOrderProcessor() {
        if (orderProcessor == null) {
            orderProcessor = new OrderProcessor();
        }
        return orderProcessor;
    }

    /**
     * Shutdown all services. Call this when the application closes.
     */
    public static void shutdown() {
        if (orderProcessor != null) {
            orderProcessor.shutdown();
        }
    }
}
