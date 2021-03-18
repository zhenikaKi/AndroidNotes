package ru.kirea.androidnotes.presenters;

import android.content.Context;

import ru.kirea.androidnotes.models.LocalNotesServiceImpl;
import ru.kirea.androidnotes.models.Note;
import ru.kirea.androidnotes.models.NotesService;

public class NoteEditPresenter {
    private Context context;
    private NoteEditView noteEditView;
    private NotesService notesService;

    public NoteEditPresenter(Context context, NoteEditView noteEditView) {
        this.context = context;
        this.noteEditView = noteEditView;
        notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок

        notesService.init();
    }

    //получить конкретную заметку
    public Note getNote(long id) {
        return notesService.findNote(id);
    }

    //обработка выбора даты
    public void createDateClicked(long dateTime) {
        showDateTimeDialog(dateTime, true);
    }

    //обработка выбора времени
    public void createTimeClicked(long dateTime) {
        showDateTimeDialog(dateTime, false);
    }

    private void showDateTimeDialog(long dateTime, boolean date) {
        DateTimeDialog dateTimeDialog = new DateTimeDialog(context, dateTime);
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
}
