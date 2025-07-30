package Purchase_Order_Management;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SoyaPurchaseOrder extends SoyaSalesManager{
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private JTable tablePOData;
    private JScrollPane scrollPaneTable;
    private JPanel panelPO;
    private int rows;
    private int rowsNewTable;
    private int columns;
    private String fileName = "Purchase Order.txt";
    private String[][] purchaseOrderData;
    private String[][] orderFound;
    private String[] title = {"No.", "PO ID", "PR ID", "Item ID", "Quantity", "Supplier ID", "Shipping Status","Approval Status", "PM Name"};

    public SoyaPurchaseOrder(){}

    public SoyaPurchaseOrder(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel){
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        rows = getFileRows(fileName);
        columns = title.length - 1;
        purchaseOrderData = new String[rows][title.length];
    }

    @Override
    public void setPanel(){
        tablePOData = setTable(fileName, rows, columns, purchaseOrderData, title);
        panelPO = new JPanel(new GridBagLayout());
        scrollPaneTable = new JScrollPane(tablePOData);
        JButton buttonBackMenu = new JButton("Back to Main Page");
        JButton buttonSearch = new JButton("Search");
        JButton buttonRestore = new JButton("Restore table");
        JTextField textforSearch = new JTextField();
        JLabel labelsearchOrder = new JLabel("Enter PO ID to search:");
        JLabel labelListPO = new JLabel("List of Purchase Order");
        GridBagConstraints gbc = new GridBagConstraints();
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = textforSearch.getText();
                searchData(inputID);
            }
        });
        buttonRestore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPaneTable.setViewportView(tablePOData);
            }
        });
        buttonBackMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backMain();
            }
        });
        gbc.insets = new Insets(5, 5, 5, 5);
        
        setGridBagLayout(gbc, 1, 3, 1, 1);
        panelPO.add(buttonBackMenu, gbc);

        setGridBagLayout(gbc, 0, 3, 1, 1);
        panelPO.add(buttonRestore, gbc);

        setGridBagLayout(gbc, 0, 0, 1, 1);
        gbc.anchor = GridBagConstraints.EAST;
        panelPO.add(labelsearchOrder, gbc);

        setGridBagLayout(gbc, 2, 0, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        panelPO.add(buttonSearch, gbc);

        setGridBagLayout(gbc, 1, 0, 1, 1);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPO.add(textforSearch, gbc);

        setGridBagLayout(gbc, 1, 1, 1, 1);
        panelPO.add(labelListPO, gbc);

        setGridBagLayout(gbc, 0, 2, 3, 1);
        gbc.fill = GridBagConstraints.BOTH;
        panelPO.add(scrollPaneTable, gbc);
    }
    
    @Override
    public void searchData(String dataInput) {
        rowsNewTable = getNewArrRowIndex(rows, dataInput, purchaseOrderData); 
        orderFound = new String[rowsNewTable][title.length];
        createSearchTable(mainFrame, rows, dataInput, purchaseOrderData, orderFound, title, scrollPaneTable, 1);
    }

    @Override
    public void restoreTable()
    {
        scrollPaneTable.setViewportView(tablePOData);
    }

    @Override
    public void backMain() {
        scrollPaneTable.setViewportView(tablePOData);
        mainCardLayout.show(mainCardPanel,"Main Page");
    }

    public JPanel soyaGetPurchaseOrderPanel(){
        setPanel();
        return panelPO;
    }

    @Override
    public void addData() {
        throw new UnsupportedOperationException("This function not used in Purchgase Order");
    }

    @Override
    public void editData() {
        throw new UnsupportedOperationException("This function not used in Purchgase Order");
    }

    @Override
    public void deleteData() {
        throw new UnsupportedOperationException("This function not used in Purchgase Order");
    }
    
    @Override
    public void saveData() {
        throw new UnsupportedOperationException("This function not used in Purchgase Order");
    }    
}
