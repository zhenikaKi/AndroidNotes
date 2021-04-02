package ru.kirea.androidnotes.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context context;

    //настройки приложения
    private static final String PREFERENCE_NAME = "appSettings";
    private static final String VK_TOKEN = "vkToken";
    private static final String VK_USER_ID = "vkUserId";

    public Preferences(Context context) {
        this.context = context;
    }

    //сохранить данные ВК
    public void setVk(String accessToken, Integer userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //обновляем токен
        if (accessToken == null) {
            editor.remove(VK_TOKEN);
        } else {
            editor.putString(VK_TOKEN, accessToken);
        }

        //обновляем пользователя
        if (userId == null) {
            editor.remove(VK_USER_ID);
        } else {
            editor.putInt(VK_USER_ID, userId);
        }

        editor.apply();
    }

    //получить id пользователя из ВК
    public int getVkUserId() {
        return getSettingInt(VK_USER_ID, 0);
    }

    //получить токен из ВК
    public String getVkToken() {
        return getSetting(VK_TOKEN, null);
    }

    //получить строковый параметр
    private String getSetting(String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    //получить числовой параметр
    private int getSettingInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }
}
