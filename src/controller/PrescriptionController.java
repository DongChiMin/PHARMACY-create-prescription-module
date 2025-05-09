package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import java.util.ArrayList;
import java.util.List;
import model.Prescription;
import model.MongoConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class PrescriptionController {
    private MongoCollection<Document> collection;

    public PrescriptionController() {
        MongoConnection database = new MongoConnection();
        collection = database.getCollection("prescription");
    }

    // Lấy danh sách tất cả đơn thuốc
    public List<Prescription> getDanhSachDonThuoc() {
        List<Prescription> list = new ArrayList<>();

        for (Document doc : collection.find()) {
            String promotionId = "";
            if(doc.getObjectId("promotion_id") != null){
                promotionId = doc.getObjectId("promotion_id").toString();
            }
            Prescription prescription = new Prescription(
                doc.getObjectId("_id").toString(),
                doc.getDate("created_date"),
                doc.getDouble("total_price"),
                doc.getDouble("discount_amount"),
                doc.getDouble("final_price"),
                doc.getString("status"),
                doc.getObjectId("customer_id").toString(),  // customer_id được lưu dưới dạng ObjectId
                promotionId,
                doc.getString("note")  
            );
            list.add(prescription);
        }
        return list;
    }

    // Thêm đơn thuốc mới vào cơ sở dữ liệu
    public ObjectId themDonThuoc(Prescription prescription) {
    Document doc;
    if(prescription.getPromotionId() != ""){
        doc = new Document()
        .append("created_date", prescription.getCreatedDate())
        .append("total_price", prescription.getTotalPrice())
        .append("discount_amount", prescription.getDiscountAmount())
        .append("final_price", prescription.getFinalPrice())
        .append("status", prescription.getStatus())
        .append("customer_id", new ObjectId(prescription.getCustomerId()))
        .append("promotion_id", new ObjectId(prescription.getPromotionId()))
        .append("note", prescription.getNote());
    }
    else{
        doc = new Document()
        .append("created_date", prescription.getCreatedDate())
        .append("total_price", prescription.getTotalPrice())
        .append("discount_amount", prescription.getDiscountAmount())
        .append("final_price", prescription.getFinalPrice())
        .append("status", prescription.getStatus())
        .append("customer_id", new ObjectId(prescription.getCustomerId()))
        .append("promotion_id", null)
        .append("note", prescription.getNote());
    }
    

    InsertOneResult result = collection.insertOne(doc);
    return result.getInsertedId().asObjectId().getValue();  // Trả về ObjectId được tạo
}


}
