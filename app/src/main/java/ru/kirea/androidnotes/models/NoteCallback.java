package ru.kirea.androidnotes.models;

public interface NoteCallback<T> {
    void onResult(T value);
}
