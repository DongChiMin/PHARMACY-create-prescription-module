package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import model.MongoConnection;
import model.PrescriptionDetail;
import org.bson.Document;
import org.bson.types.ObjectId;

public class PrescriptionDetailController {
    private MongoCollection<Document> collection;

    public PrescriptionDetailController() {
        MongoConnection database = new MongoConnection();
        collection = database.getCollection("prescriptionDetail");
    }

    // Thêm PrescriptionDetail vào MongoDB
    public boolean addPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
        // Tạo Document từ PrescriptionDetail
       Document doc = new Document()
        .append("prescription_id", prescriptionDetail.getPrescriptionId())  // Chỉ lưu dưới dạng String
        .append("medicine_id", prescriptionDetail.getMedicineId())  // Chỉ lưu dưới dạng String
        .append("quantity", prescriptionDetail.getQuantity())
        .append("price", prescriptionDetail.getPrice())
        .append("total", prescriptionDetail.getTotal());

        // Thêm PrescriptionDetail vào collection "prescription_detail"
        try {
            InsertOneResult result = collection.insertOne(doc);
            return result.wasAcknowledged();  // Trả về true nếu thêm thành công
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return false;  // Trả về false nếu có lỗi xảy ra
        }
    }

}
