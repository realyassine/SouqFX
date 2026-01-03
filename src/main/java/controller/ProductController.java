package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.*;
import service.CartService;
import service.FileService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * PRODUCT CONTROLLER - FXML CONTROLLER FOR PRODUCT VIEW
 * =============================================================================
 * 
 * CONCEPT: FXML CONTROLLER
 * ------------------------
 * A controller class handles the logic for an FXML view.
 * 
 * Key annotations:
 * - @FXML: Marks fields/methods to be injected/called by FXMLLoader
 * - Initializable: Interface with initialize() method called after FXML loading
 * 
 * Naming conventions:
 * - fx:id="searchField" → @FXML private TextField searchField;
 * - onAction="#handleSearch" → @FXML private void handleSearch()
 * 
 * =============================================================================
 */
public class ProductController {

    // -------------------------------------------------------------------------
    // FXML INJECTED FIELDS
    // -------------------------------------------------------------------------

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private Label cartCountLabel;

    // -------------------------------------------------------------------------
    // SERVICES AND DATA
    // -------------------------------------------------------------------------

    private CartService cartService;
    private FileService fileService;
    private List<Product> allProducts;
    private ObservableList<Product> productList;
    private NavigationHandler navigationHandler;

  
    @FXML
    public void initialize() {
        // Get shared services
        cartService = SharedServices.getCartService();
        fileService = SharedServices.getFileService();

        loadProducts();

        filterComboBox.getItems().addAll("All Products", "Electronics", "Clothing");
        filterComboBox.setValue("All Products");
        filterComboBox.setOnAction(e -> filterProducts());

        setupProductListView();

        updateCartCount();
    }

    /**
     * Load products from file or create sample data.
     */
    private void loadProducts() {
        allProducts = fileService.loadProductsNIO();

        if (allProducts.isEmpty()) {
            System.out.println("No products found. Creating sample data...");
            fileService.createSampleProductData();
            allProducts = fileService.loadProductsNIO();
        }

        productList = FXCollections.observableArrayList(allProducts);
        productListView.setItems(productList);
    }

    /**
     * Setup custom cell factory for the product ListView.
     * 
     * CONCEPT: CELL FACTORY
     * ---------------------
     * A cell factory creates custom cells for ListView items.
     * Each cell can have complex layouts with multiple controls.
     */
    private void setupProductListView() {
        productListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create custom cell layout
                    HBox cellBox = new HBox(10);
                    cellBox.setAlignment(Pos.CENTER_LEFT);

                    // Product info section
                    VBox infoBox = new VBox(2);
                    Label nameLabel = new Label(product.getName());
                    nameLabel.setStyle("-fx-font-weight: bold;");

                    // POLYMORPHISM: getDescription() returns type-specific description
                    Label descLabel = new Label(product.getDescription());
                    descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

                    infoBox.getChildren().addAll(nameLabel, descLabel);
                    HBox.setHgrow(infoBox, Priority.ALWAYS);

                    // Price label (Moroccan Dirham)
                    Label priceLabel = new Label(String.format("%.2f DH", product.getPrice()));
                    priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: green;");

                    // Add to Cart button
                    Button addButton = new Button("Add to Cart");
                    addButton.setOnAction(e -> addToCart(product));

                    cellBox.getChildren().addAll(infoBox, priceLabel, addButton);
                    setGraphic(cellBox);
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // FXML EVENT HANDLERS
    // -------------------------------------------------------------------------

    /**
     * Handle search button click.
     */
    @FXML
    private void handleSearch() {
        filterProducts();
    }

    /**
     * Handle reset button click.
     */
    @FXML
    private void handleReset() {
        searchField.clear();
        filterComboBox.setValue("All Products");
        productList.setAll(allProducts);
    }

    /**
     * Handle View Cart button click.
     */
    @FXML
    private void handleViewCart() {
        if (navigationHandler != null) {
            navigationHandler.showCartView();
        }
    }

    // -------------------------------------------------------------------------
    // ACTIONS
    // -------------------------------------------------------------------------

    /**
     * Add a product to the cart.
     * 
     * @param product The product to add
     */
    private void addToCart(Product product) {
        cartService.addToCart(product);
        updateCartCount();

        // Show feedback alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Cart");
        alert.setHeaderText(null);
        alert.setContentText(product.getName() + " added to cart!");
        alert.showAndWait();
    }

    /**
     * Filter products based on search and filter criteria.
     * 
     * CONCEPT: STREAM API - FILTERING
     * -------------------------------
     * We use Stream filter() to find products matching the criteria.
     * This is a functional programming approach to data processing.
     */
    private void filterProducts() {
        String searchTerm = searchField.getText().toLowerCase();
        String filterType = filterComboBox.getValue();

        List<Product> filtered = allProducts.stream()
                .filter(product -> {
                    // Filter by type using instanceof
                    if ("Electronics".equals(filterType) && !(product instanceof Electronics)) {
                        return false;
                    }
                    if ("Clothing".equals(filterType) && !(product instanceof Clothing)) {
                        return false;
                    }
                    // Filter by search term
                    if (!searchTerm.isEmpty()) {
                        return product.getName().toLowerCase().contains(searchTerm);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        productList.setAll(filtered);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Update the cart count label.
     */
    public void updateCartCount() {
        int count = cartService.getTotalItemCount();
        cartCountLabel.setText("Cart: " + count + " item" + (count != 1 ? "s" : ""));
    }

    /**
     * Set the navigation handler.
     * 
     * @param handler The navigation handler
     */
    public void setNavigationHandler(NavigationHandler handler) {
        this.navigationHandler = handler;
    }
}
