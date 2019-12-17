package com.road.quzhibathhousemqtt.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenqi
 * @Title: MapConvertBaseUtil
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/21下午8:08
 */
@Slf4j
public class MapConvertBaseUtil {


    /**
     * 实体对象转换成map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            log.error("对象转map异常 clazz: {} ,obj: {}",clazz,obj );
            log.error("对象转map异常" + e);
            e.printStackTrace();
        }
        return map;
    }

    /**
     *  map转实体
     * @param map
     * @param clazz
     * @return
     */
    public static Object map2Object(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (Exception e) {
            log.error("map转实体异常 clazz: {} ,obj: {}",clazz,obj );
            log.error("map转实体异常" + e);
            e.printStackTrace();
        }
        return obj;
    }

}

