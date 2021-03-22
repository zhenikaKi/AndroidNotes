package ru.kirea.androidnotes.models;

import android.view.View;

import ru.kirea.androidnotes.db.models.Note;

public interface NoteClickable {
    void noteClick(Note note);

    void noteMenuClick(View view, Note note);
}
