import java.util.*;

public class MikeTeaShop {
    // Tea and material prices - now dynamic
    static Map<String, Double> teaMap = new HashMap<>();
    static Map<String, Double> materialMap = new HashMap<>();
    static Map<String, Double> sweetLevelMap = new HashMap<>();

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

    // ===================== MANAGEMENT METHODS =====================

    // Tea management
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

    // Material management
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

    // Sweet level management
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

    // ===================== DISPLAY METHODS =====================

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

    private static String capitalizeWords(String str) {
        return Arrays.stream(str.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }

    // ===================== PRICING CALCULATION =====================


    public static double calculatePriceWithBreakdown(String input) {
        String[] parts = input.toLowerCase().split(",\\s*");

        System.out.println("\n--- Order: " + input + " ---");

        if (parts.length < 2) {
            System.out.println("Invalid Order Format. Need at least tea and sweetness level.");
            return 0.0;
        }

        String tea = parts[0].trim();
        if (!teaMap.containsKey(tea)) {
            System.out.println("Invalid tea: " + tea + ". Available: " + teaMap.keySet());
            return 0.0;
        }

        double teaPrice = teaMap.get(tea);
        double materialCost = 0.0;
        List<String> materials = new ArrayList<>();

        System.out.println("Tea: " + capitalizeWords(tea) + " - $" + String.format("%.2f", teaPrice));

        // Handle materials
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

        // Handle sweetness
        String sweetness = parts[parts.length - 1].trim();
        if (!sweetLevelMap.containsKey(sweetness)) {
            System.out.println("Invalid sweetness level: " + sweetness + ". Available: " + sweetLevelMap.keySet());
            return 0.0;
        }

        double sweetAdjustment = sweetLevelMap.get(sweetness);
        System.out.println("Sweet Level: " + sweetness);

        if (sweetAdjustment < 0) {
            System.out.println("Discount: -$" + String.format("%.2f", Math.abs(sweetAdjustment)));
        } else if (sweetAdjustment > 0) {
            System.out.println("Surcharge: +$" + String.format("%.2f", sweetAdjustment));
        }

        double totalPrice = Math.max(teaPrice + materialCost + sweetAdjustment, 0.50);
        //System.out.println("TOTAL: $" + String.format("%.2f", totalPrice));
        System.out.println("-------------------------");
        return totalPrice;
    }


    // ===================== MAIN DEMO =====================

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