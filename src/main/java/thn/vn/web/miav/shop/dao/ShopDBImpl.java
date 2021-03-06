package thn.vn.web.miav.shop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.List;

@Repository
@Transactional
public class ShopDBImpl implements ShopDBDao{
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Query setParam(Query query, ParameterSql... args){
        if (args.length > 0) {
            int paramIndex = 0;
            for (ParameterSql obj : args) {
                query.setParameter(paramIndex, obj.getValue());
                paramIndex++;
            }
        }
        return query;
    }

    @Override
    public Session getSession() {
        return entityManagerFactory.unwrap(SessionFactory.class).openSession();
    }

    @Override
    public  List getList(ShopDBBuilder shopDBBuilder) {
        Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        try {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("from ");
            strQuery.append(shopDBBuilder.getClazz().getSimpleName());
            if (!Utils.isEmpty(shopDBBuilder.getClause())){
                strQuery.append(" where " + shopDBBuilder.getClause());
            }
            Query query = session.createQuery(strQuery.toString());
            if (shopDBBuilder.getArgs()!=null){
                if (shopDBBuilder.getArgs().length>0 && !Utils.isEmpty(shopDBBuilder.getClause())){
                    query=setParam(query,shopDBBuilder.getArgs());
                }
            }
            if (shopDBBuilder.getListParameter().size()>0){
                HashMap<String,List> paramList = shopDBBuilder.getListParameter();
                for (String paramName:paramList.keySet()){
                    query.setParameterList(paramName,paramList.get(paramName));
                }
            }
            if (shopDBBuilder.getMaxResult()>0){
                query.setMaxResults(shopDBBuilder.getMaxResult());
                query.setFirstResult(shopDBBuilder.getFirstResult());
            }

            List list = query.getResultList();
            return list;
        } catch (Exception e){
            throw  e;
        } finally {
            session.close();
        }
    }

    @Override
    public Object getEntity(ShopDBBuilder shopDBBuilder) {
        List list = getList(shopDBBuilder);
        if (list.size() ==0){
            return null;
        } else {
            return list.get(0);
        }
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
    public void delete(ShopDBBuilder shopDBBuilder) {
        Session session = null;
        try {
            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();


            StringBuilder strQuery = new StringBuilder();
            strQuery.append("delete from " + shopDBBuilder.getClazz().getSimpleName());
            if (!Utils.isEmpty(shopDBBuilder.getClause())){
                strQuery.append(" where " + shopDBBuilder.getClause());
            }
            Query query = session.createQuery(strQuery.toString());
            if (shopDBBuilder.getArgs()!=null){
                if (shopDBBuilder.getArgs().length>0 && !Utils.isEmpty(shopDBBuilder.getClause())){
                    query=setParam(query,shopDBBuilder.getArgs());
                }
            }
            session.beginTransaction();
            int result = query.executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

//    @Override
//    public String getMax(String column,ShopDBBuilder shopDBBuilder) {
//        Session session = null;
//        try {
//            session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            CriteriaQuery query = builder.createQuery(shopDBBuilder.getClass());
//            Root contactRoot = query.from(shopDBBuilder.getClass());
//            Query<Integer> query2 = session.createQuery(query);
//
//            query.select(builder.max(contactRoot.get("")));
////            return builder.uniqueResult().toString();
//            return "";
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            session.close();
//        }
//    }
//
//    @Override
//    public String getMin(String column,ShopDBBuilder shopDBBuilder) {
//        return null;
//    }
}
