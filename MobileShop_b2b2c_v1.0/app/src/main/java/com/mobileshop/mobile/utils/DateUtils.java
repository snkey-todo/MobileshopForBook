package com.mobileshop.mobile.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dawei on 5/23/15.
 */
public class DateUtils {

    /**
     * 将时间戳转换为字符串
     * @param time
     * @param pattern
     * @return
     */
    public static String toString(Long time,String pattern){
        if(time>0){
            if(time.toString().length()==10){
                time = time*1000;
            }
            Date date = new Date(time);
            String str  = toString(date, pattern);
            return str;
        }
        return "";
    }

    /**
     * 把日期转换成字符串型
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern){
        if(date == null){
            return "";
        }
        if(pattern == null){
            pattern = "yyyy-MM-dd";
        }
        String dateString = "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateString = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateString;
    }

}
