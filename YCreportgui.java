package Purchase_Order_Management;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class YCreportgui extends YCBaseFrame implements ActionListener {

    private ArrayList<String> date_1 = new ArrayList<>();
    private ArrayList<String> date_2 = new ArrayList<>();
    private JComboBox<String> comboBox;
    private JLabel label;
    private JButton button;

    public YCreportgui() {
        super("Report Month", 800, 400);
        setLayout(null); // Absolute positioning (use with caution)


        loadDataFromFile();

        for (String date : date_1) {
            String[] parts = date.split("/");
            if (parts.length == 3) {
                date_2.add(parts[1]); 
            }
        }

        // Remove duplicate months (e.g., Jan, Feb only once)
        Set<String> uniqueMonths = new LinkedHashSet<>(date_2);

        button = new JButton("Confirm");
        button.addActionListener(this);
        button.setBounds(100, 100, 100, 30);
        add(button);

        label = new JLabel("Select a month:");
        label.setBounds(20, 20, 150, 30);
        add(label);

        comboBox = new JComboBox<>();
        for (String month : uniqueMonths) {
            comboBox.addItem(month);
        }
        comboBox.setBounds(180, 20, 120, 30);
        comboBox.addActionListener(this);
        add(comboBox);

        setVisible(true);
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Daily Sale Record.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    date_1.add(parts[4].trim()); 
                }
            }
        } catch (IOException e) {
            showError("Error reading 'Daily Sale Record.txt'", "File Error");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboBox) {
            String selectedMonth = (String) comboBox.getSelectedItem();
            JOptionPane.showMessageDialog(this, "You selected month: " + selectedMonth);
        }else if(e.getSource() == button){
            String selectedMonth = (String) comboBox.getSelectedItem();
            SwingUtilities.invokeLater(() -> new YCreport(selectedMonth));
            dispose();
        }

    }

}
