package ru.kirea.androidnotes.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    //настройки приложения
    private static final String PREFERENCE_NAME = "appSettings";
    public static final String VK_TOKEN = "vkToken";
    public static final String VK_USER_ID = "vkUserId";

    //сохранить строковый параметр
    public static void setSetting(Context context, String key, String value) {
        if (context == null) {
            return;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //получить строковый параметр
    public static String getSetting(Context context, String key, String defaultValue) {
        if (context == null) {
            return defaultValue;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    //сохранить числовой параметр
    public static void setSettingInt(Context context, String key, Integer value) {
        if (context == null) {
            return;
        }

        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putInt(key, value);
        }
        editor.apply();
    }

    //получить числовой параметр
    public static int getSettingInt(Context context, String key, int defaultValue) {
        if (context == null) {
            return defaultValue;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }
}
