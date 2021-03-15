import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Item {
    public final String name;
    public double price;
    public String currency;
    private static Map<String, Float> usdToXFactors;

    public Item convertCurrency(String newCurrency) {
        if (usdToXFactors == null) {
            usdToXFactors = new HashMap<>();
            usdToXFactors.put("usd", 1.f);
            usdToXFactors.put("eur", 0.84f);
            usdToXFactors.put("rub", 73.08f);
        }
        price /= usdToXFactors.get(this.currency); // convert current currency to usd
        price *= usdToXFactors.get(newCurrency); // convert usd to the new currency
        this.currency = newCurrency;
        return this;
    }

    public Item(Document doc) {
        this(doc.getString("name"), doc.getDouble("price"));
    }

    public Item(String name, double usdPrice) {
        this.name = name;
        this.price = usdPrice;
        this.currency = "usd";
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price='" + String.format("%.2f", price) + '\'' +
                ", currency='" + currency + '\'' +
                "}\n";
    }
}
