// Main entry point for the assignment3-warmup project
import java.util.Scanner;
import java.util.TreeMap;

public class CygnusMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String storeName = null;
        int balance = 0;
        TreeMap<String, Product> inventory = new TreeMap<>();

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
                    String id = fields[1];
                    if (inventory.containsKey(id)) {
                        throw new RuntimeException("Duplicate product id.");
                    }
                    Product product = new Product(id, fields[2], Integer.parseInt(fields[3]), fields[4], fields[5], Integer.parseInt(fields[6]));
                    inventory.put(id, product);
                }

                if (fields[0].equals("ENDINVENTORY")) break;

                if (fields[0].equals("ORDER")) {

                }
            }
        }

        catch (RuntimeException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }
}