package com.m7.abs.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class BeanUtil {


    public static void setRequestMapToMap(Map<Object, Object> requestMap, Map<Object, Object> map) {

        for (Iterator<Object> iter = requestMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
    }


    public static void setVOToVO(Object objVO1, Object objVO2) {

        try {
            BeanUtils.copyProperties(objVO2, objVO1);
        } catch (Exception ex) {
            log.error("setVOToVO is Error!!!" + ex.getMessage());
        }

    }

    public static void setVOToVO(Object objVO1, Map<Object, Object> map) {

        try {
            BeanUtils.copyProperties(objVO1, map);
        } catch (Exception ex) {
            log.error("setVOToVO is Error!!!" + ex.getMessage());
        }

    }

    public static void setVOFiledToMap(Object objVO1, Map<Object, Object> map) {

        try {
            List<Field> fList = new ArrayList<Field>();

            Field[] f1 = objVO1.getClass().getDeclaredFields();
            fList.addAll(Arrays.asList(f1));
            if (objVO1.getClass().getSuperclass() != null) {
                f1 = objVO1.getClass().getSuperclass().getDeclaredFields();
                fList.addAll(Arrays.asList(f1));
            }

            Field[] f = (Field[]) fList.toArray(new Field[0]);

            int l = f.length;
            for (int i = 0; i < l; i++) {
                // log.error(f[i].getName()+":"+BeanUtils.getProperty(objVO1, f[i].getName()));
                map.put(f[i].getName(), BeanUtils.getProperty(objVO1, f[i].getName()));
            }
        } catch (Exception ex) {
            log.error("setVOToVO is Error!!!" + ex.getMessage());
        }
    }


    public static String createPramaList(Map<Object, Object> paramMap) {
        String content = "";

        content = "var paramList=new Array();\n";


        int paramMapCount = 0;
        if (paramMap != null) {
            for (Iterator<Object> iter = paramMap.keySet().iterator(); iter
                    .hasNext(); ) {
                String name = (String) iter.next();
                String tempStr = "";
                if (paramMap.get(name) != null) {
                    tempStr = ValidationUtil.removeNullString((String) paramMap.get(name));
                }

                if (!tempStr.equals("")) {
                    tempStr = tempStr.replaceAll("\r", "").replaceAll("\n", "");
                    tempStr = tempStr.replaceAll("\"", "＂");
                    content += "paramList[" + paramMapCount + "] = new Array(\"" + name + "\",\"" + tempStr + "\");\n";
                    paramMapCount += 1;
                }

            }
        }
        return content;
    }

    public static void setMapToVOfiled(Map<Object, Object> map, Object objVO1) {
        try {

            Field[] f = objVO1.getClass().getDeclaredFields();
            int l = f.length;
            for (int i = 0; i < l; i++) {
                Object valueObj = map.get(f[i].getName());
                BeanUtils.setProperty(objVO1, f[i].getName(), valueObj);
            }
        } catch (Exception ex) {
            log.error("setMapToVOfiled is Error!!!" + ex.getMessage());
        }
    }

    public static String getParamStr(Object param) {
        String str = null;
        if (param != null) {
            try {
                str = param.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return str;
    }

    public static String getParamDate(Object param) {
        String str = null;
        if (param != null && !param.toString().equals("")) {
            try {
                str = param.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return str;
    }

    public static Integer getParamInt(Object param) {
        Integer str = null;
        if (param != null && !param.toString().equals("")) {
            try {
                str = Integer.parseInt(param.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return str;
    }

    public static Double getParamDouble(Object param) {
        Double str = null;
        if (param != null && !param.toString().equals("")) {
            try {
                str = Double.parseDouble(param.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static int getEven(int num) {
        int flag = 1;
        if (num % 2 != 0) {
            flag = 1;

        } else {
            flag = 2;
        }
        return flag;
    }

    public static Map<String, String> beanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性  
                if (!key.equals("class")) {
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null) {
                        map.put(key, value + "");
                    }
                }
            }
        } catch (Exception e) {
            log.error("transBean2Map Error " + e);
        }
        return map;
    }

    public static Map<String, Object> beanToMapObject(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null) {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 实体类对象转URL参
     *
     * @param t         实体类对象
     * @param callSuper 是否转换父类成员
     * @param <T>       实体类泛型
     * @param sort      是否根据名称排序
     * @return a=1&b=2
     */
    public static <T> String entityToUrlParam(T t, boolean callSuper, boolean sort) {
        // URL 参数存储器
        StringBuffer urlParam = new StringBuffer();
        //扩展转换父类成员功能
        Map<String, Object> dataMap = entitySuperclassToUrlParam(t, t.getClass(), callSuper);

        if (sort && dataMap != null) {
            dataMap = sortMapByKey(dataMap);
        }

        dataMap.forEach((key, value) -> {
            urlParam.append(key + "=" + value + "&");
        });

        if (urlParam.length() > 0) {
            //去除最后一个&字符
            urlParam.deleteCharAt(urlParam.length() - 1);
        }
        return urlParam.toString();
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 实体类对象转URL参
     *
     * @param t         实体类对象
     * @param clazz     实体类类型
     * @param callSuper 是否转换父类成员
     * @param <T>       实体类泛型
     * @return a=1&b=2
     */
    @Deprecated
    public static <T> Map<String, Object> entitySuperclassToUrlParam(T t, Class clazz, boolean callSuper) {
        Map<String, Object> valueMap = new HashMap<>();
        //如果实体类对象为Object类型，则不处理
        if (!clazz.equals(Object.class)) {
            //获取实体类对象下的所有成员，并保存到 URL 参数存储器中
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                //设置可以操作私有成员
                field.setAccessible(true);
                try {
                    //获取成员值
                    Object value = field.get(t);
                    //成员值为 Null 时，则不处理
                    if (Objects.nonNull(value)) {
                        valueMap.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    log.error("获取对象参数异常", e);
                }
            });
            //是否转换父类成员
            if (callSuper) {
                //获取父类类型
                Class superclass = clazz.getSuperclass();
                //递归调用，获取父类的处理结果
                Map<String, Object> parentMap = entitySuperclassToUrlParam(t, superclass, callSuper);
                valueMap.putAll(parentMap);
            }
        }
        return valueMap;
    }

}
