package ru.kirea.androidnotes.models;

import java.util.List;

public interface NotesService {
    //инициализация
    void init();

    //получить список всех заметок
    List<Note> getNotes();

    //найти заметку по id
    Note findNote(long id);

    //добавить или обновить заметку
    void saveNote(Note note);

    //удалить заметку
    void deleteNote(Note note);
}
