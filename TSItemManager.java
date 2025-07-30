package Purchase_Order_Management;

import java.util.ArrayList;
import java.util.List;

public class TSItemManager {
    private List<TSItem> items;
    
    public TSItemManager() {
        items = new ArrayList<>();
        loadItems();
    }
    
    /**
     * Load items from the file
     */
    public void loadItems() {
        List<String> lines = TSInventoryFileHelper.readFile(TSInventoryFileHelper.ITEMS_FILE);
        items.clear();
        
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 5) {
                String itemID = parts[0];
                String description = parts[1];
                String supplierID = parts[2];
                double unitPrice = Double.parseDouble(parts[3]);
                double salesPrice = Double.parseDouble(parts[4]);
                
                items.add(new TSItem(itemID, description, supplierID, unitPrice, salesPrice));
            }
        }
    }
    
    /**
     * Save items to the file
     */
    public void saveItems() {
        List<String> lines = new ArrayList<>();
        for (TSItem item : items) {
            lines.add(item.toString());
        }
        TSInventoryFileHelper.writeFile(TSInventoryFileHelper.ITEMS_FILE, lines, false);
    }
    
    /**
     * Add a new item
     * @param item The item to add
     */
    public void addItem(TSItem item) {
        items.add(item);
        saveItems();
    }
    
    /**
     * Update an existing item
     * @param oldItem The old item
     * @param newItem The new item
     */
    public void updateItem(TSItem oldItem, TSItem newItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemID().equals(oldItem.getItemID())) {
                items.set(i, newItem);
                break;
            }
        }
        saveItems();
    }
    
    /**
     * Delete an item
     * @param item The item to delete
     */
    public void deleteItem(TSItem item) {
        items.removeIf(i -> i.getItemID().equals(item.getItemID()));
        saveItems();
    }
    
    /**
     * Get all items
     * @return List of all items
     */
    public List<TSItem> getAllItems() {
        return items;
    }
    
    /**
     * Find an item by ID
     * @param itemID The item ID
     * @return The item or null if not found
     */
    public TSItem findItemByID(String itemID) {
        for (TSItem item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Generate a new item ID
     * @return A new item ID
     */
    public String generateNewItemID() {
        return TSInventoryFileHelper.generateNewID("IV", TSInventoryFileHelper.ITEMS_FILE, 0);
    }
} 