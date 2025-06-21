# Mike's Tea Shop Management System

![Mike's Tea Shop Banner](/tea.png)

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Exception Handling](#exception-handling)
- [Testing](#testing)
- [Version Comparison](#version-comparison)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Mike's Tea Shop Management System is a Java application designed to manage a tea shop's menu, process customer orders, and calculate prices with various customization options. The system comes in two versions:

1. **Basic Version**: Simple implementation with core functionality
2. **Enhanced Version**: Robust implementation with comprehensive exception handling and advanced features

## Features

### Core Functionality
- 🍵 Tea management (add/remove teas with prices)
- 🥥 Material management (add/remove ingredients)
- 🍯 Sweetness level configuration
- 💰 Order price calculation with detailed breakdown
- 📜 Menu display system

### Enhanced Version Extras
- ✅ Comprehensive exception handling
- 🔄 Smart price updates for existing items
- 🛡️ Input validation and error recovery
- 💬 Helpful error messages with suggestions
- ⚙️ Configuration management

## System Architecture

### Class Diagram (Enhanced Version)

![Mike's Tea Shop Banner2](/diagram.png)


### Data Storage
- Three `HashMap` collections store:
    - `teaMap`: Teas and base prices
    - `materialMap`: Additional materials and costs
    - `sweetLevelMap`: Sweetness levels and price adjustments

## Installation

1. Ensure you have Java JDK 8 or later installed
2. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/tea-shop-system.git
   ```
3. Compile the Java files:
   ```bash
   javac MikeTeaShop.java  # For basic version
   javac MikeTeaShop2.java # For enhanced version
   ```

## Usage

### Running the Application
```bash
java MikeTeaShop   # Basic version
java MikeTeaShop2  # Enhanced version
```

### Basic Operations
```java
// Add a new tea
addTea("Earl Grey", 3.25);

// Add a new material
addMaterial("Tapioca", 0.60);

// Process an order
double price = calculatePriceWithBreakdown("green tea, pearl, coconut, 50%");
```

### Example Output
```
=== MIKE'S TEA SHOP - DYNAMIC SYSTEM ===


=== MIKE'S TEA SHOP MENU ===

TEAS:
  Green Tea - $3.50
  Oolong Tea - $4.00
  Black Tea - $3.00

MATERIALS:
  Honey Bean - +$0.80
  Pearl - +$0.75
  Coconut - +$0.50

SWEET LEVELS:
  100%
  0% (-$0.50)
  30%
  50%
=============================

=== ORIGINAL ORDERS ===

--- Order: black tea, 0% ---
Tea: Black Tea - $3.00
Sweet Level: 0%
Discount: -$0.50
-------------------------
Price: $2.50

--- Order: green tea, pearl, coconut, 0% ---
Tea: Green Tea - $3.50
+ Pearl - $0.75
+ Coconut - $0.50
Sweet Level: 0%
Discount: -$0.50
-------------------------
Price: $4.25

--- Order: oolong tea, coconut, 50% ---
Tea: Oolong Tea - $4.00
+ Coconut - $0.50
Sweet Level: 50%
-------------------------
Price: $4.50

=== ADDING NEW ITEMS ===
Added tea: Earl Grey ($3.25)
Added tea: Jasmine Tea ($3.75)
Added material: Tapioca ($0.60)
Added material: Jelly ($0.45)
Added sweet level: 75% (no change $0.00)
Added sweet level: Extra Sweet (surcharge $0.25)
Updated tea: Earl Grey price from $3.25 to $3.50

=== MIKE'S TEA SHOP MENU ===

TEAS:
  Green Tea - $3.50
  Jasmine Tea - $3.75
  Oolong Tea - $4.00
  Black Tea - $3.00
  Earl Grey - $3.50

MATERIALS:
  Honey Bean - +$0.80
  Tapioca - +$0.60
  Jelly - +$0.45
  Pearl - +$0.75
  Coconut - +$0.50

SWEET LEVELS:
  100%
  extra sweet (+$0.25)
  0% (-$0.50)
  75%
  30%
  50%
=============================

=== ORDERS WITH NEW ITEMS ===

--- Order: Earl Grey, Tapioca, Jelly, 75% ---
Tea: Earl Grey - $3.50
+ Tapioca - $0.60
+ Jelly - $0.45
Sweet Level: 75%
-------------------------

--- Order: Jasmine Tea, Pearl, Extra Sweet ---
Tea: Jasmine Tea - $3.75
+ Pearl - $0.75
Sweet Level: extra sweet
Surcharge: +$0.25
-------------------------

=== REMOVING ITEMS ===
Removed tea: Earl Grey (was $3.50)
Removed material: Jelly (was $0.45)
Removed sweet level: Extra Sweet
Failed to remove item: Material 'Nonexistent Material' not found.

=== MIKE'S TEA SHOP MENU ===

TEAS:
  Green Tea - $3.50
  Jasmine Tea - $3.75
  Oolong Tea - $4.00
  Black Tea - $3.00

MATERIALS:
  Honey Bean - +$0.80
  Tapioca - +$0.60
  Pearl - +$0.75
  Coconut - +$0.50

SWEET LEVELS:
  100%
  0% (-$0.50)
  75%
  30%
  50%
=============================


=== TESTING INVALID ORDERS ===

1. Testing Invalid Order Format:

--- Order:  ---
ERROR: Invalid order format. Need at least tea and sweetness level.

--- Order: green tea ---
ERROR: Invalid order format. Need at least tea and sweetness level.

--- Order: ,50% ---
ERROR: Invalid tea: 
Available teas: [green tea, jasmine tea, oolong tea, black tea]

--- Order: green tea, ---
ERROR: Invalid order format. Need at least tea and sweetness level.

--- Order: green tea, ,50% ---
Tea: Green Tea - $3.50
ERROR: Invalid material: 
Available materials: [honey bean, tapioca, pearl, coconut]

2. Testing Invalid Tea:

--- Order: nonexistent tea,50% ---
ERROR: Invalid tea: nonexistent tea
Available teas: [green tea, jasmine tea, oolong tea, black tea]

--- Order: BLACK TEA WITH TYPO,50% ---
ERROR: Invalid tea: black tea with typo
Available teas: [green tea, jasmine tea, oolong tea, black tea]

--- Order: 12345,50% ---
ERROR: Invalid tea: 12345
Available teas: [green tea, jasmine tea, oolong tea, black tea]

3. Testing Invalid Materials:

--- Order: green tea, fake pearl,50% ---
Tea: Green Tea - $3.50
ERROR: Invalid material: fake pearl
Available materials: [honey bean, tapioca, pearl, coconut]

--- Order: black tea, pearl, invalid topping,50% ---
Tea: Black Tea - $3.00
+ Pearl - $0.75
ERROR: Invalid material: invalid topping
Available materials: [honey bean, tapioca, pearl, coconut]

--- Order: oolong tea, 123material,50% ---
Tea: Oolong Tea - $4.00
ERROR: Invalid material: 123material
Available materials: [honey bean, tapioca, pearl, coconut]

4. Testing Invalid Sweet Levels:

--- Order: green tea,20% ---
Tea: Green Tea - $3.50
ERROR: Invalid sweetness level: 20%
Available sweet levels: [100%, 0%, 75%, 30%, 50%]

--- Order: black tea,pearl,150% ---
Tea: Black Tea - $3.00
+ Pearl - $0.75
ERROR: Invalid sweetness level: 150%
Available sweet levels: [100%, 0%, 75%, 30%, 50%]

--- Order: oolong tea,coconut,no sugar ---
Tea: Oolong Tea - $4.00
+ Coconut - $0.50
ERROR: Invalid sweetness level: no sugar
Available sweet levels: [100%, 0%, 75%, 30%, 50%]

--- Order: green tea,pearl, ---
Tea: Green Tea - $3.50
ERROR: Invalid sweetness level: pearl
Available sweet levels: [100%, 0%, 75%, 30%, 50%]

5. Testing Edge Cases:

--- Order: green tea,pearl,50%,extra ---
Tea: Green Tea - $3.50
+ Pearl - $0.75
ERROR: Invalid material: 50%
Available materials: [honey bean, tapioca, pearl, coconut]

--- Order:   green tea  ,  pearl  ,  50%   ---
Tea: Green Tea - $3.50
+ Pearl - $0.75
Sweet Level: 50%
-------------------------

--- Order: green tea,pearl,pearl,50% ---
Tea: Green Tea - $3.50
+ Pearl - $0.75
+ Pearl - $0.75
Sweet Level: 50%
-------------------------

--- Order: green tea,50%,pearl ---
Tea: Green Tea - $3.50
ERROR: Invalid material: 50%
Available materials: [honey bean, tapioca, pearl, coconut]

6. Testing Case Sensitivity:

--- Order: GREEN TEA,PEARL,50% ---
Tea: Green Tea - $3.50
+ Pearl - $0.75
Sweet Level: 50%
-------------------------

--- Order: Green Tea,Coconut,30% ---
Tea: Green Tea - $3.50
+ Coconut - $0.50
Sweet Level: 30%
-------------------------
```

## Exception Handling (Enhanced Version)

The system throws custom exceptions for various error scenarios:

| Exception Class | Description | Example Trigger |
|----------------|------------|----------------|
| `InvalidTeaException` | Invalid tea specified | `calculatePrice("unknown tea,50%")` |
| `InvalidMaterialException` | Invalid material specified | `calculatePrice("green tea,unknown,50%")` |
| `InvalidSweetLevelException` | Invalid sweetness level | `calculatePrice("green tea,20%")` |
| `InvalidOrderFormatException` | Malformed order string | `calculatePrice("green tea")` |
| `ItemAlreadyExistsException` | Adding duplicate item | `addTea("green tea", 3.5)` |
| `ItemNotFoundException` | Removing non-existent item | `removeTea("unknown tea")` |

## Testing

The system includes comprehensive test cases:

```java
// Test invalid orders
calculatePriceWithBreakdown("");  // Empty input
calculatePriceWithBreakdown("green tea");  // Missing sweet level

// Test invalid teas
calculatePriceWithBreakdown("nonexistent tea,50%");

// Test invalid materials
calculatePriceWithBreakdown("green tea, fake pearl,50%");

// Test management operations
try {
    addTea("Earl Grey", 3.25);
    addTea("Earl Grey", 3.25); // Duplicate
} catch (ItemAlreadyExistsException e) {
    System.out.println(e.getMessage());
}
```

## Version Comparison

| Feature                | Basic Version | Enhanced Version |
|------------------------|---------------|------------------|
| Exception Handling     | Basic         | Comprehensive    |
| Tea Updates            | Overwrites    | Checks differences|
| Input Validation       | Simple        | Robust           |
| Error Messages         | Basic         | Detailed         |
| Case Handling          | Implicit      | Explicit         |
| Management Operations  | Simple        | Prevent duplicates|

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

---