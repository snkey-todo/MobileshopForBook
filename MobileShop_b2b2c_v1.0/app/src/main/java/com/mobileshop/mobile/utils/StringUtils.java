package com.mobileshop.mobile.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dawei on 4/1/15.
 */
public class StringUtils {

    /**
     * 验证字符串是否为null或空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

    /**
     * 验证手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        boolean flag = false;
        try {
            Pattern p = Pattern
                    .compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobile);
            flag = m.matches();
        } catch (Exception e) {
            Log.d("验证手机号码错误", e.getMessage());
            flag = false;
        }
        return flag;
    }

}
