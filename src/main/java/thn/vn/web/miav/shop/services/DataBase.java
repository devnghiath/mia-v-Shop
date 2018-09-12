package thn.vn.web.miav.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thn.vn.web.miav.shop.dao.DataBaseDao;
import thn.vn.web.miav.shop.utils.ParameterSql;


import java.util.List;

@Service
public class DataBase implements DataBaseService {
    @Autowired
    private DataBaseDao dataBaseDao;

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        return dataBaseDao.getAll(clazz);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz, String clause, ParameterSql... args) {
        return dataBaseDao.getAll(clazz, clause, args);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz, String sql) {
        return dataBaseDao.getAll(clazz, sql);
    }

    @Override
    public <T> T find(Class<T> clazz, String clause, ParameterSql... args) {
        return dataBaseDao.find(clazz, clause, args);
    }

    @Override
    public <T> T find(Class<T> clazz, String sql) {
        return dataBaseDao.find(clazz, sql);
    }

    @Override
    public <T> void delete(Class<T> clazz, String clause, ParameterSql... args) {
        dataBaseDao.delete(clazz,clause,args);
    }

    @Override
    public <T> void save(T entity) {
        dataBaseDao.save(entity);
    }

    @Override
    public <T> boolean checked(Class<T> clazz, String clause, ParameterSql... args) {
        return getAll(clazz, clause, args).size() > 0;
    }
}
