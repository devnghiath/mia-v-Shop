package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "invoice_export_header")
public class InvoiceExportHeader {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateExport() {
        return dateExport;
    }

    public void setDateExport(String dateExport) {
        this.dateExport = dateExport;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String dateExport;
    private String dateUpdate;
    private float total;
    private int userId;
    private String note;
    private int isComplete = 0;
    private int isPay = 0;
}
