package Purchase_Order_Management;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class KxiStartMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Prevents GUI freezes and keeps the application responsive
            kxiMainPage();
        });
    }
    
    public static void kxiMainPage() {
        JFrame mainFrame = KxiDesign.createCustomSizeFrame("OWSB Main Page", 800, 450);

        JLabel label1 = KxiDesign.createLabelCustomTheme("Welcome to", "Garamond", false, 30);
        JLabel label2 = KxiDesign.createLabelCustomTheme("O W S B", "Tahoma", true, 72);
        JLabel label3 = KxiDesign.createLabelCustomTheme("Automated Purchase", "Comic Sans MS", false, 36);
        JLabel label4 = KxiDesign.createLabelCustomTheme("Order Management", "Comic Sans MS", false, 36);
        JLabel label5 = KxiDesign.createLabelCustomTheme("System", "Comic Sans MS", false, 36);

        JButton logInButton = KxiDesign.createCustomSizeButton("Log In", 114, 45);
        JButton registerButton = KxiDesign.createCustomSizeButton("Register", 114, 45);
        JButton informButton = KxiDesign.createCustomSizeButton("Help",114, 45);

        // =================== logo layout ==========================================================

        JPanel introductionPanel = new JPanel();
        introductionPanel.setOpaque(false); // Make sure the background gradient is see-able
        introductionPanel.setLayout(new BoxLayout(introductionPanel, BoxLayout.Y_AXIS)); // set Label from top to left
        // design label layout
        introductionPanel.add(Box.createVerticalGlue()); // add glue
        introductionPanel.add(label1);
        introductionPanel.add(label2);
        introductionPanel.add(label3);
        introductionPanel.add(label4);
        introductionPanel.add(label5);
        introductionPanel.add(Box.createVerticalGlue());

        // =================== button layout ========================================================

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        // design button layout
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(logInButton);
        buttonPanel.add(Box.createVerticalStrut(30)); // add space between button
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(informButton);
        buttonPanel.add(Box.createVerticalGlue());

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS)); // set Label from left to right
        // design whole layout
        containerPanel.add(Box.createHorizontalGlue());
        containerPanel.add(introductionPanel);
        containerPanel.add(Box.createHorizontalStrut(0));
        containerPanel.add(buttonPanel);
        containerPanel.add(Box.createHorizontalGlue());

        // =================== frame design ========================================================

        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER); // design frame layout

        mainFrame.setContentPane(mainPanel); // display panel in frame
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close tab then stop

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        logInButton.addActionListener(_ -> {
            mainFrame.dispose();
            KxiLogIn.kxiLogInProcess();
        });

        registerButton.addActionListener(_ -> {
            mainFrame.dispose();
            KxiRegisterAccount.kxiRegistrationPage();
        });

        informButton.addActionListener(_ -> {
            mainFrame.dispose();
            KxiViewUser.kxiDisplayRegistrantList(true, null);
        });
    }
}