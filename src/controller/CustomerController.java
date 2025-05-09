package controller;

import com.mongodb.client.MongoCollection;
import model.Customer;
import model.MongoConnection;
import org.bson.Document;
import com.mongodb.client.model.Filters;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

public class CustomerController {
    private MongoCollection<Document> collection;

    public CustomerController() {
        MongoConnection database = new MongoConnection();
        collection = database.getCollection("customer");
    }

    public List<Customer> getDanhSachKhachHang() {
        List<Customer> list = new ArrayList<>();

        for (Document doc : collection.find()) {
            Customer customer = new Customer(
                doc.getObjectId("_id").toString(),
                doc.getString("name"),
                doc.getString("phoneNumber"),
                doc.getDate("birthDate"),
                doc.getString("address")
            );
            list.add(customer);
        }

        return list;
    }
    
public Date GetNgaySinhbyID(String id) {
    try {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return doc.getDate("birthDate");
        }
    } catch (IllegalArgumentException e) {
        System.err.println("ID không hợp lệ: " + id);
    }
    return null; // Không tìm thấy hoặc ID sai
}

public void ThemKhachHang(Customer customer) {
    // Tạo đối tượng Document từ Customer
    Document doc = new Document("name", customer.getName())
                      .append("phoneNumber", customer.getPhoneNumber())
                      .append("birthDate", customer.getBirthDate())
                      .append("address", customer.getAddress());
    
    // Thêm Document vào MongoDB collection
    collection.insertOne(doc);

    System.out.println("Khách hàng đã được thêm thành công!");
}


}
