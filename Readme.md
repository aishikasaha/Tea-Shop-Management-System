# Mike's Tea Shop Management System

![Mike's Tea Shop Banner](/tea.jpg)

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
=== MIKE'S TEA SHOP MENU ===

TEAS:
  Black Tea - $3.00
  Green Tea - $3.50
  Oolong Tea - $4.00

MATERIALS:
  Pearl - +$0.75
  Coconut - +$0.50
  Honey Bean - +$0.80

SWEET LEVELS:
  100%
  50%
  30%
  0% (-$0.50)
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