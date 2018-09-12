package thn.vn.web.miav.shop.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "attributes_detail")
public class AttributesDetail  implements Serializable {
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
    public int getAttributesId() {
        return attributesId;
    }

    public void setAttributesId(int attributesId) {
        this.attributesId = attributesId;
    }

    public int getAttributesValue() {
        return attributesValue;
    }

    public void setAttributesValue(int attributesValue) {
        this.attributesValue = attributesValue;
    }

    public String getAttributesValueName() {
        return attributesValueName;
    }

    public void setAttributesValueName(String attributesValueName) {
        this.attributesValueName = attributesValueName;
    }

    @Id
    private int attributesId;
    @Id
    private int attributesValue;
    private String attributesValueName;
}
