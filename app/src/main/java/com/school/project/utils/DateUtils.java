package com.school.project.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    //日期字符串转换Date实体
    public static Date parseServerTime(String serverTime, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINESE);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = dateFormat.parse(serverTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    //秒数转换成时分秒
    public static String convertSecToTimeString(long lSeconds) {
        long nHour = lSeconds / 3600;
        long nMin = lSeconds % 3600;
        long nSec = nMin % 60;
        nMin = nMin / 60;

        return String.format("%02d小时%02d分钟%02d秒", nHour, nMin, nSec);
    }

    //Date对象获取时间字符串
    public static String getDateStr(Date date, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    //时间戳转换日期格式字符串
    public static String timeStamp2Date(long time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd-HHmmssSSS
     *
     * @param millisecond 时间毫秒
     * @return 时间格式: yyyy-MM-dd-HHmmssSSS
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
        Date date = new Date(millisecond);
        return simpleDateFormat.format(date);
    }

    //日期格式字符串转换时间戳
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return String.valueOf(dateFormat.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取某个日期前后N天的日期
     *
     * @param beginDate
     * @param distanceDay 前后几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @param format      日期格式，默认"yyyy-MM-dd"
     * @return
     */
    public static String getOldDateByDay(Date beginDate, int distanceDay, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dateFormat.parse(dateFormat.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormat.format(endDate);
    }

    /**
     * 获取前后几个月的日期
     *
     * @param beginDate
     * @param distanceMonth
     * @param format
     * @return
     */
    public static String getOldDateByMonth(Date beginDate, int distanceMonth, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) + distanceMonth);
        Date endDate = null;
        try {
            endDate = dateFormat.parse(dateFormat.format(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormat.format(endDate);
    }


    public static boolean isCurrentInTimeScope(int deadlineHour, int deadlineMin) {
        boolean result;
        // 1000 * 60 * 60 * 24
        final long aDayInMillis = 86400000;
        final long currentTimeMillis = System.currentTimeMillis();
        //截止时间
        Time deadlineTime = new Time();
        deadlineTime.set(currentTimeMillis);
        deadlineTime.hour = deadlineHour;
        deadlineTime.minute = deadlineMin;
        //当前时间
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        //当前时间推后20分钟
        Date d = new Date(currentTimeMillis);
        long myTime = (d.getTime() / 1000) + 20 * 60;
        d.setTime(myTime * 1000);
        Time endTime = new Time();
        endTime.set(myTime);
        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !deadlineTime.before(startTime) && !deadlineTime.after(endTime);
            // startTime <= deadlineTime <=endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!deadlineTime.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !deadlineTime.before(startTime) && !deadlineTime.after(endTime);
            // startTime <= deadlineTime <=endTime
        }
        return result;
    }

    /**
     * 将long行毫秒值转换为 00:00格式的string
     * @param ms 毫秒值
     * @return 00:00格式的string
     */
    public static String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long seconds = totalSeconds % 60;
        long minutes = totalSeconds / 60 % 60;
        long hours = totalSeconds / 60 / 60;
        String timeStr = "";
        if (hours > 9) {
            timeStr += hours + ":";
        } else if (hours > 0) {
            timeStr += "0" + hours + ":";
        }
        if (minutes > 9) {
            timeStr += minutes + ":";
        } else if (minutes > 0) {
            timeStr += "0" + minutes + ":";
        } else {
            timeStr += "00:";
        }
        if (seconds > 9) {
            timeStr += seconds;
        } else if (seconds > 0) {
            timeStr += "0" + seconds;
        } else {
            timeStr += "00";
        }

        return timeStr;
    }
}
