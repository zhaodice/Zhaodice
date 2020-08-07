package com.zhao.dice.model;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

public class XposedUtil {
/*
    public static void setField(Object object, String fieldName, Object value, Type type) {
        Field field = getField(object, fieldName, type);
        if (field == null) return;
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static Field getField(Object object, String fieldName, Type type) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType()==type && field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }*/
    public static Class<?> findClassOrNull(String className, ClassLoader classLoader) {//找class 找不到则为空
        try {
            return XposedHelpers.findClass(className,classLoader);
        }catch (XposedHelpers.ClassNotFoundError e){
            return null;
        }
    }
    /** 获取Object对象，所有成员变量属性值 */
    public static void getObjAttr(Object obj) {
        // 获取对象obj的所有属性域
        Field[] fields = obj.getClass().getFields();

        for (Field field : fields) {
            // 对于每个属性，获取属性名
            String varName = field.getName();
            try {
                boolean access = field.isAccessible();
                if (!access) field.setAccessible(true);

                //从obj中获取field变量
                Object o = field.get(obj);
                AwLog.Log("变量"+o.getClass()+" " + varName + " = " + o);

                if (!access) field.setAccessible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    public static List getFiledsInfo(Object o)
    {
        Log.d("chulhu","---do 1---");
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List list = new ArrayList();
        Map infoMap = null;
        Log.d("chulhu","---do 2---");
        for (int i=0;i<fields.length;i++)
        {

            infoMap = new HashMap();
            String type=fields[i].getType().toString();
            String name=fields[i].getName();
            infoMap.put("type",type);
            infoMap.put("name",name);
            Log.d("chulhu",type+" "+name+"="+XposedHelpers.getObjectField(o,name));
            infoMap.put("value", XposedHelpers.getObjectField(o,name));
            list.add(infoMap);
        }
        return list;
    }
}
