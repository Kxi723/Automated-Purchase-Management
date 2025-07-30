package Purchase_Order_Management;

import java.util.ArrayList;
import java.util.List;

public class TSSupplierManager {
    private List<TSSupplier> suppliers;
    
    private static final String SUPPLIER_FILE = TSInventoryFileHelper.SUPPLIERS_FILE;

    public TSSupplierManager() {
        suppliers = new ArrayList<>();
        loadSuppliers();
    }

    /**
     * Load suppliers from the file
     */
    public void loadSuppliers() {
        List<String> lines = TSInventoryFileHelper.readFile(SUPPLIER_FILE);
        suppliers.clear();

        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 3) {
                String supplierID = parts[0];
                String supplierName = parts[1];
                String address = parts[2];

                suppliers.add(new TSSupplier(supplierID, supplierName, address));
            }
        }
    }

    /**
     * Save suppliers to the file (if needed for future tasks, not required for this one)
     */
    public void saveSuppliers() {
        // Implementation for saving if necessary
    }

    /**
     * Get all suppliers
     * @return List of all suppliers
     */
    public List<TSSupplier> getAllSuppliers() {
        return suppliers;
    }

    /**
     * Find a supplier by ID
     * @param supplierID The supplier ID
     * @return The supplier or null if not found
     */
    public TSSupplier findSupplierByID(String supplierID) {
        for (TSSupplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                return supplier;
            }
        }
        return null;
    }

    /**
     * Find a supplier name by ID
     * @param supplierID The supplier ID
     * @return The supplier name or "N/A" if not found
     */
    public String getSupplierNameByID(String supplierID) {
        TSSupplier supplier = findSupplierByID(supplierID);
        return (supplier != null) ? supplier.getSupplierName() : "N/A";
    }
} 