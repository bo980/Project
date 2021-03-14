package com.school.project.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String USER_PREFERENCES = "app_data";

    private static SharedPreferencesUtils sSharedPreferencesUtils;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    //单例模式
    public static SharedPreferencesUtils getInstance() {
        if (sSharedPreferencesUtils == null) {
            sSharedPreferencesUtils = new SharedPreferencesUtils();
        }
        return sSharedPreferencesUtils;
    }

    @SuppressLint("CommitPrefEdits")
    private SharedPreferencesUtils() {
        mSharedPreferences = AppContext.getContext().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putInt(String key, int value) {
        mEditor.putInt(key, value).commit();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }
}
