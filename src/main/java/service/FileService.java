package service;

import model.Product;
import model.Electronics;
import model.Clothing;
import model.Order;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * =============================================================================
 * FILE SERVICE - HANDLES FILE I/O OPERATIONS
 * =============================================================================
 * 
 * CONCEPT: FILE I/O vs NIO
 * ------------------------
 * 
 * TRADITIONAL I/O (java.io package):
 * - FileReader, FileWriter, BufferedReader, BufferedWriter
 * - Stream-based (reads/writes byte by byte or line by line)
 * - Blocking I/O (waits until operation completes)
 * - Been around since Java 1.0
 * 
 * NIO (java.nio package - "New I/O"):
 * - Files, Paths, Path classes
 * - Buffer-based (reads into buffers)
 * - Can be non-blocking
 * - Introduced in Java 1.4, enhanced in Java 7 (NIO.2)
 * - More modern, often more convenient for simple operations
 * 
 * In this class, we demonstrate BOTH approaches:
 * - saveProductsIO() / loadProductsIO() - Traditional I/O
 * - saveProductsNIO() / loadProductsNIO() - NIO approach
 * 
 * For this project, we use CSV format for simplicity.
 * 
 * =============================================================================
 */
public class FileService {

    // -------------------------------------------------------------------------
    // FILE PATHS
    // -------------------------------------------------------------------------

    // File paths for data storage
    private static final String PRODUCTS_FILE = "data/products.csv";
    private static final String ORDERS_FILE = "data/orders.csv";
    private static final String DATA_DIRECTORY = "data";

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    public FileService() {
        // Ensure data directory exists
        createDataDirectory();
    }

    /**
     * Create the data directory if it doesn't exist.
     * 
     * CONCEPT: NIO - Files.createDirectories
     * --------------------------------------
     * Creates directory and all parent directories if they don't exist.
     */
    private void createDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Created data directory: " + DATA_DIRECTORY);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    // =========================================================================
    // TRADITIONAL I/O APPROACH (java.io)
    // =========================================================================

    /**
     * Save products using Traditional I/O (FileWriter).
     * 
     * CONCEPT: Traditional I/O with FileWriter and BufferedWriter
     * -----------------------------------------------------------
     * - FileWriter: Writes characters to a file
     * - BufferedWriter: Wraps FileWriter for efficiency (buffers output)
     * - try-with-resources: Automatically closes the writer
     * 
     * CSV Format: TYPE,ID,NAME,PRICE,EXTRA1,EXTRA2
     * - Electronics: ELECTRONICS,id,name,price,brand,warrantyMonths
     * - Clothing: CLOTHING,id,name,price,size,material
     * 
     * @param products List of products to save
     */
    public void saveProductsIO(List<Product> products) {
        // try-with-resources ensures the writer is closed automatically
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {

            // Write header line
            writer.write("TYPE,ID,NAME,PRICE,EXTRA1,EXTRA2");
            writer.newLine();

            // Write each product
            for (Product product : products) {
                String line = productToCsv(product);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Products saved successfully using Traditional I/O!");

        } catch (IOException e) {
            System.err.println("Error saving products (IO): " + e.getMessage());
        }
    }

    /**
     * Load products using Traditional I/O (FileReader).
     * 
     * CONCEPT: Traditional I/O with FileReader and BufferedReader
     * -----------------------------------------------------------
     * - FileReader: Reads characters from a file
     * - BufferedReader: Wraps FileReader for efficiency, allows readLine()
     * 
     * @return List of loaded products
     */
    public List<Product> loadProductsIO() {
        List<Product> products = new ArrayList<>();
        File file = new File(PRODUCTS_FILE);

        // Check if file exists
        if (!file.exists()) {
            System.out.println("Products file not found. Starting with empty product list.");
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            // Read file line by line
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Parse CSV line and create Product object
                Product product = csvToProduct(line);
                if (product != null) {
                    products.add(product);
                }
            }

            System.out.println("Loaded " + products.size() + " products using Traditional I/O.");

        } catch (IOException e) {
            System.err.println("Error loading products (IO): " + e.getMessage());
        }

        return products;
    }

    // =========================================================================
    // NIO APPROACH (java.nio)
    // =========================================================================

    /**
     * Save products using NIO (Files.write).
     * 
     * CONCEPT: NIO with Files.write
     * -----------------------------
     * - Files.write() writes all lines at once
     * - More concise than Traditional I/O
     * - Handles file creation and closing automatically
     * - StandardOpenOption controls how file is opened
     * 
     * @param products List of products to save
     */
    public void saveProductsNIO(List<Product> products) {
        try {
            // Prepare lines to write
            List<String> lines = new ArrayList<>();

            // Add header
            lines.add("TYPE,ID,NAME,PRICE,EXTRA1,EXTRA2");


            for (Product product : products) {
                lines.add(productToCsv(product));
            }

            Files.write(
                    Paths.get(PRODUCTS_FILE),
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);

            System.out.println("Products saved successfully using NIO!");

        } catch (IOException e) {
            System.err.println("Error saving products (NIO): " + e.getMessage());
        }
    }

    /**
     * Load products using NIO (Files.readAllLines).
     * 
     * CONCEPT: NIO with Files.readAllLines
     * ------------------------------------
     * - Reads entire file into a List<String> in one call
     * - Very convenient for small to medium files
     * - For very large files, use Files.lines() which is lazy/streaming
     * 
     * @return List of loaded products
     */
    public List<Product> loadProductsNIO() {
        List<Product> products = new ArrayList<>();
        Path path = Paths.get(PRODUCTS_FILE);

        // Check if file exists using NIO
        if (!Files.exists(path)) {
            System.out.println("Products file not found. Starting with empty product list.");
            return products;
        }

        try {
            // Read all lines at once
            List<String> lines = Files.readAllLines(path);

            // Skip header (first line) and process rest
            for (int i = 1; i < lines.size(); i++) {
                Product product = csvToProduct(lines.get(i));
                if (product != null) {
                    products.add(product);
                }
            }

            System.out.println("Loaded " + products.size() + " products using NIO.");

        } catch (IOException e) {
            System.err.println("Error loading products (NIO): " + e.getMessage());
        }

        return products;
    }

    // =========================================================================
    // ORDER FILE OPERATIONS
    // =========================================================================

    /**
     * Save an order to file.
     * Uses NIO for modern approach.
     * 
     * @param order The order to save
     */
    public void saveOrder(Order order) {
        try {
            // Prepare order line
            String orderLine = String.format("%d,%s,%s,%.2f,%b",
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getFormattedDate(),
                    order.calculateTotal(),
                    order.isPaid());

            Path path = Paths.get(ORDERS_FILE);

            // If file doesn't exist, create with header
            if (!Files.exists(path)) {
                List<String> lines = new ArrayList<>();
                lines.add("ORDER_ID,CUSTOMER,DATE,TOTAL,PAID");
                lines.add(orderLine);
                Files.write(path, lines);
            } else {
                // Append to existing file
                Files.write(path,
                        List.of(orderLine),
                        StandardOpenOption.APPEND);
            }

            System.out.println("Order #" + order.getOrderId() + " saved successfully!");

        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    /**
     * Load all orders from file.
     * 
     * @return List of orders (without items - just the order info)
     */
    public List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();
        Path path = Paths.get(ORDERS_FILE);

        if (!Files.exists(path)) {
            return orders;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 5) {
                    int orderId = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    LocalDateTime date = LocalDateTime.parse(parts[2],
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    boolean paid = Boolean.parseBoolean(parts[4]);

                    Order order = new Order(orderId, customerName, date, paid);
                    orders.add(order);
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }

        return orders;
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /**
     * Convert a Product to CSV line.
     * 
     * CONCEPT: POLYMORPHISM & DOWNCASTING
     * -----------------------------------
     * We check the actual type using instanceof, then DOWNCAST to access
     * subclass-specific methods (getBrand, getSize, etc.)
     * 
     * @param product The product to convert
     * @return CSV formatted string
     */
    private String productToCsv(Product product) {
        // DOWNCASTING: Check type and cast to access specific methods
        if (product instanceof Electronics) {
            Electronics e = (Electronics) product; // DOWNCAST
            return String.format("ELECTRONICS,%d,%s,%.2f,%s,%d",
                    e.getId(), e.getName(), e.getPrice(), e.getBrand(), e.getWarrantyMonths());
        } else if (product instanceof Clothing) {
            Clothing c = (Clothing) product; // DOWNCAST
            return String.format("CLOTHING,%d,%s,%.2f,%s,%s",
                    c.getId(), c.getName(), c.getPrice(), c.getSize(), c.getMaterial());
        }
        return "";
    }

    /**
     * Convert a CSV line to Product object.
     * 
     * CONCEPT: FACTORY-LIKE BEHAVIOR
     * ------------------------------
     * Based on the TYPE field, we create the appropriate subclass.
     * The returned Product reference can hold either type (UPCASTING).
     * 
     * @param csvLine The CSV line to parse
     * @return Product object (Electronics or Clothing)
     */
    private Product csvToProduct(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            String type = parts[0];
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            double price = Double.parseDouble(parts[3]);

            // Create appropriate subclass based on type
            if ("ELECTRONICS".equals(type)) {
                String brand = parts[4];
                int warranty = Integer.parseInt(parts[5]);
                // UPCASTING: Electronics is returned as Product
                return new Electronics(id, name, price, brand, warranty);
            } else if ("CLOTHING".equals(type)) {
                String size = parts[4];
                String material = parts[5];
                // UPCASTING: Clothing is returned as Product
                return new Clothing(id, name, price, size, material);
            }
        } catch (Exception e) {
            System.err.println("Error parsing CSV line: " + csvLine);
        }
        return null;
    }

    /**
     * Create sample product data file.
     * Useful for initializing the application with test data.
     */
    public void createSampleProductData() {
        List<Product> sampleProducts = new ArrayList<>();

        // Sample Electronics (Prices in Moroccan Dirham - DH)
        sampleProducts.add(new Electronics(1, "Laptop", 9999.00, "Dell", 24));
        sampleProducts.add(new Electronics(2, "Smartphone", 6999.00, "Samsung", 12));
        sampleProducts.add(new Electronics(3, "Headphones", 1499.00, "Sony", 12));
        sampleProducts.add(new Electronics(4, "Tablet", 4499.00, "Apple", 12));
        sampleProducts.add(new Electronics(5, "Smart Watch", 2999.00, "Xiaomi", 12));

        // Sample Clothing (Moroccan brands and styles)
        sampleProducts.add(new Clothing(6, "Djellaba", 450.00, "L", "Cotton"));
        sampleProducts.add(new Clothing(7, "Caftan", 1200.00, "M", "Silk"));
        sampleProducts.add(new Clothing(8, "Babouche", 180.00, "42", "Leather"));
        sampleProducts.add(new Clothing(9, "T-Shirt", 149.00, "M", "Cotton"));
        sampleProducts.add(new Clothing(10, "Jeans", 350.00, "L", "Denim"));

        // Save using NIO (modern approach)
        saveProductsNIO(sampleProducts);

        System.out.println("Sample product data created!");
    }
}
