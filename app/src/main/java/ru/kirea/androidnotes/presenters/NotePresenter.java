package ru.kirea.androidnotes.presenters;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.List;

import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.BDNoteServiceImpl;
import ru.kirea.androidnotes.models.NotesService;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

public class NotePresenter {
    private final String KEY_SELECTED_NOTE_ID = "selectedNoteId";

    private Context context;
    private NoteView noteView;
    private NotesService notesService;

    private Long selectedNoteId;

    public NotePresenter(Context context, NoteView noteView) {
        this.context = context;
        this.noteView = noteView;
        //notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок
        notesService = new BDNoteServiceImpl(); //подключаемся к хранилищу заметок в базе

        notesService.init();
    }

    //получить список заметок
    public List<Note> getNotes() {
        return notesService.getNotes();
    }

    //выбор заметки из общего списка
    public void noteSelected(long noteId) {
        selectedNoteId = noteId;
        if (isLandscape()) { //уведомляем фрагмент о том, что ему надо сбоку показать информацию по заметке
            noteView.showFragmentInLandscape(NoteFragment.newInstance(noteId));
        } else { //уведомляем фрагмент о том, что он должен в новом окне открыть информацию о заметке
            noteView.showFragmentInMain(NoteFragment.newInstance(noteId));
        }
    }

    //проверить альбомная ли сейчас ориентация
    public boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    //сохранить настройки
    public void saveInstanceState(Bundle outState) {
        if (selectedNoteId != null) {
            outState.putLong(KEY_SELECTED_NOTE_ID, selectedNoteId);
        }
    }

    //загрузить настройки
    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            long savedNoteId = savedInstanceState.getLong(KEY_SELECTED_NOTE_ID, 0);
            if (savedNoteId > 0) {
                noteSelected(savedNoteId);
            }
        }
    }
}
