package com.road.quzhibathhousemqtt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wenqi
 * @Title: ServerUtil
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20上午11:50
 */
public class ServerUtil {

    /**
     * 字符长度不够补0
     */
    public static String strZeroFill(String data, int len) {
        return String.format("%" + len + "s", data).replace(" ", "0");
    }

    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strToHex(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * @param data
     * @return 格式化数据库取出的通讯密码
     */
    public static String formatPassword(int data) {
        String strData = String.valueOf(data);
        int len = strData.length();
        String result = "";
        String hex = "";
        for (int i = 0; i < len; i++) {
            hex = Integer.toString(Integer.valueOf(strData.charAt(i)), 16);
            result = result + strZeroFill(hex, 2);
        }
        return result;
    }

    /**
     * 整形转16进制字符串
     */
    public static String intToHex(int source, int len) {
        String hexStr = String.format("%" + (len > 0 ? len : "") + "s",
                Integer.toHexString(source)).replace(' ', '0');
        hexStr = hexStr.toUpperCase();
        return (len == 0) ? hexStr :
                hexStr.substring(hexStr.length() - len, hexStr.length());
    }

    /**
     * 整形转16进制字符串
     */
    public static String intToHex(long l, int len) {
        String hexStr = String.format("%" + (len > 0 ? len : "") + "s",
                Long.toHexString(l)).replace(' ', '0');
        return (len == 0) ? hexStr :
                hexStr.substring(hexStr.length() - len, hexStr.length());
    }

    /**
     * 16进制字符串转int
     *
     * @param hexStr
     * @return
     */
    public static int hexToInt(String hexStr) {
        try {
            return Integer.valueOf(hexStr, 16);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 十六进制转long
     *
     * @param hexStr
     * @return
     */
    public static long hexToLong(String hexStr) {
        try {
            return Long.valueOf(hexStr, 16);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * string to int
     *
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * string to Long
     *
     * @param str
     * @return
     */
    public static long strToLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 判断字符串是否为null或为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    /**
     * 判断字符串是十六进制显示
     *
     * @param str
     * @return
     */
    public static boolean isHex(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int strLen = str.length();
        char c;
        for (int i = 0; i < strLen; i++) {
            c = str.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全是数字显示
     *
     * @param str
     * @return
     */
    public static boolean isNum(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int strLen = str.length();
        char c;
        for (int i = 0; i < strLen; i++) {
            c = str.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 日期格式化
     *
     * @param format
     * @param date
     * @return
     */
    public static String formatDateTime(String format, Date date) {
        date = (date == null) ? (new Date()) : date;
        format = isEmpty(format) ? "yyyyMMdd" : format;
        return (new SimpleDateFormat(format)).format(date);
    }

    /**
     * 日期加减天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date incDate(Date date, int day) {
        date = (date == null) ? (new Date()) : date;
        long time = date.getTime() + ((long) day) * 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    /**
     * 密令中格式化时间
     *
     * @param date
     * @return
     */
    public static String dateFormat(String date) {
        StringBuffer formatDate = new StringBuffer();
        if (date.length() == 6) {
            formatDate.append("20");
            formatDate.append(date.substring(0, 2));
            formatDate.append("-");
            formatDate.append(date.substring(2, 4));
            formatDate.append("-");
            formatDate.append(date.substring(4, 6));
        } else if (date.length() == 8) {
            formatDate.append(date.substring(0, 4));
            formatDate.append("-");
            formatDate.append(date.substring(4, 6));
            formatDate.append("-");
            formatDate.append(date.substring(6, 8));
        } else if (date.length() == 14) {
            formatDate.append(date.substring(0, 4));
            formatDate.append("-");
            formatDate.append(date.substring(4, 6));
            formatDate.append("-");
            formatDate.append(date.substring(6, 8));
            formatDate.append(" ");
            formatDate.append(date.substring(8, 10));
            formatDate.append(":");
            formatDate.append(date.substring(10, 12));
            formatDate.append(":");
            formatDate.append(date.substring(12, 14));
        } else if (date.length() == 12) {
            formatDate.append("20");
            formatDate.append(date.substring(0, 2));
            formatDate.append("-");
            formatDate.append(date.substring(2, 4));
            formatDate.append("-");
            formatDate.append(date.substring(4, 6));
            formatDate.append(" ");
            formatDate.append(date.substring(6, 8));
            formatDate.append(":");
            formatDate.append(date.substring(8, 10));
            formatDate.append(":");
            formatDate.append(date.substring(10, 12));
        } else {
            formatDate.append(date);
        }
        return formatDate.toString();

    }

}
