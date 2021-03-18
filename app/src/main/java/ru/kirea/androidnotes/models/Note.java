package ru.kirea.androidnotes.models;

import ru.kirea.androidnotes.helpers.DateHelper;

public class Note {
    private static long incrementId = 1; //для автоматического заполнения id'шника заметки

    private long id;
    private String title;
    private String description;
    private long createDate;
    private Long updateDate;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        id = incrementId++;
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

    public Long getUpdateDate() {
        return updateDate;
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
