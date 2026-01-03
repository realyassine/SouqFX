package model;

public abstract class Product {

    protected int id;
    protected String name;
    protected double price;


    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public abstract String getDescription();

    
    public String getProductType() {
        return "Generic Product";
    }

   
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   
    @Override
    public String toString() {
        return name + " - " + String.format("%.2f", price) + " DH";
    }
}
