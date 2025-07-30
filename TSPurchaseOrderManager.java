package Purchase_Order_Management;

import java.util.ArrayList;
import java.util.List;

public class TSPurchaseOrderManager {
    private List<TSPurchaseOrder> purchaseOrders;
    private TSStockManager stockManager;
    
    public TSPurchaseOrderManager(TSStockManager stockManager) {
        this.purchaseOrders = new ArrayList<>();
        this.stockManager = stockManager;
        loadPurchaseOrders();
    }
    
    /**
     * Load purchase orders from the file
     */
    public void loadPurchaseOrders() {
        List<String> lines = TSInventoryFileHelper.readFile(TSInventoryFileHelper.PO_FILE);
        purchaseOrders.clear();
        
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 8) {
                String poID = parts[0];
                String prID = parts[1];
                String itemID = parts[2];
                int quantity = Integer.parseInt(parts[3]);
                String supplierID = parts[4];
                String shippingStatus = parts[5];
                String approvalStatus = parts[6];
                String pmName = parts[7];
                
                purchaseOrders.add(new TSPurchaseOrder(poID, prID, itemID, quantity, supplierID, 
                                   shippingStatus, approvalStatus, pmName));
            }
        }
    }
    
    /**
     * Save purchase orders to the file
     */
    public void savePurchaseOrders() {
        List<String> lines = new ArrayList<>();
        for (TSPurchaseOrder po : purchaseOrders) {
            lines.add(po.toString());
        }
        TSInventoryFileHelper.writeFile(TSInventoryFileHelper.PO_FILE, lines, false);
    }
    
    /**
     * Update an existing purchase order
     * @param oldPO The old purchase order
     * @param newPO The new purchase order
     */
    public void updatePurchaseOrder(TSPurchaseOrder oldPO, TSPurchaseOrder newPO) {
        for (int i = 0; i < purchaseOrders.size(); i++) {
            if (purchaseOrders.get(i).getPoID().equals(oldPO.getPoID())) {
                purchaseOrders.set(i, newPO);
                break;
            }
        }
        savePurchaseOrders();
    }
    
    /**
     * Delete a purchase order
     * @param po The purchase order to delete
     */
    public void deletePurchaseOrder(TSPurchaseOrder po) {
        purchaseOrders.removeIf(p -> p.getPoID().equals(po.getPoID()));
        savePurchaseOrders();
    }
    
    /**
     * Approve a purchase order
     * @param poID The purchase order ID
     * @return True if successful, false if purchase order not found
     */
    public boolean approvePurchaseOrder(String poID) {
        for (TSPurchaseOrder po : purchaseOrders) {
            if (po.getPoID().equals(poID)) {
                TSPurchaseOrder updatedPO = new TSPurchaseOrder(
                    po.getPoID(), 
                    po.getPrID(), 
                    po.getItemID(), 
                    po.getQuantity(), 
                    po.getSupplierID(), 
                    po.getShippingStatus(), 
                    "Approved", 
                    po.getPmName()
                );
                
                updatePurchaseOrder(po, updatedPO);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update the shipping status of a purchase order
     * @param poID The purchase order ID
     * @param status The new shipping status
     * @return True if successful, false if purchase order not found
     */
    public boolean updateShippingStatus(String poID, String status) {
        for (TSPurchaseOrder po : purchaseOrders) {
            if (po.getPoID().equals(poID)) {
                TSPurchaseOrder updatedPO = new TSPurchaseOrder(
                    po.getPoID(), 
                    po.getPrID(), 
                    po.getItemID(), 
                    po.getQuantity(), 
                    po.getSupplierID(), 
                    status, 
                    po.getApprovalStatus(), 
                    po.getPmName()
                );
                
                // If status is "Arrived", update stock
                if (status.equals("Arrived") && po.getApprovalStatus().equals("Approved")) {
                    stockManager.updateStockQuantity(po.getItemID(), po.getQuantity(), po.getPoID());
                }
                
                updatePurchaseOrder(po, updatedPO);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get all purchase orders
     * @return List of all purchase orders
     */
    public List<TSPurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrders;
    }
    
    /**
     * Find a purchase order by ID
     * @param poID The purchase order ID
     * @return The purchase order or null if not found
     */
    public TSPurchaseOrder findPurchaseOrderByID(String poID) {
        for (TSPurchaseOrder po : purchaseOrders) {
            if (po.getPoID().equals(poID)) {
                return po;
            }
        }
        return null;
    }
    
    /**
     * Get purchase orders for a specific purchase requisition
     * @param prID The purchase requisition ID
     * @return List of purchase orders for the purchase requisition
     */
    public List<TSPurchaseOrder> getPurchaseOrdersByPR(String prID) {
        List<TSPurchaseOrder> result = new ArrayList<>();
        for (TSPurchaseOrder po : purchaseOrders) {
            if (po.getPrID().equals(prID)) {
                result.add(po);
            }
        }
        return result;
    }
    
    /**
     * Get purchase orders for a specific purchase manager
     * @param pmName The purchase manager name
     * @return List of purchase orders for the purchase manager
     */
    public List<TSPurchaseOrder> getPurchaseOrdersByPM(String pmName) {
        List<TSPurchaseOrder> result = new ArrayList<>();
        for (TSPurchaseOrder po : purchaseOrders) {
            if (po.getPmName().equals(pmName)) {
                result.add(po);
            }
        }
        return result;
    }
    
    /**
     * Generate a new purchase order ID
     * @return A new purchase order ID
     */
    public String generateNewPOID() {
        return TSInventoryFileHelper.generateNewID("PO", TSInventoryFileHelper.PO_FILE, 0);
    }
} 