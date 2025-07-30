package Purchase_Order_Management;

public class KHSupplier {
    private String supplierId;
    private String name;
    private String address;

    public KHSupplier(String supplierId, String name, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.address = address;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return supplierId + ";" + name + ";" + address;
    }
}
