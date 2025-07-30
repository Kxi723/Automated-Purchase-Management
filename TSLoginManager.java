package Purchase_Order_Management;

import java.util.List;

public class TSLoginManager {
    private static final String USERS_FILE = "Name List.txt";
    
    public static boolean validateLogin(String username, String password) {
        List<String> lines = TSInventoryFileHelper.readFile(USERS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 2) {
                String storedUsername = parts[0].trim();
                String storedPassword = parts[1].trim();
                
                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void showOWSBApp() {
        TSInventoryMainPage.tsInventoryManager();
    }
} 