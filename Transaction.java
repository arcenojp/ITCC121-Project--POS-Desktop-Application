import java.util.Date;

public class Transaction {
    private final String transactionId;
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double unitPrice;
    private final double total;
    private final String customer;
    private final String paymentMethod;
    private final Date date;


    public Transaction(String transactionId, String productId, String productName,
                       int quantity, double unitPrice, double total,
                       String customer, String paymentMethod, Date date) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }
    public String getTransactionId() { return transactionId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotal() { return total; }
    public String getCustomer() { return customer; }
    public String getPaymentMethod() { return paymentMethod; }
    public Date getDate() { return date; }
}