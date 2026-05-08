public class Product {
    final private String productId;
    final private String name;
    final private int price;
    final private String type;
    final private String code;
    final private int stock;

    public Product(String productId, String name, int price, String type, String code, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.code = code;
        this.stock = stock;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getType() { return type; }
    public String getCode() { return code; }
    public int getStock() { return stock; }
}