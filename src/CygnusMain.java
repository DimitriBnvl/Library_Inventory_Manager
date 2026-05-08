import java.util.*;

public class CygnusMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String storeName = null;
        int balance = 0;

        TreeMap<String, Product> inventory = new TreeMap<>();
        List<Order> orders = new ArrayList<>();
        Set<String> seenOrderIds = new HashSet<>();
        String currentOrderId = null;
        TreeMap<String, Integer> currentItems = null;
        TreeSet<String> currentDiscounts = null;
        boolean inventoryStarted = false;
        boolean inventoryEnded = false;

        try {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.isBlank()) continue;
                if (line.startsWith("#")) continue;

                String[] fields = line.split("\\|");

                if (fields[0].equals("STORE")) {
                    storeName = fields[1];
                }
                else if (fields[0].equals("INVENTORY")) {
                    if (inventoryStarted) throw new RuntimeException("Duplicate INVENTORY record.");
                    inventoryStarted = true;
                    balance = Integer.parseInt(fields[1]);
                    if (balance < 0) throw new RuntimeException("Negative balance.");
                }
                else if (fields[0].equals("PRODUCT")) {
                    Product product = Product.parse(fields, inventory);
                    inventory.put(product.getProductId(), product);
                }
                else if (fields[0].equals("ENDINVENTORY")) {
                    inventoryEnded = true;
                }
                else if (fields[0].equals("ORDER")) {
                    String orderId = fields[1];
                    if (seenOrderIds.contains(orderId)) throw new RuntimeException("Duplicate order ID.");
                    seenOrderIds.add(orderId);
                    currentOrderId = orderId;
                    currentItems = new TreeMap<>();
                    currentDiscounts = new TreeSet<>();
                }
                else if (fields[0].equals("ITEM")) {
                    if (currentItems == null) throw new RuntimeException("ITEM outside of ORDER.");
                    int quantity = Integer.parseInt(fields[2]);
                    if (quantity <= 0) throw new RuntimeException("Item quantity must be positive.");
                    currentItems.merge(fields[1], quantity, Integer::sum);
                }
                else if (fields[0].equals("DISCOUNT")) {
                    if (currentDiscounts == null) throw new RuntimeException("DISCOUNT outside of ORDER.");
                    String code = fields[1];
                    if (!Set.of("UNI", "HEALTH", "PROMO", "EMPLOYEE").contains(code))
                        throw new RuntimeException("Invalid discount code.");
                    currentDiscounts.add(code);
                }
                else if (fields[0].equals("ENDORDER")) {
                    orders.add(new Order(currentOrderId, currentItems, currentDiscounts));
                    currentOrderId = null;
                    currentItems = null;
                    currentDiscounts = null;
                }
                else {
                    throw new RuntimeException("Unknown record: " + fields[0]);
                }
            }

            if (!inventoryEnded) throw new RuntimeException("Missing ENDINVENTORY.");
            if (currentOrderId != null) throw new RuntimeException("Missing ENDORDER.");

            for (Order order : orders) {
                for (String productId : order.getItems().keySet()) {
                    if (!inventory.containsKey(productId))
                        throw new RuntimeException("Unknown product in order: " + productId);
                }
                order.computeAcceptance(inventory);
            }

        } catch (RuntimeException e) {
            System.out.println("ERROR " + e.getMessage());
            return;
        }

        System.out.println("STORE|" + storeName);
        System.out.println();
        for (Order order : orders) {
            System.out.println("ORDER|" + order.getOrderId() + "|" + order.getStatus() + "|" + order.getTotalPrice());
            for (var entry : order.getItems().entrySet()) {
                System.out.println("ITEM|" + entry.getKey() + "|" + entry.getValue());
            }
            if (!order.getDiscountCode().isEmpty()) {
                System.out.println("DISCOUNT|" + order.getDiscountCode().first());
            }
            System.out.println("ENDORDER");
            System.out.println();
        }
        System.out.println("INVENTORY|" + balance);
        for (Product p : inventory.values()) {
            System.out.println("PRODUCT|" + p.getProductId() + "|" + p.getName() +
                    "|" + p.getPrice() + "|" + p.getType() + "|" + p.getCode() + "|" + p.getStock());
        }
        System.out.println("ENDINVENTORY");
    }
}