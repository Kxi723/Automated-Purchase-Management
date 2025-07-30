package Purchase_Order_Management;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
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
import javax.swing.table.DefaultTableModel;

public class KxiViewUser {
    private static final String USER_FILE = "Name List.txt";

    public static void kxiDisplayRegistrantList(boolean backMainPage, String userName) {
        JFrame registerListFrame = KxiDesign.createCustomSizeFrame("User List", 600, 450);
        
        JLabel titleLabel = KxiDesign.createLabelCustomTheme("User List", "Garamond", false, 30);

        JButton backButton = KxiDesign.createCustomSizeButton("Back", 70, 36);

        String[] columnNames = {"Role", "ID", "Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // false = uneditable
            }
        };

        Dimension tableSize = new Dimension(400, 120);

        JTable listTable = new JTable(tableModel);
        KxiDesign.styleTable(listTable, false, tableSize);
        KxiDesign.setColumnWidths(listTable, 100, 30, 150);

        JScrollPane scrollPane = KxiDesign.createTransparentScrollPane(listTable, tableSize);

        // =================== table layout ==========================================================

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
        // design table layout
        tablePanel.add(Box.createHorizontalGlue());
        tablePanel.add(scrollPane);
        tablePanel.add(Box.createHorizontalGlue());

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        // design whole layout
        containerPanel.add(Box.createVerticalGlue());
        containerPanel.add(titleLabel);
        containerPanel.add(Box.createVerticalStrut(20));
        containerPanel.add(tablePanel);
        containerPanel.add(Box.createVerticalStrut(30));
        containerPanel.add(backButton);
        containerPanel.add(Box.createVerticalGlue());

        // =================== frame design ========================================================

        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        registerListFrame.setContentPane(mainPanel);
        registerListFrame.setVisible(true);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        List<String[]> allData = new ArrayList<>(); // array cannot difference the line, list size is fix, so list<string> better
        try (BufferedReader fileReader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    allData.add(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(registerListFrame, "Error found while loading users list: " + e.getMessage(),
            "Error Message", JOptionPane.WARNING_MESSAGE);
        }

        loadDataToTable(allData, tableModel);

        registerListFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                registerListFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });

        backButton.addActionListener(_ -> {
            registerListFrame.dispose();
            if (backMainPage){
                KxiStartMain.kxiMainPage();
            } else {
                KxiAdmin.kxiAdminPage(userName);
            }
        });
    }

    private static void loadDataToTable(List<String[]> data, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        for (String[] parts : data) { // for loop the list, so store array data in array form
            if (parts.length == 6) {
                String[] writenRow = {parts[0], parts[1], parts[2]}; // store data in writenRow
                tableModel.addRow(writenRow); 
            }
        }
    }
}