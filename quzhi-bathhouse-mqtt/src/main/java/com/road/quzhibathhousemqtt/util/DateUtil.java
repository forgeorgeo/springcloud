package com.road.quzhibathhousemqtt.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.apache.tomcat.jni.Time.now;

/**
 * @author wenqi
 * @Title: DateUtil
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/21上午11:48
 */
public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
            "yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
            "yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat Millisecond = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    public static String geTimeToSSS() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String str = df.format(date);
        return str;
    }

    public static String getMillisecond() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        String str = df.format(date);
        return str;
    }

    public static String getTimeHHSSS() {
        Date date = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("hhmmssSSS");
        String str = sdf2.format(date);
        return str;
    }

    public static String getTimeHH24MISS() {
        Date date = new Date();
        //只显示出时分秒
        DateFormat df3 = DateFormat.getTimeInstance();
        String a = df3.format(date);

        String f = a.substring(1, 2);
        String b = "";
        if (f.equals(":")) {
            b = "0" + a.substring(0, 1);
            String c = a.substring(2, 4);
            String d = a.substring(5, 7);
            String e = b + c + d;
            return e;
        } else {
            b = a.substring(0, 2);
            String c = a.substring(3, 5);
            String d = a.substring(6, 8);
            String e = b + c + d;
            return e;
        }
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getLastYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        return sdfYear.format(y);
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description: TODO(日期比较 ， 如果s > = e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate2(s) == null || fomatDate2(e) == null) {
            return false;
        }
        return fomatDate2(s).getTime() >= fomatDate2(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate2(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取yyyyMMddHHmmss格式
     *
     * @return
     */
    public static String getFormatTime(Date date) {
        SimpleDateFormat myFormatTime = new SimpleDateFormat("yyyyMMddHHmmss");
        return myFormatTime.format(date);
    }


    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long aa = 0;
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);


        return day;
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        // java.util包
        Calendar canlendar = Calendar.getInstance();
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, daysInt);
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        // java.util包
        Calendar canlendar = Calendar.getInstance();
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, daysInt);
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    public static String getString(String date) {
        Date da = StrToDate(date);
        return DateToStr(da);
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }


    /**
     * 得到若干分钟后的时间
     *
     * @param minutes
     * @return
     */
    public static String getAfterMinute(String minutes) {
        int minutesInt = Integer.parseInt(minutes);
        Calendar canlendar = Calendar.getInstance();
        // 分钟加
        canlendar.add(Calendar.MINUTE, minutesInt);
        Date date = canlendar.getTime();
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdfd.format(date);
        return dateStr;
    }


    /**
     * 获取yyyyMMddHHmmss格式
     *
     * @return
     */
    public static String getFormatTime() {
        SimpleDateFormat myFormatTime = new SimpleDateFormat("yyyyMMddHHmmss");
        return myFormatTime.format(new Date());
    }


    /**
     * 获取7位随机数
     *
     * @return
     */
    public static String getRandom7() {
        //定义变长字符串
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < 7; i++) {
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        int num = Integer.parseInt(str.toString());
        String rand = String.valueOf(num);
        return rand;
    }

    /**
     * 获取yyMMddHHmmss格式日期
     *
     * @return
     */
    public static String getFormatTimeTwelve() {
        SimpleDateFormat myFormatTime = new SimpleDateFormat("yyyyMMddHHmmss");
        return myFormatTime.format(System.currentTimeMillis());
    }

    /**
     * 之前时间+ 间隔时间 和 现在时间 对比
     *
     * @param timestamp
     * @param intervalTime
     * @return
     */
    public static boolean comparisonTime(long timestamp, long intervalTime) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = myFormat.parse(String.valueOf(timestamp));
            Date afterDate = new Date(date.getTime() + intervalTime * 1000);

            if (afterDate.getTime() >= System.currentTimeMillis()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }


        return false;

    }

    /**
     * 获取四位随机数
     *
     * @return
     */
    public static int getRandom4() {
        int rand = (int) ((Math.random() * 9 + 1) * 1000);
        return rand;
    }
}
