package controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import model.Medicine;
import model.MongoConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MedicineController {
    private MongoCollection<Document> collection;

    public MedicineController() {
        MongoConnection database = new MongoConnection();
        collection = database.getCollection("medicine");
    }

    public List<Medicine> getDanhSachThuoc() {
        List<Medicine> list = new ArrayList<>();

        for (Document doc : collection.find()) {
            Medicine medicine = new Medicine(
                doc.getObjectId("_id").toString(),
                doc.getString("name"),
                doc.getString("category"),
                doc.getString("brand"),
                doc.getString("dosage"),
                doc.getString("unit"),
                doc.getDouble("price"),
                doc.getString("description")
            );
            list.add(medicine);
        }
        return list;
    }
    
    public Medicine getThuocTheoId(String id) {
    try {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return new Medicine(
                doc.getObjectId("_id").toString(),
                doc.getString("name"),
                doc.getString("category"),
                doc.getString("brand"),
                doc.getString("dosage"),
                doc.getString("unit"),
                doc.getDouble("price"),
                doc.getString("description")
            );
        }
    } catch (IllegalArgumentException e) {
        System.err.println("ID không hợp lệ: " + id);
    }
    return null; // Không tìm thấy hoặc lỗi ID
}
    
}
