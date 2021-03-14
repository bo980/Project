package com.school.project.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtils {
    public static int getWidthPixels(Context context){
        final float scale = context.getResources().getDisplayMetrics().widthPixels;
        return (int) scale;
    }

    public static int getHeightPixels(Context context){
        final float scale = context.getResources().getDisplayMetrics().heightPixels;
        return (int) scale;
    }

    /**
     * 描述：根据手机分辨率把dip转换成px像素
     */
    public static int dip2px(Context context , float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 描述：根据手机分辨率把px像素转换成dip
     */
    public static int px2dip(Context context , float px){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }


    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
