package me.yummykang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties文件工具类（common工具类）.
 *
 * @author demon
 * @Date 2016/11/30 14:21
 */
public class PropertiesUtils {
    private Properties properties = new Properties();

    public PropertiesUtils(String fileName) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取properties的值(返回Object类型)
     *
     * @param key 键值
     * @return Object
     */
    public Object getValue(String key) {
        return properties.get(key);
    }

    /**
     * 获取properties的值(返回String类型)
     *
     * @param key 键值
     * @return java.lang.String
     */
    public String getStringValue(String key) {
        return String.valueOf(getValue(key));
    }

    /**
     * 获取properties的值(返回Integer类型)
     *
     * @param key 键值
     * @return java.lang.Integer
     */
    public Integer getIntValue(String key) {
        return Integer.valueOf(getStringValue(key));
    }
}
