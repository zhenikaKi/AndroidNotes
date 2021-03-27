package ru.kirea.androidnotes.presenters;

import android.content.Context;
import android.content.res.Configuration;

import androidx.fragment.app.FragmentManager;
import ru.kirea.androidnotes.AppNotes;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.BDNoteServiceImpl;
import ru.kirea.androidnotes.models.NotesService;

public class NoteEditPresenter {
    private Context context;
    private NoteEditView noteEditView;
    private NotesService notesService;

    public NoteEditPresenter(Context context, NoteEditView noteEditView) {
        this.context = context;
        this.noteEditView = noteEditView;
        //notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок
        notesService = new BDNoteServiceImpl(); //подключаемся к хранилищу заметок в базе

        notesService.init();
    }

    //получить конкретную заметку
    public Note getNote(long id) {
        return notesService.findNote(id);
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

    public void save(long id, String title, String description, long createDate) {
        Note note = new Note(id, title, description, createDate);
        notesService.saveNote(note);
        noteEditView.saved();
    }

    //проверить альбомная ли сейчас ориентация
    public boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
