package org.smart4j.framework.util;

/**
 * Created by tommy on 2015/9/2.
 * ^^
 */
public class NameUtils {
    public static final char UNDERLINE='_';

    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                if(i != 0) {
                    sb.append(UNDERLINE);
                }
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (c==UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
