import java.util.TreeMap;
import java.util.TreeSet;

public class Order {
    final private String orderId;
    private String status;
    final private int totalPrice;
    final private TreeMap<String, Integer> items;
    final private TreeSet<String> discountCode;

    public Order(String orderId, TreeMap<String, Integer> items, TreeSet<String> discountCode) {
        this.orderId = orderId;
        this.status = "rejected";
        this.totalPrice = 0;
        this.items = items;
        this.discountCode = discountCode;
    }

    public void computeAcceptance(TreeMap<String, Product> inventory) {
        for (var entry : items.entrySet()) {
            if (inventory.get(entry.getKey()).getStock() < entry.getValue()) {
                return;
            }
        }
        status = "accepted";
    }

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public int getTotalPrice() { return totalPrice; }
    public TreeMap<String, Integer> getItems() { return items; }
    public TreeSet<String> getDiscountCode() { return discountCode; }
}