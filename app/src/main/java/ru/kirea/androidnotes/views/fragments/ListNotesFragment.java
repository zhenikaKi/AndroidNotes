package ru.kirea.androidnotes.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.models.Note;
import ru.kirea.androidnotes.models.NoteClickable;
import ru.kirea.androidnotes.models.NotePublisher;
import ru.kirea.androidnotes.presenters.ListNotesAdapter;
import ru.kirea.androidnotes.presenters.NoteObserver;
import ru.kirea.androidnotes.presenters.NotePresenter;
import ru.kirea.androidnotes.presenters.NoteView;

public class ListNotesFragment extends Fragment implements NoteView, NoteObserver {

    private Context context;
    private NotePresenter notePresenter;
    private RecyclerView recyclerNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notePresenter = new NotePresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotePublisher.getInstance().add(this);
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        recyclerNotes = view.findViewById(R.id.recycler_notes);
        showNotes();
    }

    @Override
    public void runActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_note_info_id, fragment); // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void updateNotes() {
        showNotes();
    }

    //показать список заметок
    private void showNotes() {
        List<Note> notes = notePresenter.getNotes();

        ListNotesAdapter adapter = new ListNotesAdapter(notes);
        adapter.setNoteClickable(new NoteClickable() {
            @Override
            public void noteClick(Note note) {
                notePresenter.noteSelected(note.getId());
            }
        });
        recyclerNotes.setAdapter(adapter);

        recyclerNotes.setLayoutManager(new LinearLayoutManager(context));
        recyclerNotes.setItemAnimator(new DefaultItemAnimator());
        recyclerNotes.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
    }
}
