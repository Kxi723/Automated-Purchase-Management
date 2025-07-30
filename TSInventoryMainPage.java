package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class TSInventoryMainPage {
    // Controllers
    private static TSItemManager itemManager;
    private static TSStockManager stockManager;
    private static TSPurchaseOrderManager poManager;
    private static TSSupplierManager supplierManager;

    // Main frame
    private static JFrame mainFrame;
    // Current panel
    private static JFrame lowStockMonitorFrame = null;

    public static void tsInventoryManager() {
        // Initialize controllers
        itemManager = new TSItemManager();
        supplierManager = new TSSupplierManager();
        stockManager = new TSStockManager(itemManager);
        poManager = new TSPurchaseOrderManager(stockManager);

        SwingUtilities.invokeLater(() -> {
            showIMMainMenu();
            showLowStockAlertIfNeeded();
        });
    }

    /**
     * Show the Inventory Manager main menu
     */
    private static void showIMMainMenu() {
        mainFrame = new JFrame("Inventory - Inventory Manager Main Menu");
        //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 420);
        mainFrame.setLocationRelativeTo(null);

        // kxi add
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) { // if close the window, then KxiStartMain.kxiMainPage()
                mainFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });
        

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Left panel for Welcome text and Logout button
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setLayout(new BorderLayout(0, 20));
        JLabel welcomeLabel = new JLabel("<html><div style=\"text-align: center;\"><h1>Welcome to<br>Inventory Management System</h1></div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        logoutButton.addActionListener(_ -> {
            mainFrame.dispose();
            JOptionPane.showMessageDialog(null, "Logged out. Returning to login page.");
            KxiLogIn.kxiLogInProcess();
        });
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(Color.LIGHT_GRAY);
        logoutPanel.add(logoutButton);
        leftPanel.add(welcomeLabel, BorderLayout.CENTER);
        leftPanel.add(logoutPanel, BorderLayout.SOUTH);

        // Right panel for IM function buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setLayout(new GridLayout(5, 1, 20, 20));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton viewItemsButton = new JButton("View Item List");
        viewItemsButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewItemsButton.addActionListener(_ -> showItemListWindow());
        rightPanel.add(viewItemsButton);

        JButton viewPOButton = new JButton("View Purchase Orders");
        viewPOButton.setFont(new Font("Arial", Font.BOLD, 20));
        viewPOButton.addActionListener(_ -> showPOListWindow());
        rightPanel.add(viewPOButton);

        JButton updateStockButton = new JButton("Update Stock");
        updateStockButton.setFont(new Font("Arial", Font.BOLD, 20));
        updateStockButton.addActionListener(_ -> showUpdateStockWindow());
        rightPanel.add(updateStockButton);

        JButton lowStockButton = new JButton("View Stock");
        lowStockButton.setFont(new Font("Arial", Font.BOLD, 20));
        lowStockButton.setText("View Stock");
        lowStockButton.addActionListener(_ -> showStockWindow());
        rightPanel.add(lowStockButton);

        JButton stockReportButton = new JButton("Generate Stock Report");
        stockReportButton.setFont(new Font("Arial", Font.BOLD, 20));
        stockReportButton.addActionListener(_ -> showStockReportWindow());
        rightPanel.add(stockReportButton);

        // Use a central panel to hold left and right panels closer
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 0));
        centerPanel.setBackground(Color.LIGHT_GRAY);
        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainFrame.setContentPane(mainPanel);
        mainFrame.setVisible(true);
    }

    /**
     * Helper method to extract the numeric part of a PO ID.
     * Returns 0 or a negative value for invalid or initial IDs like "N/A".
     */
    private static int getPoNumber(String poId) {
        if (poId == null || poId.trim().isEmpty() || !poId.toUpperCase().startsWith("PO")) {
            return 0; // Treat as the lowest possible ID
        }
        try {
            return Integer.parseInt(poId.substring(2)); // Extract the number after "PO"
        } catch (NumberFormatException e) {
            // Handle cases like non-numeric part after "PO", treat as lowest
            return 0;
        }
    }

    // --- Stock Window ---
    public static void showStockWindow() {
        if (lowStockMonitorFrame != null) {
            lowStockMonitorFrame.toFront();
            return;
        }
        lowStockMonitorFrame = new JFrame("Stock");
        lowStockMonitorFrame.setSize(1100, 700);
        lowStockMonitorFrame.setLocationRelativeTo(mainFrame);
        lowStockMonitorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lowStockMonitorFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                lowStockMonitorFrame = null;
            }
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                lowStockMonitorFrame = null;
            }
        });
        refreshStockTable(null);
        lowStockMonitorFrame.setVisible(true);
    }
    private static void refreshStockTable(String filterLevel) {
        if (lowStockMonitorFrame == null) return;
        List<TSStock> allStocks = stockManager.getAllStocks();
        List<TSStock> filteredStocks = new ArrayList<>();

        // Apply filter
        if (filterLevel == null || "All".equals(filterLevel)) {
            filteredStocks = allStocks;
        } else {
            for (TSStock s : allStocks) {
                if (filterLevel.equals(stockManager.getStockLevel(s.getItemID(), s.getQuantity()))) {
                    filteredStocks.add(s);
                }
            }
        }

        String[] columns = {"Item ID", "Description", "Quantity", "Stock Level", "Last Updated"};
        Object[][] data = new Object[filteredStocks.size()][5];
        for (int i = 0; i < filteredStocks.size(); i++) {
            TSStock s = filteredStocks.get(i);
            // Get item description
            TSItem item = itemManager.findItemByID(s.getItemID());
            String description = (item != null) ? item.getDescription() : "N/A";

            data[i][0] = s.getItemID();
            data[i][1] = description;
            data[i][2] = s.getQuantity();
            data[i][3] = stockManager.getStockLevel(s.getItemID(), s.getQuantity());
            data[i][4] = s.getLastUpdated();
        }

        // Recreate table and panel content
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Apply custom renderer for coloring
        StockLevelRenderer renderer = new StockLevelRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);
        table.getColumnModel().getColumn(3).setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create new main panel for Stock Window
        JPanel mainStockPanel = new JPanel(new BorderLayout());
        mainStockPanel.setBackground(Color.LIGHT_GRAY);
        mainStockPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create filter buttons panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.LIGHT_GRAY);
        filterPanel.add(new JLabel("Filter by Stock Level:"));
        JButton allBtn = new JButton("All");
        JButton highBtn = new JButton("High");
        JButton mediumBtn = new JButton("Medium");
        JButton lowBtn = new JButton("Low");

        allBtn.addActionListener(_ -> refreshStockTable("All"));
        highBtn.addActionListener(_ -> refreshStockTable("High"));
        mediumBtn.addActionListener(_ -> refreshStockTable("Medium"));
        lowBtn.addActionListener(_ -> refreshStockTable("Low"));

        filterPanel.add(allBtn);
        filterPanel.add(highBtn);
        filterPanel.add(mediumBtn);
        filterPanel.add(lowBtn);

        // Bottom panel with Request and Back buttons
        JPanel bottomStockPanel = new JPanel();
        bottomStockPanel.setBackground(Color.LIGHT_GRAY);
        JButton requestBtn = new JButton("Request Item");
        requestBtn.setFont(new Font("Arial", Font.BOLD, 18));
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(_ -> lowStockMonitorFrame.dispose());

        bottomStockPanel.add(requestBtn);
        bottomStockPanel.add(backBtn);

        // Add components to the main stock panel
        mainStockPanel.add(filterPanel, BorderLayout.NORTH);
        mainStockPanel.add(scrollPane, BorderLayout.CENTER);
        mainStockPanel.add(bottomStockPanel, BorderLayout.SOUTH);

        // Update the frame content
        lowStockMonitorFrame.setContentPane(mainStockPanel);
        lowStockMonitorFrame.revalidate();
        lowStockMonitorFrame.repaint();

        // Add action listener to requestBtn AFTER the table is set up
        requestBtn.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get Item ID from the table model (assuming Item ID is in the first column - index 0)
                String itemID = (String) table.getModel().getValueAt(table.convertRowIndexToModel(selectedRow), 0);
                JOptionPane.showMessageDialog(lowStockMonitorFrame, "Request for item " + itemID + " sent to Purchase Manager.", "Request Sent", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(lowStockMonitorFrame, "Please select an item to request.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // --- Function Pop-up Window ---
    private static void showItemListWindow() {
        JFrame win = new JFrame("Item List");
        win.setSize(1100, 700);
        win.setLocationRelativeTo(mainFrame);
        List<TSItem> items = itemManager.getAllItems();
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchPanel.add(new JLabel("Search Description, Supplier ID, or Supplier Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        String[] columns = {"Item ID", "Description", "Supplier ID", "Supplier Name", "Quantity", "Unit Price", "Sales Price"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        loadItemTableData(tableModel, items);

        searchButton.addActionListener(_ -> {
            String query = searchField.getText().toLowerCase();
            List<TSItem> allItems = itemManager.getAllItems();
            List<TSItem> filteredItems = new ArrayList<>();
            if (query.isEmpty()) {
                filteredItems = allItems;
            } else {
                for (TSItem item : allItems) {
                    String supplierName = supplierManager.getSupplierNameByID(item.getSupplierID());
                    if (item.getDescription().toLowerCase().contains(query) ||
                        item.getSupplierID().toLowerCase().contains(query) ||
                        supplierName.toLowerCase().contains(query)) {
                        filteredItems.add(item);
                    }
                }
            }
            loadItemTableData(tableModel, filteredItems);
        });

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(_ -> win.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backBtn);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        win.setContentPane(mainPanel);
        win.setVisible(true);
    }

    private static void loadItemTableData(javax.swing.table.DefaultTableModel tableModel, List<TSItem> items) {
        tableModel.setRowCount(0);
        for (TSItem it : items) {
            TSStock stock = stockManager.findStockByItemID(it.getItemID());
            int quantity = (stock != null) ? stock.getQuantity() : 0;
            String supplierName = supplierManager.getSupplierNameByID(it.getSupplierID());
            tableModel.addRow(new Object[]{
                it.getItemID(),
                it.getDescription(),
                it.getSupplierID(),
                supplierName,
                quantity,
                String.valueOf(it.getUnitPrice()),
                String.valueOf(it.getSalesPrice())
            });
        }
    }

    private static void showPOListWindow() {
        JFrame win = new JFrame("Purchase Orders");
        win.setSize(1100, 700);
        win.setLocationRelativeTo(mainFrame);
        List<TSPurchaseOrder> pos = poManager.getAllPurchaseOrders();
        String[] columns = {"PO ID", "PR ID", "Item ID", "Item Description", "Quantity", "Supplier ID", "Supplier Name", "Shipping Status", "Approval Status", "PM Name"};
        Object[][] data = new Object[pos.size()][10];
        for (int i = 0; i < pos.size(); i++) {
            TSPurchaseOrder po = pos.get(i);
            TSItem item = itemManager.findItemByID(po.getItemID());
            String itemDescription = (item != null) ? item.getDescription() : "N/A";
            String supplierID = (item != null) ? item.getSupplierID() : "N/A";
            String supplierName = supplierManager.getSupplierNameByID(supplierID);
            data[i][0] = po.getPoID();
            data[i][1] = po.getPrID();
            data[i][2] = po.getItemID();
            data[i][3] = itemDescription;
            data[i][4] = po.getQuantity();
            data[i][5] = supplierID;
            data[i][6] = supplierName;
            data[i][7] = po.getShippingStatus();
            data[i][8] = po.getApprovalStatus();
            data[i][9] = po.getPmName();
        }
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(100);
        table.getColumnModel().getColumn(9).setPreferredWidth(100);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(_ -> win.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        win.setContentPane(panel);
        win.setVisible(true);
    }

    public static void showUpdateStockWindow() {
        showUpdateStockWindow(null);
    }
    public static void showUpdateStockWindow(String itemIdToSelect) {
        JFrame win = new JFrame("Update Stock");
        win.setSize(1100, 700);
        win.setLocationRelativeTo(mainFrame);
        List<TSPurchaseOrder> pos = poManager.getAllPurchaseOrders();
        pos.removeIf(po -> !"Arrived".equalsIgnoreCase(po.getShippingStatus()) || !"Approved".equalsIgnoreCase(po.getApprovalStatus()));
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.LIGHT_GRAY);
        filterPanel.add(new JLabel("Filter Status:"));
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Updated"}); // Simplified status options
        filterPanel.add(statusFilter);

        filterPanel.add(new JLabel("Supplier ID or Name:"));
        JTextField supplierFilterField = new JTextField(15);
        filterPanel.add(supplierFilterField);

        JButton applyFilterButton = new JButton("Apply Filter");
        filterPanel.add(applyFilterButton);

        // Table setup
        String[] columns = {"PO ID", "Item ID", "Description", "Quantity", "Supplier ID", "Supplier Name", "Update Status"}; // Added Supplier Name
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // PO ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80); // Item ID
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Description
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Quantity
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Supplier ID
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Supplier Name
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // Update Status - Increased width

        JScrollPane scrollPane = new JScrollPane(table);

        // Store the original list of POs with initial status
        List<TSPurchaseOrder> originalPosWithStatus = new ArrayList<>();
        for(TSPurchaseOrder po : pos) {
            // Create a temporary object or map to hold PO and its status
            // For simplicity, let's add a temporary status field to PurchaseOrder if possible, or use a Map
            // Given the constraints, let's use a Map for demonstration.
            // In a real application, the status might be stored elsewhere or in the PO object.
            Map<String, Object> poData = new HashMap<>();
            poData.put("po", po);
            poData.put("status", "Pending");
            originalPosWithStatus.add((TSPurchaseOrder) poData.get("po")); // Store original PO
            // We need to associate the status with the displayed data, not the original PO object
        }

        // Helper to load filtered data into the table
        Runnable loadFilteredUpdateStockData = () -> {
            tableModel.setRowCount(0); // Clear existing data
            String selectedStatus = (String) statusFilter.getSelectedItem();
            String supplierQuery = supplierFilterField.getText().trim().toLowerCase();

            for (TSPurchaseOrder po : pos) { // Iterate through the initial list of Arrived & Approved POs
                boolean statusMatch = true;
                String currentStatus;
                TSStock itemStock = stockManager.findStockByItemID(po.getItemID());

                // Determine status based on the item's stock's last updated PO ID
                String lastUpdatedPoId = (itemStock != null) ? itemStock.getLastUpdated() : "N/A";

                if (itemStock != null && "PO000".equals(lastUpdatedPoId)) {
                    currentStatus = "Pending"; // Display Pending if last updated by PO000
                } else if (itemStock != null && !"N/A".equals(lastUpdatedPoId) && !lastUpdatedPoId.isEmpty()) {
                    currentStatus = "Updated by " + lastUpdatedPoId; // Display Updated by actual PO ID
                }
                else {
                    currentStatus = "Pending"; // Default to Pending (e.g., stock item not found or lastUpdated is N/A/empty)
                }

                // Apply status filter
                if ("Pending".equals(selectedStatus) && !"Pending".equals(currentStatus)) {
                    statusMatch = false;
                } else if ("Updated".equals(selectedStatus) && "Pending".equals(currentStatus)) {
                    statusMatch = false;
                }

                boolean supplierMatch = true;
                if (!supplierQuery.isEmpty()) {
                    TSItem item = itemManager.findItemByID(po.getItemID());
                    String supplierID = (item != null) ? item.getSupplierID() : "N/A";
                    String supplierName = supplierManager.getSupplierNameByID(supplierID);
                    if (!supplierID.toLowerCase().contains(supplierQuery) && !supplierName.toLowerCase().contains(supplierQuery)) {
                        supplierMatch = false;
                    }
                }

                if (statusMatch && supplierMatch) {
                    TSItem item = itemManager.findItemByID(po.getItemID());
                    String itemDescription = (item != null) ? item.getDescription() : "N/A";
                    String supplierID = (item != null) ? item.getSupplierID() : "N/A";
                    String supplierName = supplierManager.getSupplierNameByID(supplierID);
                    tableModel.addRow(new Object[]{
                        po.getPoID(),
                        po.getItemID(),
                        itemDescription,
                        po.getQuantity(),
                        supplierID,
                        supplierName,
                        currentStatus // Display calculated status
                    });
                }
            }
            // Re-select the previously selected row if possible after filtering
            // This is complex with dynamic filtering, might skip for now or add later
        };

        // Load initial data
        loadFilteredUpdateStockData.run();

        // Apply filter button action
        applyFilterButton.addActionListener(_ -> loadFilteredUpdateStockData.run());

        // Update button action
        JButton updateBtn = new JButton("Update Stock"); // Removed text
        updateBtn.setFont(new Font("Arial", Font.BOLD, 18));
        updateBtn.addActionListener(_ -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(win, "Please select a row to update!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get the PO ID from the selected row in the table model
            String poId = (String) table.getModel().getValueAt(table.convertRowIndexToModel(row), 0);

            // Find the corresponding PurchaseOrder object from the original list of Arrived & Approved POs
            TSPurchaseOrder poToUpdate = null;
            for(TSPurchaseOrder po : pos) {
                if(po.getPoID().equals(poId)) {
                    poToUpdate = po;
                    break;
                }
            }

            if (poToUpdate != null) {
                TSStock stock = stockManager.findStockByItemID(poToUpdate.getItemID());
                if (stock != null) {
                    int currentPoNum = getPoNumber(poToUpdate.getPoID());
                    int lastUpdatedPoNum = getPoNumber(stock.getLastUpdated());

                    if (currentPoNum > lastUpdatedPoNum) {
                        // Allow update if current PO number is greater than last updated PO number
                        boolean success = stockManager.updateStockQuantity(poToUpdate.getItemID(), poToUpdate.getQuantity(), poToUpdate.getPoID()); // Update stock with the current PO ID
                        if (success) {
                            JOptionPane.showMessageDialog(win, "Stock updated for Item ID: " + poToUpdate.getItemID() + " using PO " + poToUpdate.getPoID(), "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                            // Re-load data to update status after successful update
                            loadFilteredUpdateStockData.run();
                            refreshStockTable(null); // Refresh the Stock view after update
                        } else {
                            JOptionPane.showMessageDialog(win, "Failed to update stock for Item ID: " + poToUpdate.getItemID(), "Update Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Prevent update if current PO number is not greater than last updated PO number
                        JOptionPane.showMessageDialog(win, "Stock for Item ID " + poToUpdate.getItemID() + " can only be updated with a later Purchase Order ID. Last updated by PO " + stock.getLastUpdated(), "Update Not Allowed", JOptionPane.WARNING_MESSAGE);
                    }
                }
             }
        });

        // Back button
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(_ -> win.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY); // Match main panel background
        bottomPanel.add(updateBtn);
        bottomPanel.add(backBtn);

        // Add components to main panel
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        win.setContentPane(mainPanel);
        win.setVisible(true);
    }

    public static void showStockReportWindow() {
        JFrame win = new JFrame("Stock Report");
        win.setSize(1100, 700);
        win.setLocationRelativeTo(mainFrame);
        List<TSStock> stocks = stockManager.getAllStocks();
        double totalWarehouseValue = 0.0;
        
        String[] columns = {"Item ID", "Description", "Supplier ID", "Quantity", "Stock Level", "Item Value (Unit Price * Quantity)", "Last Updated"};
        Object[][] data = new Object[stocks.size()][7];
        for (int i = 0; i < stocks.size(); i++) {
            TSStock s = stocks.get(i);
            TSItem item = itemManager.findItemByID(s.getItemID());
            String description = (item != null) ? item.getDescription() : "N/A";
            String supplierID = (item != null) ? item.getSupplierID() : "N/A";
            double unitPrice = (item != null) ? item.getUnitPrice() : 0.0;
            double itemValue = s.getQuantity() * unitPrice;
            totalWarehouseValue += itemValue;

            data[i][0] = s.getItemID();
            data[i][1] = description;
            data[i][2] = supplierID;
            data[i][3] = s.getQuantity();
            data[i][4] = stockManager.getStockLevel(s.getItemID(), s.getQuantity());
            data[i][5] = String.format("%.2f", itemValue);
            data[i][6] = s.getLastUpdated();
        }

        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton exportBtn = new JButton("Export as CSV");
        exportBtn.setFont(new Font("Arial", Font.BOLD, 18));
        exportBtn.addActionListener(_ -> exportStockReportToCSV(stocks));
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(_ -> win.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(exportBtn);
        bottomPanel.add(backBtn);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        win.setContentPane(panel);
        win.setVisible(true);
        
        JLabel totalValueLabel = new JLabel("Total Warehouse Value: $" + String.format("%.2f", totalWarehouseValue));
        totalValueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(totalValueLabel);
        panel.add(topPanel, BorderLayout.NORTH);
    }

    private static void exportStockReportToCSV(List<TSStock> stocks) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Stock Report as CSV");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultName = "Stock_Report_" + sdf.format(new java.util.Date()) + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultName));
        int userSelection = fileChooser.showSaveDialog(mainFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write("Item ID,Description,Supplier ID,Quantity,Stock Level,Item Value (Unit Price * Quantity),Last Updated\n");
                double totalWarehouseValue = 0.0;
                for (TSStock s : stocks) {
                    TSItem item = itemManager.findItemByID(s.getItemID());
                    String description = (item != null) ? item.getDescription().replace(",", "") : "N/A";
                    String supplierID = (item != null) ? item.getSupplierID() : "N/A";
                    double unitPrice = (item != null) ? item.getUnitPrice() : 0.0;
                    double itemValue = s.getQuantity() * unitPrice;
                    totalWarehouseValue += itemValue;
                    writer.write(String.format("%s,%s,%s,%d,%s,%.2f,%s\n", s.getItemID(), description, supplierID, s.getQuantity(), stockManager.getStockLevel(s.getItemID(), s.getQuantity()), itemValue, s.getLastUpdated()));
                }
                writer.write(String.format("\nTotal Warehouse Value:,$%.2f\n", totalWarehouseValue));
                JOptionPane.showMessageDialog(mainFrame, "Stock report exported as CSV!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Export failed: " + ex.getMessage());
            }
        }
    }


    // Show low stock alert as a small dialog
    private static void showLowStockAlertIfNeeded() {
        List<TSStock> lowStocks = stockManager.getLowStocks();
        if (!lowStocks.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("The following items are low in stock:\n\n");
            sb.append(String.format("%-10s %-10s %-10s\n", "ItemID", "Quantity", "Level"));
            int maxItemsToShow = Math.min(lowStocks.size(), 5);
            for (int i = 0; i < maxItemsToShow; i++) {
                TSStock s = lowStocks.get(i);
                sb.append(String.format("%-10s %-10d %-10s\n", s.getItemID(), s.getQuantity(), stockManager.getStockLevel(s.getItemID(), s.getQuantity())));
            }
            if (lowStocks.size() > maxItemsToShow) {
                sb.append("... and " + (lowStocks.size() - maxItemsToShow) + " more.\n");
            }
            sb.append("\nPlease check the Low Stock Monitor for details.");

            Object[] options = {"OK", "Check Detail"};

            int choice = JOptionPane.showOptionDialog(
                mainFrame,
                sb.toString(),
                "Low Stock Alert",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == JOptionPane.NO_OPTION) {
                showStockWindow();
            }
        }
    }
}

class StockLevelRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String stockLevel = (value != null) ? value.toString() : "";

        if ("High".equals(stockLevel)) {
            c.setBackground(new Color(144, 238, 144));
        } else if ("Medium".equals(stockLevel)) {
            c.setBackground(new Color(255, 255, 153));
        } else if ("Low".equals(stockLevel)) {
            c.setBackground(new Color(255, 99, 71));
        } else {
            c.setBackground(table.getBackground());
        }

        return c;
    }
} 