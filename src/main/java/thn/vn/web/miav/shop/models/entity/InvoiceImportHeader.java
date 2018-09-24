package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "invoice_import_header")
public class InvoiceImportHeader {

    public String getDateImport() {
        return dateImport;
    }

    public void setDateImport(String dateImport) {
        this.dateImport = dateImport;
    }



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
    public void setInvoiceBuyId(int invoiceBuyId) {
        this.invoiceBuyId = invoiceBuyId;
    }

    public int getInvoiceBuyId() {
        return invoiceBuyId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String dateImport;
    private int invoiceBuyId =0;
    private String dateUpdate;
    private float total;
    private int userId;
    private String note;
    private int isComplete = 0;
    private int isPay = 0;
}
