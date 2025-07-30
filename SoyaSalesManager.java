package Purchase_Order_Management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public abstract class SoyaSalesManager {
    public int getFileRows(String fileName){
        int countRows = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) { // Read line by line
                countRows++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countRows;
    }

    public void readFile(String fileName, int rows, int columns, String[][] dataArr)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            for (int i = 0; i < rows; i++)
            {
                line = br.readLine();
                String[] data = line.split(";");
                dataArr[i][0] = (Integer.toString(i+1) + "."); //[Line][Serial Number]
                for(int j = 0; j < columns; j++)
                {
                    dataArr[i][j+1] = data[j]; // j + 1 It is to avoid the position of the serial number
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String fileName, String[][] newData){
        int rows = newData.length; 
        int column = newData[0].length; 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < rows; i++) {
                String line = "";
                for(int j = 1; j < column; j++) //Write all the data of the same row into "line"
                {
                    if(j == column - 1) // The last data in the same row
                    {
                        line = line + newData[i][j];
                    }
                    else // The continuous data in the same row
                    {
                        line = line + newData[i][j] + ";"; 
                    }
                }
                writer.write(line);
                writer.newLine(); // change line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create table
    public JTable setTable(String fileName, int rows, int columns, String[][] data, String[] title)
    {
        readFile(fileName, rows, columns, data); //
        JTable tableData = new JTable(new DefaultTableModel(data, title){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells cannot be edited
            }
        });
        tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row is allowed to be selected
        tableData.setAutoCreateRowSorter(false); // Not Allow clickable title sorting
        tableData.getTableHeader().setReorderingAllowed(false); // Disable dragging of columns
        tableData.getColumnModel().getColumn(0).setMaxWidth(30); // Make sure the serial number doesn't take up too much space
        return tableData;
    }

    // How many lines of relevant data found
    public int getNewArrRowIndex(int rows, String inputData, String[][] data) 
    {
        int countRows = 0;
        for(int i = 0; i < rows; i++)
        {
            String id = data[i][1];
            if(id.toLowerCase().contains(inputData.toLowerCase()))
            {
                countRows += 1;
            }
        }
        return countRows;
    }

    // Integrate all the data in the edited table into a new array
    public void setNewArray(int newRow, int column, String[][] arrAfter, JTable mainTable)
    {
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel(); // Changing the type to use a specific code
        for(int i = 0; i < newRow; i++)
        {
            for(int j = 0; j < column; j++)
            {
                String text = (String)model.getValueAt(i, j); // The data get(Object) -->(String)
                arrAfter[i][j] = text; // store data into the array
            }
        }
    }

    // Make a table with only search data.
    public void createSearchTable(JFrame mainFrame, int rows, String inputData, String[][] data, String[][] newArr, String[] title, JScrollPane scrollPane, int indexData)
    {
        int indexNewArr = 0; // new array starting rows
        int notFound = 0; // Used to record the number of times data cannot be found
        if(inputData.equals("")) // Verify that the user input is not empty
        {
            checkInputData(mainFrame, inputData, "Search Bar"); // remind user it is blank
        }
        else
        {
            for(int i = 0; i < rows; i++)
            {
                String compData = data[i][indexData]; // Index data is used to confirm the position of my data in the array
                /* Considering that the user may input incomplete information, fuzzy comparison is used. 
                As long as a few characters are the same, it is considered to be found. */
                if(compData.toLowerCase().contains(inputData.toLowerCase()))
                {
                    for(int x = 0; x < title.length ; x++)
                    {
                        newArr[indexNewArr][x] = data[i][x]; // Store all relevant data into a new array
                    }
                    indexNewArr += 1; //+1 to ensure that the data will not be overwritten by the same index.
                }
                else
                {
                    notFound += 1; // If the same data is not found, not found + 1
                }
            }
            /*After comparing all the data, if the number of not found same as number of rows, 
            it means there is no same data. */
            if(notFound == rows){
                JOptionPane.showMessageDialog(
                mainFrame,
                "Data Not Found",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            }
            else // data is found, then create a new table
            {
                JTable tableFoundData = new JTable(new DefaultTableModel(newArr,title){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tableFoundData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tableFoundData.setAutoCreateRowSorter(false);
                tableFoundData.getTableHeader().setReorderingAllowed(false);
                tableFoundData.getColumnModel().getColumn(0).setMaxWidth(30);
                scrollPane.setViewportView(tableFoundData);
            }
        }
    }

    // Check the data obtained from the text field. It cannot be blank. 
    // Fill in "what data" in Notification, it cannot be blank
    public boolean checkInputData(JFrame mainFrame, String dataInput, String notification){
        if(dataInput.equals("")){
            JOptionPane.showMessageDialog(
            mainFrame,
            notification + " cannot be blank !",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    // Ask for confirmation
    public boolean confirmOption(JFrame mainFrame, String notification)
    {
        int result = JOptionPane.showConfirmDialog(
        mainFrame,
        "Confirm " + notification,
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    // Remind user haven't select table row
    public void remindSelectedTable(JFrame mainFrame)
    {
        JOptionPane.showMessageDialog(
            mainFrame,
            "You didn't selected a data !",
            "Error",
            JOptionPane.WARNING_MESSAGE);
    }

    // Before exiting the page, ask the user whether to save data
    public int askSaveData(JFrame mainFrame, String fileName, String[][] newData)
    {
        int result = JOptionPane.showConfirmDialog(
            mainFrame,
            "Save your changes to file?",
            "Confirmation",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            writeFile(fileName, newData);
            return 1;
        }else if (result == JOptionPane.NO_OPTION) {
            return 2;
        }
        return 0;
    }

    // After the user clicks the save button, confirm whether to save
    public boolean saveConfirmation(JFrame mainFrame, String fileName, String[][] newData)
    {
        int result = JOptionPane.showConfirmDialog(
            mainFrame,
            "Confirm save data? After saving, the existing data will be overwritten.",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            writeFile(fileName, newData);
            return true;
        }
        return false;
    }

    //If each button has to be set in its own panel, it will be very long. This is designed to shorten it.
    public void setGridBagLayout(GridBagConstraints gbc, int x, int y ,int width, int height){
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1; //default
        gbc.weighty = 1;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = GridBagConstraints.CENTER; //default
        gbc.fill = GridBagConstraints.NONE; //default
    }

    public void windowNumber(JFrame mainFrame,String typePrice, String[] itemPrice){
        String[] price = {""};
        /*In an inner class, only one assignment is allowed and it cannot be changed.
        You can use an array to change it because the address has not been moved. */
        JButton numberOne = new JButton("1");
        JButton numberTwo = new JButton("2");
        JButton numberThree = new JButton("3");
        JButton numberFour = new JButton("4");
        JButton numberFive = new JButton("5");
        JButton numberSix = new JButton("6");
        JButton numberSeven = new JButton("7");
        JButton numberEight = new JButton("8");
        JButton numberNine = new JButton("9");
        JButton numberZero = new JButton("0");
        JButton buttonDot = new JButton(".");
        JTextField textPrice = new JTextField("");
        textPrice.setEditable(false);
        JButton buttonEnter = new JButton("Enter");
        JButton buttonCancel = new JButton("Cancel");
        JButton buttonClearAll = new JButton("Clear All");
        JButton buttonBackSpace = new JButton("BackSpace");
        JDialog dialogNumber = new JDialog(mainFrame, typePrice, true);
        JPanel panelNumber = new JPanel(new GridBagLayout());

        numberOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "1";
                textPrice.setText(price[0]);
            }
        });
        numberTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "2";
                textPrice.setText(price[0]);
            }
        });
        numberThree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "3";
                textPrice.setText(price[0]);
            }
        });
        numberFour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "4";
                textPrice.setText(price[0]);
            }
        });
        numberFive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "5";
                textPrice.setText(price[0]);
            }
        });
        numberSix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "6";
                textPrice.setText(price[0]);
            }
        });
        numberSeven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "7";
                textPrice.setText(price[0]);
            }
        });
        numberEight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "8";
                textPrice.setText(price[0]);
            }
        });
        numberNine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "9";
                textPrice.setText(price[0]);
            }
        });
        numberZero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + "0";
                textPrice.setText(price[0]);
            }
        });
        buttonDot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = price[0] + ".";
                textPrice.setText(price[0]);
            }
        });
        buttonClearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                price[0] = "";
                textPrice.setText(price[0]);
            }
        });
        buttonBackSpace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (price[0] != null && price[0].length() > 0) {
                    price[0] = price[0].substring(0, price[0].length() - 1); 
                    //Read from index0 to the second last index, the last index is like being deleted
                }  
                textPrice.setText(price[0]);
            }
        });
        
        buttonEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemPrice[0] = price[0];
                dialogNumber.dispose();
            }

        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogNumber.dispose();
            }
        });
        GridBagConstraints gbcDialog = new GridBagConstraints();
        gbcDialog.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbcDialog, 0,0,4,1);
        gbcDialog.fill = GridBagConstraints.HORIZONTAL;
        panelNumber.add(textPrice, gbcDialog);

        setGridBagLayout(gbcDialog, 0,1,1,1);
        panelNumber.add(numberSeven, gbcDialog);

        setGridBagLayout(gbcDialog, 1,1,1,1);
        panelNumber.add(numberEight, gbcDialog);

        setGridBagLayout(gbcDialog, 2,1,1,1);
        panelNumber.add(numberNine, gbcDialog);

        setGridBagLayout(gbcDialog, 3,1,1,1);
        panelNumber.add(buttonBackSpace, gbcDialog);

        setGridBagLayout(gbcDialog, 0,2,1,1);
        panelNumber.add(numberFour, gbcDialog);

        setGridBagLayout(gbcDialog, 1,2,1,1);
        panelNumber.add(numberFive, gbcDialog);

        setGridBagLayout(gbcDialog, 2,2,1,1);
        panelNumber.add(numberSix, gbcDialog);

        setGridBagLayout(gbcDialog, 3,2,1,1);
        panelNumber.add(buttonClearAll, gbcDialog);

        setGridBagLayout(gbcDialog, 0,3,1,1);
        panelNumber.add(numberOne, gbcDialog);

        setGridBagLayout(gbcDialog, 1,3,1,1);
        panelNumber.add(numberTwo, gbcDialog);

        setGridBagLayout(gbcDialog, 2,3,1,1);
        panelNumber.add(numberThree, gbcDialog);

        setGridBagLayout(gbcDialog, 3,3,1,1);
        panelNumber.add(buttonEnter, gbcDialog);
        
        setGridBagLayout(gbcDialog, 1,4,1,1);
        panelNumber.add(numberZero, gbcDialog);

        setGridBagLayout(gbcDialog, 2,4,1,1);
        panelNumber.add(buttonDot, gbcDialog);

        setGridBagLayout(gbcDialog, 3,4,1,1);
        panelNumber.add(buttonCancel, gbcDialog);

        dialogNumber.setSize(350, 500);
        dialogNumber.add(panelNumber);
        dialogNumber.setLocationRelativeTo(mainFrame);
        dialogNumber.setVisible(true);
    }

    // Get the row of the data I want, and the return value is the index in the table
    public int getSelectedRow(JScrollPane scrollPaneTable){
        JTable tableScroll = (JTable) scrollPaneTable.getViewport().getView();
        int indexSelected = 0; // Used to store the index of the row I selected
        int selectedRow = tableScroll.getSelectedRow();
        if (selectedRow == -1){ // No row selected
            return -1;
        }
        else
        {
            String dataTemp = (String)tableScroll.getModel().getValueAt(selectedRow, 0); 
            dataTemp = dataTemp.substring(0, dataTemp.length() - 1); 
            indexSelected = Integer.parseInt(dataTemp) - 1;
            return indexSelected;
        }
    }

    /* Directly open the file where the data I want to select is, and simply generate a panel with all the data (table). 
    You can search, and then get all the data in that row, return it, and then get the data you need. */
    public void getSelectedData(JFrame mainFrame, String fileName, String[] title, String typeID, String[] idGet){
        int rowsTable = getFileRows(fileName);
        int columns = title.length - 1;
        String[][] data = new String[rowsTable][title.length];
        JTable tableData = setTable(fileName, rowsTable, columns, data, title);
        JPanel panelData = new JPanel(new GridBagLayout());
        JScrollPane scrollPaneTable = new JScrollPane(tableData);
        JButton buttonRestore = new JButton("Restore");
        JButton buttonSearch = new JButton("Search");
        JButton buttonCancel = new JButton("Cancel");
        JButton buttonConfirm = new JButton("Confirm");
        JTextField textSearchBar = new JTextField();
        JLabel labelSearch = new JLabel("Enter "+ typeID +" to search");
        JDialog dialogSelectedData = new JDialog(mainFrame,"", true);

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

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexNeeded = getSelectedRow(scrollPaneTable);
                if (indexNeeded == -1) {
                    remindSelectedTable(mainFrame);
                }
                else
                {
                    for(int i = 0; i < idGet.length; i++)
                    {
                        idGet[i] = (String)tableData.getModel().getValueAt(indexNeeded, i);
                    }
                    dialogSelectedData.dispose();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogSelectedData.dispose();
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

        setGridBagLayout(gbcDialog, 0, 1, 4, 1);
        gbcDialog.fill = GridBagConstraints.BOTH;
        panelData.add(scrollPaneTable, gbcDialog);

        setGridBagLayout(gbcDialog, 0, 2, 1, 1);
        panelData.add(buttonConfirm, gbcDialog);

        setGridBagLayout(gbcDialog, 1, 2, 1, 1);
        panelData.add(buttonRestore, gbcDialog);

        setGridBagLayout(gbcDialog, 2, 2, 1, 1);
        panelData.add(buttonCancel, gbcDialog);

        dialogSelectedData.setSize(460, 500);
        dialogSelectedData.add(panelData);
        dialogSelectedData.setLocationRelativeTo(mainFrame);
        dialogSelectedData.setVisible(true);
    }

    // typeID: "SPL"....  lengthNum to determine the number of digits
    public String generateID(String typeID, JTable mainTable, int lengthNum){
        DefaultTableModel model = (DefaultTableModel)mainTable.getModel();
        String numCode; //save new number
        String newID = null; // save new ID
        int lastindex = mainTable.getRowCount() - 1; // Get the index of the last row in my table
        int typeIDCharacter = typeID.length(); // Determine which word I should start reading from
        String id = (String)model.getValueAt(lastindex, 1); // Get the id in the table
        String numberStr = id.substring(typeIDCharacter); // Take out the code, but it is still a String
        int number = Integer.parseInt(numberStr); // String turn into integer
        if(lengthNum == 3)
        {
            if((number / 100) == 0) // Check if the number is 3 digits
            {
                numCode = String.format("%03d", number + 1); // If not, force 3 digits.
                newID = typeID + numCode;
            }
            else{
                numCode = String.valueOf(number + 1); 
                newID = typeID + numCode;
            }
        }
        else if(lengthNum == 4)
        {
            if((number / 1000) == 0) // Check if the number is 4 digits
            {
                numCode = String.format("%04d", number + 1); // If not, force 4 digits.
                newID = typeID + numCode;
            }
            else{
                numCode = String.valueOf(number + 1); 
                newID = typeID + numCode;
            }
        }
        return newID;
    }

    public String getDate()
    {
        Instant now = Instant.now();
        long timeSeedGiven = now.toEpochMilli()/1000;
        int month_12 [] = {31,28,31,30,31,30,31,31,30,31,30,31}; // Non-leap year
        int minute = 0, hour = 0, day = 0, month = 1, year = 0; // save time
        int days = 0; //Used calculate how many extra days (leap year)
        year = (int)timeSeedGiven / (3600 * 24 * 365); // How many years have passed since 1970?

        int year1970 = 1970; // Used to calculate how many leap years there are since 1970
        while(year1970 != (year + 1970))
        {
            if(year1970 < (year + 1970))
            {
                if ((year1970 % 4 == 0 && year1970 % 100 != 0) || (year1970 % 400 == 0))
                {
                    days++;
                }
            }
            year1970++;
        }
        long timeSeedYear = (year * 365 *24 * 3600) + (days * 24 *3600) - (8 * 3600);

        long timeSeed = timeSeedGiven - timeSeedYear;
        year = year + 1970; //Add 1970 to know the current year
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
        {
            month_12[1] = 29;
        }
        day =  (int)timeSeed / 86400 + 1; // The number of days you spent that year

        for(int i = 0; i < 12; i++)
        {
            if(day > month_12[i]) 
            {
                day = day - month_12[i];
                month++;
            }
            else
            {
                break;
            }
        }
        return String.format("%02d/%02d/%04d", day, month, year);
    }


    public abstract void setPanel(); // Most of them have same function but need to override
    public abstract void addData();
    public abstract void editData();
    public abstract void deleteData();
    public abstract void searchData(String dataInput);
    public abstract void saveData();
    public abstract void restoreTable();
    public abstract void backMain();
    
}
