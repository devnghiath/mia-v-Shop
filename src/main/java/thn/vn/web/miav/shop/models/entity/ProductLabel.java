package thn.vn.web.miav.shop.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "product_label")
public class ProductLabel implements Serializable {
    @Id
    private int productId;
    private String label;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getManualLabel() {
        return manualLabel;
    }

    public void setManualLabel(String manualLabel) {
        this.manualLabel = manualLabel;
    }

    private String manualLabel;
}
