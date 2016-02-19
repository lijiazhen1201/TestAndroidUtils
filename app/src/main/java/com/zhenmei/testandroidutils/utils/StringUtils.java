package com.zhenmei.testandroidutils.utils;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否为 null/空/无内容
     */
    public static boolean isEmptyString(String str) {
        if (null == str) {
            return true;
        }
        if ("".equals(str.trim())) {
            return true;
        }
        if (str.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 字符串相等 null和空字符串认为相等，忽略字符串前后空格
     */
    public static boolean isSameString(String str1, String str2) {
        if (null == str1) {
            str1 = "";
        }
        if (null == str2) {
            str2 = "";
        }
        if (str1.trim().equals(str2.trim())) {
            return true;
        }
        return false;
    }

}
