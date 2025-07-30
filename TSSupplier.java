package Purchase_Order_Management;

public class TSSupplier {
    private String supplierID;
    private String supplierName;
    private String address;

    public TSSupplier(String supplierID, String supplierName, String address) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.address = address;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s", supplierID, supplierName, address);
    }
} 