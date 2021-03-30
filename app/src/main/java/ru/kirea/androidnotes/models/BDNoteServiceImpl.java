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

    @Override
    public void init() {
        noteDao = AppNotes.getInstance().getDatabase().noteDao();
    }

    @Override
    public void getNotes(final NoteCallback<List<Note>> noteCallback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                noteCallback.onResult(noteDao.getNotes());
            }
        });
        executorService.shutdown();
    }

    @Override
    public void findNote(final String id, final NoteCallback<Note> noteCallback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                noteCallback.onResult(noteDao.getNoteOnId(id));
            }
        });
        executorService.shutdown();

    }

    @Override
    public void saveNote(final Note note, final NoteCallback<Note> noteCallback) {
        if (note == null) {
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //проверяем, есть ли заметка в базе
                Note checkNote = noteDao.getNoteOnId(note.getId());
                if (checkNote == null) {
                    noteDao.add(note); //заметки нет, добавляем
                } else {
                    noteDao.update(note); //заметка есть, обновляем ее
                }
                noteCallback.onResult(note);
            }
        });
        executorService.shutdown();
    }

    @Override
    public void deleteNote(Note note) {
        noteDao.delete(note);
    }
}