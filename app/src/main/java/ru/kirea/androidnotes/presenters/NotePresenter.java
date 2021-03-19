package ru.kirea.androidnotes.presenters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.List;

import ru.kirea.androidnotes.models.BDNoteServiceImpl;
import ru.kirea.androidnotes.models.LocalNotesServiceImpl;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.NotesService;
import ru.kirea.androidnotes.views.activities.NoteActivity;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

public class NotePresenter {
    private Context context;
    private NoteView noteView;
    private NotesService notesService;

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
        if (isLandscape()) { //уведомляем фрагмент о том, что ему надо сбоку показать информацию по заметке
            noteView.updateFragment(NoteFragment.newInstance(noteId));
        } else { //уведомляем фрагмент о том, что он должен в новом окне открыть информацию о заметке
            Intent intent = new Intent();
            intent.setClass(context, NoteActivity.class);
            intent.putExtra(NoteFragment.KEY_NOTE_ID, noteId);
            noteView.runActivity(intent);
        }
    }

    //проверить альбомная ли сейчас ориентация
    private boolean isLandscape() {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
