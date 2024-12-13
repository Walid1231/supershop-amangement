import java.util.ArrayList;
import java.util.Scanner;

// Base Product class
class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void displayProduct() {
        System.out.printf("| %-8d | %-20s | %-10.2f | %-10d |\n", id, name, price, stock);
    }
}

// Specialized Product: PerishableProduct
class ExpireableProduct extends Product {
    private String expiryDate;

    public ExpireableProduct(int id, String name, double price, int stock, String expiryDate) {
        super(id, name, price, stock);
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void displayProduct() {
        super.displayProduct();
        System.out.printf("  Expiry Date: %s\n", expiryDate);
    }
}

// ElectronicProduct
class ElectronicProduct extends Product {
    private int warrantyPeriod; // in months
    private int powerUsage;     // in watts

    public ElectronicProduct(int id, String name, double price, int stock, int warrantyPeriod, int powerUsage) {
        super(id, name, price, stock);
        this.warrantyPeriod = warrantyPeriod;
        this.powerUsage = powerUsage;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    @Override
    public void displayProduct() {
        super.displayProduct();
        System.out.printf("  Warranty: %d months, Power Usage: %dW\n", warrantyPeriod, powerUsage);
    }
}

// Sale class
class Sale {
    private int productId;
    private String productName;
    private int quantity;
    private double totalCost;

    public Sale(int productId, String productName, int quantity, double totalCost) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    public void displaySale() {
        System.out.printf("| %-8d | %-20s | %-10d | %-12.2f |\n", productId, productName, quantity, totalCost);
    }
}

// Main application class
public class Main {
    private static ArrayList<Product> productList = new ArrayList<>();
    private static ArrayList<Sale> salesHistory = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=============================");
            System.out.println(" Super Shop Management System ");
            System.out.println("=============================");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Stock");
            System.out.println("4. Sell Product");
            System.out.println("5. Search Product by Name");
            System.out.println("6. View Low Stock Products");
            System.out.println("7. Delete Product");
            System.out.println("8. View Sales History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = readIntInput();

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    updateStock();
                    break;
                case 4:
                    sellProduct();
                    break;
                case 5:
                    searchProductByName();
                    break;
                case 6:
                    viewLowStockProducts();
                    break;
                case 7:
                    deleteProduct();
                    break;
                case 8:
                    viewSalesHistory();
                    break;
                case 9:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

   private static void addProduct() {
    System.out.println("Select product type:");
    System.out.println("1. General Product");
    System.out.println("2. Expireable Product");
    System.out.println("3. Electronic Product");
    System.out.print("Enter choice: ");
    int type = readIntInput();

    System.out.print("Enter Product ID: ");
    int id = readIntInput();

    // Check if a product with the same ID already exists
    if (findProductById(id) != null) {
        System.out.println("Error: A product with this ID already exists. Please enter a unique ID.");
        return; // Exit the method if the ID is already taken
    }

    scanner.nextLine(); // Consume leftover newline
    System.out.print("Enter Product Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Product Price: ");
    double price = readDoubleInput();
    scanner.nextLine(); // Consume leftover newline after reading double
    System.out.print("Enter Stock Quantity: ");
    int stock = readIntInput();
    scanner.nextLine(); // Consume leftover newline

    switch (type) {
        case 1:
            productList.add(new Product(id, name, price, stock));
            break;
        case 2:
            System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
            String expiryDate = scanner.nextLine().trim();
            productList.add(new ExpireableProduct(id, name, price, stock, expiryDate));
            break;
        case 3:
            System.out.print("Enter Warranty Period (months): ");
            int warrantyPeriod = readIntInput();
            System.out.print("Enter Power Usage (watts): ");
            int powerUsage = readIntInput();
            productList.add(new ElectronicProduct(id, name, price, stock, warrantyPeriod, powerUsage));
            break;
        default:
            System.out.println("Invalid choice! Product not added.");
            return;
    }
    System.out.println("Product added successfully!");
}


   private static void viewProducts() {
    if (productList.isEmpty()) {
        System.out.println("No products available.");
        return;
    }
    
    // Display the table header
    System.out.printf("\n| %-8s | %-20s | %-10s | %-10s | %-15s |\n", "ID", "Name", "Price", "Stock", "Additional Info");
    System.out.println("+----------+----------------------+------------+------------+-----------------+");

    try {
        // Loop through each product in the list
        for (Product product : productList) {
            if (product instanceof ExpireableProduct) {
                ExpireableProduct expProduct = (ExpireableProduct) product;
                System.out.printf("| %-8d | %-20s | %-10.2f | %-10d | %-15s |\n", 
                    expProduct.getId(), expProduct.getName(), expProduct.getPrice(), expProduct.getStock(), 
                    "Expiry: " + (expProduct.getExpiryDate() != null ? expProduct.getExpiryDate() : "N/A"));
            } else if (product instanceof ElectronicProduct) {
                ElectronicProduct elecProduct = (ElectronicProduct) product;
                System.out.printf("| %-8d | %-20s | %-10.2f | %-10d | %-15s |\n", 
                    elecProduct.getId(), elecProduct.getName(), elecProduct.getPrice(), elecProduct.getStock(), 
                    "Warranty: " + elecProduct.getWarrantyPeriod() + "M");
            } else {
                // General Product
                System.out.printf("| %-8d | %-20s | %-10.2f | %-10d | %-15s |\n", 
                    product.getId(), product.getName(), product.getPrice(), product.getStock(), "-");
            }
        }
    } catch (Exception e) {
        // Catch any unexpected runtime exceptions and display an error
        System.out.println("\nError displaying products: " + e.getMessage());
        e.printStackTrace(); // Optional: Print stack trace for debugging
    }
}


    private static void updateStock() {
    int id = -1;
    boolean validInput = false;
    
    // Loop until a valid product ID is entered
    while (!validInput) {
        System.out.print("Enter Product ID to update stock: ");
        id = readIntInput();

        Product product = findProductById(id);
        if (product == null) {
            System.out.println("Product not found! Please try again.");
        } else {
            validInput = true;
        }
    }

    validInput = false;
    int newStock = -1;

    // Loop until a valid new stock quantity is entered
    while (!validInput) {
        System.out.print("Enter new stock quantity: ");
        newStock = readIntInput();
        
        if (newStock < 0) {
            System.out.println("Stock quantity cannot be negative! Please enter a valid quantity.");
        } else {
            validInput = true;
        }
    }

    // Update stock and confirm success
    Product product = findProductById(id);
    product.setStock(newStock);
    System.out.println("Stock updated successfully!");
}


   private static void sellProduct() {
    int id = -1;
    boolean validInput = false;
    
    // Loop until a valid product ID is entered
    while (!validInput) {
        System.out.print("Enter Product ID to sell: ");
        id = readIntInput();

        Product product = findProductById(id);
        if (product == null) {
            System.out.println("Product not found! Please try again.");
        } else {
            validInput = true;
        }
    }

    validInput = false;
    int quantity = -1;

    // Loop until a valid quantity is entered
    while (!validInput) {
        System.out.print("Enter quantity to sell: ");
        quantity = readIntInput();

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0! Please enter a valid quantity.");
        } else {
            Product product = findProductById(id);
            if (quantity > product.getStock()) {
                System.out.println("Insufficient stock! Please enter a lower quantity.");
            } else {
                validInput = true;
            }
        }
    }

    // Perform the sale and update stock
    Product product = findProductById(id);
    product.setStock(product.getStock() - quantity);
    double totalCost = quantity * product.getPrice();
    salesHistory.add(new Sale(id, product.getName(), quantity, totalCost));
    System.out.printf("Product sold! Total cost: %.2f\n", totalCost);
}

    private static void searchProductByName() {
        System.out.print("Enter product name to search: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();

        boolean found = false;
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                product.displayProduct();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No products found with the given name.");
        }
    }

    private static void viewLowStockProducts() {
        System.out.print("Enter stock threshold: ");
        int threshold = readIntInput();

        boolean found = false;
        System.out.printf("\n| %-8s | %-20s | %-10s | %-10s |\n", "ID", "Name", "Price", "Stock");
        System.out.println("+----------+----------------------+------------+------------+");
        for (Product product : productList) {
            if (product.getStock() < threshold) {
                product.displayProduct();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No products below the specified stock threshold.");
        }
    }

    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int id = readIntInput();

        Product product = findProductById(id);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        productList.remove(product);
        System.out.println("Product deleted successfully!");
    }

    private static void viewSalesHistory() {
        if (salesHistory.isEmpty()) {
            System.out.println("No sales history available.");
            return;
        }
        System.out.printf("\n| %-8s | %-20s | %-10s | %-12s |\n", "Product ID", "Product Name", "Quantity", "Total Cost");
        System.out.println("+----------+----------------------+------------+------------+");
        for (Sale sale : salesHistory) {
            sale.displaySale();
        }
    }

    private static Product findProductById(int id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // Clear the invalid input
        }
        return scanner.nextInt();
    }

    private static double readDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a decimal number: ");
            scanner.next(); // Clear the invalid input
        }
        return scanner.nextDouble();
    }
}s
