package ru.kirea.androidnotes.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.db.models.ItemType;
import ru.kirea.androidnotes.db.models.Note;
import ru.kirea.androidnotes.models.NoteViewModel;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

public class NotePresenter {
    private final String KEY_SELECTED_NOTE_ID = "selectedNoteId";

    private Context context;
    private NoteView noteView;
    private NoteViewModel noteViewModel;

    private String selectedNoteId;

    private NotePresenter() {
    }

    public NotePresenter(Fragment fragment, NoteView noteViewInit) {
        this.context = fragment.getContext();
        this.noteView = noteViewInit;

        noteViewModel = new ViewModelProvider(fragment).get(NoteViewModel.class);
        noteViewModel.getNotes(); //сразу запускаем формирование списка заметок
    }

    //включить слушателя изменений заметок
    public void startObserve(LifecycleOwner lifecycleOwner) {
        noteViewModel.getNotesLiveData().observe(lifecycleOwner, new Observer<List<ItemType>>() {
            @Override
            public void onChanged(List<ItemType> notes) {



                noteView.showNotes(notes);
            }
        });
    }

    //выбор заметки из общего списка
    public void noteSelected(String noteId) {
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
            outState.putString(KEY_SELECTED_NOTE_ID, selectedNoteId);
        }
    }

    //загрузить настройки
    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String savedNoteId = savedInstanceState.getString(KEY_SELECTED_NOTE_ID);
            if (savedNoteId != null) {
                noteSelected(savedNoteId);
            }
        }
    }

    //добавление новой заметки
    public void addNote() {
        noteSelected(null);
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
    public void getNotes() {
        noteViewModel.getNotes();
    }

    //удалить заметку
    public void delete(Note note) {
        noteViewModel.getNotesService().deleteNote(note);
        noteViewModel.getNotes();
    }
}
