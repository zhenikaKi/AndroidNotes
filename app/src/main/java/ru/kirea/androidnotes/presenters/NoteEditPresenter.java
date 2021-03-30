package ru.kirea.androidnotes.presenters;

import android.content.Context;
import android.content.res.Configuration;

import androidx.fragment.app.FragmentManager;
import ru.kirea.androidnotes.AppNotes;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.NoteCallback;
import ru.kirea.androidnotes.models.NotesService;

public class NoteEditPresenter {
    private Context context;
    private NoteEditView noteEditView;
    private NotesService notesService;

    public NoteEditPresenter(Context context, NoteEditView noteEditView) {
        this.context = context;
        this.noteEditView = noteEditView;
        notesService = AppNotes.getDBService(); //подключаемся к облачному хранилищу заметок

        notesService.init();
    }

    //получить конкретную заметку
    public void getNote(String id, NoteCallback<Note> noteCallback) {
        notesService.findNote(id, noteCallback);
    }

    //обработка выбора даты
    public void createDateClicked(FragmentManager fragmentManager, long dateTime) {
        showDateTimeDialog(fragmentManager, dateTime, true);
    }

    //обработка выбора времени
    public void createTimeClicked(FragmentManager fragmentManager, long dateTime) {
        showDateTimeDialog(fragmentManager, dateTime, false);
    }

    private void showDateTimeDialog(FragmentManager fragmentManager, long dateTime, boolean date) {
        DateTimeDialog dateTimeDialog = new DateTimeDialog(fragmentManager, dateTime);
        dateTimeDialog.setDateTimeListener(new DateTimeDialog.DateTimeListener() {
            @Override
            public void selectedDateTime(long dateTime) {
                noteEditView.showCreateDateTime(dateTime);
            }
        });

        if (date) {
            dateTimeDialog.showDate();
        } else {
            dateTimeDialog.showTime();
        }
    }

    public void save(String id, String title, String description, long createDate) {
        Note note = new Note(id, title, description, createDate);
        notesService.saveNote(note, new NoteCallback<Note>() {
            @Override
            public void onResult(Note value) {
                noteEditView.saved();
            }
        });
    }

    //проверить альбомная ли сейчас ориентация
    public boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
