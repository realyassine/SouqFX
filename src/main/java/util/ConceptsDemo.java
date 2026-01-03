package util;

/**
 * =============================================================================
 * CONCEPTS DEMONSTRATION CLASS
 * =============================================================================
 * 
 * This class provides clear examples of the key Java concepts used in this project.
 * It can be run independently to see the concepts in action without the JavaFX UI.
 * 
 * Run this class to see console output demonstrating:
 * - Inheritance
 * - Polymorphism
 * - Upcasting and Downcasting
 * - Interface implementation
 * - Stream API
 * - Threading basics
 * 
 * =============================================================================
 */

import model.*;
import service.CartService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConceptsDemo {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         JAVA CONCEPTS DEMONSTRATION                          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateInheritance();
        demonstratePolymorphism();
        demonstrateUpcastingDowncasting();
        demonstrateInterface();
        demonstrateCollections();
        demonstrateStreamAPI();
        demonstrateFileIO();
        demonstrateThreads();

        System.out.println("\n✓ All concepts demonstrated successfully!");
    }

    // =========================================================================
    // 1. INHERITANCE DEMONSTRATION
    // =========================================================================

    private static void demonstrateInheritance() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 1. INHERITANCE                                              │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("CONCEPT: Inheritance allows a class to inherit attributes and");
        System.out.println("         methods from another class.");
        System.out.println();
        System.out.println("In this project:");
        System.out.println("  - Product (abstract class) is the PARENT class");
        System.out.println("  - Electronics extends Product (CHILD class)");
        System.out.println("  - Clothing extends Product (CHILD class)");
        System.out.println();

        // Create instances
        Electronics laptop = new Electronics(1, "Laptop", 999.99, "Dell", 24);
        Clothing shirt = new Clothing(2, "T-Shirt", 29.99, "M", "Cotton");

        System.out.println("Created Electronics: " + laptop.getName() + " - $" + laptop.getPrice());
        System.out.println("  → Inherited from Product: id, name, price");
        System.out.println("  → Own attributes: brand=" + laptop.getBrand() + ", warranty=" + laptop.getWarrantyMonths()
                + " months");
        System.out.println();
        System.out.println("Created Clothing: " + shirt.getName() + " - $" + shirt.getPrice());
        System.out.println("  → Inherited from Product: id, name, price");
        System.out.println("  → Own attributes: size=" + shirt.getSize() + ", material=" + shirt.getMaterial());
        System.out.println();
    }

    // =========================================================================
    // 2. POLYMORPHISM DEMONSTRATION
    // =========================================================================

    private static void demonstratePolymorphism() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 2. POLYMORPHISM                                             │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("CONCEPT: Polymorphism means 'many forms'. The same method call");
        System.out.println("         can behave differently based on the actual object type.");
        System.out.println();

        // Create a list of Products (can hold any subtype)
        List<Product> products = new ArrayList<>();
        products.add(new Electronics(1, "Smartphone", 699.99, "Samsung", 12));
        products.add(new Clothing(2, "Jeans", 59.99, "L", "Denim"));
        products.add(new Electronics(3, "Tablet", 449.99, "Apple", 12));

        System.out.println("Calling getDescription() on each Product:");
        System.out.println("(Same method name, different behavior based on actual type)");
        System.out.println();

        for (Product product : products) {
            // POLYMORPHISM: getDescription() is called on Product reference
            // but the actual method called depends on the real object type
            System.out.println("  " + product.getProductType() + ": " + product.getDescription());
        }
        System.out.println();
    }

    // =========================================================================
    // 3. UPCASTING AND DOWNCASTING DEMONSTRATION
    // =========================================================================

    private static void demonstrateUpcastingDowncasting() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 3. UPCASTING AND DOWNCASTING                                │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        // UPCASTING: Child → Parent (implicit, always safe)
        System.out.println("UPCASTING (Child → Parent) - IMPLICIT, always safe:");
        Electronics laptop = new Electronics(1, "Laptop", 999.99, "Dell", 24);
        Product product = laptop; // Upcasting: Electronics → Product
        System.out.println("  Electronics laptop = new Electronics(...);");
        System.out.println("  Product product = laptop;  // UPCASTING");
        System.out.println("  Result: product.getName() = " + product.getName());
        System.out.println();

        // DOWNCASTING: Parent → Child (explicit, requires check)
        System.out.println("DOWNCASTING (Parent → Child) - EXPLICIT, needs instanceof check:");
        System.out.println();

        Product mystery = new Clothing(2, "Jacket", 89.99, "XL", "Polyester");

        System.out.println("  Product mystery = new Clothing(...);");
        System.out.println();
        System.out.println("  SAFE way (using instanceof):");

        // SAFE DOWNCASTING with instanceof check
        if (mystery instanceof Clothing) {
            Clothing c = (Clothing) mystery; // Downcasting
            System.out.println("    if (mystery instanceof Clothing) {");
            System.out.println("        Clothing c = (Clothing) mystery;  // DOWNCASTING");
            System.out.println("        c.getMaterial() = " + c.getMaterial());
            System.out.println("    }");
        }

        System.out.println();
        System.out.println("  UNSAFE (would throw ClassCastException):");
        System.out.println("    Electronics e = (Electronics) mystery;  // CRASH!");
        System.out.println("    (mystery is actually Clothing, not Electronics)");
        System.out.println();
    }

    // =========================================================================
    // 4. INTERFACE DEMONSTRATION
    // =========================================================================

    private static void demonstrateInterface() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 4. INTERFACES                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("CONCEPT: An interface defines a contract (set of methods) that");
        System.out.println("         implementing classes must provide.");
        System.out.println();
        System.out.println("In this project:");
        System.out.println("  - Payable interface defines: calculateTotal(), processPayment(), getPaymentSummary()");
        System.out.println("  - Order implements Payable");
        System.out.println();

        // Create an order and add items
        Order order = new Order("John Doe");
        order.addItem(new Electronics(1, "Laptop", 999.99, "Dell", 24));
        order.addItem(new Clothing(2, "T-Shirt", 29.99, "M", "Cotton"));

        // Use Payable interface reference (shows interface polymorphism)
        Payable payable = order; // Order IS-A Payable

        System.out.println("Using Payable interface reference:");
        System.out.println("  Payable payable = order;");
        System.out.println("  payable.calculateTotal() = $" + String.format("%.2f", payable.calculateTotal()));
        System.out.println("  payable.processPayment() = " + payable.processPayment());
        System.out.println();
    }

    // =========================================================================
    // 5. COLLECTIONS DEMONSTRATION
    // =========================================================================

    private static void demonstrateCollections() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 5. COLLECTIONS (ArrayList, HashMap)                         │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("ArrayList - Dynamic array that can grow/shrink:");
        List<Product> products = new ArrayList<>();
        products.add(new Electronics(1, "Phone", 699.99, "Apple", 12));
        products.add(new Clothing(2, "Hoodie", 49.99, "L", "Cotton"));
        System.out.println("  products.add(...) → Size: " + products.size());
        System.out.println();

        System.out.println("HashMap - Key-Value pairs for fast lookup:");
        CartService cart = new CartService();
        cart.addToCart(new Electronics(1, "Tablet", 449.99, "Samsung", 12));
        cart.addToCart(new Clothing(2, "Sneakers", 79.99, "42", "Leather"));
        System.out.println("  Map<ProductId, Product> for cart storage");
        System.out.println("  Cart items: " + cart.getTotalItemCount());
        System.out.println();
    }

    // =========================================================================
    // 6. STREAM API DEMONSTRATION
    // =========================================================================

    private static void demonstrateStreamAPI() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 6. STREAM API                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("CONCEPT: Streams provide a functional way to process collections.");
        System.out.println();

        List<Product> products = new ArrayList<>();
        products.add(new Electronics(1, "Laptop", 999.99, "Dell", 24));
        products.add(new Electronics(2, "Phone", 699.99, "Samsung", 12));
        products.add(new Clothing(3, "Shirt", 29.99, "M", "Cotton"));
        products.add(new Clothing(4, "Pants", 59.99, "L", "Denim"));

        // Filter: Keep only Electronics
        System.out.println("FILTER - Get only Electronics:");
        System.out.println("  products.stream().filter(p -> p instanceof Electronics)...");
        long electronicsCount = products.stream()
                .filter(p -> p instanceof Electronics)
                .count();
        System.out.println("  Result: " + electronicsCount + " electronics found");
        System.out.println();

        // Map + Sum: Calculate total price
        System.out.println("MAP + SUM - Calculate total price:");
        System.out.println("  products.stream().mapToDouble(Product::getPrice).sum()");
        double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        System.out.println("  Result: $" + String.format("%.2f", total));
        System.out.println();

        // Filter + Collect: Search by name
        System.out.println("FILTER + COLLECT - Search products containing 'a':");
        List<Product> searchResults = products.stream()
                .filter(p -> p.getName().toLowerCase().contains("a"))
                .collect(java.util.stream.Collectors.toList());
        System.out.println("  Found: " + searchResults.size() + " products");
        searchResults.forEach(p -> System.out.println("    - " + p.getName()));
        System.out.println();
    }

    // =========================================================================
    // 7. FILE I/O DEMONSTRATION
    // =========================================================================

    private static void demonstrateFileIO() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 7. FILE I/O (Traditional IO vs NIO)                         │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("Traditional I/O (java.io):");
        System.out.println("  BufferedWriter writer = new BufferedWriter(new FileWriter(...))");
        System.out.println("  BufferedReader reader = new BufferedReader(new FileReader(...))");
        System.out.println("  - Stream-based (line by line)");
        System.out.println("  - More verbose code");
        System.out.println();

        System.out.println("NIO (java.nio):");
        System.out.println("  Files.write(path, lines)");
        System.out.println("  Files.readAllLines(path)");
        System.out.println("  - Buffer-based");
        System.out.println("  - More concise, modern API");
        System.out.println();

        System.out.println("See FileService.java for both implementations.");
        System.out.println();
    }

    // =========================================================================
    // 8. THREADS DEMONSTRATION
    // =========================================================================

    private static void demonstrateThreads() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ 8. THREADS & EXECUTORSERVICE                                │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("CONCEPT: Threads allow concurrent execution. ExecutorService");
        System.out.println("         manages a pool of threads efficiently.");
        System.out.println();

        // Create ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(2);

        System.out.println("RUNNABLE - Task without return value:");
        Runnable task1 = () -> {
            System.out.println("  [Runnable] Started on thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            System.out.println("  [Runnable] Completed!");
        };
        executor.submit(task1);

        System.out.println();
        System.out.println("CALLABLE + FUTURE - Task with return value:");
        Callable<String> task2 = () -> {
            System.out.println("  [Callable] Started on thread: " + Thread.currentThread().getName());
            Thread.sleep(500);
            return "  [Callable] Result: Success!";
        };

        Future<String> future = executor.submit(task2);

        try {
            // Wait for result (with timeout)
            String result = future.get(5, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Shutdown executor
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        System.out.println();
        System.out.println("WHY THREADS? → Prevent UI freezing during long operations!");
        System.out.println("See OrderProcessor.java for complete implementation.");
        System.out.println();
    }
}
