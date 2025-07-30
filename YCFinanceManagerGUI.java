package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class YCFinanceManagerGUI extends YCBaseFrame implements ActionListener {
    private JList<String> poList;
    private JTextField quantityField;
    private JComboBox<String> supplierCombo;
    private JLabel statusLabel;
    private ArrayList<String> supplier_list = new ArrayList<>();
    public String supplier_choice;
    private ArrayList<String> po_list = new ArrayList<>();
    private JButton modifyBtn;
    private JButton approveBtn;
    public String quantity;
    private String poID;
    private String supplier;
    private String newsupplier;
    private String newquantity;

    public YCFinanceManagerGUI(String poID, String quantity, String supplier, String status) {
        super("Finance Manager - PO Approval: " + poID, 600, 400);

        loadSupplier("Supplier.txt");
        loadPOName("Purchase Order.txt");

        this.quantity = quantity;
        this.poID = poID;
        this.supplier = supplier;

        DefaultListModel<String> poModel = new DefaultListModel<>();
        for (String POName : po_list) {
            poModel.addElement(POName);
        }

        poList = new JList<>(poModel);
        poList.setPreferredSize(new Dimension(50, 30));
        poList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane poScroll = new JScrollPane(poList);
        poScroll.setBorder(BorderFactory.createTitledBorder("Purchase Order"));

        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        quantityField = new JTextField();
        quantityField.setText(quantity);
        newquantity = quantityField.getText().trim();

        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = quantityField.getText().trim();
                    if (input.matches("[0-9]+")) {
                        newquantity = input;
                        quantityField.setText(newquantity);
                    } else {
                        showWarning("Please enter a valid numeric quantity", "Input Error");
                    }
                }
            }
        });

        supplierCombo = new JComboBox<>(supplier_list.toArray(new String[0]));
        supplierCombo.addActionListener(this);
        for (String s : supplier_list) {
            if (s.startsWith(supplier)) {
                supplierCombo.setSelectedItem(s);
                newsupplier = supplier;
            }
        }

        statusLabel = new JLabel(status);

        detailsPanel.setBorder(BorderFactory.createTitledBorder("PO Details"));
        detailsPanel.add(new JLabel("Quantity:"));
        detailsPanel.add(quantityField);
        detailsPanel.add(new JLabel("Supplier:"));
        detailsPanel.add(supplierCombo);
        detailsPanel.add(new JLabel("Status:"));
        detailsPanel.add(statusLabel);

        approveBtn = new JButton("Approve");
        approveBtn.addActionListener(this);
        modifyBtn = new JButton("Modify");
        modifyBtn.addActionListener(this);
        JButton backbtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(backbtn);

        add(poScroll, BorderLayout.WEST);
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

        backbtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new YCPurchaseOrderGUI());
            }
        });
    }

    public void loadSupplier(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    supplier_list.add(parts[0] + " | " + parts[1]);
                }
            }
        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage(), "File Error");
        }
    }

    private void loadPOName(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] po = line.split(";");
                po_list.add(po[0]);
            }
        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage(), "File Error");
        }
    }

    public void approve(String filepath) {
        File file = new File(filepath);
        StringBuilder content = new StringBuilder();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].trim().equals(poID)) {
                    found = true;
                    if (parts[parts.length - 2].equals("Approved")) {
                        showInfo("Purchase Order already approved", "Info");
                        return;
                    }
                    parts[parts.length - 2] = "Approved";
                    line = String.join(";", parts);
                }
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage(), "File Error");
            return;
        }

        if (found) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content.toString());
            } catch (IOException e) {
                showError("Error writing file: " + e.getMessage(), "Write Error");
            }
        }
    }

    public void editfile(String filepath) {
        File file = new File(filepath);
        StringBuilder filecontent = new StringBuilder();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts[0].equals(poID)) {
                    found = true;
                    String oldcontent = line.trim();
                    supplier = supplier.trim();
                    newsupplier = newsupplier.trim();
                    quantity = quantity.trim();
                    newquantity = newquantity.trim();

                    String newcontent;
                    if (supplier.equals(newsupplier) && quantity.equals(newquantity)) {
                        showWarning("No Changes Were Made", "Input Error");
                        return;
                    } else if (!supplier.equals(newsupplier) && quantity.equals(newquantity)) {
                        newcontent = oldcontent.replace(supplier, newsupplier);
                    } else if (supplier.equals(newsupplier) && !quantity.equals(newquantity)) {
                        newcontent = oldcontent.replace(quantity, newquantity);
                    } else {
                        newcontent = oldcontent.replace(supplier, newsupplier).replace(quantity, newquantity);
                    }

                    filecontent.append(newcontent).append("\n");
                } else {
                    filecontent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            showError("Error reading file: " + e.getMessage(), "File Error");
        }

        if (found) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(filecontent.toString());
            } catch (IOException e) {
                showError("Error writing file: " + e.getMessage(), "Write Error");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == modifyBtn) {
            newquantity = quantityField.getText().trim();
            editfile("Purchase Order.txt");
            dispose();
            SwingUtilities.invokeLater(() -> new YCPurchaseOrderGUI());

        } else if (e.getSource() == supplierCombo) {
            newsupplier = supplierCombo.getItemAt(supplierCombo.getSelectedIndex());
            int pipeIndex = newsupplier.indexOf("|");
            newsupplier = newsupplier.substring(0, pipeIndex).trim();

        } else if (e.getSource() == approveBtn) {
            approve("Purchase Order.txt");
            dispose();
            SwingUtilities.invokeLater(() -> new YCPurchaseOrderGUI());
        }
    }
}