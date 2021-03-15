import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxNettyHttpServer {
    private final Database db;
    private static final List<String> allowedCurrencies = Arrays.asList("usd", "eur", "rub");

    public RxNettyHttpServer(Database db) {
        this.db = db;
    }

    public void run() {
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    String[] reqParts = req.getDecodedPath().substring(1).split("/");
                    Observable<String> response = null;
                    switch (reqParts[0]) {
                        case "add_user": {
                            int id = Integer.parseInt(reqParts[1]);
                            String currency = reqParts[2];
                            if (!allowedCurrencies.contains(currency)) {
                                response = Observable.just("Unknown currency");
                                break;
                            }
                            db.addUser(id, currency);
                            response = Observable
                                    .just("Added " + new User(id, currency));
                            break;
                        }
                        case "add_item": {
                            String name = reqParts[1];
                            double usdPrice = Double.parseDouble(reqParts[2]);
                            db.addItem(name, usdPrice);
                            response = Observable
                                    .just("Added " + new Item(name, usdPrice));
                            break;
                        }
                        case "get_items": {
                            int id = Integer.parseInt(reqParts[1]);
                            response = db.getUser(id)
                                    .map(user -> user.currency)
                                    .flatMap(db::getItems)
                                    .map(Item::toString);
                            break;
                        }
                        default: {
                            response = Observable.just("Unknown request");
                        }
                    }
                    return resp.writeString(response);
                })
                .awaitShutdown();
    }
}
