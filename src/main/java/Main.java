public class Main {
    public static void main(String[] args) {
        Database db = new Database("rx");
        new RxNettyHttpServer(db).run();
    }
}
