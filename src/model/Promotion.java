package model;

import java.util.Date;
import java.util.List;

public class Promotion {
    private String id;
    private String name;
    private String discountType; // "PERCENT" hoặc "FIXED"
    private double discountValue;
    private double minOrderValue;
    private Date startDate;
    private Date endDate;
    private List<String> appliedProducts; // Danh sách ID của thuốc được áp dụng khuyến mãi
    private String description;

    public Promotion(String id, String name, String discountType, double discountValue, double minOrderValue,
                     Date startDate, Date endDate, List<String> appliedProducts, String description) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appliedProducts = appliedProducts;
        this.description = description;
    }

    // In thông tin
    public void print() {
        System.out.println("ID: " + id);
        System.out.println("Tên: " + name);
        System.out.println("Loại giảm giá: " + discountType);
        System.out.println("Giá trị giảm: " + discountValue);
        System.out.println("Đơn tối thiểu: " + minOrderValue);
        System.out.println("Bắt đầu: " + startDate);
        System.out.println("Kết thúc: " + endDate);
        System.out.println("Áp dụng cho: " + (appliedProducts.isEmpty() ? "Tất cả" : appliedProducts));
        System.out.println("Mô tả: " + description);
        System.out.println();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDiscountType() { return discountType; }
    public double getDiscountValue() { return discountValue; }
    public double getMinOrderValue() { return minOrderValue; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public List<String> getAppliedProductIDs() { return appliedProducts; }
    public String getDescription() { return description; }
}
