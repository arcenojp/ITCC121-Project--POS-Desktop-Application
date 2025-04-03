import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Inventory  extends JFrame{
    private JTable inventoryTable;
    private JPanel mainPanel;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton deleteButton;
    private JButton addButton;
    private JTextField productIdField;
    private JButton clearButton;
    private JButton updateButton;
    private JButton searchButton;
    private JComboBox <String> categoryComboBox;
    private JTextField searchField;
    private JButton processTransactionButton;
    private JButton btnSalesReport;
    private final DefaultTableModel tableModel;


    public Inventory() {
        setTitle("Inventory");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        String[] columnNames = {"Product ID", "Product Name", "Category", "Price (₱)", "Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable.setModel(tableModel);
        String[] categories = {"Electronics", "Clothing", "Food", "Furniture", "Other"};
        categoryComboBox.setModel(new DefaultComboBoxModel<>(categories));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }});
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct();
            }
        });
        processTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTransactionForm();
            }
        });
        btnSalesReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             openSalesReport();
            }private void openSalesReport() {
                new SalesReport().setVisible(true);
            }
        });
    }
    private void addProduct() {
        if (validateInputs()) {
            String[] rowData = {
                    productIdField.getText(),
                    productNameField.getText(),
                    (String) categoryComboBox.getSelectedItem(),
                    priceField.getText(),
                    quantityField.getText()
            };
            tableModel.addRow(rowData);
            clearForm();
        }
    }
    private void updateProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow >= 0 && validateInputs()) {
            tableModel.setValueAt(productIdField.getText(), selectedRow, 0);
            tableModel.setValueAt(productNameField.getText(), selectedRow, 1);
            tableModel.setValueAt(categoryComboBox.getSelectedItem(), selectedRow, 2);
            tableModel.setValueAt(priceField.getText(), selectedRow, 3);
            tableModel.setValueAt(quantityField.getText(), selectedRow, 4);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select a product to update.");
        }
    }

    private void deleteProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select a product to delete.");
        }
    }

    private void searchProduct() {
        String searchTerm = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);

        if (searchTerm.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm));
        }
    }

    private void clearForm() {
        productIdField.setText("");
        productNameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        priceField.setText("");
        quantityField.setText("");
    }

    private boolean validateInputs() {
        return true;
    }
    private void openTransactionForm() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first!");
            return;
        }

        String productID = inventoryTable.getValueAt(selectedRow, 0).toString();
        String productName = inventoryTable.getValueAt(selectedRow, 1).toString();
        double price = Double.parseDouble(inventoryTable.getValueAt(selectedRow, 3).toString());
        int currentQuantity = Integer.parseInt(inventoryTable.getValueAt(selectedRow, 4).toString());
        new Sales(productID, productName, price, currentQuantity,
                (DefaultTableModel) inventoryTable.getModel(), selectedRow).setVisible(true);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

            public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                    new Inventory().setVisible(true);
                });
            }}
