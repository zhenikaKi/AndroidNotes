package ru.kirea.androidnotes.db.models;

import ru.kirea.androidnotes.presenters.ListNotesAdapter;

public class Title implements ItemType {
    public String text;

    public Title(String text) {
        this.text = text;
    }

    @Override
    public int getType() {
        return ListNotesAdapter.ITEM_TITLE;
    }

    public String getText() {
        return text;
    }
}
