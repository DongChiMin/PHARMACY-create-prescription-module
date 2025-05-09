package model;

import com.mongodb.client.*;
import org.bson.Document;


public class MongoConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoConnection() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("pharmacy");
    }
    
    public MongoCollection<Document> getCollection(String collectionName){
        return database.getCollection(collectionName);
    }
}
