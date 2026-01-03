package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Order;
import model.Product;
import service.CartService;

/**
 * =============================================================================
 * CART CONTROLLER - FXML CONTROLLER FOR CART VIEW
 * =============================================================================
 * 
 * Handles the Shopping Cart screen logic.
 * Displays cart items, calculates total, and handles checkout.
 * 
 * =============================================================================
 */
public class CartController {

    @FXML
    private ListView<String> cartListView;

    @FXML
    private Label totalLabel;

    private CartService cartService;
    private ObservableList<String> cartDisplayList;
    private NavigationHandler navigationHandler;
    private OrderController orderController;

    @FXML
    public void initialize() {
        cartService = SharedServices.getCartService();
        cartDisplayList = FXCollections.observableArrayList();
        cartListView.setItems(cartDisplayList);
    }

    @FXML
    private void handleBackToProducts() {
        if (navigationHandler != null) {
            navigationHandler.showProductView();
        }
    }

    @FXML
    private void handleClearCart() {
        cartService.clearCart();
        updateCartDisplay();
    }

    @FXML
    private void handleCheckout() {
        if (cartService.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty!");
            alert.showAndWait();
            return;
        }

        // Create order from cart items
        Order order = new Order("Customer");
        order.addAllItems(cartService.getCartItemsList());

        // Pass order to order controller
        if (orderController != null) {
            orderController.setCurrentOrder(order);
            orderController.processOrder();
        }

        // Navigate to order view
        if (navigationHandler != null) {
            navigationHandler.showOrderView();
        }
    }

    public void updateCartDisplay() {
        cartDisplayList.clear();

        for (Product product : cartService.getCartItemsList()) {
            int qty = cartService.getQuantity(product.getId());

            cartDisplayList.add(product.toString() + " x " + qty +
                    " = " + String.format("%.2f DH", product.getPrice() * qty));
        }

        totalLabel.setText("Total: " + String.format("%.2f DH", cartService.calculateTotal()));
    }

    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }

    public void setOrderController(OrderController controller) {
        this.orderController = controller;
    }
}
