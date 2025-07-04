import java.util.*;

/**
 * MIKE'S TEA SHOP - MONOLITHIC IMPLEMENTATION
 *
 * This implementation combines all architectural layers in a single class.
 * In a proper Spring Boot application, these would be separated into distinct layers.
 */
public class MikeTeaShop {

    // ===================== DATA LAYER (PERSISTENCE) =====================
    // These Maps serve as in-memory database tables
    // In a layered architecture, these would be replaced with:
    // - Database tables
    // - Repository interfaces
    // - Entity classes

    static Map<String, Double> teaMap = new HashMap<>();          // Tea "table"
    static Map<String, Double> materialMap = new HashMap<>();     // Material "table"
    static Map<String, Double> sweetLevelMap = new HashMap<>();   // Sweet level "table"

    static {
        // Initialize default teas
        teaMap.put("black tea", 3.0);
        teaMap.put("green tea", 3.5);
        teaMap.put("oolong tea", 4.0);

        // Initialize default materials
        materialMap.put("pearl", 0.75);
        materialMap.put("coconut", 0.50);
        materialMap.put("honey bean", 0.80);

        // Initialize sweet levels (discount/surcharge)
        sweetLevelMap.put("100%", 0.0);
        sweetLevelMap.put("50%", 0.0);
        sweetLevelMap.put("30%", 0.0);
        sweetLevelMap.put("0%", -0.50); // 50 cents discount
    }

    // ===================== SERVICE LAYER (BUSINESS LOGIC) =====================
    // These methods contain the core business logic
    // In a layered architecture, these would be in @Service classes

    /**
     * Tea management service methods
     */
    public static void addTea(String name, double price) {
        teaMap.put(name.toLowerCase(), price);
        System.out.println("Added tea: " + name + " ($" + String.format("%.2f", price) + ")");
    }

    public static boolean removeTea(String name) {
        Double removed = teaMap.remove(name.toLowerCase());
        if (removed != null) {
            System.out.println("Removed tea: " + name + " (was $" + String.format("%.2f", removed) + ")");
            return true;
        }
        System.out.println("Tea not found: " + name);
        return false;
    }

    /**
     * Material management service methods
     */
    public static void addMaterial(String name, double price) {
        materialMap.put(name.toLowerCase(), price);
        System.out.println("Added material: " + name + " ($" + String.format("%.2f", price) + ")");
    }

    public static boolean removeMaterial(String name) {
        Double removed = materialMap.remove(name.toLowerCase());
        if (removed != null) {
            System.out.println("Removed material: " + name + " (was $" + String.format("%.2f", removed) + ")");
            return true;
        }
        System.out.println("Material not found: " + name);
        return false;
    }

    /**
     * Sweet level management service methods
     */
    public static void addSweetLevel(String name, double priceAdjustment) {
        sweetLevelMap.put(name.toLowerCase(), priceAdjustment);
        String type = priceAdjustment < 0 ? "discount" : priceAdjustment > 0 ? "surcharge" : "no change";
        System.out.println("Added sweet level: " + name + " (" + type + " $" + String.format("%.2f", Math.abs(priceAdjustment)) + ")");
    }

    public static boolean removeSweetLevel(String name) {
        Double removed = sweetLevelMap.remove(name.toLowerCase());
        if (removed != null) {
            System.out.println("Removed sweet level: " + name);
            return true;
        }
        System.out.println("Sweet level not found: " + name);
        return false;
    }

    // ===================== PRESENTATION LAYER (VIEW/CONTROLLER) =====================
    // These methods handle display and user interaction
    // In a layered architecture, these would be split into:
    // - @Controller classes (for input handling)
    // - View templates or DTOs (for output)

    /**
     * Displays the current menu (View functionality)
     */
    public static void showMenu() {
        System.out.println("\n=== MIKE'S TEA SHOP MENU ===");

        System.out.println("\nTEAS:");
        teaMap.forEach((tea, price) ->
                System.out.println("  " + capitalizeWords(tea) + " - $" + String.format("%.2f", price)));

        System.out.println("\nMATERIALS:");
        materialMap.forEach((material, price) ->
                System.out.println("  " + capitalizeWords(material) + " - +$" + String.format("%.2f", price)));

        System.out.println("\nSWEET LEVELS:");
        sweetLevelMap.forEach((level, adjustment) -> {
            String adj = adjustment == 0 ? "" :
                    adjustment < 0 ? " (-$" + String.format("%.2f", Math.abs(adjustment)) + ")" :
                            " (+$" + String.format("%.2f", adjustment) + ")";
            System.out.println("  " + level + adj);
        });

        System.out.println("=============================\n");
    }

    /**
     * Helper method for formatting display strings
     */
    private static String capitalizeWords(String str) {
        return Arrays.stream(str.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }

    // ===================== CORE BUSINESS SERVICE =====================
    /**
     * Calculates order price with detailed breakdown
     * Combines:
     * - Controller logic (input handling)
     * - Service logic (price calculation)
     * - View logic (console output)
     */
    public static double calculatePriceWithBreakdown(String input) {
        // Input validation (Controller aspect)
        String[] parts = input.toLowerCase().split(",\\s*");
        System.out.println("\n--- Order: " + input + " ---");

        if (parts.length < 2) {
            System.out.println("Invalid Order Format. Need at least tea and sweetness level.");
            return 0.0;
        }

        // Tea validation and pricing (Service aspect)
        String tea = parts[0].trim();
        if (!teaMap.containsKey(tea)) {
            System.out.println("Invalid tea: " + tea + ". Available: " + teaMap.keySet());
            return 0.0;
        }

        double teaPrice = teaMap.get(tea);
        double materialCost = 0.0;
        List<String> materials = new ArrayList<>();

        System.out.println("Tea: " + capitalizeWords(tea) + " - $" + String.format("%.2f", teaPrice));

        // Material handling (Service aspect)
        for (int i = 1; i < parts.length - 1; i++) {
            String material = parts[i].trim();
            if (materialMap.containsKey(material)) {
                double price = materialMap.get(material);
                materialCost += price;
                materials.add(material);
                System.out.println("+ " + capitalizeWords(material) + " - $" + String.format("%.2f", price));
            } else {
                System.out.println("Invalid material: " + material + ". Available: " + materialMap.keySet());
                return 0.0;
            }
        }

        // Sweetness level handling (Service aspect)
        String sweetness = parts[parts.length - 1].trim();
        if (!sweetLevelMap.containsKey(sweetness)) {
            System.out.println("Invalid sweetness level: " + sweetness + ". Available: " + sweetLevelMap.keySet());
            return 0.0;
        }

        double sweetAdjustment = sweetLevelMap.get(sweetness);
        System.out.println("Sweet Level: " + sweetness);

        // Display adjustments (View aspect)
        if (sweetAdjustment < 0) {
            System.out.println("Discount: -$" + String.format("%.2f", Math.abs(sweetAdjustment)));
        } else if (sweetAdjustment > 0) {
            System.out.println("Surcharge: +$" + String.format("%.2f", sweetAdjustment));
        }

        // Price calculation (Core business logic)
        double totalPrice = Math.max(teaPrice + materialCost + sweetAdjustment, 0.50);
        System.out.println("-------------------------");
        return totalPrice;
    }

    // ===================== APPLICATION LAYER =====================
    /**
     * Main method - serves as application controller
     * Orchestrates the workflow between all components
     * In a layered architecture, this would be replaced with:
     * - Proper Spring Boot application startup
     * - Dependency injection
     * - REST endpoints or proper UI framework
     */
    public static void main(String[] args) {
        System.out.println("=== MIKE'S TEA SHOP - DYNAMIC SYSTEM ===\n");

        // Show initial menu
        showMenu();

        // Test original functionality
        System.out.println("=== ORIGINAL ORDERS ===");
        System.out.println("Price: $" + String.format("%.2f", calculatePriceWithBreakdown("black tea, 0%")));
        System.out.println("Price: $" + String.format("%.2f", calculatePriceWithBreakdown("green tea, pearl, coconut, 0%")));
        System.out.println("Price: $" + String.format("%.2f", calculatePriceWithBreakdown("oolong tea, coconut, 50%")));

        // Demo: Add new items
        System.out.println("\n=== ADDING NEW ITEMS ===");
        addTea("Earl Grey", 3.25);
        addTea("Jasmine Tea", 3.75);
        addMaterial("Tapioca", 0.60);
        addMaterial("Jelly", 0.45);
        addSweetLevel("75%", 0.0);
        addSweetLevel("Extra Sweet", 0.25); // 25 cent surcharge

        showMenu();

        // Test new items
        System.out.println("=== ORDERS WITH NEW ITEMS ===");
        calculatePriceWithBreakdown("Earl Grey, Tapioca, Jelly, 75%");
        calculatePriceWithBreakdown("Jasmine Tea, Pearl, Extra Sweet");

        // Demo: Remove items
        System.out.println("\n=== REMOVING ITEMS ===");
        removeTea("Earl Grey");
        removeMaterial("Jelly");
        removeSweetLevel("Extra Sweet");

        showMenu();

        // Test invalid orders
        System.out.println("\n=== TESTING INVALID ORDERS ===");
        calculatePriceWithBreakdown("uyuyuy,50%");
        calculatePriceWithBreakdown("Green Tea, Invalid Material, 50%");
        calculatePriceWithBreakdown("Green Tea, Invalid Sweet Level");
    }
}