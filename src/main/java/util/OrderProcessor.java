package util;

import model.Order;

import java.util.concurrent.*;

/**
 * =============================================================================
 * ORDER PROCESSING - THREADS AND EXECUTORSERVICE
 * =============================================================================
 * 
 * CONCEPT: THREADS
 * ----------------
 * A thread is a separate path of execution in your program.
 * Without threads, code runs sequentially (one thing after another).
 * With threads, multiple operations can run concurrently.
 * 
 * WHY USE THREADS?
 * - Prevent UI from freezing during long operations
 * - Perform background tasks (loading, saving, processing)
 * - Improve responsiveness of the application
 * 
 * TWO WAYS TO CREATE THREADS:
 * 1. Extend Thread class
 * 2. Implement Runnable interface (preferred)
 * 
 * CONCEPT: EXECUTORSERVICE
 * ------------------------
 * ExecutorService is a higher-level replacement for working with threads
 * directly.
 * Benefits:
 * - Manages a pool of threads (reuses threads instead of creating new ones)
 * - Provides Callable (returns a value) and Future (get result later)
 * - Easier to manage thread lifecycle
 * 
 * =============================================================================
 */
public class OrderProcessor {

    // -------------------------------------------------------------------------
    // EXECUTORSERVICE
    // -------------------------------------------------------------------------

    /**
     * CONCEPT: THREAD POOL
     * --------------------
     * A thread pool manages a fixed number of worker threads.
     * Tasks are submitted to the pool and executed when a thread is available.
     * This is more efficient than creating a new thread for each task.
     */
    private ExecutorService executorService;

    // Callback interface for notifying when processing is complete
    private OrderProcessingCallback callback;

    // -------------------------------------------------------------------------
    // CALLBACK INTERFACE
    // -------------------------------------------------------------------------

    /**
     * Callback interface for order processing events.
     * This allows the UI to be notified when processing completes.
     */
    public interface OrderProcessingCallback {
        void onProcessingStarted(int orderId);

        void onProcessingProgress(int orderId, int progressPercent);

        void onProcessingCompleted(int orderId, boolean success, String message);
    }

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    public OrderProcessor() {
        // Create a thread pool with 2 threads
        // This means up to 2 orders can be processed simultaneously
        this.executorService = Executors.newFixedThreadPool(2);
    }

    /**
     * Set the callback for processing events.
     * 
     * @param callback The callback implementation
     */
    public void setCallback(OrderProcessingCallback callback) {
        this.callback = callback;
    }

    // -------------------------------------------------------------------------
    // METHOD 1: USING RUNNABLE (No return value)
    // -------------------------------------------------------------------------

    /**
     * Process an order using Runnable (traditional approach).
     * 
     * CONCEPT: RUNNABLE INTERFACE
     * ---------------------------
     * Runnable is a functional interface with one method: run()
     * - Does not return a value
     * - Cannot throw checked exceptions
     * - Used when you just need to execute code in a thread
     * 
     * @param order The order to process
     */
    public void processOrderWithRunnable(Order order) {
        // Create a Runnable using lambda expression
        Runnable task = () -> {
            try {
                int orderId = order.getOrderId();

                // Notify: Processing started
                if (callback != null) {
                    callback.onProcessingStarted(orderId);
                }

                System.out.println("[Thread: " + Thread.currentThread().getName() +
                        "] Starting to process Order #" + orderId);

                // Simulate order processing with progress updates
                // This demonstrates why threads are useful - the UI won't freeze
                for (int progress = 0; progress <= 100; progress += 20) {
                    // Simulate work being done
                    Thread.sleep(500); // Sleep 500ms

                    System.out.println("[Thread: " + Thread.currentThread().getName() +
                            "] Order #" + orderId + " - Progress: " + progress + "%");

                    // Notify: Progress update
                    if (callback != null) {
                        callback.onProcessingProgress(orderId, progress);
                    }
                }

                // Process payment (from Payable interface)
                boolean success = order.processPayment();

                System.out.println("[Thread: " + Thread.currentThread().getName() +
                        "] Order #" + orderId + " processing complete!");

                // Notify: Processing completed
                if (callback != null) {
                    callback.onProcessingCompleted(orderId, success,
                            success ? "Order processed successfully!" : "Payment failed!");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Order processing was interrupted!");
                if (callback != null) {
                    callback.onProcessingCompleted(order.getOrderId(), false, "Processing interrupted!");
                }
            }
        };

        // Submit the task to the executor service
        executorService.submit(task);
    }

    // -------------------------------------------------------------------------
    // METHOD 2: USING CALLABLE AND FUTURE (Returns a value)
    // -------------------------------------------------------------------------

    /**
     * Process an order using Callable and Future.
     * 
     * CONCEPT: CALLABLE<V> INTERFACE
     * ------------------------------
     * Callable is similar to Runnable but:
     * - CAN return a value (the type parameter V)
     * - CAN throw checked exceptions
     * - Used when you need a result from the thread
     * 
     * CONCEPT: FUTURE<V>
     * ------------------
     * Future represents the result of an async computation.
     * - future.get() blocks until result is available
     * - future.isDone() checks if computation is complete
     * - future.cancel() attempts to cancel the computation
     * 
     * @param order The order to process
     * @return Future containing the order confirmation message
     */
    public Future<String> processOrderWithCallable(Order order) {
        // Create a Callable that returns a String (the confirmation)
        Callable<String> task = () -> {
            int orderId = order.getOrderId();

            System.out.println("[Callable Thread: " + Thread.currentThread().getName() +
                    "] Processing Order #" + orderId);

            // Simulate processing time (3 seconds total)
            Thread.sleep(3000);

            // Process payment
            boolean success = order.processPayment();

            // Return the result
            if (success) {
                return "Order #" + orderId + " confirmed! Total: " +
                        String.format("%.2f", order.calculateTotal()) + " DH";
            } else {
                return "Order #" + orderId + " failed!";
            }
        };

        // Submit and return the Future
        // The caller can use future.get() to wait for and retrieve the result
        return executorService.submit(task);
    }

    // -------------------------------------------------------------------------
    // METHOD 3: USING THREAD CLASS DIRECTLY
    // -------------------------------------------------------------------------

    /**
     * Process order using Thread class directly.
     * 
     * CONCEPT: THREAD CLASS
     * ---------------------
     * Creating a thread by extending Thread or passing Runnable to constructor.
     * This is a lower-level approach compared to ExecutorService.
     * 
     * @param order The order to process
     */
    public void processOrderWithThread(Order order) {
        // Create thread with Runnable
        Thread processingThread = new Thread(() -> {
            try {
                System.out.println("[Direct Thread] Processing Order #" + order.getOrderId());
                Thread.sleep(2000); // Simulate work
                order.processPayment();
                System.out.println("[Direct Thread] Order #" + order.getOrderId() + " complete!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Set thread name for debugging
        processingThread.setName("OrderProcessor-" + order.getOrderId());

        // Start the thread
        processingThread.start();
    }

    // -------------------------------------------------------------------------
    // DEMONSTRATING FUTURE METHODS
    // -------------------------------------------------------------------------

    /**
     * Example of how to use Future to get results and check status.
     * 
     * @param order The order to process
     */
    public void demonstrateFuture(Order order) {
        // Submit the callable task
        Future<String> future = processOrderWithCallable(order);

        System.out.println("Order submitted. Future isDone? " + future.isDone());

        // You can do other work here while waiting...
        System.out.println("Doing other work while waiting...");

        try {
            // Wait for result with timeout
            // This blocks for at most 10 seconds
            String result = future.get(10, TimeUnit.SECONDS);
            System.out.println("Result: " + result);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for result");
        } catch (ExecutionException e) {
            System.err.println("Error during execution: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for result!");
            future.cancel(true); // Cancel the task
        }
    }

    // -------------------------------------------------------------------------
    // SHUTDOWN
    // -------------------------------------------------------------------------

    /**
     * Shutdown the executor service.
     * Should be called when the application closes.
     * 
     * IMPORTANT: Always shutdown ExecutorService to prevent resource leaks!
     */
    public void shutdown() {
        System.out.println("Shutting down OrderProcessor...");

        // Disable new tasks from being submitted
        executorService.shutdown();

        try {
            // Wait for existing tasks to terminate
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                // Force shutdown if tasks don't complete in time
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("OrderProcessor shutdown complete.");
    }
}
