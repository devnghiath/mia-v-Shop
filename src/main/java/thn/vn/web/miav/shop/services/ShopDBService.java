package thn.vn.web.miav.shop.services;

import thn.vn.web.miav.shop.dao.ShopDBBuilder;

import java.util.List;

public interface ShopDBService {
    List getList(ShopDBBuilder shopDBBuilder);
    Object getEntity(ShopDBBuilder shopDBBuilder);
    <T>void save(T entity);
    void delete(ShopDBBuilder shopDBBuilder);
}
