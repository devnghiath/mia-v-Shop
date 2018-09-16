package thn.vn.web.miav.shop.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String name;
    private String path;
    private int isDelete = 0;
    private String dateUpdate;
}
