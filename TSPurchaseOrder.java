package Purchase_Order_Management;

public class TSPurchaseOrder {
    private String poID;
    private String prID;
    private String itemID;
    private int quantity;
    private String supplierID;
    private String shippingStatus;
    private String approvalStatus;
    private String pmName;

    public TSPurchaseOrder(String poID, String prID, String itemID, int quantity, String supplierID, 
                         String shippingStatus, String approvalStatus, String pmName) {
        this.poID = poID;
        this.prID = prID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.supplierID = supplierID;
        this.shippingStatus = shippingStatus;
        this.approvalStatus = approvalStatus;
        this.pmName = pmName;
    }

    public String getPoID() {
        return poID;
    }

    public void setPoID(String poID) {
        this.poID = poID;
    }

    public String getPrID() {
        return prID;
    }

    public void setPrID(String prID) {
        this.prID = prID;
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

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    @Override
    public String toString() {
        return poID + ";" + prID + ";" + itemID + ";" + quantity + ";" + supplierID + ";" 
               + shippingStatus + ";" + approvalStatus + ";" + pmName;
    }
} 