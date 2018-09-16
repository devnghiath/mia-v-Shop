package thn.vn.web.miav.shop.dao;

import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.List;

public interface ShopDBDao {
     List getList(ShopDBBuilder shopDBBuilder);
     Object getEntity(ShopDBBuilder shopDBBuilder);
     <T>void save(T entity);
}
