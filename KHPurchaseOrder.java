package Purchase_Order_Management;

public class KHPurchaseOrder {
    private String poId;
    private String prId;
    private String itemId;
    private int quantity;
    private String supplierId;
    private String shippingStatus;
    private String approvalStatus;
    private String pmName;

    public KHPurchaseOrder(String poId, String prId, String itemId, int quantity, String supplierId, String shippingStatus, String approvalStatus, String pmName) {
        this.poId = poId;
        this.prId = prId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.shippingStatus = shippingStatus;
        this.approvalStatus = approvalStatus;
        this.pmName = pmName;
    }

    public String getPOId() {
        return poId;
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

    public String getShippingStatus() {
        return shippingStatus;
    }
    
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getPmName() {
        return pmName;
    }

    @Override
    public String toString() {
        return poId + ";" + prId + ";" + itemId + ";" + quantity + ";" + supplierId + ";" + shippingStatus + ";" + approvalStatus + ";" + pmName;
    }
}
