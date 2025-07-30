package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class YCreport extends YCBaseFrame{
    private JFrame frame;
    private ArrayList<Object[]> salesdata = new ArrayList<>();
    private ArrayList<Object[]> purchasedata = new ArrayList<>();
    private ArrayList<String> date_1 = new ArrayList<>();
    private ArrayList<String> date_2 = new ArrayList<>();   
    private Set<String> uniqueMonths = new LinkedHashSet<>();
    private String selectedDate;

    public YCreport(String selectedDate) {
        super("Report", 900, 650);
        setLayout(new BorderLayout(10, 10));
        
        this.selectedDate = selectedDate;
        salesreport("Daily Sale Record.txt");
        paymentdata("Payment.txt");

        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel 1 - Sales Report
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(BorderFactory.createTitledBorder("Sales Report"));

        String[] salesCols = {"Item Code", "Quantity", "Unit Price", "Total Amount", "Date", "Salesperson"};
        DefaultTableModel salesModel = new DefaultTableModel(salesCols, 0);
        for (Object[] record : salesdata) {
            salesModel.addRow(new Object[] {
                record[0],
                record[1],
                String.format("RM %.2f", record[2]),
                String.format("RM %.2f", record[3]),
                record[4],
                record[5]
            });
        }
        JTable salesTable = new JTable(salesModel);
        salesTable.setRowHeight(25);
        JScrollPane salesScroll = new JScrollPane(salesTable);
        panel1.add(salesScroll, BorderLayout.CENTER);

        // Panel 2 - Purchase Report
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBorder(BorderFactory.createTitledBorder("Purchase Report"));

        String[] purchaseCols = {"PO ID", "Amount", "Date"};
        DefaultTableModel purchaseModel = new DefaultTableModel(purchaseCols, 0);
        for (Object[] pr : purchasedata) {
            purchaseModel.addRow(new Object[] {
                pr[0],
                String.format("RM %.2f", pr[1]),
                pr[2]
            });
        }
        
        JTable purchaseTable = new JTable(purchaseModel);
        purchaseTable.setRowHeight(25);
        JScrollPane purchaseScroll = new JScrollPane(purchaseTable);
        panel2.add(purchaseScroll, BorderLayout.CENTER);

        // Panel 3 - Net Profit/Loss
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.setBorder(BorderFactory.createTitledBorder("Financial Summary"));

        double totalSales = salesdata.stream().mapToDouble(s -> (double) s[3]).sum();
        double totalPurchase = purchasedata.stream().mapToDouble(p -> (double) p[1]).sum();
        double net = totalSales - totalPurchase;

        JLabel label3 = new JLabel("Net Profit/Loss: RM " + String.format("%.2f", net));
        label3.setFont(new Font("Arial", Font.BOLD, 24));
        if (net<0){
            label3.setForeground(Color.RED);
        }else{label3.setForeground(Color.GREEN);}
        
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel3.add(Box.createVerticalGlue());
        panel3.add(label3);
        panel3.add(Box.createVerticalGlue());

        // Add panels to main panel
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new YCFinanceManagerGUI3());
            }
        });
        bottomPanel.add(backButton);

        // Add to frame
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void salesreport(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    String[] month = parts[4].split("/");
                    if (month[1].equals(selectedDate)){
                        String itemCode = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double unitPrice = Double.parseDouble(parts[2]);
                        double totalAmount = Double.parseDouble(parts[3]);
                        String date = parts[4];
                        String salesperson = parts[5];

                        salesdata.add(new Object[] {
                            itemCode, quantity, unitPrice, totalAmount, date, salesperson
                        });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error reading sales data: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void paymentdata(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String [] month = parts[2].split("/");
                    if (month[1].equals(selectedDate)){
                    String poId = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String date = parts[2];

                    purchasedata.add(new Object[] {
                        poId, amount, date
                    });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error reading purchase data: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
