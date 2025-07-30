package Purchase_Order_Management;

public class KHPurchaseRequisition {
    private String prId;
    private String itemId;
    private int quantity;
    private String supplierId;
    private String date;
    private String smName;

    public KHPurchaseRequisition(String prId, String itemId, int quantity, String supplierId, String date, String smName) {
        this.prId = prId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.date = date;
        this.smName = smName;
    }

    public String getPRId() {
        return prId;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getDate() {
        return date;
    }

    public String getSmName() {
        return smName;
    }

    @Override
    public String toString() {
        return prId + ";" + itemId + ";" + quantity + ";" + supplierId + ";" + date + ";" + smName;
    }
}