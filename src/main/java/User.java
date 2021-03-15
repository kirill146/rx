import org.bson.Document;

public class User {
    public final int id;
    public final String currency;

    public User(Document doc) {
        this(doc.getInteger("_id"), doc.getString("currency"));
    }

    public User(int id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + id +
                ", currency='" + currency + '\'' +
                "}\n";
    }
}
