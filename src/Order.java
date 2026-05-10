import java.util.*;

public class Order {
    final private int orderId;
    private String status;
    private int totalPrice;
    final private TreeMap<String, Integer> items;
    final private TreeSet<String> discountCode;
    private String appliedDiscountCode;

    public Order(String orderId, TreeMap<String, Integer> items, TreeSet<String> discountCode) {
        this.orderId = Integer.parseInt(orderId);
        this.status = "rejected";
        this.totalPrice = 0;
        this.items = items;
        this.discountCode = discountCode;
        this.appliedDiscountCode = "";
    }

    public void computeAcceptance(TreeMap<String, Product> inventory) {
        if (orderId <= 0) throw new RuntimeException("Order ID must be nonnegative.");

        for (var entry : items.entrySet()) {
            if (inventory.get(entry.getKey()).getStock() < entry.getValue()) {
                return;
            }
        }
        status = "accepted";
    }

    public void computePrice(TreeMap<String, Product> inventory) {
        TreeMap<String, Integer> restricted = new TreeMap<>();
        TreeMap<String, Integer> eligible = new TreeMap<>();

        for (var entry : items.entrySet()) {
            if (inventory.get(entry.getKey()).getCode().contains("r")) {
                restricted.put(entry.getKey(), entry.getValue());
            } else {
                eligible.put(entry.getKey(), entry.getValue());
            }
        }

        int promoOnlyPrice = applyPromoDiscount(items, inventory);
        int restrictedPrice = applyPromoDiscount(restricted, inventory);

        int bestIndividualPrice = Integer.MAX_VALUE;
        String bestCode = "";
        for (String code : discountCode) {
            double rate = switch (code) {
                case "UNI"    -> 0.1;
                case "HEALTH" -> 0.2;
                default       -> -1;
            };
            if (rate < 0) continue;
            int comboPrice = restrictedPrice + applyIndividualDiscount(eligible, rate, inventory);
            if (comboPrice < bestIndividualPrice || (comboPrice == bestIndividualPrice && code.compareTo(bestCode) < 0)) {
                bestIndividualPrice = comboPrice;
                bestCode = code;
            }
        }

        if (bestIndividualPrice < promoOnlyPrice) {
            totalPrice = bestIndividualPrice;
            appliedDiscountCode = bestCode;
        }
        else {
            totalPrice = promoOnlyPrice;
            appliedDiscountCode = "";
        }
    }

    private int applyPromoDiscount(TreeMap<String, Integer> subset, TreeMap<String, Product> inventory) {
        List<Integer> eligiblePrices = new ArrayList<>();
        int total = 0;

        for (var entry : subset.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            int productPrice = inventory.get(productId).getPrice();
            total += productPrice * quantity;

            if (inventory.get(productId).getCode().contains("c")) {
                for (int i = 0; i < quantity; i++) {
                    eligiblePrices.add(productPrice);
                }
            }
        }

        if (eligiblePrices.size() >= 3) {
            total -= Collections.min(eligiblePrices);
        }

        return total;
    }

    private int applyIndividualDiscount(TreeMap<String, Integer> subset, double rate, TreeMap<String, Product> inventory) {
        int total = 0;
        for (var entry : subset.entrySet()) {
            int productPrice = inventory.get(entry.getKey()).getPrice();
            int quantity = entry.getValue();
            int discountAmount = (int) Math.ceil(productPrice * rate);
            total += (productPrice - discountAmount) * quantity;
        }
        return total;
    }

    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public int getTotalPrice() { return totalPrice; }
    public TreeMap<String, Integer> getItems() { return items; }
    public String getAppliedDiscountCode() { return appliedDiscountCode; }
}