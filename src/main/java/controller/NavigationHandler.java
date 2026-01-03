package controller;

/**
 * =============================================================================
 * NAVIGATION HANDLER - INTERFACE FOR VIEW NAVIGATION
 * =============================================================================
 * 
 * CONCEPT: INTERFACE
 * ------------------
 * This interface defines the contract for navigation between views.
 * The MainApp implements this interface and passes it to controllers.
 * This allows controllers to navigate without knowing about each other.
 * 
 * =============================================================================
 */
public interface NavigationHandler {

    /**
     * Navigate to the Product Catalog view.
     */
    void showProductView();

    /**
     * Navigate to the Shopping Cart view.
     */
    void showCartView();

    /**
     * Navigate to the Order Processing view.
     */
    void showOrderView();
}
