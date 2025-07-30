package Purchase_Order_Management;

import java.util.ArrayList;
import java.util.List;

public class TSStockManager {
    private List<TSStock> stocks;
    private int lowStockThreshold = 100;
    private TSItemManager itemManager;
    
    public TSStockManager(TSItemManager itemManager) {
        this.itemManager = itemManager;
        stocks = new ArrayList<>();
        loadStocks();
    }
    
    /**
     * Load stocks from the file
     */
    public void loadStocks() {
        List<String> lines = TSInventoryFileHelper.readFile(TSInventoryFileHelper.STOCK_FILE);
        stocks.clear();
        
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 4) {
                String itemID = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                String stockLevel = parts[2];
                String lastUpdated = parts[3];
                
                stocks.add(new TSStock(itemID, quantity, stockLevel, lastUpdated));
            }
        }
    }
    
    /**
     * Save stocks to the file
     */
    public void saveStocks() {
        List<String> lines = new ArrayList<>();
        for (TSStock stock : stocks) {
            lines.add(stock.toString());
        }
        TSInventoryFileHelper.writeFile(TSInventoryFileHelper.STOCK_FILE, lines, false);
    }
    
    /**
     * Add a new stock
     * @param stock The stock to add
     */
    public void addStock(TSStock stock) {
        stocks.add(stock);
        saveStocks();
    }
    
    /**
     * Update an existing stock
     * @param oldStock The old stock
     * @param newStock The new stock
     */
    public void updateStock(TSStock oldStock, TSStock newStock) {
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getItemID().equals(oldStock.getItemID())) {
                stocks.set(i, newStock);
                break;
            }
        }
        saveStocks();
    }
    
    /**
     * Delete a stock
     * @param stock The stock to delete
     */
    public void deleteStock(TSStock stock) {
        stocks.removeIf(s -> s.getItemID().equals(stock.getItemID()));
        saveStocks();
    }
    
    /**
     * Get all stocks
     * @return List of all stocks
     */
    public List<TSStock> getAllStocks() {
        return stocks;
    }
    
    /**
     * Find a stock by item ID
     * @param itemID The item ID
     * @return The stock or null if not found
     */
    public TSStock findStockByItemID(String itemID) {
        for (TSStock stock : stocks) {
            if (stock.getItemID().equals(itemID)) {
                return stock;
            }
        }
        return null;
    }
    
    /**
     * Update stock quantity
     * @param itemID The item ID
     * @param quantity The quantity to add (positive) or subtract (negative)
     * @param poID The PO ID for the last updated field
     * @return True if successful, false if stock not found
     */
    public boolean updateStockQuantity(String itemID, int quantity, String poID) {
        TSStock stock = findStockByItemID(itemID);
        if (stock != null) {
            int newQuantity = stock.getQuantity() + quantity;
            if (newQuantity < 0) {
                newQuantity = 0;
            }
            
            // Get the new stock level based on the updated quantity
            String newStockLevel = getStockLevel(stock.getItemID(), newQuantity);
            TSStock newStock = new TSStock(stock.getItemID(), newQuantity, newStockLevel, poID);
            updateStock(stock, newStock);
            return true;
        }
        return false;
    }
    
    /**
     * Get stocks with low stock level
     * @return List of stocks with low stock level
     */
    public List<TSStock> getLowStocks() {
        List<TSStock> lowStocks = new ArrayList<>();
        for (TSStock stock : stocks) {
            if ("Low".equals(getStockLevel(stock.getItemID(), stock.getQuantity()))) {
                lowStocks.add(stock);
            }
        }
        return lowStocks;
    }
    
    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }
    
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Determine the stock level (High, Medium, Low) based on item description and quantity.
     * @param itemID The item ID.
     * @param quantity The current stock quantity.
     * @return The stock level string.
     */
    public String getStockLevel(String itemID, int quantity) {
        TSItem item = itemManager.findItemByID(itemID);
        if (item == null) {
            return "Unknown";
        }

        String description = item.getDescription().toLowerCase();

        int highThreshold;
        int mediumThresholdMin;
        int lowThresholdMax;

        if (description.contains("pen") || description.contains("pencil") || description.contains("note")) {
            highThreshold = 300;
            mediumThresholdMin = 101;
            lowThresholdMax = 100;
        } else if (description.contains("ruler") || description.contains("eraser") || description.contains("highlighter")) {
            highThreshold = 250;
            mediumThresholdMin = 81;
            lowThresholdMax = 80;
        } else if (description.contains("calculator")) {
            highThreshold = 100;
            mediumThresholdMin = 26;
            lowThresholdMax = 25;
        } else {
            highThreshold = 150;
            mediumThresholdMin = 51;
            lowThresholdMax = 50;
        }

        if (quantity >= highThreshold) {
            return "High";
        } else if (quantity >= mediumThresholdMin && quantity <= lowThresholdMax) {
            if (quantity > lowThresholdMax) {
                return "Medium";
            } else {
                return "Low";
            }
        } else if (quantity <= lowThresholdMax) {
            return "Low";
        } else {
            return "Medium";
        }
    }
} 