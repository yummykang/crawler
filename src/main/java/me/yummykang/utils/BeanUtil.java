package me.yummykang.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * bean工具类.
 *
 * @author demon
 * @Date 2016/5/11 9:40
 */
public class BeanUtil {

    /**
     * 将java bean转到map，map的key为给定的attrs
     *
     * @param obj   bean实例
     * @param clazz bean的class
     * @param attrs 需要转换的属性
     * @return 实体的属性map，key为属性名，value为属性值
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object obj, Class clazz, String[] attrs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> resultMap = new HashMap<>();
        for (String attr : attrs) {
            Method method = clazz.getMethod(getMethodGetName(attr));
            resultMap.put(attr, method.invoke(obj));
        }
        return resultMap;
    }

    /**
     * bean转为map
     *
     * @param obj
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object obj, Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> resultMap = new HashMap<>();
        for (Field field : fields) {
            if (getMethodGetName(field.getName()).equalsIgnoreCase("getSerialVersionUID")) {
                continue;
            }
            Method method = clazz.getMethod(getMethodGetName(field.getName()));
            resultMap.put(field.getName(), method.invoke(obj));
        }
        return resultMap;
    }

    /**
     * 组装get方法名
     *
     * @param attrName
     * @return
     */
    public static String getMethodGetName(String attrName) {
        StringBuffer result = new StringBuffer("get");
        result.append(attrName.substring(0, 1).toUpperCase());
        result.append(attrName.substring(1, attrName.length()));
        return result.toString();
    }

    /**
     * 组装set方法名
     *
     * @param attrName
     * @return
     */
    public static String getMethodSetName(String attrName) {
        StringBuffer result = new StringBuffer("set");
        result.append(attrName.substring(0, 1).toUpperCase());
        result.append(attrName.substring(1, attrName.length()));
        return result.toString();
    }

    /**
     * 复制属性值
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (getMethodGetName(field.getName()).equalsIgnoreCase("getSerialVersionUID")) {
                continue;
            }
            try {
                Method getMethod = source.getClass().getMethod(getMethodGetName(field.getName()));
                Method setMethod = target.getClass().getMethod(getMethodSetName(field.getName()), field.getType());
                setMethod.invoke(target, getMethod.invoke(source));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 获取复制的实体
     *
     * @param obj
     * @return
     */
    public static <T> T getDeepCopy(Object obj) {
        T result = null;
        try {
            result = (T) obj.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        copyProperties(obj, result);
        return result;
    }

    /**
     * 调用指定属性的set方法
     *
     * @param clazz
     * @param field
     * @param obj
     * @param args
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void invokeSet(Class clazz, Field field, Object obj, Object... args) {
        Method method = null;
        try {
            method = clazz.getMethod(getMethodSetName(field.getName()), field.getType());
            method.invoke(obj, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用get方法
     *
     * @param clazz
     * @param source
     * @param fieldName
     * @return
     */
    public static Object invokeGet(Class clazz, Object source, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        Object result = null;
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                try {
                    Method getMethod = clazz.getMethod(getMethodGetName(field.getName()));
                    result = getMethod.invoke(source);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Object数组转bean
     *
     * @param objArr
     * @param t
     * @param attrs
     * @param <T>
     * @return
     */
    public static <T> T objArrToBean(Object[] objArr, Class<T> t, Set<String> attrs) {
        if (objArr.length != attrs.size()) {
            return null;
        }
        T result = null;
        try {
            result = t.newInstance();
            Field[] fields = t.getDeclaredFields();
            int i = 0;
            for (String attr : attrs) {
                Stream<Field> stream = Stream.of(fields);
                Field field = stream.filter(field1 -> field1.getName().equals(attr)).findFirst().get();
                invokeSet(t, field, result, objArr[i]);
                i++;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * list<Object[]>转成list<T>
     *
     * @param objList
     * @param t
     * @param attrs
     * @param <T>
     * @return
     */
    public static <T> List<T> listObjToListBean(List<Object[]> objList, Class<T> t, Set<String> attrs) {
        List<T> resultList = new ArrayList<>();
        for (Object[] objArr : objList) {
            T bean = objArrToBean(objArr, t, attrs);
            resultList.add(bean);
        }
        return resultList;
    }
}
