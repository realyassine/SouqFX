package model;

public class Clothing extends Product {

    private String size; 
    private String material; 

    
    public Clothing(int id, String name, double price, String size, String material) {        
        super(id, name, price);
        this.size = size;
        this.material = material;
    }

    
    @Override
    public String getDescription() {
        return "Clothing: " + name +
                " | Size: " + size +
                " | Material: " + material +
                " | Price: " + String.format("%.2f", price) + " DH";
    }

   
    @Override
    public String getProductType() {
        return "Clothing";
    }


    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return "[Clothing] " + name + " (Size: " + size + ") - " + String.format("%.2f", price) + " DH";
    }
}
