import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.text.MessageFormat;


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
                return false; 
            }
        };
        salesTable.setModel(model);
    }

    private void generateReport(ActionEvent e) {
        DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
        model.setRowCount(0); 


        List<Transaction> transactions = Sales.getAllTransactions();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double grandTotal = 0;


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

        lblTotalSales.setText(String.format("Total Sales: ₱%.2f", grandTotal));

  
        for (int i = 0; i < salesTable.getColumnCount(); i++) {
            salesTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
    }


    private void printReport(ActionEvent e) {
      try {
            MessageFormat header = new MessageFormat("Sales Report");
            MessageFormat footer = new MessageFormat("Page {0}");

            JTable.PrintMode printMode = JTable.PrintMode.FIT_WIDTH;
            boolean complete = salesTable.print(printMode, header, footer);
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing complete", "Print Status",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing cancelled", "Print Status",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (java.awt.print.PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Printing failed: " + pe.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
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
 
        switch (filterType) {
            case "Product ID":
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 2));
                break;
            case "Customer":
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 7));
                break;
            default:
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
}}
