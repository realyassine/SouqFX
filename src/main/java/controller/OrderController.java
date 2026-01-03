package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Order;
import service.CartService;
import service.FileService;
import util.OrderProcessor;

/**
 * =============================================================================
 * ORDER CONTROLLER - FXML CONTROLLER FOR ORDER VIEW
 * =============================================================================
 * 
 * Handles the Order Processing/Confirmation screen logic.
 * 
 * CONCEPT: PLATFORM.RUNLATER
 * --------------------------
 * JavaFX UI updates MUST happen on the JavaFX Application Thread.
 * When updating UI from a background thread (like order processing),
 * we use Platform.runLater() to schedule the update on the correct thread.
 * 
 * =============================================================================
 */
public class OrderController {

    // -------------------------------------------------------------------------
    // FXML INJECTED FIELDS
    // -------------------------------------------------------------------------

    @FXML
    private Label orderStatusLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea orderDetailsArea;

    // -------------------------------------------------------------------------
    // SERVICES AND DATA
    // -------------------------------------------------------------------------

    private OrderProcessor orderProcessor;
    private FileService fileService;
    private CartService cartService;
    private Order currentOrder;
    private NavigationHandler navigationHandler;
    private ProductController productController;
    private Stage primaryStage;

    // -------------------------------------------------------------------------
    // INITIALIZATION
    // -------------------------------------------------------------------------

    @FXML
    public void initialize() {
        orderProcessor = SharedServices.getOrderProcessor();
        fileService = SharedServices.getFileService();
        cartService = SharedServices.getCartService();

        // Setup callback for order processing events
        setupOrderProcessingCallback();
    }

    /**
     * Setup callback for order processing events.
     * 
     * CONCEPT: CALLBACK PATTERN
     * -------------------------
     * The OrderProcessor runs in a background thread.
     * It notifies us of progress via callbacks.
     * We use Platform.runLater() to safely update the UI.
     */
    private void setupOrderProcessingCallback() {
        orderProcessor.setCallback(new OrderProcessor.OrderProcessingCallback() {
            @Override
            public void onProcessingStarted(int orderId) {
                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    orderStatusLabel.setText("Processing Order #" + orderId + "...");
                    progressBar.setProgress(0);
                });
            }

            @Override
            public void onProcessingProgress(int orderId, int progressPercent) {
                // Update progress bar on JavaFX thread
                Platform.runLater(() -> {
                    progressBar.setProgress(progressPercent / 100.0);
                    orderStatusLabel.setText("Order #" + orderId + " - " + progressPercent + "% complete");
                });
            }

            @Override
            public void onProcessingCompleted(int orderId, boolean success, String message) {
                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    progressBar.setProgress(1.0);
                    orderStatusLabel.setText(success ? "✓ " + message : "✗ " + message);
                    orderStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " +
                            (success ? "green" : "red") + ";");

                    // Update order details and save to file
                    if (currentOrder != null && success) {
                        orderDetailsArea.setText(currentOrder.getPaymentSummary());
                        fileService.saveOrder(currentOrder);
                    }
                });
            }
        });
    }

    // -------------------------------------------------------------------------
    // FXML EVENT HANDLERS
    // -------------------------------------------------------------------------

    /**
     * Handle New Order button click.
     */
    @FXML
    private void handleNewOrder() {
        cartService.clearCart();

        // Update product view cart count
        if (productController != null) {
            productController.updateCartCount();
        }

        // Navigate back to products
        if (navigationHandler != null) {
            navigationHandler.showProductView();
        }
    }

    /**
     * Handle Exit button click.
     */
    @FXML
    private void handleExit() {
        SharedServices.shutdown();

        if (primaryStage != null) {
            primaryStage.close();
        } else {
            Platform.exit();
        }
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Set the current order to process.
     * 
     * @param order The order to process
     */
    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    /**
     * Start processing the current order.
     * Resets the UI and starts background processing.
     */
    public void processOrder() {
        // Reset UI
        progressBar.setProgress(0);
        orderStatusLabel.setText("Processing your order...");
        orderStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        orderDetailsArea.clear();

        // Process order in background thread
        if (currentOrder != null) {
            orderProcessor.processOrderWithRunnable(currentOrder);
        }
    }

    /**
     * Set the navigation handler.
     * 
     * @param handler The navigation handler
     */
    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    /**
     * Set the product controller reference.
     * Needed to update cart count after new order.
     * 
     * @param controller The product controller
     */
    public void setProductController(ProductController controller) {
        this.productController = controller;
    }

    /**
     * Set the primary stage reference.
     * Needed for exit functionality.
     * 
     * @param stage The primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
