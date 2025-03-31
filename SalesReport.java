import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;


public class SalesReport extends JFrame {
    private JComboBox<String> cmbFilter;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JTable salesTable;
    private JButton btnClose;
    private JButton btnGenerateReport;
    private JButton btnPrint;
    private JPanel MainPanel;
    private JLabel lblTotalSales;

    public SalesReport() {
        setTitle("Sales Report");
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setSize(900, 600);
        setLocationRelativeTo(null);

        initTable();
        btnGenerateReport.addActionListener(this::generateReport);
        btnClose.addActionListener(e -> dispose());
        btnPrint.addActionListener(this::printReport);
        btnSearch.addActionListener(this::searchTransactions);
        String[] filters = {"All", "Product ID", "Customer", "Date Range"};
        cmbFilter.setModel(new DefaultComboBoxModel<>(filters));
    }

    private void initTable() {
        String[] columns = {"Transaction ID", "Date", "Product ID", "Product Name",
                "Qty", "Unit Price", "Total", "Customer", "Payment Method"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        salesTable.setModel(model);
    }

    private void generateReport(ActionEvent e) {
        DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
        model.setRowCount(0); // Clear existing data

        // Get transactions from Sales class
        List<Transaction> transactions = Sales.getAllTransactions();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double grandTotal = 0;

        // Populate table with real transaction data
        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                    t.getTransactionId(),
                    dateFormat.format(t.getDate()),
                    t.getProductId(),
                    t.getProductName(),
                    t.getQuantity(),
                    String.format("₱%.2f", t.getUnitPrice()),
                    String.format("₱%.2f", t.getTotal()),
                    t.getCustomer(),
                    t.getPaymentMethod()
            });
            grandTotal += t.getTotal();
        }

        // Update total sales label
        lblTotalSales.setText(String.format("Total Sales: ₱%.2f", grandTotal));

        // Auto-resize columns
        for (int i = 0; i < salesTable.getColumnCount(); i++) {
            salesTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
    }


    private void printReport(ActionEvent e) {
        // TODO: Add printing logic
        JOptionPane.showMessageDialog(this, "Printing feature will be implemented here");
    }

private void searchTransactions(ActionEvent e) {
    String keyword = txtSearch.getText().trim().toLowerCase();
    String filterType = cmbFilter.getSelectedItem().toString();

    DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    salesTable.setRowSorter(sorter);

    if (keyword.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        // Search in specific columns based on filter
        switch (filterType) {
            case "Product ID":
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 2));
                break;
            case "Customer":
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 7));
                break;
            default: // Search all columns
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
}}
