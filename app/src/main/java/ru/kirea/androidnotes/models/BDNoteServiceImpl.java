package ru.kirea.androidnotes.models;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.kirea.androidnotes.AppNotes;
import ru.kirea.androidnotes.db.dao.NoteDao;
import ru.kirea.androidnotes.db.models.Note;

//хранилище заметок в базе
public class BDNoteServiceImpl implements NotesService {
    private NoteDao noteDao;
    private ExecutorService executorService;

    @Override
    public void init() {
        noteDao = AppNotes.getInstance().getDatabase().noteDao();
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void getNotes(final Callback<List<Note>> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResult(noteDao.getNotes());
            }
        });
        executorService.shutdown();
    }

    @Override
    public void findNote(final String id, final Callback<Note> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResult(noteDao.getNoteOnId(id));
            }
        });
        executorService.shutdown();

    }

    @Override
    public void saveNote(final Note note, final Callback<Note> callback) {
        if (note == null) {
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //проверяем, есть ли заметка в базе
                Note checkNote = noteDao.getNoteOnId(note.getId());
                if (checkNote == null) {
                    noteDao.add(note); //заметки нет, добавляем
                } else {
                    noteDao.update(note); //заметка есть, обновляем ее
                }
                callback.onResult(note);
            }
        });
        executorService.shutdown();
    }

    @Override
    public void deleteNote(Note note) {
        noteDao.delete(note);
    }
}