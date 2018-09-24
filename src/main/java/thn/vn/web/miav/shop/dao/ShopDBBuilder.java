package thn.vn.web.miav.shop.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import thn.vn.web.miav.shop.services.ShopDBService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopDBBuilder {
    private Class<Object> clazz;
    private String clause;
    private ParameterSql[] args;
    private int maxResult = 0;
    private int firstResult = 0;
    private ShopDBService shopDBService;
    private HashMap<String, List> listParameter = new HashMap<>();

    public ShopDBBuilder setListParameter(String paramName, List listParameter) {
        this.listParameter.put(paramName, listParameter);
        return this;
    }

    public HashMap<String, List> getListParameter() {
        return this.listParameter;
    }

    public static ShopDBBuilder newInstance(ShopDBService shopDBService) {
        return new ShopDBBuilder(shopDBService);
    }

    public static ShopDBBuilder newInstance(ShopDBService shopDBService, Class clazz, String clause, ParameterSql... args) {
        return new ShopDBBuilder(shopDBService, clazz, clause, args);
    }

    public static ShopDBBuilder newInstance(ShopDBService shopDBService, Class clazz) {
        return new ShopDBBuilder(shopDBService, clazz);
    }

    public ShopDBBuilder(ShopDBService shopDBService) {
        this.shopDBService = shopDBService;
    }

    public ShopDBBuilder(ShopDBService shopDBService, Class clazz) {
        this.select(clazz);
        this.shopDBService = shopDBService;
    }

    public ShopDBBuilder(ShopDBService shopDBService, Class clazz, String clause, ParameterSql... args) {
        this.select(clazz);
        this.setClause(clause);
        this.setParameterSql(args);
        this.shopDBService = shopDBService;
    }

    public ShopDBBuilder setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public ShopDBBuilder setMaxResult(int maxResult) {
        this.maxResult = maxResult;
        return this;
    }

    public ShopDBBuilder select(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public ShopDBBuilder setClause(String clause) {
        this.clause = clause;
        return this;
    }

    public ShopDBBuilder setParameterSql(ParameterSql... args) {
        this.args = args;
        return this;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public Class<Object> getClazz() {
        return clazz;
    }

    public String getClause() {
        return clause;
    }

    public ParameterSql[] getArgs() {
        return args;
    }

    private Query setParam(Query query, ParameterSql... args) {
        if (args.length > 0) {
            int paramIndex = 0;
            for (ParameterSql obj : args) {
                query.setParameter(paramIndex, obj.getValue());
                paramIndex++;
            }
        }
        return query;
    }

    public List getList() {
        Session session = shopDBService.getSession();
        try {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("from ");
            strQuery.append(clazz.getSimpleName());
            if (!Utils.isEmpty(clause)) {
                strQuery.append(" where " + clause);
            }
            Query query = session.createQuery(strQuery.toString());
            if (args != null) {
                if (args.length > 0 && !Utils.isEmpty(clause)) {
                    query = setParam(query, args);
                }
            }
            if (listParameter.size() > 0) {
                HashMap<String, List> paramList = listParameter;
                for (String paramName : paramList.keySet()) {
                    query.setParameterList(paramName, paramList.get(paramName));
                }
            }
            if (maxResult > 0) {
                query.setMaxResults(maxResult);
                query.setFirstResult(firstResult);
            }

            List list = query.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
//        return this.shopDBService.getList(this);
    }

    public Object getEntity() {
        return shopDBService.getEntity(this);
    }

    public void delete() {
        shopDBService.delete(this);
    }

    public Boolean check() {
        return shopDBService.getEntity(this) != null;
    }
    public String getMax(String column){
        Session session = shopDBService.getSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
            Root root = criteriaQuery.from(clazz);
            criteriaQuery.select(builder.max(root.get(column)));
            Query<String> query = session.createQuery(criteriaQuery);
            String max = query.getSingleResult();
            return max;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
}
