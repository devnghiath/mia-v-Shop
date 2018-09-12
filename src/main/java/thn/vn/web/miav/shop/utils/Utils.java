package thn.vn.web.miav.shop.utils;

import javax.persistence.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utils {
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_YYYY_MM_DD_JP = "yyyyMMdd";
    public static final String DATE_YYYYMMDD = "yyyy/MM/dd";
    public static final String DATE_HH_MM = "HH:mm";
    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_HHMMSS = "HHmmss";
    public static final String DATE_YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";
    public static final String DATE_YYYY_MM_DD_HH_MM_SS_TAG = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FILE = "yyyyMMddHHmmss";
    public static final String DATE_FILE_NOW = "yyyyMMddHHmmss.SSS";
    public static String getTableName ( Class<?> clazz){
        try {
            return clazz.getAnnotation(Table.class).name();
        } catch (Exception e){
            return "";
        }
    }
    public static boolean isEmpty(Object object) {
        if (object == null ) return  true;
        if (object instanceof String) {
            return ((String) object).isEmpty();
        } else {
            return object == null ? true : false;
        }
    }
    public static String DateNow(String format){
        if (isEmpty(format)){
            format = DATE_FILE;
        }
        SimpleDateFormat fm = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        return fm.format(calendar.getTime());
    }
    public static String convertDate(String inputDate,String inPutFormat,String outPutFormat){
        if (Utils.isEmpty(inputDate)) return "";
        SimpleDateFormat infm = new SimpleDateFormat(inPutFormat);
        SimpleDateFormat outfm = new SimpleDateFormat(outPutFormat);
        try {
            Date date = infm.parse(inputDate);
            return outfm.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
    public static String generateBarcode(int length){
        String[]numbers = new String[]{"0","1","2","3","4","5","6","7","8","9"};
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++){
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
    public static String generateId(int length,String prefix){
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++){
            result.append(random.nextInt(10));
        }
        return prefix+result.toString();
    }
    public static String getMD5EncryptedValue(String value){
        value = value+"MIA-V";
        final byte[] defaultBytes = value.getBytes();
        try {
            final MessageDigest md5MsgDigest = MessageDigest.getInstance("MD5");
            md5MsgDigest.reset();
            md5MsgDigest.update(defaultBytes);
            final byte messageDigest[] = md5MsgDigest.digest();
            final StringBuffer hexString = new StringBuffer();
            for (final byte element : messageDigest) {
                final String hex = Integer.toHexString(0xFF & element);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            value = hexString + "";
        } catch (final NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return value;
    }
}
