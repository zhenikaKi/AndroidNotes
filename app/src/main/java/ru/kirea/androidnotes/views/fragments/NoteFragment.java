package ru.kirea.androidnotes.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.DateHelper;
import ru.kirea.androidnotes.models.Note;
import ru.kirea.androidnotes.presenters.NotePresenter;
import ru.kirea.androidnotes.presenters.NoteView;

public class NoteFragment extends Fragment implements NoteView {
    public static final String KEY_NOTE_ID = "noteId";

    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private TextInputLayout inputCreateDate;
    private EditText editTitle;
    private EditText editDescription;
    private EditText editCreateDate;
    private TextView updateDate;

    private long noteId;
    private NotePresenter notePresenter;
    
    public static NoteFragment newInstance(long noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getLong(KEY_NOTE_ID);
        }
        notePresenter = new NotePresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputTitle = view.findViewById(R.id.input_title_id);
        inputDescription = view.findViewById(R.id.input_description_id);
        inputCreateDate = view.findViewById(R.id.input_date_id);
        editTitle = view.findViewById(R.id.edit_title_id);
        editDescription = view.findViewById(R.id.edit_description_id);
        editCreateDate = view.findViewById(R.id.edit_date_id);
        updateDate = view.findViewById(R.id.update_value_id);
        showNote();
    }

    @Override
    public void runActivity(Intent intent) {
    }

    @Override
    public void updateFragment(Fragment fragment) {
    }

    //показать данные по заметке
    private void showNote() {
        Log.d("My", "noteId = " + noteId);
        Note note = notePresenter.getNote(noteId);
        Log.d("My", "note = " + note.toString());
        editTitle.setText(note.getTitle());
        editDescription.setText(note.getDescription());
        String date = DateHelper.timestampToString(note.getCreateDate(), DateHelper.DateFormat.DDMMYYYY_HHMM);
        editCreateDate.setText(date);
        date = note.getUpdateDate() == null ? "" : DateHelper.timestampToString(note.getUpdateDate(), DateHelper.DateFormat.DDMMYYYY_HHMM);
        updateDate.setText(date);
    }
}
