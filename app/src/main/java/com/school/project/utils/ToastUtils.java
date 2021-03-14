package com.school.project.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
