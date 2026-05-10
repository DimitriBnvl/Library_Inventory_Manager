import java.util.Set;
import java.util.TreeMap;

public class Product {
    final private String productId;
    final private String name;
    final private int price;
    final private String type;
    final private String code;
    private int stock;

    private Product(String productId, String name, int price, String type, String code, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.code = code;
        this.stock = stock;
    }

    public static Product parse(String[] fields, TreeMap<String, Product> inventory) {
        String productId = fields[1];
        String name = fields[2];
        int price = Integer.parseInt(fields[3]);
        String type = fields[4];
        String promoCode = fields[5];
        int stock = Integer.parseInt(fields[6]);

        if (!Character.isUpperCase(productId.charAt(0)))
            throw new RuntimeException("Product ID must start with a capital letter.");
        if (inventory.containsKey(productId)) throw new RuntimeException("Duplicate product id.");
        if (!Set.of("book", "children", "stationery", "game").contains(type))
            throw new RuntimeException("Invalid product type.");
        if (!promoCode.equals("_") && (promoCode.isEmpty() || !promoCode.matches("c?f?h?r?t?")))
            throw new RuntimeException("Invalid promo code.");
        if (price <= 0) throw new RuntimeException("Product price must be positive.");
        if (stock < 0) throw new RuntimeException("Product stock must be nonnegative.");

        return new Product(productId, name, price, type, promoCode, stock);
    }

    public void reduceStock(int quantity) {
        stock -= quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getType() { return type; }
    public String getCode() { return code; }
    public int getStock() { return stock; }
}