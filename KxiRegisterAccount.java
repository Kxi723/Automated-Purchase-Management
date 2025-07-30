package Purchase_Order_Management;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class KxiRegisterAccount {
    private static String selectedRole = null;
    private static final String USER_FILE = "Name List.txt";
    private static final String REGISTER_FILE = "Register List.txt";
    
    public static void kxiRegistrationPage()  {
        JFrame registerFrame = KxiDesign.createCustomSizeFrame("Registration Page", 1000, 700);        

        JLabel roleLabel = KxiDesign.createLabelTheme1("Choose Your Role.");
        JLabel fillInLabel = KxiDesign.createLabelTheme1("Fill In Your Personnal Detail.");

        JButton smButton = KxiDesign.createCustomSizeButton("Sales Manager",150, 36);
        JButton pmButton = KxiDesign.createCustomSizeButton("Purchase Manager",180, 36);
        JButton adButton = KxiDesign.createCustomSizeButton("Administrator",145, 36);
        JButton imButton = KxiDesign.createCustomSizeButton("Inventory Manager",190, 36);
        JButton fmButton = KxiDesign.createCustomSizeButton("Finance Manager",165, 36);

        JLabel nameLabel = KxiDesign.createLabelTheme1("  Name");
        JLabel phoneLabel = KxiDesign.createLabelTheme1("  Phone Number");
        JLabel emailLabel = KxiDesign.createLabelTheme1("  Email");
        JLabel passwordLabel = KxiDesign.createLabelTheme1("  Password");
        JLabel confirmPasswordLabel = KxiDesign.createLabelTheme1("  Confirm Password");

        JTextField nameField = KxiDesign.createCustomTextField();
        JTextField phoneField = KxiDesign.createCustomTextField();
        JTextField emailField = KxiDesign.createCustomTextField();
        JPasswordField passwordField = KxiDesign.createCustomPasswordField();
        JPasswordField confirmPasswordField = KxiDesign.createCustomPasswordField();

        JButton registerButton = KxiDesign.createCustomSizeButton("Submit",90, 36);
        JButton backButton = KxiDesign.createCustomSizeButton("Back",80, 36);

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // =================== role selection layout ==========================================================

        JPanel roleButtonPanel = new JPanel();
        roleButtonPanel.setOpaque(false);
        roleButtonPanel.setLayout(new BoxLayout(roleButtonPanel, BoxLayout.X_AXIS));
        // design button layout
        roleButtonPanel.add(Box.createHorizontalGlue());
        roleButtonPanel.add(smButton);
        roleButtonPanel.add(Box.createHorizontalStrut(10));
        roleButtonPanel.add(pmButton);
        roleButtonPanel.add(Box.createHorizontalStrut(10));
        roleButtonPanel.add(adButton);
        roleButtonPanel.add(Box.createHorizontalStrut(10));
        roleButtonPanel.add(imButton);
        roleButtonPanel.add(Box.createHorizontalStrut(10));
        roleButtonPanel.add(fmButton);
        roleButtonPanel.add(Box.createHorizontalGlue());

        // =================== fill in field layout ==========================================================
    
        JPanel leftFillInPanel = new JPanel();
        leftFillInPanel.setOpaque(false);
        leftFillInPanel.setLayout(new BoxLayout(leftFillInPanel, BoxLayout.Y_AXIS));
        // design inform layout
        leftFillInPanel.add(Box.createVerticalGlue());
        leftFillInPanel.add(nameLabel);
        leftFillInPanel.add(Box.createVerticalStrut(10));
        leftFillInPanel.add(nameField);
        leftFillInPanel.add(Box.createVerticalStrut(40));
        leftFillInPanel.add(phoneLabel);
        leftFillInPanel.add(Box.createVerticalStrut(10));
        leftFillInPanel.add(phoneField);
        leftFillInPanel.add(Box.createVerticalStrut(40));
        leftFillInPanel.add(emailLabel);
        leftFillInPanel.add(Box.createVerticalStrut(10));
        leftFillInPanel.add(emailField);
        leftFillInPanel.add(Box.createVerticalGlue());

        JPanel rightFillInPanel = new JPanel();
        rightFillInPanel.setOpaque(false);
        rightFillInPanel.setLayout(new BoxLayout(rightFillInPanel, BoxLayout.Y_AXIS));
        // design password layout
        rightFillInPanel.add(Box.createVerticalGlue());
        rightFillInPanel.add(passwordLabel);
        rightFillInPanel.add(Box.createVerticalStrut(10));
        rightFillInPanel.add(passwordField);
        rightFillInPanel.add(Box.createVerticalStrut(40));
        rightFillInPanel.add(confirmPasswordLabel);        
        rightFillInPanel.add(Box.createVerticalStrut(10));
        rightFillInPanel.add(confirmPasswordField);
        rightFillInPanel.add(Box.createVerticalGlue());

        JPanel fillInPanel = new JPanel();
        fillInPanel.setOpaque(false);
        fillInPanel.setLayout(new BoxLayout(fillInPanel, BoxLayout.X_AXIS));
        // design whole field layout
        fillInPanel.add(Box.createHorizontalGlue());
        fillInPanel.add(Box.createHorizontalStrut(130));
        fillInPanel.add(leftFillInPanel);
        fillInPanel.add(Box.createHorizontalStrut(20));
        fillInPanel.add(rightFillInPanel);
        fillInPanel.add(Box.createHorizontalGlue());

        // =================== control button layout ==========================================================

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        // design button layout
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(registerButton);
        controlPanel.add(Box.createHorizontalStrut(40));
        controlPanel.add(backButton);
        controlPanel.add(Box.createHorizontalGlue());

        // =================== whole layout ========================================================

        JPanel containerPanel = new JPanel();
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        // design whole layout
        containerPanel.add(Box.createVerticalGlue());
        containerPanel.add(roleLabel);
        containerPanel.add(roleButtonPanel);
        containerPanel.add(Box.createVerticalStrut(10));
        containerPanel.add(fillInLabel);
        containerPanel.add(fillInPanel);
        containerPanel.add(controlPanel);
        containerPanel.add(Box.createVerticalGlue());

        // =================== frame  design ========================================================

        JPanel mainPanel = KxiDesign.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(containerPanel, BorderLayout.CENTER);

        registerFrame.setContentPane(mainPanel);
        registerFrame.setVisible(true);

        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  Function   XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        ActionListener roleButtonListener = e -> {
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
         
        registerButton.addActionListener(_ -> {
            String inputUsername = nameField.getText().trim();
            String inputPhone = phoneField.getText().trim();
            String inputEmail = emailField.getText().trim();
            String inputPassword = new String(passwordField.getPassword());
            String inputConfirmPassword = new String(confirmPasswordField.getPassword());
            
            // role button not selected
            if (selectedRole == null) {
                JOptionPane.showMessageDialog(registerFrame, "Please select your role.", "Warning Message⚠️", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // not enter all detail
            if(inputUsername.isEmpty() || inputPhone.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Please fill in all information.", "System Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // confirm password not same as input password
            if(!inputPassword.equals(inputConfirmPassword)) {
                JOptionPane.showMessageDialog(registerFrame, "Please make sure your passwords match.", 
                "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean hasUpperCase = !inputPassword.equals(inputPassword.toLowerCase());
            boolean hasDigit = inputPassword.matches(".*\\d.*");
            boolean hasSpecialChar = inputPassword.matches(".*[!@#$%^&*\\-_=+?/].*");
            boolean hasMinLength = inputPassword.length() >= 8;

            if(!(hasUpperCase && hasDigit && hasSpecialChar && hasMinLength)) {
                JOptionPane.showMessageDialog(registerFrame,  "Password must at least 8 characters\n1 Upper Case\n1 Number\n1 Special Character", 
                "Warning Message⚠️", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // check repeat
            try (BufferedReader fileReader = new BufferedReader(new FileReader(USER_FILE))) {
                String line;
                
                while ((line = fileReader.readLine()) != null) {
                    String[] parts = line.split(";");
                    
                    if (parts.length < 6) {
                        continue;
                    }

                    if (parts[2].equals(inputUsername) && parts[3].equals(inputPassword) && parts[0].equals(selectedRole)) {
                        JOptionPane.showMessageDialog(registerFrame, "Account Registed Already",
                        "System Message", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(registerFrame, "Error found while check registration: " + ex.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            }

            // write data
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(REGISTER_FILE, true))) {
                fileWriter.write(selectedRole + ";" + inputUsername + ";" +  inputPassword + ";" + inputPhone + ";" + inputEmail + ";pending");
                fileWriter.newLine();
            
                JOptionPane.showMessageDialog(registerFrame, "Your request will be reviewed by the administrator within 5 working days.",
                "System Message", JOptionPane.INFORMATION_MESSAGE);
                
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(registerFrame, "Error found while sending registration: " + ex.getMessage(),
                "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                registerFrame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });

        backButton.addActionListener(_ -> {
            registerFrame.dispose();
            KxiStartMain.kxiMainPage();
        });
    }
}