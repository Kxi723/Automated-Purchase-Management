package Purchase_Order_Management;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SoyaSupplierEntry extends SoyaSalesManager {
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private JTable tableSEData;
    private JTable tableBackUpData;
    private JScrollPane scrollPaneTable;
    private JPanel panelSE;
    private int rowsTable;
    private int rowsNewTable;
    private int rowsFoundTable;
    private int columns;
    private int countEdit = 0;
    private String fileName = "Supplier.txt";
    private String[][] supplierData;
    private String[][] supplierAfter;
    private String[][] supplierFound;
    private String[] title = {"No.", "Supplier ID", "Name", "Address"};
    private String showID;
    private String showName;
    private String showAddress;

    public SoyaSupplierEntry(){
    }

    public SoyaSupplierEntry(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel){
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        rowsTable = getFileRows(fileName);
        columns = title.length - 1;
        supplierData = new String[rowsTable][title.length];
    }

    @Override
    public void setPanel(){
        tableSEData = setTable(fileName, rowsTable, columns, supplierData, title); // Table in use
        tableBackUpData = setTable(fileName, rowsTable, columns, supplierData, title); //Backup, will be used when restoring
        panelSE = new JPanel(new GridBagLayout());
        scrollPaneTable = new JScrollPane(tableSEData);
        JButton buttonAddSupplier = new JButton("Add Supplier");
        JButton buttonEdit = new JButton("Edit Info");
        JButton buttonDelete = new JButton("Delete Supplier");
        JButton buttonSave = new JButton("Save Info");
        JButton buttonBackMain = new JButton("Back to Main Page");
        JButton buttonSearch = new JButton("Search");
        JButton buttonRestore = new JButton("Restore Table");
        JTextField textSearchBar = new JTextField();
        JLabel labelFunction= new JLabel("Supplier Entry");
        JLabel labelSearch = new JLabel("Enter Supplier ID to search");
        
        buttonAddSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addData();;
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editData();
            }
        });
        
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = textSearchBar.getText();
                searchData(inputID);
            }
        });

        buttonRestore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreTable();
            }
        });

        buttonBackMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backMain();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        
        setGridBagLayout(gbc, 1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        panelSE.add(labelSearch, gbc);

        setGridBagLayout(gbc, 2, 1, 2, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelSE.add(textSearchBar, gbc);

        setGridBagLayout(gbc, 0, 0, 5, 1);
        panelSE.add(labelFunction, gbc);

        setGridBagLayout(gbc, 4, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        panelSE.add(buttonSearch, gbc);

        setGridBagLayout(gbc, 0, 1, 1, 1);
        panelSE.add(buttonBackMain, gbc);

        setGridBagLayout(gbc, 0, 3, 1, 1);
        gbc.ipady = 10;
        panelSE.add(buttonAddSupplier, gbc);

        setGridBagLayout(gbc, 1, 3, 1, 1);
        panelSE.add(buttonEdit, gbc);

        setGridBagLayout(gbc, 2, 3, 1, 1);
        panelSE.add(buttonDelete, gbc);

        setGridBagLayout(gbc, 3, 3, 1, 1);
        panelSE.add(buttonSave, gbc);

        setGridBagLayout(gbc, 4, 3, 1, 1);
        panelSE.add(buttonRestore, gbc);

        setGridBagLayout(gbc, 0, 2, 5, 1);
        gbc.fill = GridBagConstraints.BOTH;
        panelSE.add(scrollPaneTable, gbc);
    }

    private void setDialogGetData(String mode, int rowChange){
        JDialog dialogAddEdit = new JDialog(mainFrame, "", true);
        JButton buttonConfirm = new JButton();
        if (mode.equals("ADD")) {
            dialogAddEdit.setTitle("Add Supplier");
            buttonConfirm.setText("Add Supplier");
            showID = generateID("SPL", tableSEData, 3); 
            showName = "";
            showAddress = "";
        }
        else if(mode.equals("EDIT"))
        {
            dialogAddEdit.setTitle("Edit Supplier Information");
            buttonConfirm.setText("Save Changes");
        }
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelRemindData = new JLabel("*** Company Name & Address cannot be blank ***");
        JLabel labelID = new JLabel("Supplier ID:");
        JLabel labelName = new JLabel("Company Name:");
        JLabel labelAddress = new JLabel("Address:");
        JTextField textID = new JTextField(showID);
        textID.setEditable(false);
        JTextField textName = new JTextField(showName);
        JTextField textAddress = new JTextField(showAddress);

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textID.getText();
                String companyName = textName.getText();
                String address = textAddress.getText();
                boolean checkName = checkInputData(mainFrame, companyName, "Company Name");
                boolean checkAddress = checkInputData(mainFrame, address, "Address");
                if(checkName && checkAddress)
                {   
                    boolean repeatName = checkRepeatData(mainFrame, tableSEData, companyName, 2, "Company Name", true, rowChange);
                    if(repeatName)
                    {
                        boolean result;
                        String[] newData = new String[title.length];
                        int newIndex = tableSEData.getModel().getRowCount() + 1; 
                        newData[0] = (Integer.toString(newIndex)+ ".");
                        newData[1] = id;
                        newData[2] = companyName;
                        newData[3] = address;
                        if(mode.equals("ADD"))
                        {
                            result = confirmOption(mainFrame, "add supplier?");
                            if (result) {
                                countEdit = 1;
                                addDatatoTable(newData);
                                scrollPaneTable.setViewportView(tableSEData);
                                dialogAddEdit.dispose();
                            }
                            else
                            {
                                dialogAddEdit.dispose();
                            }
                        }
                        else if(mode.equals("EDIT"))
                        {
                            result = confirmOption(mainFrame, "edit suppplier info?");
                            if (result) {
                                countEdit = 1;
                                DefaultTableModel model = (DefaultTableModel)tableSEData.getModel();
                                model.setValueAt(id, rowChange, 1);
                                model.setValueAt(companyName, rowChange, 2);
                                model.setValueAt(address, rowChange, 3);
                                scrollPaneTable.setViewportView(tableSEData);
                                dialogAddEdit.dispose();
                            }
                            else
                            {
                                dialogAddEdit.dispose();
                            }
                        }
                    }
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 2,0,1,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(labelRemindData,gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelID,gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelName,gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelAddress,gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textID,gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textName,gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textAddress,gbcDialog);

        setGridBagLayout(gbcDialog, 2,4,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogAddEdit.setSize(460, 400);
        dialogAddEdit.add(panelDialog);
        dialogAddEdit.setLocationRelativeTo(mainFrame);
        dialogAddEdit.setVisible(true);
    }

    private void addDatatoTable(String[] newData){
        DefaultTableModel model = (DefaultTableModel)tableSEData.getModel();
        model.addRow(newData);
    }

    private void setDialogDeleteData(int rowChange){
        JDialog dialogDelete = new JDialog(mainFrame, "Delete Supplier", true);
        JButton buttonConfirm = new JButton("Delete");
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelID = new JLabel("Supplier ID:");
        JLabel labelName = new JLabel("Company Name:");
        JLabel labelAddress = new JLabel("Address:");
        JTextField textID = new JTextField(showID);
        JTextField textName = new JTextField(showName);
        JTextField textAddress = new JTextField(showAddress);
        textID.setEditable(false);
        textName.setEditable(false);
        textAddress.setEditable(false);

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean option = confirmOption(mainFrame, "delete this supplier?"); 
                if (option) {
                    DefaultTableModel model = (DefaultTableModel)tableSEData.getModel();
                    model.removeRow(rowChange);
                    for(int i = rowChange; i < tableSEData.getRowCount(); i++)
                    {
                        String newNo = Integer.toString(i+1) + ".";
                        model.setValueAt(newNo, i, 0);
                    }
                    dialogDelete.dispose();
                    scrollPaneTable.setViewportView(tableSEData);
                    countEdit = 1;
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelID,gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelName,gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelAddress,gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textID,gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textName,gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textAddress,gbcDialog);

        setGridBagLayout(gbcDialog, 2,4,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogDelete.setSize(460, 400);
        dialogDelete.add(panelDialog);
        dialogDelete.setLocationRelativeTo(mainFrame);
        dialogDelete.setVisible(true);
    }

    @Override
    public void addData()
    {
        setDialogGetData("ADD", -1);
    }

    @Override
    public void editData()
    {
        int indexNeeded = getSelectedRow(scrollPaneTable);
        if (indexNeeded == -1) {
            remindSelectedTable(mainFrame);
        }
        else
        {
            showID = (String)tableSEData.getModel().getValueAt(indexNeeded,1);
            showName = (String)tableSEData.getModel().getValueAt(indexNeeded,2);
            showAddress = (String)tableSEData.getModel().getValueAt(indexNeeded,3);
            setDialogGetData("EDIT", indexNeeded);
        }
    }

    @Override
    public void deleteData(){
        int indexNeeded = getSelectedRow(scrollPaneTable);
        if (indexNeeded == -1) {
            remindSelectedTable(mainFrame);
        }
        else
        {
            showID = (String)tableSEData.getModel().getValueAt(indexNeeded,1);
            showName = (String)tableSEData.getModel().getValueAt(indexNeeded,2);
            showAddress = (String)tableSEData.getModel().getValueAt(indexNeeded,3);
            setDialogDeleteData(indexNeeded);
        }
    }

    @Override
    public void saveData()
    {
        if (countEdit == 0) {
            JOptionPane.showMessageDialog(
            mainFrame,
            "You doesn't make changes !",
            "",
            JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            rowsNewTable = tableSEData.getRowCount();
            supplierAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, supplierAfter, tableSEData);
            boolean result = saveConfirmation(mainFrame, fileName, supplierAfter);
            if (result) {
                countEdit = 0; 
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                supplierData = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, supplierData, title);
            }
        }
    }

    @Override
    public void searchData(String dataInput)
    {
        boolean result = checkInputData(mainFrame, dataInput, "Search Bar");
        if (result) {
            rowsNewTable = tableSEData.getRowCount();
            supplierAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, supplierAfter, tableSEData);
            rowsFoundTable = getNewArrRowIndex(rowsNewTable, dataInput, supplierAfter); 
            supplierFound = new String[rowsFoundTable][title.length];
            createSearchTable(mainFrame, rowsNewTable, dataInput, supplierAfter, supplierFound, title, scrollPaneTable, 1);
        }
    }

    @Override
    public void restoreTable()
    {
        scrollPaneTable.setViewportView(tableSEData);
    }

    @Override
    public void backMain()
    {
        if (countEdit == 1) {
            rowsNewTable = tableSEData.getRowCount();
            supplierAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, supplierAfter, tableSEData);
            int result = askSaveData(mainFrame, fileName, supplierAfter);
            if (result == 1) { 
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                supplierData = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, supplierData, title);
                countEdit = 0;
                mainCardLayout.show(mainCardPanel, "Main Page");
            }else if (result == 2) {
                scrollPaneTable.setViewportView(tableBackUpData);
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                supplierData = new String[rowsTable][title.length];
                tableSEData = setTable(fileName, rowsTable, columns, supplierData, title);
                countEdit = 0;
                mainCardLayout.show(mainCardPanel, "Main Page");
            }
        }
        else
        {
            scrollPaneTable.setViewportView(tableBackUpData);
            mainCardLayout.show(mainCardPanel, "Main Page");
        }
    }

    public boolean checkRepeatData(JFrame mainFrame, JTable tableInScroll, String dataInput, int indexDataInArr, String notification, boolean saveRepeatData, int rowData)
    {
        DefaultTableModel model = (DefaultTableModel) tableInScroll.getModel();
        int rowCount = model.getRowCount();
        String tableData;
        for(int i = 0; i < rowCount; i++)
        {
            tableData = (String)model.getValueAt(i, indexDataInArr);
            if (tableData.toLowerCase().equals(dataInput.toLowerCase())) {
                if (rowData == -1) {
                    return remindRepeatData(mainFrame, dataInput, notification, saveRepeatData); //In multiFunction
                }
                else if (i == rowData) {
                    continue;
                }
                else
                {
                    return remindRepeatData(mainFrame, dataInput, notification, saveRepeatData); //In multiFunction
                }
            }
        }
        return true;
    }

    // dataInput(The data repeated)  notification(what type of data)
    public boolean remindRepeatData(JFrame mainFrame, String dataInput, String notification, boolean saveRepeatData)
    {
        // saveRepeatData: true; data can be repeat
        if(saveRepeatData){
            int result = JOptionPane.showConfirmDialog(
            mainFrame,
            dataInput + "is exist in file \n" + "Do you want to save same " + notification + " ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) // accept repeat data
            {
                return true;
            }
        }
        else // false: data cannot be repeat
        {
            JOptionPane.showMessageDialog(
            mainFrame,
            notification + " are repeat !",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    public JPanel soyaGetSEPanel()
    {
        setPanel();
        return panelSE;
    }
}
