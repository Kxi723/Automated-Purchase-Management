package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class payment extends YCBaseFrame {
    private JTable table;
    private DefaultTableModel model;
    private String po_id;
    private String payamount;
    private Map<String, String[]> stockMap = new HashMap<>();
    private Map<String, String[]> itemMap = new HashMap<>();

    private final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private final int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private final int year = Calendar.getInstance().get(Calendar.YEAR);

    public payment() {
        super("Matched Purchase Orders and Stock Updates", 1000, 450);

        String[] columnNames = {
            "PO ID", "PR ID", "Invoice No", "PO Amount", "Supplier ID","Delivery Status", "Stock Qty", "Stock ID", "Payment Amount"
        };

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowSorter(new TableRowSorter<>(model));
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    po_id = (String) model.getValueAt(row, 0);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        JButton paymentBtn = new JButton("Pay");
        JButton backButton = new JButton("Back");

        paymentBtn.addActionListener(e -> pay("Payment.txt"));
        backButton.addActionListener(e -> {
            dispose();
            goBack();
        });

        bottomPanel.add(paymentBtn);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadMatchedPurchaseOrdersWithStock("Stock.txt", "Purchase Order.txt", "Item.txt");        
        setVisible(true);
    }

    private void loadMatchedPurchaseOrdersWithStock(String stockFile, String poFile, String itemFile) {
        
        try (BufferedReader stockReader = new BufferedReader(new FileReader(stockFile))) {
            String line;
            while ((line = stockReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    stockMap.put(parts[3].trim(), parts);
                }
            }
        } catch (IOException e) {
            showError("Error reading stock file:\n" + e.getMessage(), "File Error");
            return;
        }

        
        try (BufferedReader itemReader = new BufferedReader(new FileReader(itemFile))) {
            String line;
            while ((line = itemReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    itemMap.put(parts[0].trim(), parts); // key = invoice number
                }
            }
        } catch (IOException e) {
            showError("Error reading item file:\n" + e.getMessage(), "File Error");
            return;
        }

        try (BufferedReader poReader = new BufferedReader(new FileReader(poFile))) {
            String line;
            while ((line = poReader.readLine()) != null) {
                String[] poParts = line.split(";");
                if (poParts.length == 8) {
                    String poId = poParts[0].trim();
                    String invoice = poParts[2].trim();

                    if (stockMap.containsKey(poId) && itemMap.containsKey(invoice)) {
                        String[] stockParts = stockMap.get(poId);
                        String[] itemParts = itemMap.get(invoice);

                        float purchaseQty = Float.parseFloat(poParts[3].trim());
                        float itemPrice = Float.parseFloat(itemParts[3].trim());
                        float payamount2 = purchaseQty * itemPrice;
                        payamount = String.format("%.2f", payamount2);

                        model.addRow(new String[]{
                            poParts[0], poParts[1], poParts[2], poParts[3], poParts[4],
                            poParts[5], stockParts[1], stockParts[0], payamount
                        });
                    }
                }
            }
        } catch (IOException e) {
            showError("Error reading PO file:\n" + e.getMessage(), "File Error");
        }
    }

    protected void goBack() {
        JOptionPane.showMessageDialog(this, "Returning to previous screen...");
        SwingUtilities.invokeLater(() -> new YCFinanceManagerGUI3());
    }

    private void pay(String filename) {
        boolean alreadyPaid = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[0].trim().equals(po_id)) {
                    alreadyPaid = true;
                    break;
                }
            }
        } catch (IOException e) {
            showError("Error reading payment file:\n" + e.getMessage(), "File Error");
            return;
        }

        if (alreadyPaid) {
            JOptionPane.showMessageDialog(this, "Payment for PO " + po_id + " has already been recorded.");
        } else {
            try (FileWriter writer = new FileWriter(filename, true)) {
                if (po_id != null && !po_id.isEmpty()){
                    String formattedDate = String.format("%02d/%02d/%02d", day, month, year);
                    writer.write(po_id + ";" + payamount + ";" + formattedDate + "\n");
                    JOptionPane.showMessageDialog(this, "Payment Successful");
                } else {
                    showWarning("Please Select A row", "Value Warning");
                }
            } catch (IOException e) {
                showError("Error writing to payment file:\n" + e.getMessage(), "Write Error");
            }
        }
    }
}