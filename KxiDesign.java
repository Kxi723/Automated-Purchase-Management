package Purchase_Order_Management;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;

public class KxiDesign {
    private static final Font TITLE_FONT = new Font("Garamond", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Comic Sans MS", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.WHITE;
    private static final Font TEXT_FIELD_FONT = new Font("Times New Roman", Font.PLAIN, 18);
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(180, 36);
    private static final Color ORANGE = new Color(255, 172, 98);
    private static final Color CYAN = new Color(145, 238, 255);
    private static final Color TABLE_TEXT_COLOR = Color.BLACK;
    private static final Color TABLE_BACKGROUND = new Color(0, 0, 0, 20);
    private static final Color TABLE_GRID_COLOR = new Color(255, 255, 255, 90);
    private static final Color TABLE_SELECTION_BACKGROUND = Color.WHITE;
    private static final Color SCROLLBAR_THUMB = new Color(0, 0, 0, 100);
    private static final Color SCROLLBAR_TRACK = new Color(0, 0, 0, 20);
    private static final Font TABLE_FONT = new Font("Garamond", Font.BOLD, 15);
    private static final int TABLE_ROW_HEIGHT = 30;

    public static JPanel createGradientPanel() {
        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gp = new GradientPaint( 0, 0, ORANGE, w, h, CYAN);
                
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        
        return panel;
    }

    public static JFrame createCustomSizeFrame(String frameName, int frameWidth, int frameHeight) {
        JFrame frame = new JFrame(frameName);

        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null); // set frame in the center of the screen

        return frame;
    }
    
    public static JLabel createLabelTheme1(String labelText) {
        JLabel label = new JLabel(labelText);
    
        label.setFont(TITLE_FONT);
        label.setForeground(FONT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    public static JLabel createLabelCustomTheme(String labelText, String theme, Boolean bold, int fontSize) {
        JLabel label = new JLabel(labelText);

        if (bold) {
            label.setFont(new Font(theme, Font.BOLD, fontSize));
        } else {
            label.setFont(new Font(theme, Font.PLAIN, fontSize));
        }
        label.setForeground(FONT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    public static JButton createCustomSizeButton(String buttonText, int widthInput, int heightInput) {
        JButton button = new JButton(buttonText);
        Dimension buttonSize = new Dimension(widthInput, heightInput);

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setForeground(FONT_COLOR);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    public static JTextField createCustomTextField() {
        JTextField textField = new JTextField();

        textField.setFont(TEXT_FIELD_FONT);
        textField.setOpaque(false);

        textField.setPreferredSize(TEXT_FIELD_SIZE);
        textField.setMinimumSize(TEXT_FIELD_SIZE);
        textField.setMaximumSize(TEXT_FIELD_SIZE);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        return textField;
    }

    public static JPasswordField createCustomPasswordField() {
        JPasswordField passwordField = new JPasswordField();

        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setOpaque(false);
        //passwordField.setEchoChar('*');

        passwordField.setPreferredSize(TEXT_FIELD_SIZE);
        passwordField.setMinimumSize(TEXT_FIELD_SIZE);
        passwordField.setMaximumSize(TEXT_FIELD_SIZE);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        return passwordField;
    }

    public static void styleTable(JTable table, boolean singleSelection, Dimension tableSize) {
        table.setFont(TABLE_FONT);
        table.getTableHeader().setFont(TABLE_FONT);
        
        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setPreferredScrollableViewportSize(tableSize);
        
        table.setForeground(TABLE_TEXT_COLOR);
        table.getTableHeader().setForeground(TABLE_TEXT_COLOR);
        table.setSelectionBackground(TABLE_SELECTION_BACKGROUND);
        table.setBackground(TABLE_BACKGROUND);
        table.getTableHeader().setBackground(TABLE_BACKGROUND);
        table.setGridColor(TABLE_GRID_COLOR);
        
        table.getTableHeader().setResizingAllowed(false); // cannot adjust the column size
        table.getTableHeader().setReorderingAllowed(false); // cannot adjust the column order
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){{
            setHorizontalAlignment(JLabel.CENTER);
        }});
        
        if (singleSelection) {
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } else {
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }

        table.setOpaque(false);
    }

    public static void setColumnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    public static JScrollPane createTransparentScrollPane(JTable table, Dimension tableSize) {
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setPreferredSize(tableSize);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = SCROLLBAR_THUMB;
                this.trackColor = SCROLLBAR_TRACK;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                return button;
            }
        });
        
        return scrollPane;
    }
}