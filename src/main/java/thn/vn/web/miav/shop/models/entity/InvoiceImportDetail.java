package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "invoice_import_Detail")
public class InvoiceImportDetail  implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDateImport() {
        return dateImport;
    }

    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setInvoiceBuyId(int invoiceBuyId) {
        this.invoiceBuyId = invoiceBuyId;
    }

    public int getInvoiceBuyId() {
        return invoiceBuyId;
    }
    public int getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(int isInventory) {
        this.isInventory = isInventory;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public int getIsWarehouse() {
        return isWarehouse;
    }

    public void setIsWarehouse(int isWarehouse) {
        this.isWarehouse = isWarehouse;
    }
    private int price;
    private int isDelete = 0;
    @Id
    private int id;
    private float amount;
    @Id
    private int productId;
    private String productName;
    private String productNameSecond;
    private String dateImport;
    private String dateUpdate;
    private int invoiceBuyId =0;
    @Id
    private int no;
    private float totalMoney =0;
    private int isInventory = 0;
    private String note;
    private int isWarehouse = 1;
}
