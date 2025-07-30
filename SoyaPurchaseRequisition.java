package Purchase_Order_Management;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;

public class SoyaPurchaseRequisition extends SoyaSalesManager {
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private String username;
    private JTable tablePRData;
    private JTable tableBackUpData;
    private JScrollPane scrollPaneTable;
    private JPanel panelPR;
    private int rowsTable;
    private int rowsNewTable;
    private int rowsFoundTable;
    private int columns;
    private int countEdit = 0;
    private String fileName = "Purchase Requisition.txt";
    private String[][] prData;
    private String[][] prAfter;
    private String[][] prFound;
    private String[] title = {"No.", "PR ID", "Item ID", "Quantity", "Supplier ID", "Date", "SM Name"};
    private String[] itemTitle = {"No.", "Item ID", "Description", "Supplier ID", "Unit Price", "Sales Price"};
    private String showPRID;
    private String showItemID;
    private String showQuantity;
    private String showSupplierID;
    private String showDate;
    private String showSMName; 

    public SoyaPurchaseRequisition(){}

    public SoyaPurchaseRequisition(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel, String username){
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        this.username = username;
        rowsTable = getFileRows(fileName);
        columns = title.length - 1;
        prData = new String[rowsTable][columns + 1];
    }

    @Override
    public void setPanel(){
        tablePRData = setTable(fileName, rowsTable, columns, prData, title);
        tableBackUpData = setTable(fileName, rowsTable, columns, prData, title);
        panelPR = new JPanel(new GridBagLayout());
        scrollPaneTable = new JScrollPane(tablePRData);
        JButton buttonAddItem = new JButton("Add PR");
        JButton buttonEdit = new JButton("Edit PR");
        JButton buttonDelete = new JButton("Delete PR");
        JButton buttonSave = new JButton("Save PR");
        JButton buttonBackMain = new JButton("Back to Main Page");
        JButton buttonSearch = new JButton("Search");
        JButton buttonRestore = new JButton("Restore Table");
        JButton buttonStock = new JButton("View Stock");
        JTextField textSearchBar = new JTextField();
        JLabel labelFunction = new JLabel("Purchase Requisition");
        JLabel labelSearch = new JLabel("Enter PR ID to search");
        
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
                scrollPaneTable.setViewportView(tablePRData);
            }
        });

        buttonStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStock(mainFrame);
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
        panelPR.add(labelSearch, gbc);

        setGridBagLayout(gbc, 2, 1, 2, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPR.add(textSearchBar, gbc);

        setGridBagLayout(gbc, 0, 0, 6, 1);
        panelPR.add(labelFunction, gbc);

        setGridBagLayout(gbc, 4, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        panelPR.add(buttonSearch, gbc);

        setGridBagLayout(gbc, 0, 1, 1, 1);
        panelPR.add(buttonBackMain, gbc);

        setGridBagLayout(gbc, 0, 3, 1, 1);
        gbc.ipady = 10;
        panelPR.add(buttonAddItem, gbc);

        setGridBagLayout(gbc, 1, 3, 1, 1);
        panelPR.add(buttonEdit, gbc);

        setGridBagLayout(gbc, 2, 3, 1, 1);
        panelPR.add(buttonDelete, gbc);

        setGridBagLayout(gbc, 3, 3, 1, 1);
        panelPR.add(buttonSave, gbc);

        setGridBagLayout(gbc, 4, 3, 1, 1);
        panelPR.add(buttonRestore, gbc);

        setGridBagLayout(gbc, 5, 3, 1, 1);
        panelPR.add(buttonStock, gbc);

        setGridBagLayout(gbc, 0, 2, 6, 1);
        gbc.fill = GridBagConstraints.BOTH;
        panelPR.add(scrollPaneTable, gbc);
    }

    private void setDialogGetData(String mode, int rowChange){
        String[] tempDataArr = new String[itemTitle.length];
        JDialog dialogAddEdit = new JDialog(mainFrame, "", true);
        JButton buttonConfirm = new JButton();
        if (mode.equals("ADD")) {
            dialogAddEdit.setTitle("Add Item");
            buttonConfirm.setText("Add Item");
            showPRID = generateID("PR", tablePRData, 3);
            showItemID = "";
            showQuantity = "";
            showSupplierID = "";
            showDate = getDate();
            showSMName = username;
        }
        else if(mode.equals("EDIT"))
        {
            dialogAddEdit.setTitle("Edit Item Information");
            buttonConfirm.setText("Save Changes");
        }
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelPRID = new JLabel("PR ID");
        JLabel labelItemID = new JLabel("Item ID:");
        JLabel labelQuantity = new JLabel("Quantity:");
        JLabel labelSupplierID = new JLabel("Supplier ID:");
        JLabel labelDate = new JLabel("Date:");
        JLabel labelSMName = new JLabel("SM Name:");
        JTextField textPRID = new JTextField(showPRID);
        textPRID.setEditable(false);
        JTextField textItemID = new JTextField(showItemID);
        textItemID.setEditable(false);
        JTextField textQuantity = new JTextField(showQuantity);
        textQuantity.setEditable(false);
        JTextField textSupplierID = new JTextField(showSupplierID);
        textSupplierID.setEditable(false); 
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
                    textSupplierID.setText(tempDataArr[3]);
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
                    double doubleQuantity = Double.parseDouble(tempDataArr[0]);
                    int intQuantity = (int)doubleQuantity;
                    String strQuantity = Integer.toString(intQuantity);
                    textQuantity.setText(strQuantity);
                }
            }
        });

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textPRID.getText();
                String itemID = textItemID.getText();
                String quantity = textQuantity.getText();
                String supplierID = textSupplierID.getText();
                String date = textDate.getText();
                String nameSM = textSMName.getText();
                boolean checkItemID = checkInputData(mainFrame, itemID, "Item ID");
                boolean checkQuantity = checkInputData(mainFrame, quantity, "Quantity");
                if(checkItemID && checkQuantity)
                {   
                    boolean result;
                    String[] newData = new String[title.length];
                    int newIndex = tablePRData.getModel().getRowCount() + 1; 
                    newData[0] = (Integer.toString(newIndex)+ ".");
                    newData[1] = id;
                    newData[2] = itemID;
                    newData[3] = quantity;
                    newData[4] = supplierID;
                    newData[5] = date;
                    newData[6] = nameSM;
                    if(mode.equals("ADD"))
                    {
                        result = confirmOption(mainFrame, "add PR?");
                        if (result) {
                            countEdit = 1;
                            addDatatoTable(newData);
                            scrollPaneTable.setViewportView(tablePRData);
                            dialogAddEdit.dispose();
                        }
                        else
                        {
                            dialogAddEdit.dispose();
                        }
                    }
                    else if(mode.equals("EDIT"))
                    {
                        result = confirmOption(mainFrame, "edit PR?");
                        if (result) {
                            countEdit = 1;
                            DefaultTableModel model = (DefaultTableModel)tablePRData.getModel();
                            model.setValueAt(id, rowChange, 1);
                            model.setValueAt(itemID, rowChange, 2);
                            model.setValueAt(quantity, rowChange, 3);
                            model.setValueAt(supplierID, rowChange, 4);
                            model.setValueAt(date, rowChange, 5);
                            model.setValueAt(nameSM, rowChange, 6);
                            scrollPaneTable.setViewportView(tablePRData);
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
        panelDialog.add(labelPRID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSupplierID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelDate, gbcDialog);

        setGridBagLayout(gbcDialog, 0,5,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 1,0,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textPRID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 3,1,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 3,2,1,1);
        gbcDialog.anchor = GridBagConstraints.WEST;
        panelDialog.add(buttonQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSupplierID, gbcDialog);

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
        JDialog dialogDelete = new JDialog(mainFrame, "Delete PR", true);
        JButton buttonConfirm = new JButton("Delete");
        JPanel panelDialog = new JPanel(new GridBagLayout());
        JLabel labelPRID = new JLabel("PR ID");
        JLabel labelItemID = new JLabel("Item ID:");
        JLabel labelQuantity = new JLabel("Quantity:");
        JLabel labelSupplierID = new JLabel("Supplier ID:");
        JLabel labelDate = new JLabel("Date:");
        JLabel labelSMName = new JLabel("SM Name:");
        JTextField textPRID = new JTextField(showPRID);
        textPRID.setEditable(false);
        JTextField textItemID = new JTextField(showItemID);
        textItemID.setEditable(false);
        JTextField textQuantity = new JTextField(showQuantity);
        textQuantity.setEditable(false);
        JTextField textSupplierID = new JTextField(showSupplierID);
        textSupplierID.setEditable(false); 
        JTextField textDate = new JTextField(showDate);
        textDate.setEditable(false); 
        JTextField textSMName = new JTextField(showSMName);
        textSMName.setEditable(false);
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean option = confirmOption(mainFrame, "delete this PR?"); 
                if (option) {
                    DefaultTableModel model = (DefaultTableModel)tablePRData.getModel();
                    model.removeRow(rowChange);
                    for(int i = rowChange; i < tablePRData.getRowCount(); i++)
                    {
                        String newNo = Integer.toString(i+1) + ".";
                        model.setValueAt(newNo, i, 0);
                    }
                    dialogDelete.dispose();
                    scrollPaneTable.setViewportView(tablePRData);
                    countEdit = 1;
                }
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,0,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelPRID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSupplierID, gbcDialog);

        setGridBagLayout(gbcDialog, 0,4,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelDate, gbcDialog);

        setGridBagLayout(gbcDialog, 0,5,1,1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelDialog.add(labelSMName, gbcDialog);

        setGridBagLayout(gbcDialog, 1,0,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textPRID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textItemID, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textQuantity, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,2,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelDialog.add(textSupplierID, gbcDialog);

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
        DefaultTableModel model = (DefaultTableModel)tablePRData.getModel();
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
            showPRID = (String)tablePRData.getModel().getValueAt(indexNeeded,1);
            if(checkPRApprovalStatus(showPRID))
            {
                showItemID = (String)tablePRData.getModel().getValueAt(indexNeeded,2);
                showQuantity = (String)tablePRData.getModel().getValueAt(indexNeeded,3);
                showSupplierID = (String)tablePRData.getModel().getValueAt(indexNeeded,4);
                showDate = (String)tablePRData.getModel().getValueAt(indexNeeded,5);
                showSMName = (String)tablePRData.getModel().getValueAt(indexNeeded,6);
                setDialogGetData("EDIT", indexNeeded);
            }
            else
            {
                JOptionPane.showMessageDialog(
                mainFrame,
                "Cannot edit the PR has been approved!",
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
            showPRID = (String)tablePRData.getModel().getValueAt(indexNeeded,1);
            if(checkPRApprovalStatus(showPRID))
            {
                showItemID = (String)tablePRData.getModel().getValueAt(indexNeeded,2);
                showQuantity = (String)tablePRData.getModel().getValueAt(indexNeeded,3);
                showSupplierID = (String)tablePRData.getModel().getValueAt(indexNeeded,4);
                showDate = (String)tablePRData.getModel().getValueAt(indexNeeded,5);
                showSMName = (String)tablePRData.getModel().getValueAt(indexNeeded,6);
                setDialogDeleteData(indexNeeded);
            }
            else
            {
                JOptionPane.showMessageDialog(
                mainFrame,
                "Cannot delete the PR has been approved!",
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
            rowsNewTable = tablePRData.getRowCount();
            prAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, prAfter, tablePRData);
            boolean result = saveConfirmation(mainFrame, fileName, prAfter);
            if (result) {
                countEdit = 0;
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                prAfter = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, prAfter, title);
            }
        }
    }

    @Override
    public void searchData(String dataInput){
        boolean result = checkInputData(mainFrame, dataInput, "Search Bar");
        if (result) {
            rowsNewTable = tablePRData.getRowCount();
            prAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, prAfter, tablePRData);
            rowsFoundTable = getNewArrRowIndex(rowsNewTable, dataInput, prAfter);
            prFound = new String[rowsFoundTable][title.length];
            createSearchTable(mainFrame, rowsNewTable, dataInput, prAfter, prFound, title, scrollPaneTable, 1);
        }
    }

    @Override
    public void restoreTable() {
        scrollPaneTable.setViewportView(tablePRData);
    }

    @Override
    public void backMain(){
        if (countEdit == 1) { 
            rowsNewTable = tablePRData.getRowCount();
            prAfter = new String[rowsNewTable][title.length];
            setNewArray(rowsNewTable, title.length, prAfter, tablePRData);
            int result = askSaveData(mainFrame, fileName, prAfter);
            if (result == 1) {
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                prData = new String[rowsTable][title.length];
                tableBackUpData = setTable(fileName, rowsTable, columns, prData, title);
                countEdit = 0;
                mainCardLayout.show(mainCardPanel, "Main Page");
            }else if (result == 2) { 
                scrollPaneTable.setViewportView(tableBackUpData);
                rowsTable = getFileRows(fileName);
                columns = title.length - 1;
                prData = new String[rowsTable][title.length];
                tablePRData = setTable(fileName, rowsTable, columns, prData, title);
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

    public boolean checkPRApprovalStatus(String idPR)
    {
        String fileName = "Purchase Order.txt";
        String[] title = {"No.", "PO ID", "PR ID", "Item ID", "Quantity", "Supplier ID", "Shipping Status","Approval Status", "PM Name"};
        int rows = getFileRows(fileName);
        int columns = title.length - 1;
        String[][] purchaseOrderData = new String[rows][title.length];
        readFile(fileName, rows, columns, purchaseOrderData);
        for(int i = 0; i < rows; i++)
        {
            if(idPR.equals(purchaseOrderData[i][2]))
            {
                String status = "Approved";
                if(status.equals(purchaseOrderData[i][7]))
                {
                    return false;
                }
                else 
                {
                    return true;
                }
            }
        }
        return true;
    }

    public void viewStock(JFrame mainFrame)
    {
        String[] title = {"No.", "Item ID", "Quantity", "Stock Level", "Last Updated"};
        String fileName = "Stock.txt";
        int rowsTable = getFileRows(fileName);
        int columns = title.length - 1;
        String[][] data = new String[rowsTable][title.length];
        JTable tableData = setTable(fileName, rowsTable, columns, data, title);
        JPanel panelData = new JPanel(new GridBagLayout());
        JScrollPane scrollPaneTable = new JScrollPane(tableData);
        JDialog dialogSelectedData = new JDialog(mainFrame,"Stock", false);
        JButton buttonRestore = new JButton("Restore");
        JButton buttonSearch = new JButton("Search");
        JTextField textSearchBar = new JTextField();
        JLabel labelSearch = new JLabel("Enter Item ID to search");

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = textSearchBar.getText();
                boolean result = checkInputData(mainFrame, inputID, "Search Bar");
                if (result){
                    int rowsFoundTable = getNewArrRowIndex(rowsTable, inputID, data);
                    String[][] supplierFound = new String[rowsFoundTable][title.length];
                    createSearchTable(mainFrame, rowsTable, inputID, data, supplierFound, title, scrollPaneTable, 1);
                }
            }
        });

        buttonRestore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPaneTable.setViewportView(tableData);
            }
        });

        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0, 0, 1, 1);
        gbcDialog.anchor = GridBagConstraints.EAST;
        panelData.add(labelSearch, gbcDialog);

        setGridBagLayout(gbcDialog, 1, 0, 2, 1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelData.add(textSearchBar, gbcDialog);

        setGridBagLayout(gbcDialog, 3, 0, 1, 1);
        panelData.add(buttonSearch, gbcDialog);

        setGridBagLayout(gbcDialog, 2, 2, 1, 1);
        panelData.add(buttonRestore, gbcDialog);

        setGridBagLayout(gbcDialog, 0, 1, 4, 1);
        gbcDialog.fill = GridBagConstraints.BOTH;
        panelData.add(scrollPaneTable, gbcDialog);

        dialogSelectedData.setSize(460, 500);
        dialogSelectedData.add(panelData);
        dialogSelectedData.setLocationRelativeTo(mainFrame);
        dialogSelectedData.setVisible(true);
    }

    public JPanel soyaGetPRPanel()
    {
        setPanel();
        return panelPR;
    }

}
