/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import model.MongoConnection;
import org.bson.Document;

import model.Promotion;
import org.bson.types.ObjectId;

/**
 *
 * @author namv2
 */
public class PromotionController {
    private MongoCollection<Document> collection;

    public PromotionController() {
        MongoConnection database = new MongoConnection();
        collection = database.getCollection("promotion");
    }
    
    public List<Promotion> getDanhSachKhuyenMai() {
    List<Promotion> list = new ArrayList<>();

    for (Document doc : collection.find()) {
        Promotion promotion = new Promotion(
            doc.getObjectId("_id").toString(),
            doc.getString("name"),
            doc.getString("discount_type"),
            doc.get("discount_value", Number.class).doubleValue(),
            doc.get("min_order_value", Number.class).doubleValue(),
            doc.getDate("start_date"),
            doc.getDate("end_date"),
            doc.getList("applied_products", ObjectId.class)
                .stream()
                .map(ObjectId::toString)
                .toList(),
            doc.getString("description")
        );
        list.add(promotion);
    }

    return list;
}

    
}
