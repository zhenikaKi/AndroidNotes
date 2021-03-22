package ru.kirea.androidnotes.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import ru.kirea.androidnotes.db.dao.NoteDao;
import ru.kirea.androidnotes.db.models.Note;

@Database(entities = {Note.class},
        version = DBConsts.VERSION)
public abstract class AppDataBase extends RoomDatabase {
    public abstract NoteDao noteDao();

    //первоначальное заполнение базы
    public static RoomDatabase.Callback insertDefaultData = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull final SupportSQLiteDatabase db) {
            final int COUNT = 5;

            Random random = new Random();
            for (int ind = 1; ind <= COUNT; ind++) {
                String title = String.format("Заметка #%s", ind);
                String description = String.format("Описание заметки. Ее номер %s из %s", ind, COUNT);

                //сделаем разброс даты создания заметки
                long crete = System.currentTimeMillis() - random.nextInt(1440) * DateUtils.MINUTE_IN_MILLIS;

                ContentValues cV = new ContentValues();
                cV.put(DBConsts.NOTE_TITLE, title);
                cV.put(DBConsts.NOTE_DESCRIPTION, description);
                cV.put(DBConsts.NOTE_CREATE_DATE, crete);
                db.insert(DBConsts.TABLE_NOTES, SQLiteDatabase.CONFLICT_IGNORE, cV);
            }
        }
    };
}
