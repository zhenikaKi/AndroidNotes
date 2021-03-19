package ru.kirea.androidnotes.db.models;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import ru.kirea.androidnotes.db.DBConsts;
import ru.kirea.androidnotes.models.LocalNotesServiceImpl;

@Entity(tableName = DBConsts.TABLE_NOTES,
        indices = {
                @Index(DBConsts.NOTE_ID),
                @Index(DBConsts.NOTE_TITLE)
        })
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBConsts.NOTE_ID)
    private long id;

    @ColumnInfo(name = DBConsts.NOTE_TITLE)
    private String title;

    @ColumnInfo(name = DBConsts.NOTE_DESCRIPTION)
    private String description;

    @ColumnInfo(name = DBConsts.NOTE_CREATE_DATE)
    private long createDate;

    @ColumnInfo(name = DBConsts.NOTE_UPDATE_DATE)
    @Nullable
    private Long updateDate;

    @Ignore
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        id = LocalNotesServiceImpl.incrementId++;
        createDate = System.currentTimeMillis();
    }

    public Note(long id, String title, String description, long createDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        updateDate = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(@Nullable Long updateDate) {
        this.updateDate = updateDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

}
