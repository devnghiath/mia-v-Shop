package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "invoice_buy_header")
public class InvoiceBuyHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    public String getDateImport() {
        return dateBuy;
    }

    public void setDateImport(String dateImport) {
        this.dateBuy = dateImport;
    }

    private String dateBuy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDateUpdate() {
        return dateUpdate;
    }


    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getIsPay() {
        return isPay;
    }

    private String dateUpdate;
    private float total;
    private int userId;
    private String note;
    private int isComplete = 0;
    private int isPay = 0;
}
