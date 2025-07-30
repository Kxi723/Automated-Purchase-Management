package Purchase_Order_Management;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;   


public class YCPurchaseReq extends YCBaseFrame {
    private JTable table;
    private DefaultTableModel model;

    public YCPurchaseReq() {
        super("Purchase Order Viewer", 800, 400);

        // Define column names
        String[] columnNames = {
            "PR ID", "Item ID", "Amount", "Supplier ID", "Date", "Requested By"
        };

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        loadPRData("Purchase Requisition.txt");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new YCFinanceManagerGUI3());
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPRData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 6) {
                    ArrayList<String> row = new ArrayList<>();
                    for (String field : fields) {
                        row.add(field.trim());
                    }
                    model.addRow(row.toArray());
                }
            }

        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage(), "File Error");
        }
    }
}