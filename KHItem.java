package Purchase_Order_Management;

public class KHItem {
    private String itemId;
    private String description;
    private String supplierId;
    private double unitPrice;
    private double sellPrice;

    public KHItem(String itemId, String description, String supplierId, double unitPrice, double sellPrice) {
        this.itemId = itemId;
        this.description = description;
        this.supplierId = supplierId;
        this.unitPrice = unitPrice;
        this.sellPrice = sellPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    @Override
    public String toString() {
        return itemId + ";" + description + ";" + supplierId + ";" + unitPrice + ";" + sellPrice;
    }
}
