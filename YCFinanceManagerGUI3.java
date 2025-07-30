package Purchase_Order_Management;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class YCFinanceManagerGUI3 {

    private JFrame frame;

    public YCFinanceManagerGUI3() {
        setupFrame();
        frame.setVisible(true);
    }

    private void setupFrame() {
        frame = new JFrame("Finance Manager - PO System");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // jason lai added
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) { // if close the window, then KxiStartMain.kxiMainPage()
                frame.dispose();
                KxiStartMain.kxiMainPage();
            }
        });
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        frame.add(buildMainPanel());
    }

    private JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titleLabel = createTitleLabel("Finance Manager Dashboard");
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add all buttons
        mainPanel.add(createButton("View Purchase Orders", () -> {
            frame.dispose();
            new YCPurchaseOrderGUI();
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(createButton("View Purchase Requests", () -> {
            frame.dispose();
            new YCPurchaseReq();
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(createButton("Payment", () -> {
            frame.dispose();
            new payment();
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(createButton("Generate Report", () -> {
            frame.dispose();
            new YCreportgui();
        }));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(createButton("Exit", () -> System.exit(0)));

        return mainPanel;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        return button;
    }

    public static void ycFinanceManager() {
        SwingUtilities.invokeLater(YCFinanceManagerGUI3::new);
    }
}
