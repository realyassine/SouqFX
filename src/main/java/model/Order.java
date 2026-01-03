package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Order implements Payable {

    private int orderId;
    private LocalDateTime orderDate;
    private List<Product> items; 
    private boolean paid;
    private String customerName;

    // Counter IDs
    private static int orderCounter = 1000;

    
    public Order(String customerName) {
        this.orderId = ++orderCounter; 
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.paid = false;
        this.customerName = customerName;
    }

    
    public Order(int orderId, String customerName, LocalDateTime orderDate, boolean paid) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.paid = paid;
        this.items = new ArrayList<>();
    }

    public void addItem(Product product) {
        items.add(product); 
    }

    public void removeItem(Product product) {
        items.remove(product);
    }

   
    public void addAllItems(List<Product> products) {
        items.addAll(products);
    }

   
    @Override
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(Product::getPrice) 
                .sum();
    }

  
    @Override
    public boolean processPayment() {
        if (items.isEmpty()) {
            System.out.println("Cannot process payment: Order is empty!");
            return false;
        }
        this.paid = true;
        System.out.println("Payment processed successfully for Order #" + orderId);
        return true;
    }

    /**
     * INTERFACE METHOD: Get payment summary.
     * 
     * CONCEPT: POLYMORPHISM IN ACTION
     * -------------------------------
     * When we call item.toString() in the loop, the ACTUAL type's
     * toString() is called (Electronics.toString() or Clothing.toString())
     * even though 'item' is declared as Product type.
     */
    @Override
    public String getPaymentSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("========== ORDER SUMMARY ==========\n");
        summary.append("Order ID: ").append(orderId).append("\n");
        summary.append("Customer: ").append(customerName).append("\n");
        summary.append("Date: ").append(orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        summary.append("-----------------------------------\n");
        summary.append("Items:\n");

        // POLYMORPHISM: Each item's toString() is called based on actual type
        for (Product item : items) {
            summary.append("  - ").append(item.toString()).append("\n");
        }

        summary.append("-----------------------------------\n");
        summary.append("Total: ").append(String.format("%.2f", calculateTotal())).append(" DH\n");
        summary.append("Status: ").append(paid ? "PAID" : "PENDING").append("\n");
        summary.append("===================================\n");

        return summary.toString();
    }

  
    public int getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<Product> getItems() {
        return new ArrayList<>(items); 
    }

    public boolean isPaid() {
        return paid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getItemCount() {
        return items.size();
    }

  
    public String getFormattedDate() {
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " - " + customerName + " - $" +
                String.format("%.2f", calculateTotal()) + " - " +
                (paid ? "PAID" : "PENDING");
    }
}
