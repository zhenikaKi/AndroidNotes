package ru.kirea.androidnotes.models;

import java.util.List;

import ru.kirea.androidnotes.db.models.Note;

public interface NotesService {
    //инициализация
    void init();

    //получить список всех заметок
    void getNotes(NoteCallback<List<Note>> noteCallback);

    //найти заметку по id
    void findNote(String id, NoteCallback<Note> noteCallback);

    //добавить или обновить заметку
    void saveNote(Note note, NoteCallback<Note> noteCallback);

    //удалить заметку
    void deleteNote(Note note);
}
