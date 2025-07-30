package Purchase_Order_Management;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class SoyaCallSalesManager {
    
    public static void soyaSalesManager(String username)
    {
        JFrame frameSM = new JFrame();
        SoyaCard card = new SoyaCard(frameSM, username);

        //frameSM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameSM.setSize(1000, 600);
        frameSM.add(card.soyaGetCardPanel());
        frameSM.setVisible(true);

        // kxi added
        frameSM.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) { // if close the window, then KxiStartMain.kxiMainPage()
                frameSM.dispose();
                KxiStartMain.kxiMainPage();
            }
        });
    }
}
