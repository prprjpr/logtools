package com.nokia.logtools.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class BeanUtil {

	public static <T> T getBean(Class<T> clazz, Properties prop) {
		// system.properties内容覆盖commons
		try {
			prop.load(BeanUtil.class.getClassLoader().getResourceAsStream("system.properties"));
		} catch (IOException e) {
		}
		
		Map<String,Object> map = new HashMap<>();
		for (Entry<Object, Object> entrySet : prop.entrySet()) {
			String key = (String) entrySet.getKey();
			Object value = entrySet.getValue();
			map.put(key, value);
		}

		return getBean(clazz, map);
	}
	
	public static <T> T getBean(Class<T> clazz, Map<String,Object> map) {
		T bean = null;
		String prefix = "";
		try {
			bean = clazz.newInstance();
			prefix = (String) clazz.getField("prefix").get(bean);
		} catch (Exception e) {
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			try {
				if ("prefix".equals(name)) {
					continue;
				}
				Method method = clazz.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), field.getType());
				method.invoke(bean, map.get(prefix + name));
			} catch (Exception e) {
			}
		}
		return bean;
	}

	public static boolean isBaseType(Class<?> clazz) {
		if (clazz.isPrimitive())
			return true;
		if (clazz.getPackage().getName().startsWith("java.lang"))
			return true;
		return false;
	}

}
