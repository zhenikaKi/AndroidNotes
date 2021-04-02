package ru.kirea.androidnotes.models;

import java.util.List;

import ru.kirea.androidnotes.db.models.Note;

public interface NotesService {
    //инициализация
    void init();

    //получить список всех заметок
    void getNotes(Callback<List<Note>> callback);

    //найти заметку по id
    void findNote(String id, Callback<Note> callback);

    //добавить или обновить заметку
    void saveNote(Note note, Callback<Note> callback);

    //удалить заметку
    void deleteNote(Note note);
}
