package model;

public class PrescriptionDetail {
    private String id;
    private String prescriptionId;
    private String medicineId;
    private int quantity;
    private double price;
    private double total;

    public PrescriptionDetail(String id, String prescriptionId, String medicineId, int quantity, double price, double total) {
        this.id = id;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "PrescriptionDetail{" +
                "id='" + id + '\'' +
                ", prescriptionId='" + prescriptionId + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
