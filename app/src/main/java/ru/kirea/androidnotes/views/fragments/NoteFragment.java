package ru.kirea.androidnotes.views.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.DateHelper;
import ru.kirea.androidnotes.models.Note;
import ru.kirea.androidnotes.models.NotePublisher;
import ru.kirea.androidnotes.presenters.NoteEditPresenter;
import ru.kirea.androidnotes.presenters.NoteEditView;

public class NoteFragment extends Fragment implements NoteEditView {
    public static final String KEY_NOTE_ID = "noteId";

    private TextInputLayout inputTitle;
    private TextInputLayout inputDescription;
    private TextInputLayout inputCreateDate;
    private EditText editTitle;
    private EditText editDescription;
    private EditText editCreateDate;
    private EditText editCreateTime;
    private TextView updateDate;

    private long noteId;
    private NoteEditPresenter noteEditPresenter;
    
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
        noteEditPresenter = new NoteEditPresenter(getContext(), this);
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
        editCreateTime = view.findViewById(R.id.edit_time_id);
        updateDate = view.findViewById(R.id.update_value_id);

        initClicked(view);
        showNote();
    }

    //показать дату и время создания заметки
    @Override
    public void showCreateDateTime(long create) {
        String date = DateHelper.timestampToString(create, DateHelper.DateFormat.DDMMYYYY);
        editCreateDate.setText(date);
        date = DateHelper.timestampToString(create, DateHelper.DateFormat.HHMM);
        editCreateTime.setText(date);
        editCreateDate.setTag(R.id.tag_datetime_id, create);
    }

    @Override
    public void saved() {
        //информируем слушателей о том, что нужно обновить список заметок
        NotePublisher.getInstance().sendChange();

        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void initClicked(View view) {
        editCreateDate.setOnClickListener(onClickListener);
        editCreateTime.setOnClickListener(onClickListener);
        view.findViewById(R.id.button_save_id).setOnClickListener(onClickListener);
    }

    //обработка нажатий
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.edit_date_id) { //поле выбора даты создания
                long dateTime = v.getTag(R.id.tag_datetime_id) == null ? 0 : Long.parseLong(v.getTag(R.id.tag_datetime_id).toString());
                noteEditPresenter.createDateClicked(dateTime);
            } else if (id == R.id.edit_time_id) { //поле выбора времени создания
                long dateTime = v.getTag(R.id.tag_datetime_id) == null ? 0 : Long.parseLong(v.getTag(R.id.tag_datetime_id).toString());
                noteEditPresenter.createTimeClicked(dateTime);
            } else if (id == R.id.button_save_id) { //кнопка сохранения
                String title = editTitle.getText().toString();
                if (title.isEmpty()) {
                    inputTitle.setError(getString(R.string.title_null));
                    return;
                }

                String description = editDescription.getText().toString();
                if (description.isEmpty()) {
                    inputDescription.setError(getString(R.string.description_null));
                    return;
                }

                long createDate = editCreateDate.getTag(R.id.tag_datetime_id) == null ? 0 : Long.parseLong(editCreateDate.getTag(R.id.tag_datetime_id).toString());
                if (createDate == 0) {
                    inputCreateDate.setError(getString(R.string.create_null));
                    return;
                }

                noteEditPresenter.save(noteId, title, description, createDate);
            }
        }
    };

    //показать данные по заметке
    private void showNote() {
        Note note = noteEditPresenter.getNote(noteId);
        editTitle.setText(note.getTitle());
        editDescription.setText(note.getDescription());
        showCreateDateTime(note.getCreateDate());
        String date = note.getUpdateDate() == null ? "" : DateHelper.timestampToString(note.getUpdateDate(), DateHelper.DateFormat.DDMMYYYY_HHMM);
        updateDate.setText(date);
    }
}
