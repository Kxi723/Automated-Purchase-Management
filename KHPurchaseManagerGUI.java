package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KHPurchaseManagerGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel; // Container for entire content (menu or full view)

    public KHPurchaseManagerGUI() {
        super("Purchase Manager System");
        setSize(1024, 576);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // jason lai added
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) { // if close the window, then KxiStartMain.kxiMainPage()
                dispose();
                KxiStartMain.kxiMainPage();
            }
        });
        setLocationRelativeTo(null);

        // Card layout to switch between main menu and full panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ===== MAIN MENU PANEL =====
        JPanel menuContainer = new JPanel(new BorderLayout());

        // Left side: title split into two lines and centered vertically
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(512, 576));
        leftPanel.setBackground(new Color(220, 220, 220));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(220, 220, 220));
        JLabel line1 = new JLabel("Welcome to", SwingConstants.CENTER);
        JLabel line2 = new JLabel("Purchase Manager System", SwingConstants.CENTER);
        line1.setFont(new Font("Arial", Font.BOLD, 30));
        line2.setFont(new Font("Arial", Font.BOLD, 30));
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(line1);
        titlePanel.add(line2);

        leftPanel.add(titlePanel);

        // Right side: buttons with slight center alignment
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(512, 576));
        rightPanel.setBackground(Color.WHITE);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = GridBagConstraints.RELATIVE;
        gbcButtons.insets = new Insets(10, 0, 10, 0);

        JButton viewItemsButton = new JButton("View Items");
        JButton viewSuppliersButton = new JButton("View Suppliers");
        JButton viewRequisitionsButton = new JButton("View Purchase Requisition");
        JButton manageOrdersButton = new JButton("Purchase Order");

        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        Dimension buttonSize = new Dimension(300, 50);

        viewItemsButton.setFont(buttonFont);
        viewItemsButton.setPreferredSize(buttonSize);
        viewSuppliersButton.setFont(buttonFont);
        viewSuppliersButton.setPreferredSize(buttonSize);
        viewRequisitionsButton.setFont(buttonFont);
        viewRequisitionsButton.setPreferredSize(buttonSize);
        manageOrdersButton.setFont(buttonFont);
        manageOrdersButton.setPreferredSize(buttonSize);

        menuPanel.add(viewItemsButton, gbcButtons);
        menuPanel.add(viewSuppliersButton, gbcButtons);
        menuPanel.add(viewRequisitionsButton, gbcButtons);
        menuPanel.add(manageOrdersButton, gbcButtons);

        rightPanel.add(menuPanel);

        // Add left and right to menu container
        menuContainer.add(leftPanel, BorderLayout.WEST);
        menuContainer.add(rightPanel, BorderLayout.CENTER);

        // ===== ITEM FULL PANEL =====
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Items", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemsPanel.add(titleLabel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Filter by Item Description: ");
        JTextField searchField = new JTextField(20);
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        itemsPanel.add(Box.createVerticalStrut(10));
        itemsPanel.add(filterPanel);

        String[] columnNames = {"Item ID", "Description", "Supplier ID", "Unit Price (RM)", "Sell Price (RM)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(960, 300));
        itemsPanel.add(Box.createVerticalStrut(10));
        itemsPanel.add(scrollPane);

        JButton backButton = new JButton("Back to main menu");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemsPanel.add(Box.createVerticalStrut(10));
        itemsPanel.add(backButton);

        loadItems(tableModel, "");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                loadItems(tableModel, text);
            }
        });

        backButton.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "Menu"));

        // ===== SUPPLIER FULL PANEL =====
        JPanel supplierPanel = new JPanel();
        supplierPanel.setLayout(new BoxLayout(supplierPanel, BoxLayout.Y_AXIS));
        supplierPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel2 = new JLabel("Suppliers", SwingConstants.CENTER);
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        supplierPanel.add(titleLabel2);

        JPanel filterPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel2 = new JLabel("Filter by Supplier Name: ");
        JTextField searchField2 = new JTextField(20);
        filterPanel2.add(searchLabel2);
        filterPanel2.add(searchField2);
        supplierPanel.add(Box.createVerticalStrut(10));
        supplierPanel.add(filterPanel2);

        String[] columnNames2 = {"Supplier ID", "Supplier Name", "Address"};
        DefaultTableModel tableModel2 = new DefaultTableModel(columnNames2, 0);
        JTable table2 = new JTable(tableModel2);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        scrollPane2.setPreferredSize(new Dimension(960, 300));
        supplierPanel.add(Box.createVerticalStrut(10));
        supplierPanel.add(scrollPane2);

        JButton backButton2 = new JButton("Back to main menu");
        backButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        supplierPanel.add(Box.createVerticalStrut(10));
        supplierPanel.add(backButton2);

        loadSuppliers(tableModel2, "");

        searchField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text2 = searchField2.getText();
                loadSuppliers(tableModel2, text2);
            }
        });

        backButton2.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "Menu"));

        // ===== PURCHASE REQUISITION FULL PANEL =====
        JPanel requisitionPanel = new JPanel();
        requisitionPanel.setLayout(new BoxLayout(requisitionPanel, BoxLayout.Y_AXIS));
        requisitionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel3 = new JLabel("Purchase Requisitions", SwingConstants.CENTER);
        titleLabel3.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        requisitionPanel.add(titleLabel3);

        JPanel filterPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel3 = new JLabel("Filter by Requisition ID: ");
        JTextField searchField3 = new JTextField(20);
        filterPanel3.add(searchLabel3);
        filterPanel3.add(searchField3);
        requisitionPanel.add(Box.createVerticalStrut(10));
        requisitionPanel.add(filterPanel3);

        String[] columnNames3 = {"Requisition ID", "Item ID", "Quantity", "Supplier ID", "Date", "Requested By"};
        DefaultTableModel tableModel3 = new DefaultTableModel(columnNames3, 0);
        JTable table3 = new JTable(tableModel3);
        JScrollPane scrollPane3 = new JScrollPane(table3);
        scrollPane3.setPreferredSize(new Dimension(960, 300));
        requisitionPanel.add(Box.createVerticalStrut(10));
        requisitionPanel.add(scrollPane3);

        JButton backButton3 = new JButton("Back to main menu");
        backButton3.setAlignmentX(Component.CENTER_ALIGNMENT);
        requisitionPanel.add(Box.createVerticalStrut(10));
        requisitionPanel.add(backButton3);

        loadPurchaseRequisitions(tableModel3, "");

        searchField3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text3 = searchField3.getText();
                loadPurchaseRequisitions(tableModel3, text3);
            }
        });

        backButton3.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "Menu"));

        // ===== DISPLAY PURCHASE ORDERS FULL PANEL =====
        JPanel displayOrdersPanel = new JPanel();
        displayOrdersPanel.setLayout(new BoxLayout(displayOrdersPanel, BoxLayout.Y_AXIS));
        displayOrdersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel4 = new JLabel("Purchase Orders", SwingConstants.CENTER);
        titleLabel4.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayOrdersPanel.add(titleLabel4);

        JPanel filterPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel4 = new JLabel("Filter by PO ID: ");
        JTextField searchField4 = new JTextField(20);
        filterPanel4.add(searchLabel4);
        filterPanel4.add(searchField4);
        displayOrdersPanel.add(Box.createVerticalStrut(10));
        displayOrdersPanel.add(filterPanel4);

        String[] columnNames4 = {"PO ID", "PR ID", "Item ID", "Quantity", "Supplier ID", "Shipping Status", "Approval Status", "PM Name"};
        DefaultTableModel tableModel4 = new DefaultTableModel(columnNames4, 0);
        JTable table4 = new JTable(tableModel4);
        JScrollPane scrollPane4 = new JScrollPane(table4);
        scrollPane4.setPreferredSize(new Dimension(960, 300));
        displayOrdersPanel.add(Box.createVerticalStrut(10));
        displayOrdersPanel.add(scrollPane4);

        JButton backButton4 = new JButton("Back to main menu");
        backButton4.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayOrdersPanel.add(Box.createVerticalStrut(10));
        displayOrdersPanel.add(backButton4);

        loadPurchaseOrders(tableModel4, "");

        searchField4.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text4 = searchField4.getText();
                loadPurchaseOrders(tableModel4, text4);
            }
        });

        backButton4.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "PurchaseOrderMenu"));

        // ===== CREATE PURCHASE ORDER - SELECT PR PANEL =====
        JPanel selectPRPanel = new JPanel();
        selectPRPanel.setLayout(new BoxLayout(selectPRPanel, BoxLayout.Y_AXIS));
        selectPRPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel5 = new JLabel("Create Purchase Order", SwingConstants.CENTER);
        titleLabel5.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel5.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectPRPanel.add(titleLabel5);

        JPanel filterPanel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel5 = new JLabel("Filter by PR ID: ");
        JTextField searchField5 = new JTextField(20);
        filterPanel5.add(searchLabel5);
        filterPanel5.add(searchField5);
        selectPRPanel.add(Box.createVerticalStrut(10));
        selectPRPanel.add(filterPanel5);

        String[] columnNames5 = {"Requisition ID", "Item ID", "Quantity", "Supplier ID", "Date", "Requested By"};
        DefaultTableModel tableModel5 = new DefaultTableModel(columnNames5, 0);
        JTable table5 = new JTable(tableModel5);
        JScrollPane scrollPane5 = new JScrollPane(table5);
        scrollPane5.setPreferredSize(new Dimension(960, 300));
        selectPRPanel.add(Box.createVerticalStrut(10));
        selectPRPanel.add(scrollPane5);

        JLabel selectMessage = new JLabel("Select row to create Purchase Order", SwingConstants.CENTER);
        selectMessage.setFont(new Font("Arial", Font.ITALIC, 14));
        selectMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectPRPanel.add(Box.createVerticalStrut(10));
        selectPRPanel.add(selectMessage);

        JButton backButton5 = new JButton("Back to main menu");
        backButton5.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectPRPanel.add(Box.createVerticalStrut(10));
        selectPRPanel.add(backButton5);

        loadPurchaseRequisitions(tableModel5, "");

        searchField5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text5 = searchField5.getText();
                loadPurchaseRequisitions(tableModel5, text5);
            }
        });

        backButton5.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "PurchaseOrderMenu"));

        // ===== CREATE PURCHASE ORDER - FORM PANEL =====
        JPanel createPOFormPanel = new JPanel();
        createPOFormPanel.setLayout(new BoxLayout(createPOFormPanel, BoxLayout.Y_AXIS));
        createPOFormPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel6 = new JLabel("Create Purchase Order", SwingConstants.CENTER);
        titleLabel6.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPOFormPanel.add(titleLabel6);
        createPOFormPanel.add(Box.createVerticalStrut(30));

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // PO ID (auto-generated, read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel poIdLabel = new JLabel("PO ID:");
        poIdLabel.setFont(labelFont);
        formPanel.add(poIdLabel, gbc);
        gbc.gridx = 1;
        JTextField poIdField = new JTextField(15);
        poIdField.setFont(fieldFont);
        poIdField.setEditable(false);
        poIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(poIdField, gbc);

        // PR ID (auto-filled, read-only)
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel prIdLabel = new JLabel("PR ID:");
        prIdLabel.setFont(labelFont);
        formPanel.add(prIdLabel, gbc);
        gbc.gridx = 1;
        JTextField prIdField = new JTextField(15);
        prIdField.setFont(fieldFont);
        prIdField.setEditable(false);
        prIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(prIdField, gbc);

        // Item ID (auto-filled, read-only)
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel itemIdLabel = new JLabel("Item ID:");
        itemIdLabel.setFont(labelFont);
        formPanel.add(itemIdLabel, gbc);
        gbc.gridx = 1;
        JTextField itemIdField = new JTextField(15);
        itemIdField.setFont(fieldFont);
        itemIdField.setEditable(false);
        itemIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(itemIdField, gbc);

        // Quantity (auto-filled, read-only)
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(labelFont);
        formPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        JTextField quantityField = new JTextField(15);
        quantityField.setFont(fieldFont);
        quantityField.setEditable(false);
        quantityField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(quantityField, gbc);

        // Supplier ID (auto-filled, read-only)
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel supplierIdLabel = new JLabel("Supplier ID:");
        supplierIdLabel.setFont(labelFont);
        formPanel.add(supplierIdLabel, gbc);
        gbc.gridx = 1;
        JTextField supplierIdField = new JTextField(15);
        supplierIdField.setFont(fieldFont);
        supplierIdField.setEditable(false);
        supplierIdField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(supplierIdField, gbc);

        // Shipping Status (dropdown)
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel shippingStatusLabel = new JLabel("Shipping Status:");
        shippingStatusLabel.setFont(labelFont);
        formPanel.add(shippingStatusLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> shippingStatusCombo = new JComboBox<>(new String[]{"Pending", "Arrived"});
        shippingStatusCombo.setFont(fieldFont);
        formPanel.add(shippingStatusCombo, gbc);

        // Approval Status (dropdown)
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel approvalStatusLabel = new JLabel("Approval Status:");
        approvalStatusLabel.setFont(labelFont);
        formPanel.add(approvalStatusLabel, gbc);
        gbc.gridx = 1;
        JComboBox<String> approvalStatusCombo = new JComboBox<>(new String[]{"Pending", "Approved"});
        approvalStatusCombo.setFont(fieldFont);
        formPanel.add(approvalStatusCombo, gbc);

        // PM Name (user input)
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel pmNameLabel = new JLabel("PM Name:");
        pmNameLabel.setFont(labelFont);
        formPanel.add(pmNameLabel, gbc);
        gbc.gridx = 1;
        JTextField pmNameField = new JTextField(15);
        pmNameField.setFont(fieldFont);
        formPanel.add(pmNameField, gbc);

        createPOFormPanel.add(formPanel);
        createPOFormPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createButton = new JButton("Create Purchase Order");
        JButton cancelButton = new JButton("Cancel");
        
        createButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        createPOFormPanel.add(buttonPanel);

        // ===== PURCHASE ORDER MENU PANEL =====
        JPanel purchaseOrderMenuPanel = new JPanel();
        purchaseOrderMenuPanel.setLayout(new BoxLayout(purchaseOrderMenuPanel, BoxLayout.Y_AXIS));
        purchaseOrderMenuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        purchaseOrderMenuPanel.setBackground(Color.WHITE);

        // Title for Purchase Order menu
        JLabel poMenuTitle = new JLabel("Purchase Orders", SwingConstants.CENTER);
        poMenuTitle.setFont(new Font("Arial", Font.BOLD, 32));
        poMenuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        purchaseOrderMenuPanel.add(poMenuTitle);
        purchaseOrderMenuPanel.add(Box.createVerticalStrut(50));

        // Button panel for Purchase Order menu options
        JPanel poButtonPanel = new JPanel();
        poButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcPO = new GridBagConstraints();
        gbcPO.gridx = 0;
        gbcPO.gridy = GridBagConstraints.RELATIVE;
        gbcPO.insets = new Insets(15, 0, 15, 0);

        // Create Purchase Order menu buttons
        JButton displayOrdersButton = new JButton("Display Purchase Orders");
        JButton createOrderButton = new JButton("Create Purchase Order");
        JButton editDeleteOrderButton = new JButton("Edit/Delete Purchase Order");

        // Set font and size for Purchase Order menu buttons
        Font poButtonFont = new Font("Arial", Font.PLAIN, 18);
        Dimension poButtonSize = new Dimension(350, 60);

        displayOrdersButton.setFont(poButtonFont);
        displayOrdersButton.setPreferredSize(poButtonSize);
        createOrderButton.setFont(poButtonFont);
        createOrderButton.setPreferredSize(poButtonSize);
        editDeleteOrderButton.setFont(poButtonFont);
        editDeleteOrderButton.setPreferredSize(poButtonSize);

        // Add buttons to Purchase Order menu panel
        poButtonPanel.add(displayOrdersButton, gbcPO);
        poButtonPanel.add(createOrderButton, gbcPO);
        poButtonPanel.add(editDeleteOrderButton, gbcPO);

        purchaseOrderMenuPanel.add(poButtonPanel);
        purchaseOrderMenuPanel.add(Box.createVerticalStrut(30));

        // Back button for Purchase Order menu
        JButton backToPOMenu = new JButton("Back to Main Menu");
        backToPOMenu.setFont(new Font("Arial", Font.PLAIN, 16));
        backToPOMenu.setPreferredSize(new Dimension(200, 40));
        backToPOMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        purchaseOrderMenuPanel.add(backToPOMenu);

        // Add all panels to the main panel with card layout
        mainPanel.add(menuContainer, "Menu");
        mainPanel.add(itemsPanel, "Items");
        mainPanel.add(supplierPanel, "Suppliers");
        mainPanel.add(requisitionPanel, "Requisitions");
        mainPanel.add(displayOrdersPanel, "DisplayOrders");
        mainPanel.add(selectPRPanel, "SelectPR");
        mainPanel.add(createPOFormPanel, "CreatePOForm");
        mainPanel.add(purchaseOrderMenuPanel, "PurchaseOrderMenu");

        add(mainPanel); // Add card layout panel to the frame

        // Set up button action listeners
        viewItemsButton.addActionListener(_ -> cardLayout.show(mainPanel, "Items"));
        viewSuppliersButton.addActionListener(_ -> cardLayout.show(mainPanel, "Suppliers"));
        viewRequisitionsButton.addActionListener(_ -> cardLayout.show(mainPanel, "Requisitions"));
        manageOrdersButton.addActionListener(_ -> cardLayout.show(mainPanel, "PurchaseOrderMenu"));

        // Purchase Order menu button listeners
        backToPOMenu.addActionListener(_ -> cardLayout.show(mainPanel, "Menu"));
        displayOrdersButton.addActionListener(_ -> cardLayout.show(mainPanel, "DisplayOrders"));
        createOrderButton.addActionListener(_ -> cardLayout.show(mainPanel, "SelectPR"));

        // Table selection listener for PR selection
        table5.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table5.getSelectedRow();
                if (selectedRow >= 0) {
                    // Get selected PR data
                    String prId = (String) table5.getValueAt(selectedRow, 0);
                    String itemId = (String) table5.getValueAt(selectedRow, 1);
                    int quantity = (Integer) table5.getValueAt(selectedRow, 2);
                    String supplierId = (String) table5.getValueAt(selectedRow, 3);
                    
                    // Generate new PO ID
                    String newPOId = generateNextPOId();
                    
                    // Fill form fields
                    poIdField.setText(newPOId);
                    prIdField.setText(prId);
                    itemIdField.setText(itemId);
                    quantityField.setText(String.valueOf(quantity));
                    supplierIdField.setText(supplierId);
                    
                    // Reset user input fields
                    shippingStatusCombo.setSelectedIndex(0);
                    approvalStatusCombo.setSelectedIndex(0);
                    pmNameField.setText("");
                    
                    // Navigate to form panel
                    cardLayout.show(mainPanel, "CreatePOForm");
                }
            }
        });

        // Form button listeners
        createButton.addActionListener(_ -> {
            String pmName = pmNameField.getText().trim();
            if (pmName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter PM Name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new purchase order
            KHPurchaseOrder newOrder = new KHPurchaseOrder(
                poIdField.getText(),
                prIdField.getText(),
                itemIdField.getText(),
                Integer.parseInt(quantityField.getText()),
                supplierIdField.getText(),
                (String) shippingStatusCombo.getSelectedItem(),
                (String) approvalStatusCombo.getSelectedItem(),
                pmName
            );
            
            // Save to file
            if (savePurchaseOrder(newOrder)) {
                JOptionPane.showMessageDialog(this, "Purchase Order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the display orders table if needed
                loadPurchaseOrders(tableModel4, "");
                // Go back to Purchase Order menu
                cardLayout.show(mainPanel, "PurchaseOrderMenu");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create Purchase Order", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(_ -> cardLayout.show(mainPanel, "SelectPR"));

        // ===== EDIT/DELETE PURCHASE ORDER PANEL =====
        // Add this after the createPOFormPanel section and before purchaseOrderMenuPanel

        JPanel editDeleteOrderPanel = new JPanel();
        editDeleteOrderPanel.setLayout(new BoxLayout(editDeleteOrderPanel, BoxLayout.Y_AXIS));
        editDeleteOrderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel7 = new JLabel("Edit/Delete Purchase Order", SwingConstants.CENTER);
        titleLabel7.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel7.setAlignmentX(Component.CENTER_ALIGNMENT);
        editDeleteOrderPanel.add(titleLabel7);

        JPanel filterPanel7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel7 = new JLabel("Filter by PO ID: ");
        JTextField searchField7 = new JTextField(20);
        filterPanel7.add(searchLabel7);
        filterPanel7.add(searchField7);
        editDeleteOrderPanel.add(Box.createVerticalStrut(10));
        editDeleteOrderPanel.add(filterPanel7);

        String[] columnNames7 = {"PO ID", "PR ID", "Item ID", "Quantity", "Supplier ID", "Shipping Status", "Approval Status", "PM Name"};
        DefaultTableModel tableModel7 = new DefaultTableModel(columnNames7, 0);
        JTable table7 = new JTable(tableModel7);
        JScrollPane scrollPane7 = new JScrollPane(table7);
        scrollPane7.setPreferredSize(new Dimension(960, 300));
        editDeleteOrderPanel.add(Box.createVerticalStrut(10));
        editDeleteOrderPanel.add(scrollPane7);

        JLabel selectMessage7 = new JLabel("Select a row to Edit/Delete", SwingConstants.CENTER);
        selectMessage7.setFont(new Font("Arial", Font.ITALIC, 14));
        selectMessage7.setAlignmentX(Component.CENTER_ALIGNMENT);
        editDeleteOrderPanel.add(Box.createVerticalStrut(10));
        editDeleteOrderPanel.add(selectMessage7);

        JButton backButton7 = new JButton("Back to main menu");
        backButton7.setAlignmentX(Component.CENTER_ALIGNMENT);
        editDeleteOrderPanel.add(Box.createVerticalStrut(10));
        editDeleteOrderPanel.add(backButton7);

        loadPurchaseOrders(tableModel7, "");

        searchField7.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text7 = searchField7.getText();
                loadPurchaseOrders(tableModel7, text7);
            }
        });

        backButton7.addActionListener((ActionEvent _) -> cardLayout.show(mainPanel, "PurchaseOrderMenu"));

        // ===== EDIT PURCHASE ORDER FORM PANEL =====
        // Add this after the editDeleteOrderPanel

        JPanel editPOFormPanel = new JPanel();
        editPOFormPanel.setLayout(new BoxLayout(editPOFormPanel, BoxLayout.Y_AXIS));
        editPOFormPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel8 = new JLabel("Edit Purchase Order", SwingConstants.CENTER);
        titleLabel8.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel8.setAlignmentX(Component.CENTER_ALIGNMENT);
        editPOFormPanel.add(titleLabel8);
        editPOFormPanel.add(Box.createVerticalStrut(30));

        // Form fields for editing
        JPanel editFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcEdit = new GridBagConstraints();
        gbcEdit.insets = new Insets(10, 10, 10, 10);
        gbcEdit.anchor = GridBagConstraints.WEST;

        // PO ID (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 0;
        JLabel editPoIdLabel = new JLabel("PO ID:");
        editPoIdLabel.setFont(labelFont);
        editFormPanel.add(editPoIdLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editPoIdField = new JTextField(15);
        editPoIdField.setFont(fieldFont);
        editPoIdField.setEditable(false);
        editPoIdField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editPoIdField, gbcEdit);

        // PR ID (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 1;
        JLabel editPrIdLabel = new JLabel("PR ID:");
        editPrIdLabel.setFont(labelFont);
        editFormPanel.add(editPrIdLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editPrIdField = new JTextField(15);
        editPrIdField.setFont(fieldFont);
        editPrIdField.setEditable(false);
        editPrIdField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editPrIdField, gbcEdit);

        // Item ID (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 2;
        JLabel editItemIdLabel = new JLabel("Item ID:");
        editItemIdLabel.setFont(labelFont);
        editFormPanel.add(editItemIdLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editItemIdField = new JTextField(15);
        editItemIdField.setFont(fieldFont);
        editItemIdField.setEditable(false);
        editItemIdField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editItemIdField, gbcEdit);

        // Quantity (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 3;
        JLabel editQuantityLabel = new JLabel("Quantity:");
        editQuantityLabel.setFont(labelFont);
        editFormPanel.add(editQuantityLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editQuantityField = new JTextField(15);
        editQuantityField.setFont(fieldFont);
        editQuantityField.setEditable(false);
        editQuantityField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editQuantityField, gbcEdit);

        // Supplier ID (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 4;
        JLabel editSupplierIdLabel = new JLabel("Supplier ID:");
        editSupplierIdLabel.setFont(labelFont);
        editFormPanel.add(editSupplierIdLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editSupplierIdField = new JTextField(15);
        editSupplierIdField.setFont(fieldFont);
        editSupplierIdField.setEditable(false);
        editSupplierIdField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editSupplierIdField, gbcEdit);

        // Shipping Status (editable dropdown)
        gbcEdit.gridx = 0; gbcEdit.gridy = 5;
        JLabel editShippingStatusLabel = new JLabel("Shipping Status:");
        editShippingStatusLabel.setFont(labelFont);
        editFormPanel.add(editShippingStatusLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JComboBox<String> editShippingStatusCombo = new JComboBox<>(new String[]{"Pending", "Arrived"});
        editShippingStatusCombo.setFont(fieldFont);
        editFormPanel.add(editShippingStatusCombo, gbcEdit);

        // Approval Status (editable dropdown)
        gbcEdit.gridx = 0; gbcEdit.gridy = 6;
        JLabel editApprovalStatusLabel = new JLabel("Approval Status:");
        editApprovalStatusLabel.setFont(labelFont);
        editFormPanel.add(editApprovalStatusLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JComboBox<String> editApprovalStatusCombo = new JComboBox<>(new String[]{"Pending", "Approved"});
        editApprovalStatusCombo.setFont(fieldFont);
        editFormPanel.add(editApprovalStatusCombo, gbcEdit);

        // PM Name (read-only)
        gbcEdit.gridx = 0; gbcEdit.gridy = 7;
        JLabel editPmNameLabel = new JLabel("PM Name:");
        editPmNameLabel.setFont(labelFont);
        editFormPanel.add(editPmNameLabel, gbcEdit);
        gbcEdit.gridx = 1;
        JTextField editPmNameField = new JTextField(15);
        editPmNameField.setFont(fieldFont);
        editPmNameField.setEditable(false);
        editPmNameField.setBackground(Color.LIGHT_GRAY);
        editFormPanel.add(editPmNameField, gbcEdit);

        editPOFormPanel.add(editFormPanel);
        editPOFormPanel.add(Box.createVerticalStrut(30));

        // Buttons for edit form
        JPanel editButtonPanel = new JPanel(new FlowLayout());
        JButton updateButton = new JButton("Update Purchase Order");
        JButton cancelEditButton = new JButton("Cancel");

        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelEditButton.setFont(new Font("Arial", Font.PLAIN, 14));

        editButtonPanel.add(updateButton);
        editButtonPanel.add(cancelEditButton);
        editPOFormPanel.add(editButtonPanel);

        // ADD THESE PANELS TO MAIN PANEL
        // Add this line in the section where you add panels to mainPanel
        mainPanel.add(editDeleteOrderPanel, "EditDeleteOrder");
        mainPanel.add(editPOFormPanel, "EditPOForm");

        // MODIFY THE EXISTING editDeleteOrderButton ACTION LISTENER
        // Replace the TODO comment with this:
        editDeleteOrderButton.addActionListener(_ -> {
            loadPurchaseOrders(tableModel7, ""); // Refresh the table
            cardLayout.show(mainPanel, "EditDeleteOrder");
        });

        // ADD THESE EVENT LISTENERS AT THE END OF CONSTRUCTOR
        // Add these after the existing event listeners:

        // Table selection listener for Edit/Delete Purchase Orders
        table7.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table7.getSelectedRow();
                if (selectedRow >= 0) {
                    // Show dialog asking user to choose Edit or Delete
                    String[] options = {"Edit", "Delete", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(
                        this,
                        "What would you like to do with this Purchase Order?",
                        "Edit or Delete",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );

                    if (choice == 0) { // Edit
                        // Get selected PO data
                        String poId = (String) table7.getValueAt(selectedRow, 0);
                        String prId = (String) table7.getValueAt(selectedRow, 1);
                        String itemId = (String) table7.getValueAt(selectedRow, 2);
                        int quantity = (Integer) table7.getValueAt(selectedRow, 3);
                        String supplierId = (String) table7.getValueAt(selectedRow, 4);
                        String shippingStatus = (String) table7.getValueAt(selectedRow, 5);
                        String approvalStatus = (String) table7.getValueAt(selectedRow, 6);
                        String pmName = (String) table7.getValueAt(selectedRow, 7);

                        // Fill edit form fields
                        editPoIdField.setText(poId);
                        editPrIdField.setText(prId);
                        editItemIdField.setText(itemId);
                        editQuantityField.setText(String.valueOf(quantity));
                        editSupplierIdField.setText(supplierId);
                        editShippingStatusCombo.setSelectedItem(shippingStatus);
                        editApprovalStatusCombo.setSelectedItem(approvalStatus);
                        editPmNameField.setText(pmName);

                        // Navigate to edit form panel
                        cardLayout.show(mainPanel, "EditPOForm");

                    } else if (choice == 1) { // Delete
                        String poId = (String) table7.getValueAt(selectedRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete Purchase Order " + poId + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            if (deletePurchaseOrder(poId)) {
                                JOptionPane.showMessageDialog(this, "Purchase Order deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                // Refresh tables
                                loadPurchaseOrders(tableModel7, searchField7.getText());
                                loadPurchaseOrders(tableModel4, "");
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to delete Purchase Order", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    // If Cancel (choice == 2), do nothing
                }
            }
        });

        // Update button listener for edit form
        updateButton.addActionListener(_ -> {
            String poId = editPoIdField.getText();
            String newShippingStatus = (String) editShippingStatusCombo.getSelectedItem();
            String newApprovalStatus = (String) editApprovalStatusCombo.getSelectedItem();
    
            if (updatePurchaseOrder(poId, newShippingStatus, newApprovalStatus)) {
                JOptionPane.showMessageDialog(this, "Purchase Order updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh tables
                loadPurchaseOrders(tableModel7, searchField7.getText());
                loadPurchaseOrders(tableModel4, "");
                // Go back to Edit/Delete panel
                cardLayout.show(mainPanel, "EditDeleteOrder");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update Purchase Order", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelEditButton.addActionListener(_ -> cardLayout.show(mainPanel, "EditDeleteOrder"));

        cardLayout.show(mainPanel, "Menu"); // Show menu on start
        setVisible(true);
    }

    private void loadItems(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("Item.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    KHItem item = new KHItem(
                        parts[0],
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4])
                    );
                    if (item.getDescription().toLowerCase().contains(filter.toLowerCase())) {
                        model.addRow(new Object[]{
                            item.getItemId(),
                            item.getDescription(),
                            item.getSupplierId(),
                            item.getUnitPrice(),
                            item.getSellPrice()
                        });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            //e.printStackTrace();
        }
    }

    private void loadSuppliers(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("Supplier.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    KHSupplier supplier = new KHSupplier(
                        parts[0],
                        parts[1],
                        parts[2]
                    );
                    if (supplier.getName().toLowerCase().contains(filter.toLowerCase())) {
                        model.addRow(new Object[]{
                            supplier.getSupplierId(),
                            supplier.getName(),
                            supplier.getAddress()
                        });
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void loadPurchaseRequisitions(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("Purchase Requisition.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    KHPurchaseRequisition requisition = new KHPurchaseRequisition(
                        parts[0],
                        parts[1],
                        Integer.parseInt(parts[2]),
                        parts[3],
                        parts[4],
                        parts[5]
                    );
                    if (requisition.getPRId().toLowerCase().contains(filter.toLowerCase())) {
                        model.addRow(new Object[]{
                            requisition.getPRId(),
                            requisition.getItemId(),
                            requisition.getQuantity(),
                            requisition.getSupplierId(),
                            requisition.getDate(),
                            requisition.getSmName()
                        });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            //e.printStackTrace();
        }
    }

    private void loadPurchaseOrders(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("Purchase Order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 8) {
                    KHPurchaseOrder order = new KHPurchaseOrder(
                        parts[0],  // poId
                        parts[1],  // prId
                        parts[2],  // itemId
                        Integer.parseInt(parts[3]),  // quantity
                        parts[4],  // supplierId
                        parts[5],  // shippingStatus
                        parts[6],  // approvalStatus
                        parts[7]   // pmName
                    );
                    if (order.getPOId().toLowerCase().contains(filter.toLowerCase())) {
                        model.addRow(new Object[]{
                            order.getPOId(),
                            order.getPRId(),
                            order.getItemId(),
                            order.getQuantity(),
                            order.getSupplierId(),
                            order.getShippingStatus(),
                            order.getApprovalStatus(),
                            order.getPmName()
                        });
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            //e.printStackTrace();
        }
    }

    private String generateNextPOId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("Purchase Order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    String poId = parts[0];
                    if (poId.startsWith("PO")) {
                        try {
                            int id = Integer.parseInt(poId.substring(2));
                            maxId = Math.max(maxId, id);
                        } catch (NumberFormatException e) {
                            // Skip invalid format
                        }
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return String.format("PO%03d", maxId + 1);
    }

    private boolean savePurchaseOrder(KHPurchaseOrder order) {
        try (java.io.FileWriter fw = new java.io.FileWriter("Purchase Order.txt", true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
            out.println(order.toString());
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }
    }

private boolean deletePurchaseOrder(String poId) {
    try {
        // Read all purchase orders
        java.util.List<String> lines = new java.util.ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Purchase Order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0 && !parts[0].equals(poId)) {
                    lines.add(line);
                }
            }
        }
        
        // Write back all lines except the deleted one
        try (java.io.FileWriter fw = new java.io.FileWriter("Purchase Order.txt");
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
            for (String line : lines) {
                out.println(line);
            }
        }
        return true;
    } catch (IOException e) {
        //e.printStackTrace();
        return false;
    }
}

private boolean updatePurchaseOrder(String poId, String newShippingStatus, String newApprovalStatus) {
    try {
        // Read all purchase orders
        java.util.List<String> lines = new java.util.ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Purchase Order.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 8 && parts[0].equals(poId)) {
                    // Update the shipping and approval status
                    parts[5] = newShippingStatus;
                    parts[6] = newApprovalStatus;
                    line = String.join(";", parts);
                }
                lines.add(line);
            }
        }
        
        // Write back all lines with updates
        try (java.io.FileWriter fw = new java.io.FileWriter("Purchase Order.txt");
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
            for (String line : lines) {
                out.println(line);
            }
        }
        return true;
    } catch (IOException e) {
        //e.printStackTrace();
        return false;
    }
}

    public static void khPurchaseManager() {
        SwingUtilities.invokeLater(() -> new KHPurchaseManagerGUI());
    }
}