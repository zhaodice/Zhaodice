package com.zhao.dice.model.plugins;


import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by fkzhang on 1/27/2016.
 * Edited by tetsai on 3/30/2020
 */
public class ReflectionUtil {
    public static String getStackTraceString(Throwable ex){//(Exception ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }
        return traceBuilder.toString();
    }
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
            if (field.getType()==type) {
                if(fieldName==null || field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    public static Object getObjectField(Object o, String fieldName, String type) {
        if(o==null)
            return null;
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ((fieldName==null || field.getName().equals(fieldName)) && field.getType().getName().equals(type)) {
                field.setAccessible(true);
                try {
                    return field.get(o);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Object getObjectField(Object o, String fieldName, Class<?> type) {
        return getObjectField(o, fieldName, type.getName());
    }
    /**
     * NSF: Neither Static nor Final
     *
     * @param obj  thisObj
     * @param type Field type
     * @return the FIRST(as declared seq in dex) field value meeting the type
     */
    @Deprecated
    public static Object getFirstNSFByType(Object obj, Class type) {
        if (obj == null) throw new NullPointerException("obj == null");
        if (type == null) throw new NullPointerException("type == null");
        Class clz = obj.getClass();
        while (clz != null && !clz.equals(Object.class)) {
            for (Field f : clz.getDeclaredFields()) {
                if (!f.getType().equals(type)) continue;
                int m = f.getModifiers();
                if (Modifier.isStatic(m) || Modifier.isFinal(m)) continue;
                f.setAccessible(true);
                try {
                    return f.get(obj);
                } catch (IllegalAccessException ignored) {
                    //should not happen
                }
            }
            clz = clz.getSuperclass();
        }
        return null;
    }

    public static Method getStaticMethod(Class<?> cls, String methodName, Class<?> returnType, Class<?>... parameters) {
        for (Method method : cls.getDeclaredMethods()) {
            if (!method.getName().equals(methodName) || method.getReturnType() != returnType)
                continue;

            Class<?>[] pars = method.getParameterTypes();
            if (parameters.length != pars.length)
                continue;
            boolean found = true;
            for (int i = 0; i < parameters.length; i++) {
                if (pars[i] != parameters[i]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return method;
            }
        }

        return null;
    }

    public static Method getMethod(Object object, String methodName, Class<?> returnType, Class<?>... parameters) {
        return getMethod(object.getClass(),methodName,returnType,parameters);
    }
    //clazz类 method方法名 returntype函数应返回的类型(为空表不限定类型) args传入的参数类型
    public static Method getMethod(Class clazz, String methodName, Class<?> returnType, Class<?>... parameters) {
        for (Method method : clazz.getDeclaredMethods()) {

            if(returnType!=null && method.getReturnType() != returnType){
                continue;
            }else if(methodName!=null && !method.getName().equals(methodName)){
                continue;
            }

            Class<?>[] pars = method.getParameterTypes();
            if (parameters.length != pars.length)
                continue;
            boolean found = true;
            for (int i = 0; i < parameters.length; i++) {
                if (pars[i] != parameters[i]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return method;
            }
        }

        return null;
    }

    public static Method getMethod(Class clazz, String methodName, String returnType, Class<?>... parameters) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(methodName) || !method.getReturnType().getName().equals(returnType))
                continue;

            Class<?>[] pars = method.getParameterTypes();
            if (parameters.length != pars.length)
                continue;
            boolean found = true;
            for (int i = 0; i < parameters.length; i++) {
                if (pars[i] != parameters[i]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return method;
            }
        }

        return null;
    }

    public static Method getMethod(Class clazz, String methodName, String returnType, String... parameters) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(methodName) || !method.getReturnType().getName().equals(returnType))
                continue;

            Class<?>[] pars = method.getParameterTypes();
            if (parameters.length != pars.length)
                continue;
            boolean found = true;
            for (int i = 0; i < parameters.length; i++) {
                if (!pars[i].getName().equals(parameters[i])) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return method;
            }
        }

        return null;
    }

    public static Object invokeMethod(Method method, Object o, Object... args) {
        if (method == null)
            return null;

        try {
            return method.invoke(o, args);
        } catch (Throwable t) {
            Log.i("chulhu", method+" invokeMethod failed"+t.getMessage());
        }
        return null;
    }

    public static Object invokeStaticMethod(Method method, Object... args) {
        if (method == null)
            return null;
        return invokeMethod(method, (Object) null, args);
    }

    public static boolean isCallingFrom(String className) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().contains(className)) {
                return true;
            }
        }
        return false;
    }
}