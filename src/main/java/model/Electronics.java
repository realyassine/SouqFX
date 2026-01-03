package model;

public class Electronics extends Product {

    private String brand; 
    private int warrantyMonths;

    
    public Electronics(int id, String name, double price, String brand, int warrantyMonths) {
        super(id, name, price);

        
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

  
    @Override
    public String getDescription() {
        return "Electronics: " + name + " by " + brand +
                " | Price: " + String.format("%.2f", price) + " DH" +
                " | Warranty: " + warrantyMonths + " months";
    }

   
    @Override
    public String getProductType() {
        return "Electronics";
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

   
    @Override
    public String toString() {
        return "[Electronics] " + name + " (" + brand + ") - " + String.format("%.2f", price) + " DH";
    }
}
