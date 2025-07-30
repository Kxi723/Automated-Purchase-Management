package Purchase_Order_Management;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class KxiViewRegistor {
    private static final String USER_FILE = "Name List.txt";
    private static final String REGISTER_FILE = "Register List.txt";

    public static void kxiApproveRegistor(String userName) {
        JFrame registerListFrame = KxiDesign.createCustomSizeFrame("Approve Registor - " + userName, 800, 700);

        JLabel titleLabel = KxiDesign.createLabelCustomTheme("Register List", "Garamond", false, 30);

        JButton approveButton = KxiDesign.createCustomSizeButton("Approve", 100, 36);
        JButton rejectButton = KxiDesign.createCustomSizeButton("Reject", 90, 36);
        JButton showPendingButton = KxiDesign.createCustomSizeButton("Filter \"Pending\"", 160, 36);
        JButton listAllButton = KxiDesign.createCustomSizeButton("Show All", 120, 36);
        JButton backButton = KxiDesign.createCustomSizeButton("Back", 70, 36);

        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);
        
        String[] columnNames = {"Role Registed", "Name", "Phone", "E-mail Address", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // false = uneditable
            }
        };

        Dimension tableSize = new Dimension(650, 350);

        JTable listTable = new JTable(tableModel);
        KxiDesign.styleTable(listTable, true, tableSize);
        KxiDesign.setColumnWidths(listTable, 90, 120, 40, 120, 30);

        JScrollPane scrollPane = KxiDesign.createTransparentScrollPane(listTable, tableSize);

        // =================== table layout ==========================================================

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
        // design table layout
        tablePanel.add(Box.createHorizontalGlue());
        tablePanel.add(scrollPane);
        tablePanel.add(Box.createHorizontalGlue());

        // =================== button layout ==========================================================

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        // design button layout
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(approveButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(rejectButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(showPendingButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(listAllButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        // design whole layout
        containerPanel.add(Box.createVerticalGlue());
        containerPanel.add(titleLabel);
        containerPanel.add(Box.createVerticalStrut(20));
        containerPanel.add(tablePanel);
        containerPanel.add(buttonPanel);
        containerPanel.add(Box.createVerticalGlue());

        // =================== frame design ========================================================

        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        registerListFrame.setContentPane(mainPanel);
        registerListFrame.setVisible(true);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        List<String[]> allData = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(REGISTER_FILE))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    allData.add(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(registerListFrame, "Error found while loading registration list: " + e.getMessage(),
            "Error Message", JOptionPane.WARNING_MESSAGE);
        }

        loadDataToTable(allData, tableModel);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        listTable.setRowSorter(sorter); // add sorting mechanism

        listAllButton.addActionListener(_ -> {
            sorter.setRowFilter(null);
        });

        showPendingButton.addActionListener(_ -> {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("pending", 4);
            sorter.setRowFilter(filter);
        });

        rejectButton.addActionListener(_ -> {
            int[] selectedRows = listTable.getSelectedRows();
            if (selectedRows.length > 0) {
                updateStatus(selectedRows, "rejected", allData, tableModel, listTable);
            }
        });

        listTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // when user releases the mouse, it go true
                int selectedRow = listTable.getSelectedRow(); // if no row selected, the value is '-1'
                boolean hasSelection = selectedRow != -1; 
                
                approveButton.setEnabled(hasSelection);
                rejectButton.setEnabled(hasSelection);
            }
        });

        approveButton.addActionListener(_ -> {
            int[] selectedRows = listTable.getSelectedRows();
            if (selectedRows.length > 0) {
                updateStatus(selectedRows, "approved", allData, tableModel, listTable);
                
                int maxUserId = 0;
                // read newest ID
                try (BufferedReader fileReader = new BufferedReader(new FileReader(USER_FILE))) {
                    String userLine;
                    while ((userLine = fileReader.readLine()) != null) {
                        String[] parts1 = userLine.split(";");

                        if (parts1.length == 6 && parts1[1].startsWith("ID")) {
                            try {
                                String idNumber = parts1[1].substring(2);
                                int id = Integer.parseInt(idNumber);
                                if (id > maxUserId) {
                                    maxUserId = id;
                                }
                            } catch (NumberFormatException e_id) {
                                // continue
                            }
                        }
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(registerListFrame, "Error found while loading ID numbers: " + e1.getMessage(), 
                    "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int modelRow = listTable.convertRowIndexToModel(selectedRows[0]); // get the first line i check
                String[] selectedData = allData.get(modelRow); // catch from allData

                // generate new id
                int nextId = maxUserId + 1;
                String newId = String.format("ID%03d", nextId);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
                    StringBuilder newLine = new StringBuilder();

                    newLine.append(selectedData[0]);
                    newLine.append(";");
                    newLine.append(newId);
                    newLine.append(";");
                    newLine.append(selectedData[1]);
                    newLine.append(";");
                    newLine.append(selectedData[2]);
                    newLine.append(";");
                    newLine.append(selectedData[3]);
                    newLine.append(";");
                    newLine.append(selectedData[4]);

                    writer.write(newLine.toString());
                    writer.newLine();

                    JOptionPane.showMessageDialog(registerListFrame,"Approve successfully.\nName: " + selectedData[1] + ", new ID: " + newId,
                    "System Message", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(registerListFrame, "Failed to write new data: " + e.getMessage(), 
                    "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerListFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                registerListFrame.dispose();
                KxiAdmin.kxiAdminPage(userName);
            }
        });

        backButton.addActionListener(_ -> {
            registerListFrame.dispose();
            KxiAdmin.kxiAdminPage(userName);
        });
    }

    private static void loadDataToTable(List<String[]> allData, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        for (String[] record : allData) {
            if (record.length == 6) {
                String[] displayRow = {record[0], record[1], record[3], record[4], record[5]};
                tableModel.addRow(displayRow);
            }
        }
    }

    private static void updateStatus(int[] selectedRows, String newStatus, List<String[]> allData, DefaultTableModel tableModel, JTable table) {
        List<String> updatedContent = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(REGISTER_FILE))) {
            String line;
            int lineIndex = 0;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                boolean shouldUpdate = false; // if got more need to update, always set to default

                for (int selectedRow : selectedRows) { // check which line is the one i select, because selectedRows is not accuracy
                    int modelRow = table.convertRowIndexToModel(selectedRow); // get the line we selected at JTable
                    if (lineIndex == modelRow && parts.length == 6) { // if now the line is same, then go update
                        shouldUpdate = true;
                        break;
                    }
                }
                
                if (shouldUpdate && parts.length == 6) {
                    parts[5] = newStatus;

                    line = String.join(";", parts); // later write into file
                    
                    for (int selectedRow : selectedRows) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        if (lineIndex == modelRow) {
                            allData.get(modelRow)[5] = newStatus; // update into allData for display
                        }
                    }
                }
                
                updatedContent.add(line); // no matter updated or not, rewrite everything
                lineIndex++; // go to new line
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGISTER_FILE))) {
                for (String updatedLine : updatedContent) { // because the format is array list, so use for to write whole
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }
            
            // refresh new data at table
            loadDataToTable(allData, tableModel);
                        
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error updating status: " + ex.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}