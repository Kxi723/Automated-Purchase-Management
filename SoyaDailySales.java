package Purchase_Order_Management;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;

public class SoyaDailySales extends SoyaSalesManager{
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private String username;
    private JTable tableDSRData;
    private JTable tableBackUpData;
    private JScrollPane scrollPaneTable;
    private JPanel panelDSR;
    private int rowsTable;
    private int rowsNewTable;
    private int rowsFoundTable;
    private int columns;
    private int countEdit = 0;
    private String fileName = "Daily Sale Record.txt";
    private String[][] salesData;
    private String[][] salesAfter;
    private String[][] salesFound;
    private String[] title = {"No.", "Item ID", "Quantity", "Sales Price", "Total Sales", "Date", "SM Name"};
    private String[] itemTitle = {"No.", "Item ID", "Description", "Supplier ID", "Unit Price", "Sales Price"};
    private String showItemID;
    private String showQuantity;
    private String showPrice;
    private String showTotalSales;
    private String showDate;
    private String showSMName; 

    public SoyaDailySales(){
    }

    public SoyaDailySales(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel, String username){
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        this.username = username;
        rowsTable = getFileRows(fileName);
        columns = title.length - 1;
        salesData = new String[rowsTable][title.length];
    }

    @Override
    public void setPanel(){
        tableDSRData = setTable(fileName, rowsTable, columns, salesData, title);
        tableBackUpData = setTable(fileName, rowsTable, columns, salesData, title);
        panelDSR = new JPanel(new GridBagLayout());
        scrollPaneTable = new JScrollPane(tableDSRData);
        JButton buttonAddItem = new JButton("Add Sales Record");
        JButton buttonEdit = new JButton("Edit Sales Record");
        JButton buttonDelete = new JButton("Delete Sales Record");
        JButton buttonSave = new JButton("Save Sales Record");
        JButton buttonBackMain = new JButton("Back to Main Page");
        JButton buttonSearch = new JButton("Search");
        JButton buttonRestore = new JButton("Restore Table");
        JButton buttonStock = new JButton("View Stock");
        JTextField textSearchBar = new JTextField();
        JLabel labelFunction = new JLabel("Daily Sales Record");
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
                scrollPaneTable.setViewportView(tableDSRData);
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
        panelDSR.add(labelSearch, gbc);

        setGridBagLayout(gbc, 2, 1, 2, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelDSR.add(textSearchBar, gbc);

        setGridBagLayout(gbc, 0, 0, 5, 1);
        panelDSR.add(labelFunction, gbc);

        setGridBagLayout(gbc, 4, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        panelDSR.add(buttonSearch, gbc);

        setGridBagLayout(gbc, 0, 1, 1, 1);
        panelDSR.add(buttonBackMain, gbc);

        setGridBagLayout(gbc, 0, 3, 1, 1);
        gbc.ipady = 10;
        panelDSR.add(buttonAddItem, gbc);

        setGridBagLayout(gbc, 1, 3, 1, 1);
        panelDSR.add(buttonEdit, gbc);

        setGridBagLayout(gbc, 2, 3, 1, 1);
        panelDSR.add(buttonDelete, gbc);

        setGridBagLayout(gbc, 3, 3, 1, 1);
        panelDSR.add(buttonSave, gbc);

        setGridBagLayout(gbc, 4, 3, 1, 1);
        panelDSR.add(buttonRestore, gbc);

        setGridBagLayout(gbc, 0, 2, 5, 1);
        gbc.fill = GridBagConstraints.BOTH;
        panelDSR.add(scrollPaneTable, gbc);
    }

    private void setDialogGetData(String mode, int rowChange){
        double[] calArr = new double[3]; // 0:Price 1:Quantity 2:Sales

        String[] tempDataArr = new String[itemTitle.length];
        JDialog dialogAddEdit = new JDialog(mainFrame, "", true);
        JButton buttonConfirm = new JButton();
        if (mode.equals("ADD")) {
            dialogAddEdit.setTitle("Add Daily Sales");
            buttonConfirm.setText("Add Daily Sales");
            showItemID = "";
            showQuantity = "";
            showPrice = "";
            showTotalSales = "";
            showDate = getDate();
            showSMName = username;
        }
        else if(mode.equals("EDIT"))
        {
            dialogAddEdit.setTitle("Edit Record Information");
            buttonConfirm.setText("Save Changes");
            calArr[0] = Double.parseDouble(showPrice);
            calArr[1] = Double.parseDouble(showQuantity);
        }
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelItemID = new JLabel("Item ID:");
        JLabel labelQuantity = new JLabel("Quantity:");
        JLabel labelSalesPrice = new JLabel("Sales Price:");
        JLabel labelTotalSales = new JLabel("Total Sales:");
        JLabel labelDate = new JLabel("Date:");
        JLabel labelSMName = new JLabel("SM Name:");
        JTextField textItemID = new JTextField(showItemID);
        textItemID.setEditable(false);
        JTextField textQuantity = new JTextField(showQuantity);
        textQuantity.setEditable(false);
        JTextField textSalesPrice = new JTextField(showPrice);
        textSalesPrice.setEditable(false);
        JTextField textTotalSales = new JTextField(showTotalSales);
        textTotalSales.setEditable(false); 
        JTextField textDate = new JTextField(showDate);
        textDate.setEditable(false); 
        JTextField textSMName = new JTextField(showSMName);
        textSMName.setEditable(false);
        JButton buttonItemID = new JButton("Select");
        JButton buttonQuantity = new JButton("Key in");


        buttonItemID.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tempDataArr[1] = "";
                getSelectedData(mainFrame, "Item.txt", itemTitle, "Item ID", tempDataArr);
                if(!(tempDataArr[1].equals("")))
                {
                    textItemID.setText(tempDataArr[1]);
                    textSalesPrice.setText(tempDataArr[5]);
                    calArr[0] = Double.parseDouble(tempDataArr[5]);
                    calArr[2] = calArr[0] * calArr[1];
                    String calSales = Double.toString(calArr[2]);
                    textTotalSales.setText(calSales);
                }
            }
        });
        buttonQuantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempDataArr[0] = "";
                windowNumber(mainFrame, "Unit Price", tempDataArr);
                if(!(tempDataArr[0].equals("")))
                {
                    
                    calArr[1] = Double.parseDouble(tempDataArr[0]);
                    int intQuantity = (int)calArr[1];
                    calArr[2] = calArr[0] * intQuantity;
                    String strQuantity = Integer.toString(intQuantity);
                    textQuantity.setText(strQuantity);
                    String calSales = Double.toString(calArr[2]);
                    textTotalSales.setText(calSales);
                }
            }
        });

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemID = textItemID.getText();
                String quantity = textQuantity.getText();
                String salesPrice = textSalesPrice.getText();
                String totalSales = textTotalSales.getText();
                String date = textDate.getText();
                String nameSM = textSMName.getText();
                boolean checkItemID = checkInputData(mainFrame, itemID, "Item ID");
                boolean checkQuantity = checkInputData(mainFrame, quantity, "Quantity");
                if(checkItemID && checkQuantity)
                {   
                    boolean result;
                    String[] newData = new String[title.length];
                    int newIndex = tableDSRData.getModel().getRowCount() + 1; 
                    newData[0] = (Integer.toString(newIndex)+ ".");
                    newData[1] = itemID;
                    newData[2] = quantity;
                    newData[3] = salesPrice;
                    newData[4] = totalSales;
                    newData[5] = date;
                    newData[6] = nameSM;
                    if(mode.equals("ADD"))
                    {
                        result = confirmOption(mainFrame, "add daily sales?");
                        if (result) {
                            countEdit = 1;
                            addDatatoTable(newData);
                            scrollPaneTable.setViewportView(tableDSRData);
                            dialogAddEdit.dispose();
                        }
                        else
                        {
                            dialogAddEdit.dispose();
                        }
                    }
                    else if(mode.equals("EDIT"))
                    {
                        result = confirmOption(mainFrame, "edit daily sales info?");
                        if (result) {
                            countEdit = 1;
                            DefaultTableModel model = (DefaultTableModel)tableDSRData.getModel();
                            // setValueAt直接更改那row的数据
                            model.setValueAt(itemID, rowChange, 1);
                            model.setValueAt(quantity, rowChange, 2);
                            model.setValueAt(salesPrice, rowChange, 3);
                            model.setValueAt(totalSales, rowChange, 4);
                            model.setValueAt(date, rowChange, 5);
                            model.setValueAt(nameSM, rowChange, 6);
                            scrollPaneTable.setViewportView(tableDSRData);
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
        panelDialog.add(labelItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelTotalSales, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelDate, gbcDialog);

        setGridBagLayout(gbcDialog, 0,5,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 1,0,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItemID, gbcDialog);
        
        setGridBagLayout(gbcDialog, 3,0,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 3,1,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textTotalSales, gbcDialog);

        setGridBagLayout(gbcDialog, 1,4,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textDate, gbcDialog);

        setGridBagLayout(gbcDialog, 1,5,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 2,6,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogAddEdit.setSize(360, 400);
        dialogAddEdit.add(panelDialog);
        dialogAddEdit.setLocationRelativeTo(mainFrame);
        dialogAddEdit.setVisible(true);
    }

    private void setDialogDeleteData(int rowChange){
        JDialog dialogDelete = new JDialog(mainFrame, "Delete Daily Sales Record", true);
        JButton buttonConfirm = new JButton("Delete");
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelItemID = new JLabel("Item ID:");
        JLabel labelQuantity = new JLabel("Quantity:");
        JLabel labelSalesPrice = new JLabel("Sales Price:");
        JLabel labelTotalSales = new JLabel("Total Sales:");
        JLabel labelDate = new JLabel("Date:");
        JLabel labelSMName = new JLabel("SM Name:");
        JTextField textItemID = new JTextField(showItemID);
        textItemID.setEditable(false);
        JTextField textQuantity = new JTextField(showQuantity);
        textQuantity.setEditable(false);
        JTextField textSalesPrice = new JTextField(showPrice);
        textSalesPrice.setEditable(false);
        JTextField textTotalSales = new JTextField(showTotalSales);
        textTotalSales.setEditable(false); 
        JTextField textDate = new JTextField(showDate);
        textDate.setEditable(false); 
        JTextField textSMName = new JTextField(showSMName);
        textSMName.setEditable(false);
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean option = confirmOption(mainFrame, "delete this daily sales record?"); 
                if (option) {
                    DefaultTableModel model = (DefaultTableModel)tableDSRData.getModel();
                    model.removeRow(rowChange); 
                    for(int i = rowChange; i < tableDSRData.getRowCount(); i++) 
                    {
                        String newNo = Integer.toString(i+1) + ".";
                        model.setValueAt(newNo, i, 0);
                    }
                    dialogDelete.dispose();
                    scrollPaneTable.setViewportView(tableDSRData);
                    countEdit = 1;
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,0,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelTotalSales, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelDate, gbcDialog);

        setGridBagLayout(gbcDialog, 0,5,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 1,0,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSalesPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textTotalSales, gbcDialog);

        setGridBagLayout(gbcDialog, 1,4,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textDate, gbcDialog);

        setGridBagLayout(gbcDialog, 1,5,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 2,6,1,1);
        gbcDialog.ipadx = 40;
        panelDialog.add(buttonConfirm, gbcDialog);

        dialogDelete.setSize(560, 450);
        dialogDelete.add(panelDialog);
        dialogDelete.setLocationRelativeTo(mainFrame);
        dialogDelete.setVisible(true);
    }

    private void addDatatoTable(String[] newData){
        DefaultTableModel model = (DefaultTableModel)tableDSRData.getModel();
        model.addRow(newData);
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
            if(indexNeeded >= rowsTable)
            {
                showItemID = (String)tableDSRData.getModel().getValueAt(indexNeeded,1);
                showQuantity = (String)tableDSRData.getModel().getValueAt(indexNeeded,2);
                showPrice = (String)tableDSRData.getModel().getValueAt(indexNeeded,3);
                showTotalSales = (String)tableDSRData.getModel().getValueAt(indexNeeded,4);
                showDate = (String)tableDSRData.getModel().getValueAt(indexNeeded,5);
                showSMName = (String)tableDSRData.getModel().getValueAt(indexNeeded,6);
                setDialogGetData("EDIT", indexNeeded);
            }
            else
            {
                JOptionPane.showMessageDialog(
                mainFrame,
                "Cannot edit stored record!",
                "Reminder",
                JOptionPane.INFORMATION_MESSAGE);
            }
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
            if(indexNeeded >= rowsTable)
            {
                showItemID = (String)tableDSRData.getModel().getValueAt(indexNeeded,1);
                showQuantity = (String)tableDSRData.getModel().getValueAt(indexNeeded,2);
                showPrice = (String)tableDSRData.getModel().getValueAt(indexNeeded,3);
                showTotalSales = (String)tableDSRData.getModel().getValueAt(indexNeeded,4);
                showDate = (String)tableDSRData.getModel().getValueAt(indexNeeded,5);
                showSMName = (String)tableDSRData.getModel().getValueAt(indexNeeded,6);
                setDialogDeleteData(indexNeeded);
            }
            else
            {
                JOptionPane.showMessageDialog(
                mainFrame,
                "Cannot delete stored record!",
                "Reminder",
                JOptionPane.INFORMATION_MESSAGE);
            }
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
            rowsNewTable = tableDSRData.getRowCount();
            salesAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, salesAfter, tableDSRData);
            boolean result = saveConfirmation(mainFrame, fileName, salesAfter);
            if (result) {
                updateStockQuantity(rowsTable, rowsNewTable, salesAfter);
                countEdit = 0;
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                salesAfter = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, salesAfter, title);
            }
        }
    }

    @Override
    public void backMain(){
        if (countEdit == 1) {
            rowsNewTable = tableDSRData.getRowCount();
            salesAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, salesAfter, tableDSRData);
            int result = askSaveData(mainFrame, fileName, salesAfter);
            if (result == 1) {
                updateStockQuantity(rowsTable, rowsNewTable, salesAfter);
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                salesData = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, salesData, title);
                countEdit = 0;
                mainCardLayout.show(mainCardPanel, "Main Page");
            }else if (result == 2) { 
                scrollPaneTable.setViewportView(tableBackUpData);
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                salesData = new String[rowsTable][title.length];
                tableDSRData = setTable(fileName, rowsTable, columns, salesData, title);
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

    @Override
    public void searchData(String dataInput){
        boolean result = checkInputData(mainFrame, dataInput, "Search Bar");
        if (result) {
            rowsNewTable = tableDSRData.getRowCount();
            salesAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, salesAfter, tableDSRData);
            rowsFoundTable = getNewArrRowIndex(rowsNewTable, dataInput, salesAfter); 
            salesFound = new String[rowsFoundTable][title.length];
            createSearchTable(mainFrame, rowsNewTable, dataInput, salesAfter, salesFound, title, scrollPaneTable, 1);
        }
    }

    @Override
    public void restoreTable() {
        scrollPaneTable.setViewportView(tableDSRData);
    }

    // The number in rowOld represents the index of the newly added data. 
    // The for loop stops at rowNew. dailySales is an array of all the data.
    public void updateStockQuantity(int rowOld, int rowNew, String[][] dailySales){
        int stockQuantity = 0;
        int salesQuantity = 0;
        String stockFileName = "Stock.txt";
        int rows = getFileRows(stockFileName);
        int columns = 4; // stock only 4 data.
        String[][] stockData = new String[rows][columns + 1];
        readFile(stockFileName, rows, columns, stockData);
        for(int i = rowOld; i < rowNew; i++)
        {
            for(int x = 0; x < rows; x++) // Compare with each data, then -quantity
            {
                if(dailySales[i][1].equals(stockData[x][1]))
                {
                    try {
                        stockQuantity = Integer.parseInt(stockData[x][2]);
                        salesQuantity = Integer.parseInt(dailySales[i][2]);
                    } catch (NumberFormatException e) {
                        System.out.println("invalid number");
                    }
                    int remainQuantity = stockQuantity - salesQuantity;
                    String strRemainQuantity = String.valueOf(remainQuantity);
                    stockData[x][2] = strRemainQuantity;
                }
            }
        }
        writeFile(stockFileName, stockData);
    }



    public JPanel soyaGetDailySalesPanel()
    {
        setPanel();
        return panelDSR;
    }
}
