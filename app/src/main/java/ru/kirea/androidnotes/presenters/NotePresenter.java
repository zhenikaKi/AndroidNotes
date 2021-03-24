package ru.kirea.androidnotes.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.BDNoteServiceImpl;
import ru.kirea.androidnotes.models.NotesService;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

public class NotePresenter extends ViewModel {
    private final String KEY_SELECTED_NOTE_ID = "selectedNoteId";

    private Context context;
    private NoteView noteView;
    private NotesService notesService;

    private final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();

    private Long selectedNoteId;

    public static NotePresenter builder(Fragment fragment, NoteView noteView) {
        NotePresenter notePresenter = new ViewModelProvider(fragment).get(NotePresenter.class);
        notePresenter.setContext(fragment.getContext());
        notePresenter.setNoteView(noteView);

        //инициализируем БД
        notePresenter.initBD();

        return notePresenter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    //получить список заметок в отдельном потоке
    public void getNotes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                notesLiveData.postValue(notesService.getNotes());
            }
        }).start();
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

    //добавление новой заметки
    public void addNote() {
        noteSelected(0);
    }

    //скопировать текст заметки
    public void copyText(Note note) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", note.getDescription());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
        }
    }

    //удалить заметку
    public void delete(Note note) {
        notesService.deleteNote(note);
        getNotes();
    }

    public LiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private void setNoteView(NoteView noteView) {
        this.noteView = noteView;
    }

    //инициализация сервиса по работе с хранилищем
    private void initBD() {
        //notesService = new LocalNotesServiceImpl(); //подключаемся к локальному хранилищу заметок
        notesService = new BDNoteServiceImpl(); //подключаемся к хранилищу заметок в базе
        notesService.init();
    }
}
