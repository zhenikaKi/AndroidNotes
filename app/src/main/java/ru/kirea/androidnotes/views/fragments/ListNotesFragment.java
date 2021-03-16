package ru.kirea.androidnotes.views.fragments;

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
import ru.kirea.androidnotes.presenters.ListNotesAdapter;
import ru.kirea.androidnotes.presenters.NotePresenter;
import ru.kirea.androidnotes.presenters.NoteView;

public class ListNotesFragment extends Fragment implements NoteView {

    private NotePresenter notePresenter;
    private RecyclerView recyclerNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notePresenter = new NotePresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    //показать список заметок
    private void showNotes() {
        List<Note> notes = notePresenter.getNotes();

        ListNotesAdapter adapter = new ListNotesAdapter(notes);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag(R.id.tag_item_id) != null) {
                    long noteId = Long.parseLong(v.getTag(R.id.tag_item_id).toString());
                    notePresenter.noteSelected(noteId);
                }
            }
        });
        recyclerNotes.setAdapter(adapter);

        recyclerNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNotes.setItemAnimator(new DefaultItemAnimator());
        recyclerNotes.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }
}
