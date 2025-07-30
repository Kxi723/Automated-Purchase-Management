package Purchase_Order_Management;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TSInventoryFileHelper {
    
    // Base path for all text files
    private static final String BASE_PATH = "";
    
    // File paths for different data
    public static final String USERS_FILE = BASE_PATH + "Name List.txt";
    public static final String ITEMS_FILE = BASE_PATH + "Item.txt";
    public static final String SUPPLIERS_FILE = BASE_PATH + "Supplier.txt";
    public static final String STOCK_FILE = BASE_PATH + "Stock.txt";
    public static final String PR_FILE = BASE_PATH + "Purchase Requisition.txt";
    public static final String PO_FILE = BASE_PATH + "Purchase Order.txt";
    
    /**
     * Read all lines from a file
     * @param filePath The path to the file
     * @return List of lines from the file
     */
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        
        return lines;
    }
    
    /**
     * Write lines to a file
     * @param filePath The path to the file
     * @param lines The lines to write
     * @param append Whether to append to the file or overwrite
     */
    public static void writeFile(String filePath, List<String> lines, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Append a line to a file
     * @param filePath The path to the file
     * @param line The line to append
     */
    public static void appendLine(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }
    
    /**
     * Delete a line from a file
     * @param filePath The path to the file
     * @param lineToDelete The line to delete
     */
    public static void deleteLine(String filePath, String lineToDelete) {
        List<String> lines = readFile(filePath);
        lines.removeIf(line -> line.equals(lineToDelete));
        writeFile(filePath, lines, false);
    }
    
    /**
     * Update a line in a file
     * @param filePath The path to the file
     * @param oldLine The line to update
     * @param newLine The new line
     */
    public static void updateLine(String filePath, String oldLine, String newLine) {
        List<String> lines = readFile(filePath);
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(oldLine)) {
                lines.set(i, newLine);
                break;
            }
        }
        writeFile(filePath, lines, false);
    }
    
    /**
     * Generate a new ID by finding the highest existing ID and incrementing it
     * @param prefix The prefix for the ID (e.g., "PR" for Purchase Requisitions)
     * @param filePath The path to the file containing IDs
     * @param position The position of the ID in the line (separated by ;)
     * @return A new ID
     */
    public static String generateNewID(String prefix, String filePath, int position) {
        List<String> lines = readFile(filePath);
        int highestNumber = 0;
        
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length > position) {
                String id = parts[position];
                if (id.startsWith(prefix)) {
                    try {
                        int number = Integer.parseInt(id.substring(prefix.length()));
                        if (number > highestNumber) {
                            highestNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        // Skip if not a valid number
                    }
                }
            }
        }
        
        return prefix + String.format("%03d", highestNumber + 1);
    }
} 