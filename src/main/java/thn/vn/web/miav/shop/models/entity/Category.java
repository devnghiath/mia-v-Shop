package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "category")
public class Category  implements Serializable {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String manualCode;
    private String name;
    private String path;
    private int isDelete = 0;
    private String dateUpdate;
}
