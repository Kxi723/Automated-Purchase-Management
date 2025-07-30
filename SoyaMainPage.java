package Purchase_Order_Management;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SoyaMainPage extends SoyaSalesManager{
    private JFrame mainFrame;
    private CardLayout mainCardLayout;
    private JPanel mainCardPanel;
    private JPanel panelMainPage;
    private String username;

    public SoyaMainPage(){
    }

    public SoyaMainPage(JFrame mainFrame, CardLayout mainCardLayout, JPanel mainCardPanel, String username)
    {
        this.mainFrame = mainFrame;
        this.mainCardLayout = mainCardLayout;
        this.mainCardPanel = mainCardPanel;
        this.username = username;
    }

    @Override
    public void setPanel(){
        panelMainPage = new JPanel(new GridBagLayout());
        JButton buttonItemEntry = new JButton("Item Entry");
        JButton buttonSupplierEntry = new JButton("Supplier Entry");
        JButton buttonDailySalesRecord = new JButton("Daily Sales Record");
        JButton buttonPurchaseRequisition = new JButton("Purchase Requisition");
        JButton buttonListOfPurchaseOrder = new JButton("View Purchase Order");
        JButton buttonLogout = new JButton("Logout");
        JLabel labelWelcome = new JLabel("Welcome " + username);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        setGridBagLayout(gbc,0,0,1,1);
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        panelMainPage.add(labelWelcome, gbc);

        setGridBagLayout(gbc,0,1,1,1);
        panelMainPage.add(buttonItemEntry, gbc);

        setGridBagLayout(gbc,0,2,1,1);
        panelMainPage.add(buttonSupplierEntry, gbc);

        setGridBagLayout(gbc,0,3,1,1);
        panelMainPage.add(buttonPurchaseRequisition, gbc);
       
        setGridBagLayout(gbc,0,4,1,1);
        panelMainPage.add(buttonDailySalesRecord, gbc);

        setGridBagLayout(gbc,0,5,1,1);
        panelMainPage.add(buttonListOfPurchaseOrder, gbc);
        
        setGridBagLayout(gbc,0,6,1,1);
        panelMainPage.add(buttonLogout, gbc);

        buttonSupplierEntry.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainCardLayout.show(mainCardPanel, "Supplier Entry");
            }
        });

        buttonListOfPurchaseOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainCardLayout.show(mainCardPanel, "Purchase Order");
            }
        });

        buttonItemEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainCardLayout.show(mainCardPanel, "Item Entry");
            }
        });

        buttonPurchaseRequisition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainCardLayout.show(mainCardPanel, "Purchase Requisition");
            }
        });

        buttonDailySalesRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainCardLayout.show(mainCardPanel, "Daily Sales Record");
            }
        });

        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });
    }

    public JPanel soyaGetMainPagePanel(){
        setPanel();
        return panelMainPage;
    }

    @Override
    public void addData() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void editData() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void deleteData() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void searchData(String dataInput) {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void saveData() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void restoreTable() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
    @Override
    public void backMain() {
        throw new UnsupportedOperationException("This function not used in Main Page");
    }
}
