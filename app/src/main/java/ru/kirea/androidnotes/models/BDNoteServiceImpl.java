package ru.kirea.androidnotes.models;

import java.util.List;

import ru.kirea.androidnotes.AppNotes;
import ru.kirea.androidnotes.db.dao.NoteDao;
import ru.kirea.androidnotes.db.models.Note;

//хранилище заметок в базе
public class BDNoteServiceImpl implements NotesService {
    private NoteDao noteDao;

    @Override
    public void init() {
        noteDao = AppNotes.getInstance().getDatabase().noteDao();
    }

    @Override
    public void getNotes(Callback<List<Note>> callback) {
        callback.onResult(noteDao.getNotes());
    }

    @Override
    public Note findNote(String id) {
        return noteDao.getNoteOnId(id);
    }

    @Override
    public void saveNote(Note note) {
        if (note == null) {
            return;
        }

        //проверяем, есть ли заметка в базе
        Note checkNote = noteDao.getNoteOnId(note.getId());
        if (checkNote == null) {
            noteDao.add(note); //заметки нет, добавляем
        } else {
            noteDao.update(note); //заметка есть, обновляем ее
        }
    }

    @Override
    public void deleteNote(Note note) {
        noteDao.delete(note);
    }
}
