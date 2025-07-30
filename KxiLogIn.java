package Purchase_Order_Management;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class KxiLogIn {
    private static String selectedRole = null;

    public static void kxiLogInProcess() {
        JFrame logInFrame = KxiDesign.createCustomSizeFrame("Log In Page", 750, 420);

        JLabel roleLabel = KxiDesign.createLabelTheme1("Choose Your Role.");

        JButton smButton = KxiDesign.createCustomSizeButton("Sales Manager",190, 36);
        JButton pmButton = KxiDesign.createCustomSizeButton("Purchase Manager",190, 36);
        JButton adButton = KxiDesign.createCustomSizeButton("Administrator",190, 36);
        JButton imButton = KxiDesign.createCustomSizeButton("Inventory Manager",190, 36);
        JButton fmButton = KxiDesign.createCustomSizeButton("Finance Manager",190, 36);

        JLabel idLabel = KxiDesign.createLabelTheme1("  ID");
        JLabel passwordLabel = KxiDesign.createLabelTheme1("  Password");
        JTextField idField = KxiDesign.createCustomTextField();
        JPasswordField passwordField = KxiDesign.createCustomPasswordField();

        JButton logInButton = KxiDesign.createCustomSizeButton("Log In",85, 36);
        JButton backButton = KxiDesign.createCustomSizeButton("Back",85, 36);

        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logInButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // =================== role selection layout ==========================================================

        JPanel roleButtonPanel = new JPanel();
        roleButtonPanel.setOpaque(false);
        roleButtonPanel.setLayout(new BoxLayout(roleButtonPanel, BoxLayout.Y_AXIS));
        // design layout
        roleButtonPanel.add(Box.createVerticalGlue());
        roleButtonPanel.add(roleLabel);
        roleButtonPanel.add(Box.createVerticalStrut(1));
        roleButtonPanel.add(smButton);
        roleButtonPanel.add(Box.createVerticalStrut(10));
        roleButtonPanel.add(pmButton);
        roleButtonPanel.add(Box.createVerticalStrut(10));
        roleButtonPanel.add(adButton);
        roleButtonPanel.add(Box.createVerticalStrut(10));
        roleButtonPanel.add(imButton);
        roleButtonPanel.add(Box.createVerticalStrut(10));
        roleButtonPanel.add(fmButton);
        roleButtonPanel.add(Box.createVerticalGlue());

        // =================== input field layout ==========================================================

        JPanel fillInPanel = new JPanel();
        fillInPanel.setOpaque(false);
        fillInPanel.setLayout(new BoxLayout(fillInPanel, BoxLayout.Y_AXIS));
        // design label & field layout
        fillInPanel.add(Box.createVerticalGlue());
        fillInPanel.add(idLabel);
        fillInPanel.add(Box.createVerticalStrut(10));
        fillInPanel.add(idField);
        fillInPanel.add(Box.createVerticalStrut(40));
        fillInPanel.add(passwordLabel);        
        fillInPanel.add(Box.createVerticalStrut(10));
        fillInPanel.add(passwordField);
        fillInPanel.add(Box.createVerticalGlue());

        // =================== control button layout ==========================================================

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        // design button layout
        controlPanel.add(Box.createVerticalGlue());
        controlPanel.add(Box.createVerticalStrut(30));
        controlPanel.add(logInButton);
        controlPanel.add(Box.createVerticalStrut(40));
        controlPanel.add(backButton);
        controlPanel.add(Box.createVerticalGlue());

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
        // design whole layout
        containerPanel.add(Box.createHorizontalGlue());
        containerPanel.add(roleButtonPanel);
        containerPanel.add(Box.createHorizontalStrut(40));
        containerPanel.add(fillInPanel);
        containerPanel.add(controlPanel);
        containerPanel.add(Box.createHorizontalGlue());

        // =================== frame  design ========================================================

        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER);
        
        logInFrame.setContentPane(mainPanel);
        logInFrame.setVisible(true);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        // lock button while selected
        ActionListener roleButtonListener = e -> { // ori: ActionListener roleButtonListener = new ActionListener() {, public void actionPerformed(ActionEvent e) { }};
            JButton clickedButton = (JButton) e.getSource();
            selectedRole = clickedButton.getText();

            // if the button got clicked, then it become true, the rest are false, then setEnable(false)
            smButton.setEnabled(smButton != clickedButton);
            pmButton.setEnabled(pmButton != clickedButton);
            adButton.setEnabled(adButton != clickedButton);
            imButton.setEnabled(imButton != clickedButton);
            fmButton.setEnabled(fmButton != clickedButton);
            };

        smButton.addActionListener(roleButtonListener);
        pmButton.addActionListener(roleButtonListener);
        adButton.addActionListener(roleButtonListener);
        imButton.addActionListener(roleButtonListener);
        fmButton.addActionListener(roleButtonListener);

        logInButton.addActionListener(_ -> {
            String idInput = idField.getText();
            String passwordInput =  new String(passwordField.getPassword());
            
            // detect role selected or not
            if (selectedRole == null) {
                JOptionPane.showMessageDialog(logInFrame, "Please select your role.", "Warning Message⚠️", JOptionPane.WARNING_MESSAGE);
                return;
            }

            KxiCheckAccount authService = new KxiCheckAccount();
            KxiLogInReturnValue checkAccountAvailable = authService.authenticate(idInput, passwordInput, selectedRole); // Available value will be return from authenticate

            // log in successful
            if (checkAccountAvailable.isSuccess()) {
                JOptionPane.showMessageDialog(logInFrame, "Login successful!\nWelcome, " + checkAccountAvailable.getUserName() + " !", 
                "System Message", JOptionPane.INFORMATION_MESSAGE);
                    
                logInFrame.dispose();
                switch (selectedRole) { // call main page
                    case "Sales Manager" -> SoyaCallSalesManager.soyaSalesManager(checkAccountAvailable.getUserName());

                    case "Purchase Manager" -> KHPurchaseManagerGUI.khPurchaseManager();

                    case "Administrator" -> KxiAdmin.kxiAdminPage(checkAccountAvailable.getUserName());

                    case "Inventory Manager" -> TSInventoryMainPage.tsInventoryManager();

                    case "Finance Manager" -> YCFinanceManagerGUI3.ycFinanceManager();

                    default -> {
                        JOptionPane.showMessageDialog(logInFrame, "Invalid role or System error occurred.\nRole: " + selectedRole, 
                        "System Message", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else { // login fail
                JOptionPane.showMessageDialog(logInFrame, checkAccountAvailable.getMessage() + // display remaining attempt or tell them give up
                (checkAccountAvailable.getRemainingAttempts() > 0 ? "\nRemaining attempts: " + checkAccountAvailable.getRemainingAttempts() : "\nPlease try again later."), 
                "System Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        logInFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) { // if close the window, then KxiStartMain.kxiMainPage()
                logInFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });

        backButton.addActionListener(_ -> {
            logInFrame.dispose();
            KxiStartMain.kxiMainPage();
        });
    }
}