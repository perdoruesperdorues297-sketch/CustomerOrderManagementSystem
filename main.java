import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final ArrayList<Customer> customers = new ArrayList<>();
    private static final ArrayList<Product> products = new ArrayList<>();
    private static final ArrayList<Order> orders = new ArrayList<>();

    private static int nextCustomerId = 1;
    private static int nextProductId = 1;
    private static int nextOrderId = 1;

    public static void main(String[] args) {
        loadData();

        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> viewCustomers();
                case 3 -> registerProduct();
                case 4 -> viewProducts();
                case 5 -> createOrder();
                case 6 -> viewOrders();
                case 7 -> searchCustomer();
                case 8 -> searchProduct();
                case 9 -> editCustomer();
                case 10 -> editProduct();
                case 11 -> deleteCustomer();
                case 12 -> deleteProduct();
                case 13 -> saveData();
                case 14 -> updateOrderStatus();
                case 15 -> reportsMenu();
                case 0 -> {
                    saveData();
                    running = false;
                    System.out.println("Program closed.");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n=================================");
        System.out.println(" CUSTOMER & ORDER MANAGEMENT");
        System.out.println("=================================");
        System.out.println("1. Register customer");
        System.out.println("2. View customers");
        System.out.println("3. Register product");
        System.out.println("4. View products");
        System.out.println("5. Create order");
        System.out.println("6. View orders");
        System.out.println("7. Search customer by ID");
        System.out.println("8. Search product by ID");
        System.out.println("9. Edit customer");
        System.out.println("10. Edit product");
        System.out.println("11. Delete customer");
        System.out.println("12. Delete product");
        System.out.println("13. Save data");
        System.out.println("14. Update order status");
        System.out.println("15. Business reports & analytics");
        System.out.println("0. Save and exit");
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isBlank()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println("Enter a finite number.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number, such as 19.99.");
            }
        }
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private static Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    private static Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    private static boolean emailAlreadyExists(String email, int excludedId) {
        for (Customer customer : customers) {
            if (customer.getId() != excludedId
                    && customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private static void registerCustomer() {
        System.out.println("\n--- Register Customer ---");

        String name = readText("Name: ");
        String email = readText("Email: ");

        if (!isValidEmail(email)) {
            System.out.println("Please enter a valid email address.");
            return;
        }

        if (emailAlreadyExists(email, -1)) {
            System.out.println("A customer with this email already exists.");
            return;
        }

        String phone = readText("Phone: ");

        Customer customer = new Customer(
                nextCustomerId, name, email, phone
        );

        customers.add(customer);
        nextCustomerId++;

        System.out.println("Customer registered successfully.");
        System.out.println("Assigned customer ID: " + customer.getId());
        saveData();
    }

    private static void viewCustomers() {
        System.out.println("\n--- Customer List ---");

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        for (Customer customer : customers) {
            customer.displayInfo();
            System.out.println("--------------------");
        }
    }

    private static void registerProduct() {
        System.out.println("\n--- Register Product ---");

        String name = readText("Product name: ");
        double price = readDouble("Price (EUR): ");

        if (price < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }

        int stock = readInt("Initial stock: ");

        if (stock < 0) {
            System.out.println("Stock cannot be negative.");
            return;
        }

        Product product = new Product(
                nextProductId, name, price, stock
        );

        products.add(product);
        nextProductId++;

        System.out.println("Product registered successfully.");
        System.out.println("Assigned product ID: " + product.getId());
        saveData();
    }

    private static void viewProducts() {
        System.out.println("\n--- Product List ---");

        if (products.isEmpty()) {
            System.out.println("No products registered.");
            return;
        }

        for (Product product : products) {
            product.displayInfo();
            System.out.println("--------------------");
        }
    }

    private static void createOrder() {
        System.out.println("\n--- Create Order ---");

        if (customers.isEmpty() || products.isEmpty()) {
            System.out.println(
                    "Register at least one customer and one product first."
            );
            return;
        }

        viewCustomers();

        int customerId = readInt("Enter customer ID: ");
        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        viewProducts();

        int productId = readInt("Enter product ID: ");
        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        int quantity = readInt("Enter quantity: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        if (quantity > product.getStock()) {
            System.out.println(
                    "Insufficient stock. Available: " + product.getStock()
            );
            return;
        }

        Order order = new Order(
                nextOrderId, customer, product, quantity
        );

        if (!product.reduceStock(quantity)) {
            System.out.println(
                    "Unable to update stock. Order was not created."
            );
            return;
        }

        orders.add(order);
        nextOrderId++;

        System.out.println("Order created successfully.");
        order.displayInfo();
        System.out.println("Remaining stock: " + product.getStock());
        saveData();
    }

    private static void viewOrders() {
        System.out.println("\n--- Order History ---");

        if (orders.isEmpty()) {
            System.out.println("No orders have been created.");
            return;
        }

        for (Order order : orders) {
            order.displayInfo();
            System.out.println("--------------------");
        }
    }

    private static void updateOrderStatus() {
        System.out.println("\n--- Update Order Status ---");

        if (orders.isEmpty()) {
            System.out.println("No orders available to update.");
            return;
        }

        viewOrders();

        int id = readInt("Enter order ID: ");
        Order order = findOrderById(id);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        System.out.println("\nCurrent status: " + order.getStatus());
        System.out.println("Choose the new status:");
        System.out.println("1. Pending");
        System.out.println("2. Processing");
        System.out.println("3. Completed");
        System.out.println("4. Cancelled");
        System.out.println("0. Cancel");

        int choice = readInt("Choose a status: ");

        String newStatus;

        switch (choice) {
            case 1 -> newStatus = "Pending";
            case 2 -> newStatus = "Processing";
            case 3 -> newStatus = "Completed";
            case 4 -> newStatus = "Cancelled";
            case 0 -> {
                System.out.println("Status update cancelled.");
                return;
            }
            default -> {
                System.out.println("Invalid status option.");
                return;
            }
        }

        if (order.setStatus(newStatus)) {
            saveData();
            System.out.println("Order status updated to: " + newStatus);
        } else {
            System.out.println("Unable to update order status.");
        }
    }

    private static void reportsMenu() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n--- Business Reports & Analytics ---");
            System.out.println("1. Sales summary");
            System.out.println("2. Order statistics");
            System.out.println("3. Top-selling products");
            System.out.println("4. Customer activity");
            System.out.println("5. Inventory report");
            System.out.println("6. Sales report by date range");
            System.out.println("0. Return to main menu");

            int choice = readInt("Choose a report: ");

            switch (choice) {
                case 1 -> salesSummary();
                case 2 -> orderStatistics();
                case 3 -> topSellingProducts();
                case 4 -> customerActivity();
                case 5 -> inventoryReport();
                case 6 -> dateBasedReport();
                case 0 -> inReports = false;
                default -> System.out.println("Invalid report option.");
            }
        }
    }

    private static void salesSummary() {
        System.out.println("\n--- Sales Summary ---");

        int completedOrders = 0;
        int completedUnits = 0;
        double completedRevenue = 0.0;
        int nonCancelledOrders = 0;
        double nonCancelledOrderValue = 0.0;

        for (Order order : orders) {
            if (!order.getStatus().equals("Cancelled")) {
                nonCancelledOrders++;
                nonCancelledOrderValue += order.calculateTotal();
            }

            if (order.getStatus().equals("Completed")) {
                completedOrders++;
                completedUnits += order.getQuantity();
                completedRevenue += order.calculateTotal();
            }
        }

        System.out.println("Total orders recorded: " + orders.size());
        System.out.println("Orders not cancelled: " + nonCancelledOrders);

        System.out.printf(
                "Value of non-cancelled orders: %.2f EUR%n",
                nonCancelledOrderValue
        );

        System.out.println("Completed orders: " + completedOrders);
        System.out.println("Units in completed orders: " + completedUnits);

        System.out.printf(
                "Revenue from completed orders: %.2f EUR%n",
                completedRevenue
        );

        System.out.println("Note: revenue counts Completed orders only.");
    }

    private static void orderStatistics() {
        System.out.println("\n--- Order Statistics ---");

        int pending = 0;
        int processing = 0;
        int completed = 0;
        int cancelled = 0;

        for (Order order : orders) {
            switch (order.getStatus()) {
                case "Pending" -> pending++;
                case "Processing" -> processing++;
                case "Completed" -> completed++;
                case "Cancelled" -> cancelled++;
                default -> {
                    // Ignore unexpected statuses.
                }
            }
        }

        System.out.println("Total orders: " + orders.size());
        System.out.println("Pending: " + pending);
        System.out.println("Processing: " + processing);
        System.out.println("Completed: " + completed);
        System.out.println("Cancelled: " + cancelled);
    }

    private static void topSellingProducts() {
        System.out.println("\n--- Top-Selling Products ---");

        if (products.isEmpty()) {
            System.out.println("No products registered.");
            return;
        }

        java.util.Map<Integer, Integer> unitsSold =
                new java.util.HashMap<>();

        for (Order order : orders) {
            if (order.getStatus().equals("Completed")) {
                int productId = order.getProduct().getId();

                unitsSold.put(
                        productId,
                        unitsSold.getOrDefault(productId, 0)
                                + order.getQuantity()
                );
            }
        }

        ArrayList<Product> rankedProducts = new ArrayList<>(products);

        rankedProducts.sort((first, second) ->
                Integer.compare(
                        unitsSold.getOrDefault(second.getId(), 0),
                        unitsSold.getOrDefault(first.getId(), 0)
                )
        );

        int rank = 1;

        for (Product product : rankedProducts) {
            System.out.printf(
                    "%d. %s (ID %d) - %d units sold%n",
                    rank,
                    product.getName(),
                    product.getId(),
                    unitsSold.getOrDefault(product.getId(), 0)
            );

            rank++;
        }

        System.out.println("Based on Completed orders only.");
    }

    private static void customerActivity() {
        System.out.println("\n--- Customer Activity ---");

        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        java.util.Map<Integer, Integer> orderCounts =
                new java.util.HashMap<>();

        for (Order order : orders) {
            if (!order.getStatus().equals("Cancelled")) {
                int customerId = order.getCustomer().getId();

                orderCounts.put(
                        customerId,
                        orderCounts.getOrDefault(customerId, 0) + 1
                );
            }
        }

        ArrayList<Customer> rankedCustomers = new ArrayList<>(customers);

        rankedCustomers.sort((first, second) ->
                Integer.compare(
                        orderCounts.getOrDefault(second.getId(), 0),
                        orderCounts.getOrDefault(first.getId(), 0)
                )
        );

        int rank = 1;

        for (Customer customer : rankedCustomers) {
            System.out.printf(
                    "%d. %s (ID %d) - %d non-cancelled orders%n",
                    rank,
                    customer.getName(),
                    customer.getId(),
                    orderCounts.getOrDefault(customer.getId(), 0)
            );

            rank++;
        }
    }

    private static void inventoryReport() {
        System.out.println("\n--- Inventory Report ---");

        int totalUnits = 0;
        double inventoryValue = 0.0;
        int outOfStock = 0;
        int lowStock = 0;

        for (Product product : products) {
            totalUnits += product.getStock();
            inventoryValue += product.getPrice() * product.getStock();

            if (product.getStock() == 0) {
                outOfStock++;
            } else if (product.getStock() <= 5) {
                lowStock++;
            }
        }

        System.out.println("Registered products: " + products.size());
        System.out.println("Total units in stock: " + totalUnits);

        System.out.printf(
                "Current inventory value: %.2f EUR%n",
                inventoryValue
        );

        System.out.println("Out-of-stock products: " + outOfStock);
        System.out.println("Low-stock products (1-5 units): " + lowStock);

        if (products.isEmpty()) {
            System.out.println(
                    "No products are available for inventory analysis."
            );
            return;
        }

        System.out.println("\nStock details:");

        for (Product product : products) {
            System.out.printf(
                    "%s (ID %d): %d units at %.2f EUR each%n",
                    product.getName(),
                    product.getId(),
                    product.getStock(),
                    product.getPrice()
            );
        }
    }

    private static void dateBasedReport() {
        System.out.println("\n--- Sales Report by Date Range ---");

        LocalDate startDate = readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("End date (YYYY-MM-DD): ");

        if (endDate.isBefore(startDate)) {
            System.out.println(
                    "End date cannot be earlier than start date."
            );
            return;
        }

        int ordersInRange = 0;
        int completedOrders = 0;
        int completedUnits = 0;
        double completedRevenue = 0.0;

        for (Order order : orders) {
            LocalDate orderDate = order.getOrderDate();

            if (!orderDate.isBefore(startDate)
                    && !orderDate.isAfter(endDate)) {

                ordersInRange++;

                if (order.getStatus().equals("Completed")) {
                    completedOrders++;
                    completedUnits += order.getQuantity();
                    completedRevenue += order.calculateTotal();
                }
            }
        }

        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("Orders created in period: " + ordersInRange);
        System.out.println("Completed orders in period: " + completedOrders);
        System.out.println("Units in completed orders: " + completedUnits);

        System.out.printf(
                "Completed-order revenue: %.2f EUR%n",
                completedRevenue
        );

        System.out.println("Revenue includes Completed orders only.");
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return LocalDate.parse(input);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println(
                        "Enter a valid date in YYYY-MM-DD format."
                );
            }
        }
    }

    private static void searchCustomer() {
        System.out.println("\n--- Search Customer ---");

        int id = readInt("Enter customer ID: ");
        Customer customer = findCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        customer.displayInfo();
    }

    private static void searchProduct() {
        System.out.println("\n--- Search Product ---");

        int id = readInt("Enter product ID: ");
        Product product = findProductById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.displayInfo();
    }

    private static void editCustomer() {
        System.out.println("\n--- Edit Customer ---");

        int id = readInt("Enter customer ID: ");
        Customer customer = findCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        customer.displayInfo();

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Phone");
        System.out.println("0. Cancel");

        int choice = readInt("Choose a field: ");

        switch (choice) {
            case 1 -> {
                customer.setName(readText("New name: "));
                System.out.println("Customer name updated.");
                saveData();
            }
            case 2 -> {
                String email = readText("New email: ");

                if (!isValidEmail(email)) {
                    System.out.println("Invalid email format.");
                    return;
                }

                if (emailAlreadyExists(email, customer.getId())) {
                    System.out.println("This email is already in use.");
                    return;
                }

                customer.setEmail(email);
                System.out.println("Customer email updated.");
                saveData();
            }
            case 3 -> {
                customer.setPhone(readText("New phone: "));
                System.out.println("Customer phone updated.");
                saveData();
            }
            case 0 -> System.out.println("Edit cancelled.");
            default -> System.out.println("Invalid option.");
        }
    }

    private static void editProduct() {
        System.out.println("\n--- Edit Product ---");

        int id = readInt("Enter product ID: ");
        Product product = findProductById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.displayInfo();

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. Price");
        System.out.println("3. Stock");
        System.out.println("0. Cancel");

        int choice = readInt("Choose a field: ");

        switch (choice) {
            case 1 -> {
                product.setName(readText("New product name: "));
                System.out.println("Product name updated.");
                saveData();
            }
            case 2 -> {
                double price = readDouble("New price (EUR): ");

                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    return;
                }

                product.setPrice(price);
                System.out.println("Product price updated.");
                System.out.println(
                        "Existing orders retain their original unit price."
                );
                saveData();
            }
            case 3 -> {
                int stock = readInt("New stock quantity: ");

                if (stock < 0) {
                    System.out.println("Stock cannot be negative.");
                    return;
                }

                product.setStock(stock);
                System.out.println("Product stock updated.");
                saveData();
            }
            case 0 -> System.out.println("Edit cancelled.");
            default -> System.out.println("Invalid option.");
        }
    }

    private static boolean customerHasOrders(int customerId) {
        for (Order order : orders) {
            if (order.getCustomer().getId() == customerId) {
                return true;
            }
        }
        return false;
    }

    private static boolean productHasOrders(int productId) {
        for (Order order : orders) {
            if (order.getProduct().getId() == productId) {
                return true;
            }
        }
        return false;
    }

    private static void deleteCustomer() {
        System.out.println("\n--- Delete Customer ---");

        int id = readInt("Enter customer ID: ");
        Customer customer = findCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (customerHasOrders(id)) {
            System.out.println(
                    "Cannot delete this customer because order history references them."
            );
            return;
        }

        customer.displayInfo();

        String confirmation = readText(
                "Type YES to confirm deletion: "
        );

        if (!confirmation.equals("YES")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        customers.remove(customer);
        saveData();
        System.out.println("Customer deleted successfully.");
    }

    private static void deleteProduct() {
        System.out.println("\n--- Delete Product ---");

        int id = readInt("Enter product ID: ");
        Product product = findProductById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (productHasOrders(id)) {
            System.out.println(
                    "Cannot delete this product because order history references it."
            );
            return;
        }

        product.displayInfo();

        String confirmation = readText(
                "Type YES to confirm deletion: "
        );

        if (!confirmation.equals("YES")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        products.remove(product);
        saveData();
        System.out.println("Product deleted successfully.");
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decode(String value) {
        return new String(
                Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }

    private static void saveData() {
        try {
            ArrayList<String> customerLines = new ArrayList<>();

            for (Customer customer : customers) {
                customerLines.add(
                        customer.getId() + "|"
                        + encode(customer.getName()) + "|"
                        + encode(customer.getEmail()) + "|"
                        + encode(customer.getPhone())
                );
            }

            ArrayList<String> productLines = new ArrayList<>();

            for (Product product : products) {
                productLines.add(
                        product.getId() + "|"
                        + encode(product.getName()) + "|"
                        + product.getPrice() + "|"
                        + product.getStock()
                );
            }

            ArrayList<String> orderLines = new ArrayList<>();

            for (Order order : orders) {
                orderLines.add(
                        order.getId() + "|"
                        + order.getCustomer().getId() + "|"
                        + order.getProduct().getId() + "|"
                        + order.getQuantity() + "|"
                        + order.getUnitPrice() + "|"
                        + order.getOrderDate() + "|"
                        + order.getStatus()
                );
            }

            DataStorage.saveTextFile("customers.txt", customerLines);
            DataStorage.saveTextFile("products.txt", productLines);
            DataStorage.saveTextFile("orders.txt", orderLines);

            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadData() {
        try {
            ArrayList<String> customerLines =
                    DataStorage.loadTextFile("customers.txt");

            ArrayList<String> productLines =
                    DataStorage.loadTextFile("products.txt");

            ArrayList<String> orderLines =
                    DataStorage.loadTextFile("orders.txt");

            for (String line : customerLines) {
                String[] parts = line.split("\\|", -1);

                if (parts.length != 4) {
                    throw new IOException(
                            "Invalid customer data format."
                    );
                }

                Customer customer = new Customer(
                        Integer.parseInt(parts[0]),
                        decode(parts[1]),
                        decode(parts[2]),
                        decode(parts[3])
                );

                if (findCustomerById(customer.getId()) != null
                        || emailAlreadyExists(customer.getEmail(), -1)) {
                    throw new IOException(
                            "Duplicate customer data detected."
                    );
                }

                customers.add(customer);

                nextCustomerId = Math.max(
                        nextCustomerId, customer.getId() + 1
                );
            }

            for (String line : productLines) {
                String[] parts = line.split("\\|", -1);

                if (parts.length != 4) {
                    throw new IOException(
                            "Invalid product data format."
                    );
                }

                Product product = new Product(
                        Integer.parseInt(parts[0]),
                        decode(parts[1]),
                        Double.parseDouble(parts[2]),
                        Integer.parseInt(parts[3])
                );

                if (findProductById(product.getId()) != null) {
                    throw new IOException(
                            "Duplicate product ID detected."
                    );
                }

                products.add(product);

                nextProductId = Math.max(
                        nextProductId, product.getId() + 1
                );
            }

            for (String line : orderLines) {
                String[] parts = line.split("\\|", -1);

                if (parts.length != 7) {
                    throw new IOException(
                            "Invalid order data format."
                    );
                }

                int orderId = Integer.parseInt(parts[0]);
                int customerId = Integer.parseInt(parts[1]);
                int productId = Integer.parseInt(parts[2]);
                int quantity = Integer.parseInt(parts[3]);

                Customer customer = findCustomerById(customerId);
                Product product = findProductById(productId);

                if (customer == null || product == null) {
                    throw new IOException(
                            "An order references a missing customer or product."
                    );
                }

                Order order = new Order(
                        orderId,
                        customer,
                        product,
                        quantity,
                        Double.parseDouble(parts[4]),
                        LocalDate.parse(parts[5]),
                        parts[6]
                );

                if (findOrderById(orderId) != null) {
                    throw new IOException(
                            "Duplicate order ID detected."
                    );
                }

                orders.add(order);

                nextOrderId = Math.max(
                        nextOrderId, order.getId() + 1
                );
            }

            System.out.println("Saved data loaded successfully.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println(
                    "Could not load saved data: " + e.getMessage()
            );

            System.out.println(
                    "Check the files in the data folder before saving again."
            );
        }
    }
}