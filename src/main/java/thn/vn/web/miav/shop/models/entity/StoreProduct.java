package thn.vn.web.miav.shop.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "store_product")
public class StoreProduct implements Serializable {
    @Id
    private int storeHouseDetailId;

    public int getStoreHouseDetailId() {
        return storeHouseDetailId;
    }

    public void setStoreHouseDetailId(int storeHouseDetailId) {
        this.storeHouseDetailId = storeHouseDetailId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getInvoiceInput() {
        return invoiceInput;
    }

    public void setInvoiceInput(int invoiceInput) {
        this.invoiceInput = invoiceInput;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDateImport() {
        return dateImport;
    }

    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Id
    private int productId;
    @Id
    private int invoiceInput;
    private float amount;
    private String dateImport;
    private int isDelete = 0;
    private String dateUpdate;
}

