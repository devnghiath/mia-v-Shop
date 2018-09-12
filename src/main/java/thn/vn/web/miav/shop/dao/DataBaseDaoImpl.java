package thn.vn.web.miav.shop.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class DataBaseDaoImpl implements DataBaseDao {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(clazz);
            Root<T> contactRoot = query.from(clazz);
            query.select(contactRoot);
            List<T> list = session.createQuery(query).getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }

    }

    @Override
    public <T> List<T> getAll(Class<T> clazz, String clause, ParameterSql... args) {
        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
            String queryString = "from " + clazz.getSimpleName() + " where " + clause;

            Query<T> query = session.createQuery(queryString);
            query=setParam(query,args);
            List<T> list = query.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz, String sql) {
        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
            Query<T> query = session.createQuery(sql);
            List<T> list = query.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public <T> T find(Class<T> clazz, String clause, ParameterSql... args) {
        List<T> list = getAll(clazz,clause,args);
        if (list.size()>0){
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T> T find(Class<T> clazz, String sql) {
        List<T> list = getAll(clazz,sql);
        if (list.size()>0){
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <T> boolean checked(Class<T> clazz, String clause, ParameterSql... args) {
        return getAll(clazz,clause,args).size()>0;
    }


    @Override
    public <T> void save(T entity) {

        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public <T> void delete(Class<T> clazz, String clause, ParameterSql... args) {
        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();

            String queryString = "delete from " + clazz.getSimpleName() + " where " + clause;

            Query query = session.createQuery(queryString);
            query=setParam(query,args);
            session.beginTransaction();
            int result = query.executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
    private Query setParam(Query query,ParameterSql... args){
        if (args.length > 0) {
            int paramIndex = 0;
            for (ParameterSql obj : args) {
                query.setParameter(paramIndex, obj.getValue());
                paramIndex++;
            }
        }
        return query;
    }
}
