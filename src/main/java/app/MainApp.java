package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.*;

import java.io.IOException;

/**
 * =============================================================================
 * MAIN APPLICATION - JAVAFX ENTRY POINT WITH FXML
 * =============================================================================
 * 
 * CONCEPT: JAVAFX APPLICATION LIFECYCLE
 * -------------------------------------
 * A JavaFX application extends the Application class.
 * 
 * Lifecycle methods:
 * 1. init() - Called before start(), for initialization (optional)
 * 2. start(Stage) - Main entry point, where you set up the UI (required)
 * 3. stop() - Called when application closes, for cleanup (optional)
 * 
 * The Stage is the main window of your application.
 * 
 * CONCEPT: FXML
 * -------------
 * FXML is an XML-based language for defining JavaFX user interfaces.
 * Benefits:
 * - Separates UI design from application logic
 * - Can be created with visual tools like Scene Builder
 * - Easier to maintain and modify UI
 * 
 * HOW TO RUN:
 * -----------
 * 1. Make sure JavaFX is installed and configured
 * 2. Run with Maven: mvn javafx:run
 * 
 * Or use an IDE like IntelliJ IDEA with JavaFX support.
 * 
 * =============================================================================
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private Scene productScene;
    private Scene cartScene;
    private Scene orderScene;

    private ProductController productController;
    private CartController cartController;
    private OrderController orderController;

    /**
     * Main entry point for the JavaFX application.
     * 
     * @param primaryStage The primary stage (window) for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            // Load FXML views
            loadFXMLViews();

            // Setup navigation between views
            setupNavigation();

            // Configure Stage
            primaryStage.setTitle("Simple E-Commerce - JavaFX FXML Demo");
            primaryStage.setScene(productScene);
            primaryStage.setOnCloseRequest(e -> SharedServices.shutdown());
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load all FXML views and their controllers.
     */
    private void loadFXMLViews() throws IOException {
        // Load Product View
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/fxml/product-view.fxml"));
        Parent productRoot = productLoader.load();
        productController = productLoader.getController();
        productScene = new Scene(productRoot, 600, 500);

        // Load Cart View
        FXMLLoader cartLoader = new FXMLLoader(getClass().getResource("/fxml/cart-view.fxml"));
        Parent cartRoot = cartLoader.load();
        cartController = cartLoader.getController();
        cartScene = new Scene(cartRoot, 600, 500);

        // Load Order View
        FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("/fxml/order-view.fxml"));
        Parent orderRoot = orderLoader.load();
        orderController = orderLoader.getController();
        orderScene = new Scene(orderRoot, 600, 500);
    }

    /**
     * Setup navigation handlers for all controllers.
     */
    private void setupNavigation() {
        // Create navigation handler implementation
        NavigationHandler navigationHandler = new NavigationHandler() {
            @Override
            public void showProductView() {
                productController.updateCartCount();
                primaryStage.setScene(productScene);
            }

            @Override
            public void showCartView() {
                cartController.updateCartDisplay();
                primaryStage.setScene(cartScene);
            }

            @Override
            public void showOrderView() {
                primaryStage.setScene(orderScene);
            }
        };

        // Set navigation handler for all controllers
        productController.setNavigationHandler(navigationHandler);
        cartController.setNavigationHandler(navigationHandler);
        cartController.setOrderController(orderController);
        orderController.setNavigationHandler(navigationHandler);
        orderController.setProductController(productController);
        orderController.setPrimaryStage(primaryStage);
    }

    /**
     * Called when the application is closing.
     * Good place for cleanup operations.
     */
    @Override
    public void stop() {
        System.out.println();
        System.out.println("Application closing...");
        System.out.println("Thank you for using Simple E-Commerce!");
        SharedServices.shutdown();
    }

    /**
     * Main method - launches the JavaFX application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        // This calls start() after setting up the JavaFX runtime
        launch(args);
    }
}
