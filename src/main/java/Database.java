import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;
import rx.Observable;

import static com.mongodb.client.model.Filters.eq;

public class Database {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> items;

    Database(String dbName) {
        MongoDatabase db = MongoClients.create("mongodb://localhost:27017").getDatabase(dbName);
        users = db.getCollection("users");
        items = db.getCollection("items");
    }

    public void addUser(int id, String currency) {
        Document user = new Document("_id", id).append("currency", currency);
        users.insertOne(user).subscribe();
    }

    public void addItem(String name, double usdPrice) {
        Document item = new Document("name", name).append("price", usdPrice);
        items.insertOne(item).subscribe();
    }

    public Observable<User> getUser(int id) {
        return users.find(eq("_id", id))
                .toObservable()
                .map(User::new);
    }

    public Observable<Item> getItems(String currency) {
        return items.find()
                .toObservable()
                .map(Item::new)
                .map(item -> item.convertCurrency(currency));
    }
}
