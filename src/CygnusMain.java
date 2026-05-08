// Main entry point for the assignment3-warmup project
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class CygnusMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String storeName = null;
        int balance = 0;

        TreeMap<String, Product> inventory = new TreeMap<>();
        List<Order> orders = new ArrayList<>();
        String currentOrderId = null;
        TreeMap<String, Integer> currentItems = null;
        TreeSet<String> currentDiscounts = null;
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

                if (fields[0].equals("INVENTORY")) {
                    balance = Integer.parseInt(fields[1]);
                }

                if (fields[0].equals("PRODUCT")) {
                    String productId = fields[1];
                    Product product = getProduct(fields, inventory, productId);
                    inventory.put(productId, product);
                }

                if (fields[0].equals("ENDINVENTORY")) {
                    inventoryEnded = true;
                }

                if (fields[0].equals("ORDER")) {
                    currentOrderId = fields[1];
                    currentItems = new TreeMap<>();
                    currentDiscounts = new TreeSet<>();
                }

                if (fields[0].equals("ITEM")) {
                    if (currentItems == null) throw new RuntimeException("ITEM outside of ORDER.");

                    String productId = fields[1];
                    int quantity = Integer.parseInt(fields[2]);
                    currentItems.put(productId, currentItems.getOrDefault(productId, 0) + quantity);
                }

                if (fields[0].equals("DISCOUNT")) {
                    if (currentDiscounts == null) throw new RuntimeException("DISCOUNT outside of ORDER.");

                    currentDiscounts.add(fields[1]);
                }

                if (fields[0].equals("ENDORDER")) {
                    orders.add(new Order(currentOrderId, currentItems, currentDiscounts));
                    currentOrderId = null;
                    currentItems = null;
                    currentDiscounts = null;
                } else if (!fields[0].equals("STORE") && !fields[0].equals("INVENTORY") &&
                           !fields[0].equals("ENDINVENTORY") && !fields[0].equals("ORDER") &&
                           !fields[0].equals("ITEM") && !fields[0].equals("DISCOUNT") &&
                           !fields[0].equals("PRODUCT")) {
                    throw new RuntimeException("Unknown record: " + fields[0]);
                }
            }
            if (!inventoryEnded) {
                throw new RuntimeException("Missing ENDINVENTORY.");
            }
            if (currentOrderId != null) {
                throw new RuntimeException("Missing ENDORDER.");
            }
        }
        catch (RuntimeException e) {
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

    private static Product getProduct(String[] fields, TreeMap<String, Product> inventory, String productId) {
        String name = fields[2];
        String type = fields[4];
        String promoCode = fields[5];
        int price = Integer.parseInt(fields[3]);
        int stock = Integer.parseInt(fields[6]);

        if (inventory.containsKey(productId)) {
            throw new RuntimeException("Duplicate product id.");
        }

        if (!(type.equals("book") || type.equals("children") || type.equals("stationary") || type.equals("game"))) {
            throw new RuntimeException("Invalid product type.");
        }

        if (!(promoCode.equals("_") || promoCode.matches("c?f?h?r?t?"))) {
            throw new RuntimeException("Invalid promo code.");
        }

        if (!(price > 0 && stock >= 0)) {
            throw new RuntimeException("Invalid product price or product is out of stock.");
        }

        return new Product(productId,
                            name,
                            price,
                            type,
                            promoCode,
                            stock);
    }
}