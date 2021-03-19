package ru.kirea.androidnotes;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;
import ru.kirea.androidnotes.db.AppDataBase;
import ru.kirea.androidnotes.db.DBConsts;

public class AppNotes extends Application {
    private static AppNotes instance;
    private AppDataBase database; //база данных

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder(this, AppDataBase.class, DBConsts.NAME)
                .addCallback(AppDataBase.insertDefaultData) //заполнение параметров по умолчанию
                //выполнение запросов в основном потоке (это конечно плохо и по хорошему с базой надо работать в фоновом потоке)
                .allowMainThreadQueries()
                .build();
    }

    public static AppNotes getInstance() {
        return instance;
    }

    public AppDataBase getDatabase() {
        return database;
    }

    public static void inLog(String text) {
        Log.d("My", text);
    }
}
