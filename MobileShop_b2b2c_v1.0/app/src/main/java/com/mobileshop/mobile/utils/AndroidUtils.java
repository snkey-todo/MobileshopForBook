package com.mobileshop.mobile.utils;

import android.content.Context;

/**
 * Created by Dawei on 4/27/15.
 */
public class AndroidUtils {

    public static int dp2px(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat / paramContext.getResources().getDisplayMetrics().density);
    }

}
