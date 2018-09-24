package thn.vn.web.miav.shop.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "invoice_buy_Detail")
public class InvoiceBuyDetail implements Serializable {
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
        return dateBuy;
    }

    public void setDateImport(String dateImport) {
        this.dateBuy = dateImport;
    }

    @Id
    private int id;
    private float amount;
    @Id
    private int productId;

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

    private String productName;
    private String productNameSecond;
    private String dateBuy;


    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    private String dateUpdate;

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    private int isDelete = 0;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;
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

    @Id
    private int no;
    private float totalMoney =0;

    public int getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(int isInventory) {
        this.isInventory = isInventory;
    }

    private int isInventory = 0;
    private String note;

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

    private int isWarehouse = 1;
}
