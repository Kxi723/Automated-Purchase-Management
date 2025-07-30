package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;


public class YCPurchaseOrderGUI extends YCBaseFrame {
    private JTable table;
    private DefaultTableModel model;
    public int po_idx;

    public YCPurchaseOrderGUI() {
        super("Purchase Order Viewer", 800, 400);

        String[] columnNames = {
            "PO ID", "PR ID", "Invoice No", "Amount", "Supplier ID",
            "Delivery Status", "Approval Status", "Requested By"
        };

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);

                int modelRow = convertRowIndexToModel(row);
                String approvalStatus = (String) getModel().getValueAt(modelRow, 6);

                if ("Approved".equalsIgnoreCase(approvalStatus) && column == 6) {
                    comp.setBackground(Color.GREEN);
                } else if ("Pending".equalsIgnoreCase(approvalStatus) && column == 6) {
                    comp.setBackground(Color.RED);
                }

                return comp;
            }
        };

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Double-click to open FinanceManagerGUI
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String poID = (String) model.getValueAt(selectedRow, 0);
                        String quantity = (String) model.getValueAt(selectedRow, 3);
                        String supplier = (String) model.getValueAt(selectedRow, 4);
                        String status = (String) model.getValueAt(selectedRow, 6);

                        dispose();
                        SwingUtilities.invokeLater(() -> new YCFinanceManagerGUI(poID, quantity, supplier, status));
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new YCFinanceManagerGUI3());
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadPOData("Purchase Order.txt");

        setVisible(true);
    }

    private void loadPOData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 8) {
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