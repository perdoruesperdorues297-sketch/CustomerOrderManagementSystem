
import java.time.LocalDate;
import java.util.Set;

public class Order {
    private static final Set<String> VALID_STATUSES =
            Set.of("Pending", "Processing", "Completed", "Cancelled");

    private final int id;
    private final Customer customer;
    private final Product product;
    private final int quantity;
    private final double unitPrice;
    private final LocalDate orderDate;
    private String status;

    public Order(int id, Customer customer, Product product, int quantity) {
        this(id, customer, product, quantity,
                product == null ? 0 : product.getPrice(),
                LocalDate.now(), "Pending");
    }

    public Order(
            int id,
            Customer customer,
            Product product,
            int quantity,
            double unitPrice,
            LocalDate orderDate,
            String status) {

        if (id <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }
        if (customer == null || product == null) {
            throw new IllegalArgumentException(
                    "Customer and product are required."
            );
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        if (!Double.isFinite(unitPrice) || unitPrice < 0) {
            throw new IllegalArgumentException("Invalid order unit price.");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date is required.");
        }

        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = orderDate;

        if (!setStatus(status)) {
            throw new IllegalArgumentException("Invalid order status.");
        }
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public double calculateTotal() {
        return unitPrice * quantity;
    }

    public boolean setStatus(String status) {
        if (status == null || !VALID_STATUSES.contains(status.trim())) {
            return false;
        }

        this.status = status.trim();
        return true;
    }

    public void displayInfo() {
        System.out.println("Order ID: " + id);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity: " + quantity);
        System.out.printf("Unit price at order time: %.2f EUR%n", unitPrice);
        System.out.printf("Total: %.2f EUR%n", calculateTotal());
        System.out.println("Date: " + orderDate);
        System.out.println("Status: " + status);
    }
}
