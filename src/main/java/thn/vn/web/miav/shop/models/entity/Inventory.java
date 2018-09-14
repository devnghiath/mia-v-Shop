package thn.vn.web.miav.shop.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public int getAmountFirst() {
        return amountFirst;
    }

    public void setAmountFirst(int amountFirst) {
        this.amountFirst = amountFirst;
    }

    public int getAmountImport() {
        return amountImport;
    }

    public void setAmountImport(int amountImport) {
        this.amountImport = amountImport;
    }

    public int getAmountExport() {
        return amountExport;
    }

    public void setAmountExport(int amountExport) {
        this.amountExport = amountExport;
    }

    public int getAmountInventory() {
        return amountInventory;
    }

    public void setAmountInventory(int amountInventory) {
        this.amountInventory = amountInventory;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameSecond() {
        return productNameSecond;
    }

    public void setProductNameSecond(String productNameSecond) {
        this.productNameSecond = productNameSecond;
    }
    @Id
    private int productId;
    private String inventoryDate;
    private int amountFirst;
    private int amountImport;
    private int amountExport;

    private int amountInventory;
    private String productName;
    private String productNameSecond;

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    private String dateUpdate;

}
