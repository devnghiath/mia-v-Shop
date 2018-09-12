package thn.vn.web.miav.shop.utils;

public class ParameterSql<T> {
    private  Class<T> mClassType;
    private Object mValue;

    public ParameterSql(Class<T> classType, Object value) {
        mClassType = classType;
        mValue=value;
    }
    public T getValue() {
        return mClassType.cast(mValue);
    }
}
