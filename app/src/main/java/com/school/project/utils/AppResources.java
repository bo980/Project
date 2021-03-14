package com.school.project.utils;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class AppResources {
    @Nullable
    public static String getString(@StringRes int strId) {
        return AppContext.getApplication().getString(strId);
    }


    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(AppContext.getApplication(), colorId);
    }
}
