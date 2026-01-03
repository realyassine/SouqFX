package model;


public interface Payable {   
    double calculateTotal();
    
    boolean processPayment();

    String getPaymentSummary();
}
