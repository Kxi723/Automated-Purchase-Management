package Purchase_Order_Management;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KxiAdmin {
    public static void kxiAdminPage(String userName)  {
        JFrame adminFrame = KxiDesign.createCustomSizeFrame("Administrator - " + userName, 800, 600);

        JLabel smLabel = KxiDesign.createLabelTheme1("Sales Manager's Main Page");
        JButton smMainPageButton = KxiDesign.createCustomSizeButton("Click to Access", 160, 36);

        JLabel pmLabel = KxiDesign.createLabelTheme1("Purchase Manager's Main Page");
        JButton pmMainPageButton = KxiDesign.createCustomSizeButton("Click to Access", 160, 36);

        JLabel adminLabel = KxiDesign.createLabelTheme1("Administrator's Functionalities:");
        JButton viewRegistrationButton = KxiDesign.createCustomSizeButton("Approve Registration", 200, 36);
        JButton viewUserButton = KxiDesign.createCustomSizeButton("View All Users", 160, 36);

        JLabel imLabel = KxiDesign.createLabelTheme1("Inventory Manager's Main Page");
        JButton imMainPageButton = KxiDesign.createCustomSizeButton("Click to Access", 160, 36);

        JLabel fmLabel = KxiDesign.createLabelTheme1("Finance Manager's Main Page");
        JButton fmMainPageButton = KxiDesign.createCustomSizeButton("Click to Access", 160, 36);

        JButton backButton = KxiDesign.createCustomSizeButton("Back", 75, 36);
        
        // =================== admin functionality layout ========================================================

        JPanel adminPanel = new JPanel();
        adminPanel.setOpaque(false);
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.X_AXIS));
        // design button layout
        adminPanel.add(Box.createHorizontalGlue());
        adminPanel.add(viewRegistrationButton);
        adminPanel.add(Box.createHorizontalStrut(20));
        adminPanel.add(viewUserButton);
        adminPanel.add(Box.createHorizontalGlue());

        // =================== top label layout ========================================================

        JPanel topLabelPanel = new JPanel();
        topLabelPanel.setOpaque(false);
        topLabelPanel.setLayout(new BoxLayout(topLabelPanel, BoxLayout.X_AXIS));
        // design label layout
        topLabelPanel.add(Box.createHorizontalGlue());
        topLabelPanel.add(smLabel);
        topLabelPanel.add(Box.createHorizontalStrut(70));
        topLabelPanel.add(pmLabel);
        topLabelPanel.add(Box.createHorizontalGlue());

        // =================== top button layout ========================================================

        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setOpaque(false);
        topButtonPanel.setLayout(new BoxLayout(topButtonPanel, BoxLayout.X_AXIS));
        // design button layout
        topButtonPanel.add(Box.createHorizontalGlue());
        topButtonPanel.add(smMainPageButton);
        topButtonPanel.add(Box.createHorizontalStrut(170));
        topButtonPanel.add(pmMainPageButton);
        topButtonPanel.add(Box.createHorizontalGlue());

        // =================== bottom label layout ========================================================

        JPanel bottomLabelPanel = new JPanel();
        bottomLabelPanel.setOpaque(false);
        bottomLabelPanel.setLayout(new BoxLayout(bottomLabelPanel, BoxLayout.X_AXIS));
        // design label layout
        bottomLabelPanel.add(Box.createHorizontalGlue());
        bottomLabelPanel.add(imLabel);
        bottomLabelPanel.add(Box.createHorizontalStrut(60));
        bottomLabelPanel.add(fmLabel);
        bottomLabelPanel.add(Box.createHorizontalGlue());
        
        // =================== bottom button layout ========================================================

        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setOpaque(false);
        bottomButtonPanel.setLayout(new BoxLayout(bottomButtonPanel, BoxLayout.X_AXIS));
        // design button layout
        bottomButtonPanel.add(Box.createHorizontalGlue());
        bottomButtonPanel.add(imMainPageButton);
        bottomButtonPanel.add(Box.createHorizontalStrut(160));
        bottomButtonPanel.add(fmMainPageButton);
        bottomButtonPanel.add(Box.createHorizontalGlue());
       
        // =================== control button layout ==========================================================

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(backButton);

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        // design whole layout
        containerPanel.add(Box.createVerticalGlue());
        containerPanel.add(topLabelPanel);
        containerPanel.add(Box.createVerticalStrut(1));
        containerPanel.add(topButtonPanel);
        containerPanel.add(Box.createVerticalStrut(30));
        containerPanel.add(adminLabel);
        containerPanel.add(Box.createVerticalStrut(10));
        containerPanel.add(adminPanel);
        containerPanel.add(Box.createVerticalStrut(15));
        containerPanel.add(bottomLabelPanel);
        containerPanel.add(Box.createVerticalStrut(1));
        containerPanel.add(bottomButtonPanel);
        containerPanel.add(Box.createVerticalStrut(15));
        containerPanel.add(controlPanel);
        containerPanel.add(Box.createVerticalGlue());

        // =================== frame design ========================================================
        
        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        adminFrame.setContentPane(mainPanel);
        adminFrame.setVisible(true);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        viewRegistrationButton.addActionListener(_ -> {
            adminFrame.dispose();
            KxiViewRegistor.kxiApproveRegistor(userName);
        });

        viewUserButton.addActionListener(_ -> {
            adminFrame.dispose();
            KxiViewUser.kxiDisplayRegistrantList(false, userName);
        });

        smMainPageButton.addActionListener(_ -> {
            adminFrame.dispose();
            SoyaCallSalesManager.soyaSalesManager(userName);
        });

        pmMainPageButton.addActionListener(_ -> {
            adminFrame.dispose();
            KHPurchaseManagerGUI.khPurchaseManager();
        });

        imMainPageButton.addActionListener(_ -> {
            adminFrame.dispose();
            TSInventoryMainPage.tsInventoryManager();
        });

        fmMainPageButton.addActionListener(_ -> {
            adminFrame.dispose();
            YCFinanceManagerGUI3.ycFinanceManager();
        });

        adminFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                adminFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });

        backButton.addActionListener(_ -> {
            adminFrame.dispose();
            KxiLogIn.kxiLogInProcess();
        });
    }
}