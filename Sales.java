import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Sales extends JFrame{
    private static List<Transaction> allTransactions = new ArrayList<>();
    private JPanel salesPanel;
    private JTextField txtProductID;
    private JTextField txtProductName;
    private JTextField txtQuantity;
    private JTextField txtPrice;
    private JTextField txtCustomer;
    private JButton processButton;
    private JButton cancelButton;
    private JLabel I;
    private JComboBox<String> paymentMethod;
    private JLabel txtTotal;
    private DefaultTableModel inventoryModel;
    private int inventoryRow;
    private int currentQuantity;

    public Sales(String productId, String productName, double price, int currentQuantity,
                 DefaultTableModel inventoryModel, int inventoryRow){
        this.inventoryModel = inventoryModel;
        this.inventoryRow = inventoryRow;
        this.currentQuantity = currentQuantity;
        setTitle("Process Transaction");
        setSize(400, 350);
        setContentPane(salesPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        txtProductID.setText(productId);
        txtProductName.setText(productName);
        txtPrice.setText(String.format("%.2f", price));


        String[] paymentOptions = {"Cash", "Card", "Online"};
        paymentMethod.setModel(new DefaultComboBoxModel<>(paymentOptions));
        paymentMethod.setSelectedIndex(0);
        txtQuantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTransaction();

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    public static List<Transaction> getAllTransactions() {
        return allTransactions;
    }
    private void calculateTotal() {
        try {
            int qty = Integer.parseInt(txtQuantity.getText());
            if (qty > currentQuantity) {
                JOptionPane.showMessageDialog(this,
                        "Only " + currentQuantity + " items available in stock",
                        "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                txtQuantity.setText(String.valueOf(currentQuantity));
                qty = currentQuantity;
            }
            double price = Double.parseDouble(txtPrice.getText());
            double total = qty * price;
            txtTotal.setText(String.format("₱%.2f", total));
        } catch (NumberFormatException ex) {
            txtTotal.setText("");
        }
    }
    private void processTransaction() {
        // Validate inputs
        if (!validateInputs()) {
            return; // Stop if validation fails
        }

        // 2. Get all transaction details
        int quantity = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(txtPrice.getText());
        double total = quantity * price;
        String customer = txtCustomer.getText().trim();
        if (customer.isEmpty()) customer = "Walk-in Customer";
        String paymentMethod = (String) this.paymentMethod.getSelectedItem();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Transaction transaction = new Transaction(
                "TRX-" + System.currentTimeMillis(), // Unique ID
                txtProductID.getText(),
                txtProductName.getText(),
                quantity,
                price,
                total,
                customer,
                paymentMethod, // Already gets selected method from combo box
                new Date()
        );
        allTransactions.add(transaction);
        // 3. Update inventory
        int newQuantity = currentQuantity - quantity;
        inventoryModel.setValueAt(newQuantity, inventoryRow, 4); // Update quantity in inventory

        // 4. Generate and show receipt
        String receipt = generateReceipt(date, quantity, total, customer, paymentMethod);

        // 5. Show confirmation dialog
        int option = JOptionPane.showConfirmDialog(
                this,
                receipt,
                "Confirm Transaction",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        // 6. If confirmed, close window
        if (option == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Transaction completed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the sales window
        }
    }


    private String generateReceipt(String date, int quantity, double total,
                                   String customer, String method) {
        String payment = (String) paymentMethod.getSelectedItem();
        return String.format(
                """
                === SALES RECEIPT ===
                Date: %s
                -----------------------------------
                Product ID: %s
                Product: %s
                Quantity: %d
                Unit Price: ₱%.2f
                -----------------------------------
                TOTAL: ₱%.2f
                -----------------------------------
                Customer: %s
                Payment Method: %s
                -----------------------------------
                Thank you for your purchase!
                """,
                date,
                txtProductID.getText(),
                txtProductName.getText(),
                quantity,
                Double.parseDouble(txtPrice.getText()),
                total,
                customer,
                payment
        );
    }
    private boolean validateInputs() {
        try {
            int qty = Integer.parseInt(txtQuantity.getText());

            // Check if quantity is positive
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity must be greater than 0",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Check if enough stock exists
            if (qty > currentQuantity) {
                JOptionPane.showMessageDialog(this,
                        "Only " + currentQuantity + " units available!",
                        "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid quantity",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        }
    }
}





