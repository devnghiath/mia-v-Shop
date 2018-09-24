package thn.vn.web.miav.shop.dao;

import org.hibernate.Session;
import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.List;

public interface ShopDBDao {
     List getList(ShopDBBuilder shopDBBuilder);
     Object getEntity(ShopDBBuilder shopDBBuilder);
     <T>void save(T entity);
    void delete(ShopDBBuilder shopDBBuilder);
    Session getSession();
//    String getMax(String column,ShopDBBuilder shopDBBuilder);
//    String getMin(String column,ShopDBBuilder shopDBBuilder);
}
