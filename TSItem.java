package Purchase_Order_Management;

public class TSItem {
    private String itemID;
    private String description;
    private String supplierID;
    private double unitPrice;
    private double salesPrice;

    public TSItem(String itemID, String description, String supplierID, double unitPrice, double salesPrice) {
        this.itemID = itemID;
        this.description = description;
        this.supplierID = supplierID;
        this.unitPrice = unitPrice;
        this.salesPrice = salesPrice;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Override
    public String toString() {
        return itemID + ";" + description + ";" + supplierID + ";" + unitPrice + ";" + salesPrice;
    }
} 