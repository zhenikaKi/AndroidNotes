package ru.kirea.androidnotes.db.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import ru.kirea.androidnotes.db.DBConsts;
import ru.kirea.androidnotes.db.models.Note;

@Dao
public interface NoteDao {
    //добавление заметки
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Note note);

    //добавление заметки
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNotes(List<Note> notes);

    //обновление заметки
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Note note);

    //получить все заметки
    @Query("select * " +
            "from " + DBConsts.TABLE_NOTES + " n " +
            "order by " + DBConsts.NOTE_CREATE_DATE + " desc")
    List<Note> getNotes();

    //получить заметку по id
    @Query("select * " +
            "from " + DBConsts.TABLE_NOTES + " n " +
            "where " + DBConsts.NOTE_ID + " = :id")
    Note getNoteOnId(long id);

    //Удаление заметки
    @Delete
    void delete(Note note);
}
