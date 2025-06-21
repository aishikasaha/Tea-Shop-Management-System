import java.util.*;

// Custom exceptions
class TeaShopException extends Exception {
    public TeaShopException(String message) {
        super(message);
    }
}

class InvalidTeaException extends TeaShopException {
    public InvalidTeaException(String teaName) {
        super("Invalid tea: " + teaName);
    }
}

class InvalidMaterialException extends TeaShopException {
    public InvalidMaterialException(String materialName) {
        super("Invalid material: " + materialName);
    }
}

class InvalidSweetLevelException extends TeaShopException {
    public InvalidSweetLevelException(String sweetLevel) {
        super("Invalid sweetness level: " + sweetLevel);
    }
}

class InvalidOrderFormatException extends TeaShopException {
    public InvalidOrderFormatException() {
        super("Invalid order format. Need at least tea and sweetness level.");
    }
}

class ItemAlreadyExistsException extends TeaShopException {
    public ItemAlreadyExistsException(String itemType, String itemName) {
        super(itemType + " '" + itemName + "' already exists.");
    }
}

class ItemNotFoundException extends TeaShopException {
    public ItemNotFoundException(String itemType, String itemName) {
        super(itemType + " '" + itemName + "' not found.");
    }
}

public class MikeTeaShop2 {
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
    public static void addTea(String name, double price) throws ItemAlreadyExistsException {
        String lowerName = name.toLowerCase();

        // Check if tea already exists
        if (teaMap.containsKey(lowerName)) {
            double existingPrice = teaMap.get(lowerName);

            // If price is different, update it
            if (Math.abs(existingPrice - price) > 0.001) {  // Using epsilon for double comparison
                teaMap.put(lowerName, price);
                System.out.println("Updated tea: " + name + " price from $" +
                        String.format("%.2f", existingPrice) + " to $" +
                        String.format("%.2f", price));
            } else {
                // If both name and price are the same, throw exception
                throw new ItemAlreadyExistsException("Tea", name + " with same price $" +
                        String.format("%.2f", price));
            }
        } else {
            // New tea - add to map
            teaMap.put(lowerName, price);
            System.out.println("Added tea: " + name + " ($" +
                    String.format("%.2f", price) + ")");
        }
    }

    public static boolean removeTea(String name) throws ItemNotFoundException {
        String lowerName = name.toLowerCase();
        if (!teaMap.containsKey(lowerName)) {
            throw new ItemNotFoundException("Tea", name);
        }
        Double removed = teaMap.remove(lowerName);
        System.out.println("Removed tea: " + name + " (was $" + String.format("%.2f", removed) + ")");
        return true;
    }

    // Material management
    public static void addMaterial(String name, double price) throws ItemAlreadyExistsException {
        String lowerName = name.toLowerCase();
        if (materialMap.containsKey(lowerName)) {
            throw new ItemAlreadyExistsException("Material", name);
        }
        materialMap.put(lowerName, price);
        System.out.println("Added material: " + name + " ($" + String.format("%.2f", price) + ")");
    }

    public static boolean removeMaterial(String name) throws ItemNotFoundException {
        String lowerName = name.toLowerCase();
        if (!materialMap.containsKey(lowerName)) {
            throw new ItemNotFoundException("Material", name);
        }
        Double removed = materialMap.remove(lowerName);
        System.out.println("Removed material: " + name + " (was $" + String.format("%.2f", removed) + ")");
        return true;
    }

    // Sweet level management
    public static void addSweetLevel(String name, double priceAdjustment) throws ItemAlreadyExistsException {
        String lowerName = name.toLowerCase();
        if (sweetLevelMap.containsKey(lowerName)) {
            throw new ItemAlreadyExistsException("Sweet level", name);
        }
        sweetLevelMap.put(lowerName, priceAdjustment);
        String type = priceAdjustment < 0 ? "discount" : priceAdjustment > 0 ? "surcharge" : "no change";
        System.out.println("Added sweet level: " + name + " (" + type + " $" + String.format("%.2f", Math.abs(priceAdjustment)) + ")");
    }

    public static boolean removeSweetLevel(String name) throws ItemNotFoundException {
        String lowerName = name.toLowerCase();
        if (!sweetLevelMap.containsKey(lowerName)) {
            throw new ItemNotFoundException("Sweet level", name);
        }
        sweetLevelMap.remove(lowerName);
        System.out.println("Removed sweet level: " + name);
        return true;
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
        try {
            String[] parts = input.toLowerCase().split(",\\s*");

            System.out.println("\n--- Order: " + input + " ---");

            if (parts.length < 2) {
                throw new InvalidOrderFormatException();
            }

            String tea = parts[0].trim();
            if (!teaMap.containsKey(tea)) {
                throw new InvalidTeaException(tea);
            }

            double teaPrice = teaMap.get(tea);
            double materialCost = 0.0;
            List<String> materials = new ArrayList<>();

            System.out.println("Tea: " + capitalizeWords(tea) + " - $" + String.format("%.2f", teaPrice));

            // Handle materials
            for (int i = 1; i < parts.length - 1; i++) {
                String material = parts[i].trim();
                if (!materialMap.containsKey(material)) {
                    throw new InvalidMaterialException(material);
                }
                double price = materialMap.get(material);
                materialCost += price;
                materials.add(material);
                System.out.println("+ " + capitalizeWords(material) + " - $" + String.format("%.2f", price));
            }

            // Handle sweetness
            String sweetness = parts[parts.length - 1].trim();
            if (!sweetLevelMap.containsKey(sweetness)) {
                throw new InvalidSweetLevelException(sweetness);
            }

            double sweetAdjustment = sweetLevelMap.get(sweetness);
            System.out.println("Sweet Level: " + sweetness);

            if (sweetAdjustment < 0) {
                System.out.println("Discount: -$" + String.format("%.2f", Math.abs(sweetAdjustment)));
            } else if (sweetAdjustment > 0) {
                System.out.println("Surcharge: +$" + String.format("%.2f", sweetAdjustment));
            }

            double totalPrice = Math.max(teaPrice + materialCost + sweetAdjustment, 0.50);
            System.out.println("-------------------------");
            return totalPrice;
        } catch (TeaShopException e) {
            System.out.println("ERROR: " + e.getMessage());
            if (e instanceof InvalidTeaException) {
                System.out.println("Available teas: " + teaMap.keySet());
            } else if (e instanceof InvalidMaterialException) {
                System.out.println("Available materials: " + materialMap.keySet());
            } else if (e instanceof InvalidSweetLevelException) {
                System.out.println("Available sweet levels: " + sweetLevelMap.keySet());
            }
            return 0.0;
        }
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
        try {
            addTea("Earl Grey", 3.25);
            addTea("Jasmine Tea", 3.75);
            addMaterial("Tapioca", 0.60);
            addMaterial("Jelly", 0.45);
            addSweetLevel("75%", 0.0);
            addSweetLevel("Extra Sweet", 0.25); // 25 cent surcharge

            // This will throw an exception
            addTea("Earl Grey", 3.50);
        } catch (TeaShopException e) {
            System.out.println("Failed to add item: " + e.getMessage());
        }

        showMenu();

        // Test new items
        System.out.println("=== ORDERS WITH NEW ITEMS ===");
        calculatePriceWithBreakdown("Earl Grey, Tapioca, Jelly, 75%");
        calculatePriceWithBreakdown("Jasmine Tea, Pearl, Extra Sweet");

        // Demo: Remove items
        System.out.println("\n=== REMOVING ITEMS ===");
        try {
            removeTea("Earl Grey");
            removeMaterial("Jelly");
            removeSweetLevel("Extra Sweet");

            // This will throw an exception
            removeMaterial("Nonexistent Material");
        } catch (TeaShopException e) {
            System.out.println("Failed to remove item: " + e.getMessage());
        }

        showMenu();

        // Test invalid orders
        System.out.println("\n=== TESTING INVALID ORDERS ===");

// Test InvalidOrderFormatException cases
        System.out.println("\n1. Testing Invalid Order Format:");
        calculatePriceWithBreakdown("");  // Empty input
        calculatePriceWithBreakdown("green tea");  // Missing sweet level
        calculatePriceWithBreakdown(",50%");  // Missing tea
        calculatePriceWithBreakdown("green tea,");  // Trailing comma
        calculatePriceWithBreakdown("green tea, ,50%");  // Empty material

// Test InvalidTeaException cases
        System.out.println("\n2. Testing Invalid Tea:");
        calculatePriceWithBreakdown("nonexistent tea,50%");
        calculatePriceWithBreakdown("BLACK TEA WITH TYPO,50%");
        calculatePriceWithBreakdown("12345,50%");

// Test InvalidMaterialException cases
        System.out.println("\n3. Testing Invalid Materials:");
        calculatePriceWithBreakdown("green tea, fake pearl,50%");
        calculatePriceWithBreakdown("black tea, pearl, invalid topping,50%");
        calculatePriceWithBreakdown("oolong tea, 123material,50%");

// Test InvalidSweetLevelException cases
        System.out.println("\n4. Testing Invalid Sweet Levels:");
        calculatePriceWithBreakdown("green tea,20%");  // Doesn't exist
        calculatePriceWithBreakdown("black tea,pearl,150%");  // Too high
        calculatePriceWithBreakdown("oolong tea,coconut,no sugar");  // Wrong format
        calculatePriceWithBreakdown("green tea,pearl,");  // Empty sweet level

// Test edge cases
        System.out.println("\n5. Testing Edge Cases:");
        calculatePriceWithBreakdown("green tea,pearl,50%,extra");  // Extra parameter
        calculatePriceWithBreakdown("  green tea  ,  pearl  ,  50%  ");  // Extra whitespace (should work)
        calculatePriceWithBreakdown("green tea,pearl,pearl,50%");  // Duplicate material (should work)
        calculatePriceWithBreakdown("green tea,50%,pearl");  // Sweet level in wrong position

// Test case sensitivity
        System.out.println("\n6. Testing Case Sensitivity:");
        calculatePriceWithBreakdown("GREEN TEA,PEARL,50%");  // All uppercase (should work)
        calculatePriceWithBreakdown("Green Tea,Coconut,30%");  // Mixed case (should work)
    }
}