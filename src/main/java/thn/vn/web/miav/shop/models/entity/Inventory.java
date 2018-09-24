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

    public float getAmountFirst() {
        return amountFirst;
    }

    public void setAmountFirst(float amountFirst) {
        this.amountFirst = amountFirst;
    }

    public float getAmountImport() {
        return amountImport;
    }

    public void setAmountImport(float amountImport) {
        this.amountImport = amountImport;
    }

    public float getAmountExport() {
        return amountExport;
    }

    public void setAmountExport(float amountExport) {
        this.amountExport = amountExport;
    }

    public float getAmountInventory() {
        return amountInventory;
    }

    public void setAmountInventory(float amountInventory) {
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


    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    @Id
    private int productId;
    private String inventoryDate;
    private float amountFirst;
    private float amountImport;
    private float amountExport;
    private float amountInventory;
    private String productName;
    private String productNameSecond;
    private String dateUpdate;

}
