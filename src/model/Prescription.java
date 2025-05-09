package model;

import java.util.Date;

public class Prescription {
    private String id;
    private Date createdDate;
    private Double totalPrice;
    private Double finalPrice;
    private Double discountAmount;
    private String status;
    private String customerId;
    private String promotionId;
    private String note;


    public Prescription(String id, Date createdDate, Double totalPrice, Double discountAmount, Double finalPrice, String status, String customerId, String promotionId, String note) {
        this.id = id;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
        this.status = status;
        this.customerId = customerId;
        this.promotionId = promotionId;
        this.note = note;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerId() {
        return customerId;
    }
    
     public String getPromotionId() {
        return promotionId;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }
    
    public String getNote() {
        return note;
    }

    @Override
public String toString() {
    return String.format(
        "Prescription{id='%s', createdDate='%s', totalPrice=%.2f, discountAmount=%.2f, finalPrice=%.2f, status='%s', customerId='%s', promotionId='%s', note='%s'}",
        id, 
        createdDate.toString(),
        totalPrice, 
        discountAmount, 
        finalPrice, 
        status, 
        customerId,
        promotionId,
        note
    );
}

}
