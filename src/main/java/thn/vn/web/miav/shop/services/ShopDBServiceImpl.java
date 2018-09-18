package thn.vn.web.miav.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.dao.ShopDBDao;

import java.util.List;
@Service
public class ShopDBServiceImpl implements ShopDBService {
    @Autowired
    ShopDBDao shopDBDao;


    @Override
    public List getList(ShopDBBuilder shopDBBuilder) {
        return shopDBDao.getList(shopDBBuilder);
    }

    @Override
    public Object getEntity(ShopDBBuilder shopDBBuilder) {
        return shopDBDao.getEntity(shopDBBuilder);
    }

    @Override
    public <T> void save(T entity) {
        shopDBDao.save(entity);
    }

    @Override
    public void delete(ShopDBBuilder shopDBBuilder) {
        shopDBDao.delete(shopDBBuilder);
    }
}
