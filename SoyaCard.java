package Purchase_Order_Management;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class SoyaCard {
    private JFrame mainFrame;
    private CardLayout soyaCardLayout = new CardLayout();
    private JPanel soyaCardPanel = new JPanel(soyaCardLayout);
    private String username;

    public SoyaCard(){
    }

    public SoyaCard(JFrame mainFrame, String username)
    {
        this.mainFrame = mainFrame;
        this.username = username;
        setCardPanel();
    }

    private void setCardPanel()
    {        
        SoyaMainPage getMPCard = new SoyaMainPage(mainFrame, soyaCardLayout, soyaCardPanel, username);
        SoyaPurchaseOrder getPOCard = new SoyaPurchaseOrder(mainFrame, soyaCardLayout, soyaCardPanel);
        SoyaSupplierEntry getSECard = new SoyaSupplierEntry(mainFrame, soyaCardLayout, soyaCardPanel);
        SoyaItemEntry getIECard = new SoyaItemEntry(mainFrame, soyaCardLayout, soyaCardPanel);
        SoyaPurchaseRequisition getPRCard = new SoyaPurchaseRequisition(mainFrame, soyaCardLayout, soyaCardPanel, username);
        SoyaDailySales getDSRCard = new SoyaDailySales(mainFrame, soyaCardLayout, soyaCardPanel, username);
        
        soyaCardPanel.add(getMPCard.soyaGetMainPagePanel(), "Main Page");
        soyaCardPanel.add(getSECard.soyaGetSEPanel(), "Supplier Entry");
        soyaCardPanel.add(getPOCard.soyaGetPurchaseOrderPanel(), "Purchase Order");
        soyaCardPanel.add(getIECard.soyaGetIEPanel(), "Item Entry");
        soyaCardPanel.add(getPRCard.soyaGetPRPanel(), "Purchase Requisition");
        soyaCardPanel.add(getDSRCard.soyaGetDailySalesPanel(), "Daily Sales Record");
    }

    public CardLayout soyaGetCardLayout(){
        return soyaCardLayout;
    }

    public JPanel soyaGetCardPanel()
    {
        return soyaCardPanel;
    }
}
