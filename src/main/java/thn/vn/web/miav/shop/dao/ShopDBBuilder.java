package thn.vn.web.miav.shop.dao;

import thn.vn.web.miav.shop.services.ShopDBService;
import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.List;

public class ShopDBBuilder {
    private Class<Object> clazz;
    private String clause;
    private  ParameterSql[] args;
    private int maxResult= 0;
    private int firstResult = 0;
    public static ShopDBBuilder newInstance(){
        return new ShopDBBuilder();
    }
    public ShopDBBuilder setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public ShopDBBuilder setMaxResult(int maxResult) {
        this.maxResult = maxResult;
        return this ;
    }
    public ShopDBBuilder select(Class clazz){
        this.clazz = clazz;
        return this;
    }
    public ShopDBBuilder setClause(String clause){
        this.clause = clause;
        return this;
    }
    public ShopDBBuilder setParameterSql(ParameterSql... args){
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

    public List getList(ShopDBService shopDBService) {
        return shopDBService.getList(this);
    }
    public Object getEntity(ShopDBService shopDBService) {
        return shopDBService.getEntity(this);
    }
    public void delete(ShopDBService shopDBService){
        shopDBService.delete(this);
    }

}
