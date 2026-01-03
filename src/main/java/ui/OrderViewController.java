package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Order;
import service.CartService;
import service.FileService;
import util.OrderProcessor;

/**
 * Controller for the order confirmation view (FXML-based).
 */
public class OrderViewController {

    @FXML
    private Label orderStatusLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea orderDetailsArea;

    private OrderProcessor orderProcessor;
    private FileService fileService;
    private CartService cartService;
    private Order currentOrder;
    private MainViewController.NavigationHandler navigationHandler;
    private MainViewController mainViewController;
    private Stage primaryStage;

    @FXML
    public void initialize() {
        orderProcessor = SharedServices.getOrderProcessor();
        fileService = SharedServices.getFileService();
        cartService = SharedServices.getCartService();

        setupOrderProcessingCallback();
    }

    private void setupOrderProcessingCallback() {
        orderProcessor.setCallback(new OrderProcessor.OrderProcessingCallback() {
            @Override
            public void onProcessingStarted(int orderId) {
                Platform.runLater(() -> {
                    orderStatusLabel.setText("Processing Order #" + orderId + "...");
                    progressBar.setProgress(0);
                });
            }

            @Override
            public void onProcessingProgress(int orderId, int progressPercent) {
                Platform.runLater(() -> {
                    progressBar.setProgress(progressPercent / 100.0);
                    orderStatusLabel.setText("Order #" + orderId + " - " + progressPercent + "% complete");
                });
            }

            @Override
            public void onProcessingCompleted(int orderId, boolean success, String message) {
                Platform.runLater(() -> {
                    progressBar.setProgress(1.0);
                    orderStatusLabel.setText(success ? "✓ " + message : "✗ " + message);
                    orderStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " +
                            (success ? "green" : "red") + ";");

                    if (currentOrder != null && success) {
                        orderDetailsArea.setText(currentOrder.getPaymentSummary());
                        fileService.saveOrder(currentOrder);
                    }
                });
            }
        });
    }

    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    public void processOrder() {
        progressBar.setProgress(0);
        orderStatusLabel.setText("Processing your order...");
        orderStatusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        orderDetailsArea.clear();

        if (currentOrder != null) {
            orderProcessor.processOrderWithRunnable(currentOrder);
        }
    }

    @FXML
    private void handleNewOrder() {
        cartService.clearCart();
        if (mainViewController != null) {
            mainViewController.updateCartCount();
        }
        if (navigationHandler != null) {
            navigationHandler.showProductView();
        }
    }

    @FXML
    private void handleExit() {
        orderProcessor.shutdown();
        if (primaryStage != null) {
            primaryStage.close();
        } else {
            Platform.exit();
        }
    }

    public void setNavigationHandler(MainViewController.NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    public void setMainViewController(MainViewController controller) {
        this.mainViewController = controller;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
