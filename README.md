# 🛒 SouqFX - JavaFX E-Commerce Application

A desktop e-commerce application built with **JavaFX** demonstrating core Java concepts including OOP, File I/O, and MVC architecture.

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=flat-square&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

## 📋 Table of Contents

- [Features](#-features)
- [Screenshots](#-screenshots)
- [Technologies Used](#-technologies-used)
- [Project Structure](#-project-structure)
- [Installation](#-installation)
- [Usage](#-usage)
- [Java Concepts Demonstrated](#-java-concepts-demonstrated)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- **Product Management**: Browse electronics and clothing products
- **Shopping Cart**: Add/remove items, view cart summary
- **Order Processing**: Create and manage customer orders
- **Data Persistence**: Save/load data using CSV files
- **Dual I/O Implementation**: Both Traditional I/O and NIO approaches

## 🖼️ Screenshots

_Coming soon_

## 🛠️ Technologies Used

- **Java 17+**
- **JavaFX 21** - GUI Framework
- **Maven** - Dependency Management & Build Tool
- **CSS** - UI Styling
- **CSV** - Data Storage

## 📁 Project Structure

```
SouqFX/
├── src/
│   └── main/
│       ├── java/
│       │   ├── app/
│       │   │   └── MainApp.java          # Application entry point
│       │   ├── controller/
│       │   │   ├── CartController.java   # Cart operations
│       │   │   ├── OrderController.java  # Order management
│       │   │   ├── ProductController.java# Product operations
│       │   │   └── NavigationHandler.java
│       │   ├── model/
│       │   │   ├── Product.java          # Base product class
│       │   │   ├── Electronics.java      # Electronics subclass
│       │   │   ├── Clothing.java         # Clothing subclass
│       │   │   ├── Order.java            # Order model
│       │   │   └── Payable.java          # Payment interface
│       │   ├── service/
│       │   │   ├── CartService.java      # Cart business logic
│       │   │   └── FileService.java      # File I/O operations
│       │   ├── ui/
│       │   │   └── OrderViewController.java
│       │   └── util/
│       │       ├── ConceptsDemo.java
│       │       └── OrderProcessor.java
│       └── resources/
│           └── fxml/
│               ├── cart-view.fxml
│               ├── order-view.fxml
│               └── product-view.fxml
├── data/
│   └── products.csv                      # Product data storage
├── pom.xml
└── README.md
```

## 🚀 Installation

### Prerequisites

- Java JDK 17 or higher
- Maven 3.8+

### Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/SouqFX.git
   cd SouqFX
   ```

2. **Build the project**

   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

## 📖 Usage

1. **Browse Products**: View available electronics and clothing items
2. **Add to Cart**: Select products and add them to your shopping cart
3. **Manage Cart**: Review items, update quantities, or remove products
4. **Place Order**: Enter customer details and complete your purchase
5. **View Orders**: Access order history and details

## 📚 Java Concepts Demonstrated

### Object-Oriented Programming (OOP)

| Concept           | Implementation                                |
| ----------------- | --------------------------------------------- |
| **Inheritance**   | `Electronics` and `Clothing` extend `Product` |
| **Polymorphism**  | Product list holds different product types    |
| **Encapsulation** | Private fields with getters/setters           |
| **Abstraction**   | `Payable` interface for payment processing    |

### File I/O

| Approach            | Classes Used                                                   |
| ------------------- | -------------------------------------------------------------- |
| **Traditional I/O** | `FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter` |
| **NIO (New I/O)**   | `Files`, `Paths`, `StandardOpenOption`                         |

### Other Concepts

- **MVC Architecture**: Separation of Model, View, and Controller
- **Interfaces**: `Payable` interface for payment behavior
- **Generics**: Type-safe collections (`List<Product>`)
- **Exception Handling**: Try-catch blocks, try-with-resources
- **Lambda Expressions**: Used in JavaFX event handlers

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Yassine**

- GitHub: [@yourusername](https://github.com/yourusername)

---

<p align="center">Made with ❤️ in Morocco 🇲🇦</p>
