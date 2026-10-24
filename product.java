
public class Product {
    private final int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }

        this.id = id;
        setName(name);
        setPrice(price);
        setStock(stock);
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

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException(
                    "Price must be a valid non-negative number."
            );
        }
        this.price = price;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public boolean reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            return false;
        }

        stock -= quantity;
        return true;
    }

    public void displayInfo() {
        System.out.println("Product ID: " + id);
        System.out.println("Name: " + name);
        System.out.printf("Price: %.2f EUR%n", price);
        System.out.println("Stock: " + stock);
    }
}
