package ru.kirea.androidnotes.models;

import ru.kirea.androidnotes.helpers.DateHelper;

public class Note {
    private static long incrementId = 1; //для автоматического заполнения id'шника задачи

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

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate + " (" + DateHelper.timestampToString(createDate, DateHelper.DateFormat.DDMMYYYY_HHMMSS) + ") " +
                ", updateDate=" + updateDate + " (" + DateHelper.timestampToString(updateDate, DateHelper.DateFormat.DDMMYYYY_HHMMSS) + ") " +
                '}';
    }
}
