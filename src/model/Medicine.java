package model;

public class Medicine {
    private String id;
    private String name;
    private String category;
    private String brand;
    private String dosage;
    private String unit;
    private double price;
    private String description;

    public Medicine(String id, String name, String category, String brand, String dosage, String unit, double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.dosage = dosage;
        this.unit = unit;
        this.price = price;
        this.description = description;
    }
    
    //in thông tin
    public void print(){
        System.out.println(id + " \n" + name + " \n" + category + " \n" + brand + " \n" + dosage + " \n" + unit + " \n" + price + " \n" + description + "\n");
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getDosage() { return dosage; }
    public String getUnit() { return unit; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
}
