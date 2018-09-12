package thn.vn.web.miav.shop.dao;

import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.List;

public interface DataBaseDao {
    <T>List<T> getAll(Class<T> clazz);
    <T>List<T> getAll(Class<T> clazz, String clause, ParameterSql... args);
    <T>List<T> getAll(Class<T> clazz, String sql);
    <T>T find(Class<T> clazz, String clause, ParameterSql... args);
    <T>T find(Class<T> clazz, String sql);
    <T> void delete(Class<T> clazz, String clause, ParameterSql... args);
    <T>boolean checked(Class<T> clazz, String clause, ParameterSql... args);
    <T>void save(T entity);
}
