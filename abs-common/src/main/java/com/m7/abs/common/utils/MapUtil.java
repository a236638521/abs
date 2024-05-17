package com.m7.abs.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/7/8 14:50
 */
public class MapUtil {

    /**
     *  根据map的key进行字典升序排序
     * @param map
     * @return map
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object>map) {
        Map<String, Object> treemap = new TreeMap<String, Object>(map);
        List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(treemap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return treemap;
    }

    /**
     * 将map转换成url参数
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                sb.append("&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    public static  Map<String, Object> getMapByUrlParams(String queryString,String charset) {
        Map<String, Object> map = new HashMap<>();
        String[] split = queryString.split("&");
        for (int i = 0; i < split.length; i++) {
            String data = split[i];
            String[] params = data.split("=");
            if (params.length == 2) {
                String key = params[0];
                String value = params[1];

                try {
                    key = URLDecoder.decode(key, charset);
                    value = URLDecoder.decode(value, charset);
                    map.put(key, value);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        return map;
    }

}
