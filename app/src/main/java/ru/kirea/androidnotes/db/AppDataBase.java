package ru.kirea.androidnotes.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
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

    //переход на строковые id
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("create table " + DBConsts.TABLE_NOTES + "_tmp as select * from " + DBConsts.TABLE_NOTES);
            db.execSQL("drop table " + DBConsts.TABLE_NOTES);

            db.execSQL("create table " + DBConsts.TABLE_NOTES + " (" +
                    DBConsts.NOTE_ID + " text primary key not null, " +
                    DBConsts.NOTE_TITLE + " text, " +
                    DBConsts.NOTE_DESCRIPTION + " text, " +
                    DBConsts.NOTE_CREATE_DATE + " integer not null, " +
                    DBConsts.NOTE_UPDATE_DATE + " integer);");
            db.execSQL("create index index_" + DBConsts.TABLE_NOTES + "_" + DBConsts.NOTE_ID + " on " + DBConsts.TABLE_NOTES + "(" + DBConsts.NOTE_ID + ");");
            db.execSQL("create index index_" + DBConsts.TABLE_NOTES + "_" + DBConsts.NOTE_TITLE + " on " + DBConsts.TABLE_NOTES + "(" + DBConsts.NOTE_TITLE + ");");

            //вставляем записи в новую структуру
            Cursor cursor = db.query(String.format("select %s, %s, %s, %s from %s",
                    DBConsts.NOTE_TITLE,
                    DBConsts.NOTE_DESCRIPTION,
                    DBConsts.NOTE_CREATE_DATE,
                    DBConsts.NOTE_UPDATE_DATE,
                    DBConsts.TABLE_NOTES + "_tmp"));
            if (cursor.moveToFirst()) {
                do {
                    Long updateDate = cursor.getLong(0) == 0 ? null : cursor.getLong(0);
                    ContentValues cV = new ContentValues();
                    cV.put(DBConsts.NOTE_ID, UUID.randomUUID().toString());
                    cV.put(DBConsts.NOTE_TITLE, cursor.getString(0));
                    cV.put(DBConsts.NOTE_DESCRIPTION, cursor.getString(1));
                    cV.put(DBConsts.NOTE_CREATE_DATE, cursor.getLong(2));
                    cV.put(DBConsts.NOTE_UPDATE_DATE, updateDate);

                    db.insert(DBConsts.TABLE_NOTES, SQLiteDatabase.CONFLICT_REPLACE, cV);
                } while (cursor.moveToNext());
            }

            cursor.close();

            db.execSQL("drop table " + DBConsts.TABLE_NOTES + "_tmp");
        }
    };
}
