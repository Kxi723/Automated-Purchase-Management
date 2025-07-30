package Purchase_Order_Management;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

public class SoyaItemEntry extends SoyaSalesManager{
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private JTable tableIEData;
    private JTable tableBackUpData;
    private JScrollPane scrollPaneTable;
    private JPanel panelIE;
    private int rowsTable; // used to create array
    private int rowsNewTable;
    private int rowsFoundTable;
    private int columns;
    private int countEdit = 0; // record user edit or not
    private int countDelete = 0; // Record how many item have been delete
    private int countNew = 0; // Record how many new item have been added
    private String fileName = "Item.txt";
    private String[][] itemData; // Storing existing data
    private String[][] itemAfter; // Data after storage
    private String[][] itemFound; // Save the data I found
    private String[] itemID; // Store the ID of the new item
    private String[] newItemID; // Stores the item ID to be deleted
    private String[] title = {"No.", "Item ID", "Description", "Supplier ID", "Unit Price", "Sales Price"};
    private String[] supplierTitle = {"No.", "Supplier ID", "Name", "Address"};
    private String showItemID; // showXXX is used to store what data need to show at JTextField
    private String showItem;
    private String showSupplier;
    private String showUnitPrice;
    private String showSalesPrice; 

    public SoyaItemEntry(){
    }

    public SoyaItemEntry(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel){
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        rowsTable = getFileRows(fileName);
        columns = title.length - 1;
        itemData = new String[rowsTable][title.length];
        itemID = new String[rowsTable + 100];
        newItemID = new String[rowsTable + 100];
    }

    @Override
    public void setPanel(){
        tableIEData = setTable(fileName, rowsTable, columns, itemData, title);
        tableBackUpData = setTable(fileName, rowsTable, columns, itemData, title);
        panelIE = new JPanel(new GridBagLayout());
        scrollPaneTable = new JScrollPane(tableIEData);
        JButton buttonAddItem = new JButton("Add Item");
        JButton buttonEdit = new JButton("Edit Item Info");
        JButton buttonDelete = new JButton("Delete Item");
        JButton buttonSave = new JButton("Save Item Info");
        JButton buttonBackMain = new JButton("Back to Main Page");
        JButton buttonSearch = new JButton("Search");
        JButton buttonRestore = new JButton("Restore Table");
        JTextField textSearchBar = new JTextField();
        JLabel labelFunction = new JLabel("Item Entry");
        JLabel labelSearch = new JLabel("Enter Item ID to search");
        
        buttonAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addData();
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
        panelIE.add(labelSearch, gbc);

        setGridBagLayout(gbc, 2, 1, 2, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelIE.add(textSearchBar, gbc);

        setGridBagLayout(gbc, 0, 0, 5, 1);
        panelIE.add(labelFunction, gbc);

        setGridBagLayout(gbc, 4, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        panelIE.add(buttonSearch, gbc);

        setGridBagLayout(gbc, 0, 1, 1, 1);
        panelIE.add(buttonBackMain, gbc);

        setGridBagLayout(gbc, 0, 3, 1, 1);
        gbc.ipady = 10;
        panelIE.add(buttonAddItem, gbc);

        setGridBagLayout(gbc, 1, 3, 1, 1);
        panelIE.add(buttonEdit, gbc);

        setGridBagLayout(gbc, 2, 3, 1, 1);
        panelIE.add(buttonDelete, gbc);

        setGridBagLayout(gbc, 3, 3, 1, 1);
        panelIE.add(buttonSave, gbc);

        setGridBagLayout(gbc, 4, 3, 1, 1);
        panelIE.add(buttonRestore, gbc);

        setGridBagLayout(gbc, 0, 2, 5, 1);
        gbc.fill = GridBagConstraints.BOTH;
        panelIE.add(scrollPaneTable, gbc);
    }

    private void setDialogGetData(String mode, int rowChange){
        String[] tempDataArr = new String[supplierTitle.length];
        JDialog dialogAddEdit = new JDialog(mainFrame, "", true);
        JButton buttonConfirm = new JButton();
        if (mode.equals("ADD")) {
            dialogAddEdit.setTitle("Add Item");
            buttonConfirm.setText("Add Item");
            showItemID = generateID("IV", tableIEData, 4); 
            //If add is used, there is no original ID, so a new ID can be generated
            showItem = "";
            showSupplier = "";
            showUnitPrice = "";
            showSalesPrice = "";
        }
        else if(mode.equals("EDIT"))
        {
            dialogAddEdit.setTitle("Edit Item Information");
            buttonConfirm.setText("Save Changes");
        }
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelID = new JLabel("Item ID:");
        JLabel labelItem = new JLabel("Item:");
        JLabel labelSupplier = new JLabel("Supplier:");
        JLabel labelUnitPrice = new JLabel("Unit Price:");
        JLabel labelSalesPrice = new JLabel("Sales Price:");
        JTextField textID = new JTextField(showItemID);
        textID.setEditable(false); //ID are system generated and therefore cannot be changed
        JTextField textItem = new JTextField(showItem);
        JTextField textSupplier = new JTextField(showSupplier);
        textSupplier.setEditable(false); 
        JTextField textUnitPrice = new JTextField(showUnitPrice);
        textUnitPrice.setEditable(false); 
        JTextField textSalesPrice = new JTextField(showSalesPrice);
        textSalesPrice.setEditable(false);
        JButton buttonSelect = new JButton("Select");
        JButton buttonKeyUnit = new JButton("Key in");
        JButton buttonKeySales = new JButton("Key in");

        buttonSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempDataArr[1] = "";
                getSelectedData(mainFrame, "Supplier.txt",supplierTitle, "Supplier ID", tempDataArr);
                if(!(tempDataArr[1].equals("")))
                {
                    textSupplier.setText(tempDataArr[1]);
                }
            }
        });
        buttonKeyUnit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempDataArr[0] = "";
                windowNumber(mainFrame, "Unit Price", tempDataArr);
                if(!(tempDataArr[0].equals("")))
                {
                    textUnitPrice.setText(tempDataArr[0]);
                }
            }
        });
        buttonKeySales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempDataArr[0] = "";
                windowNumber(mainFrame, "Sales Price", tempDataArr);
                if(!(tempDataArr[0].equals("")))
                {
                    textSalesPrice.setText(tempDataArr[0]);
                }
            }
        });
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textID.getText();
                String item = textItem.getText();
                String supplierID = textSupplier.getText();
                String unitPrice = textUnitPrice.getText();
                String salesPrice = textSalesPrice.getText();
                boolean checkItem = checkInputData(mainFrame, item, "Item");
                boolean checkSupplier = checkInputData(mainFrame, supplierID, "Supplier ID");
                boolean checkUnitPrice = checkInputData(mainFrame, unitPrice, "Unit Price");
                boolean checkSalesPrice = checkInputData(mainFrame, salesPrice, "Sales Price");
                if(checkItem && checkSupplier && checkUnitPrice && checkSalesPrice)
                {   
                    boolean result;
                    String[] newData = new String[title.length];
                    int newIndex = tableIEData.getModel().getRowCount() + 1; 
                    //Get the new row index based on the existing table
                    newData[0] = (Integer.toString(newIndex)+ ".");
                    newData[1] = id;
                    newData[2] = item;
                    newData[3] = supplierID;
                    newData[4] = unitPrice;
                    newData[5] = salesPrice;
                    if(mode.equals("ADD"))
                    {
                        result = confirmOption(mainFrame, "add item?");
                        if (result) {
                            countEdit = 1;
                            newItemID[countNew] = id;
                            countNew += 1;
                            addDatatoTable(newData); // Add new data to the table
                            scrollPaneTable.setViewportView(tableIEData);
                            dialogAddEdit.dispose(); // After using it, turn it off directly
                        }
                        else
                        {
                            dialogAddEdit.dispose();
                        }
                    }
                    else if(mode.equals("EDIT"))
                    {
                        result = confirmOption(mainFrame, "edit item info?");
                        if (result) {
                            countEdit = 1;
                            DefaultTableModel model = (DefaultTableModel)tableIEData.getModel();
                            // setValueAt directly changes the data of that row
                            model.setValueAt(id, rowChange, 1);
                            model.setValueAt(item, rowChange, 2);
                            model.setValueAt(supplierID, rowChange, 3);
                            model.setValueAt(unitPrice, rowChange, 4);
                            model.setValueAt(salesPrice, rowChange, 5);
                            scrollPaneTable.setViewportView(tableIEData);
                            dialogAddEdit.dispose();
                        }
                        else
                        {
                            dialogAddEdit.dispose();
                        }
                    }
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,0,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelItem, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSupplier, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelUnitPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 1,0,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItem, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSupplier, gbcDialog);

        setGridBagLayout(gbcDialog, 3,2,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonSelect, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textUnitPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 3,3,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonKeyUnit, gbcDialog);

        setGridBagLayout(gbcDialog, 1,4,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 3,4,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonKeySales, gbcDialog);

        setGridBagLayout(gbcDialog, 2,5,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogAddEdit.setSize(360, 400);
        dialogAddEdit.add(panelDialog);
        dialogAddEdit.setLocationRelativeTo(mainFrame);
        dialogAddEdit.setVisible(true);
    }

    private void setDialogDeleteData(int rowChange){
        JDialog dialogDelete = new JDialog(mainFrame, "Delete Item", true);
        JButton buttonConfirm = new JButton("Delete");
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelID = new JLabel("Item ID:");
        JLabel labelItem = new JLabel("Item:");
        JLabel labelSupplier = new JLabel("Supplier:");
        JLabel labelUnitPrice = new JLabel("Unit Price:");
        JLabel labelSalesPrice = new JLabel("Sales Price:");
        JTextField textID = new JTextField(showItemID);
        textID.setEditable(false);
        JTextField textItem = new JTextField(showItem);
        textItem.setEditable(false);
        JTextField textSupplier = new JTextField(showSupplier);
        textSupplier.setEditable(false); 
        JTextField textUnitPrice = new JTextField(showUnitPrice);
        textUnitPrice.setEditable(false); 
        JTextField textSalesPrice = new JTextField(showSalesPrice);
        textSalesPrice.setEditable(false);

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean option = confirmOption(mainFrame, "delete this item?"); 
                if (option) {
                    DefaultTableModel model = (DefaultTableModel)tableIEData.getModel();
                    model.removeRow(rowChange); // Delete that line of data
                     // Because that row of data has been deleted, the sorting of No will be wrong
                    for(int i = rowChange; i < tableIEData.getRowCount(); i++)
                    {
                        String newNo = Integer.toString(i+1) + "."; 
                        // Change the serial number and make sure all the sequences are correct.
                        model.setValueAt(newNo, i, 0);
                    }
                    scrollPaneTable.setViewportView(tableIEData);
                    countEdit = 1;
                    itemID[countDelete] = showItemID;
                    countDelete += 1;
                    dialogDelete.dispose();
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelItem, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSupplier, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelUnitPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 0,5,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItem, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSupplier, gbcDialog);

        setGridBagLayout(gbcDialog, 1,4,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textUnitPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 1,5,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 2,6,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogDelete.setSize(400, 350);
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
        //This is the row where the data I want is located.
        if (indexNeeded == -1) {
            remindSelectedTable(mainFrame);
        }
        else
        {
            showItemID = (String)tableIEData.getModel().getValueAt(indexNeeded,1);
            showItem = (String)tableIEData.getModel().getValueAt(indexNeeded,2);
            showSupplier = (String)tableIEData.getModel().getValueAt(indexNeeded,3);
            showUnitPrice = (String)tableIEData.getModel().getValueAt(indexNeeded,4);
            showSalesPrice = (String)tableIEData.getModel().getValueAt(indexNeeded,5);
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
            showItemID = (String)tableIEData.getModel().getValueAt(indexNeeded,1);
            showItem = (String)tableIEData.getModel().getValueAt(indexNeeded,2);
            showSupplier = (String)tableIEData.getModel().getValueAt(indexNeeded,3);
            showUnitPrice = (String)tableIEData.getModel().getValueAt(indexNeeded,4);
            showSalesPrice = (String)tableIEData.getModel().getValueAt(indexNeeded,5);
            setDialogDeleteData(indexNeeded);
        }
    }

    @Override
    public void saveData(){
        if (countEdit == 0) {
            JOptionPane.showMessageDialog(
            mainFrame,
            "You doesn't make changes !",
            "",
            JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            rowsNewTable = tableIEData.getRowCount();
            itemAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, itemAfter, tableIEData);
            boolean result = saveConfirmation(mainFrame, fileName, itemAfter);
            if (result) {
                if(countNew > 0)
                {
                    addStockItem(newItemID, countNew);
                    countNew = 0;
                }
                if (countDelete > 0) {
                    deleteStockItem(itemID, countDelete);
                    countDelete = 0;
                }
                countEdit = 0; // Reset, otherwise you will be asked whether you want to save when you exit
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                itemAfter = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, itemAfter, title);
                /*The reason why we don't directly use tableBackUpData = tableIEData is because，
                worry that the backup data table will directly store the address of the SEdata table*/
            }
        }
    }

    @Override
    public void searchData(String dataInput){
        boolean result = checkInputData(mainFrame, dataInput, "Search Bar");
        if (result) {
            rowsNewTable = tableIEData.getRowCount(); //Get Edited Table have how many row
            itemAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, itemAfter, tableIEData);
            rowsFoundTable = getNewArrRowIndex(rowsNewTable, dataInput, itemAfter); // calculate got how many data
            itemFound = new String[rowsFoundTable][title.length]; // new array for data found
            createSearchTable(mainFrame, rowsNewTable, dataInput, itemAfter, itemFound, title, scrollPaneTable, 1);
        }
    }
    @Override
    public void restoreTable() {
        scrollPaneTable.setViewportView(tableIEData);
    }

    @Override
    public void backMain(){
        if (countEdit == 1) { //edited
            rowsNewTable = tableIEData.getRowCount();
            itemAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, itemAfter, tableIEData);
            int result = askSaveData(mainFrame, fileName, itemAfter);
            if (result == 1) { // save data
                if(countNew > 0)
                {
                    addStockItem(newItemID, countNew);
                    countNew = 0;
                }
                if (countDelete > 0) {
                    deleteStockItem(itemID, countDelete);
                    countDelete = 0;
                }
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                itemData = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, itemData, title);
                countEdit = 0;
                mainCardLayout.show(mainCardPanel, "Main Page");
            }else if (result == 2) { // don't save, restore previous data
                scrollPaneTable.setViewportView(tableBackUpData);
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                itemData = new String[rowsTable][title.length];
                tableIEData = setTable(fileName, rowsTable, columns, itemData, title);
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

    public void deleteStockItem(String[] itemID, int countDelete)
    {
        String stockFileName = "Stock.txt";
        int rows = getFileRows(stockFileName);
        int columns = 4; // stock only 4 data.
        int countData = 0;
        String[][] stockData = new String[rows][columns + 1];
        readFile(stockFileName, rows, columns, stockData);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(stockFileName))) {
            for(int i = 0; i < rows; i++)
            {
                countData = 0;
                for(int j = 0; j < countDelete; j++)
                {
                    if(stockData[i][1].equals(itemID[j])) {
                        countData = 1;
                        break;
                    }
                }
                if (countData > 0) {
                    continue;
                }
                else
                {
                    String line = "";
                    for(int j = 1; j < stockData[0].length; j++) //Write all the data of the same row into "line"
                    {
                        if(j == stockData[0].length - 1) // The last data in the same row
                        {
                            line = line + stockData[i][j];
                        }
                        else // The continuous data in the same row
                        {
                            line = line + stockData[i][j] + ";"; 
                        }
                    }
                    writer.write(line);
                    writer.newLine(); // change line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStockItem(String[] newItemID, int countNew)
    {
        String stockFileName = "Stock.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(stockFileName, true))){
            for(int i = 0; i < countNew; i++)
            {
                String line = newItemID[i]+";0;Low;PO000";
                writer.write(line);
                writer.newLine(); 
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDatatoTable(String[] newData){
        DefaultTableModel model = (DefaultTableModel)tableIEData.getModel();
        model.addRow(newData);
    }

    public JPanel soyaGetIEPanel()
    {
        setPanel();
        return panelIE;
    }

}
