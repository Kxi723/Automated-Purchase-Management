package Purchase_Order_Management;

public class TSStock {
    private String itemID;
    private int quantity;
    private String stockLevel;
    private String lastUpdated;

    public TSStock(String itemID, int quantity, String stockLevel, String lastUpdated) {
        this.itemID = itemID;
        this.quantity = quantity;
        this.stockLevel = stockLevel;
        this.lastUpdated = lastUpdated;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(String stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return String.format("%s;%d;%s;%s", itemID, quantity, stockLevel, lastUpdated);
    }
} 