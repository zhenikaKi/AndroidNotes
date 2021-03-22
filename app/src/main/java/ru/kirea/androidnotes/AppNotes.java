package ru.kirea.androidnotes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import ru.kirea.androidnotes.db.AppDataBase;
import ru.kirea.androidnotes.db.DBConsts;

public class AppNotes extends Application {
    private static final String LOG_TAG = "My";
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

        //обработчик ошибок
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(e.getClass().getName())
                        .append(e.getMessage());
                for(StackTraceElement element: e.getStackTrace())
                    stringBuilder.append("\n").append(element.toString());

                Log.e(LOG_TAG, stringBuilder.toString());
            }
        });
    }

    public static AppNotes getInstance() {
        return instance;
    }

    public AppDataBase getDatabase() {
        return database;
    }

    public static void inLog(String text) {
        Log.d(LOG_TAG, text);
    }
}
