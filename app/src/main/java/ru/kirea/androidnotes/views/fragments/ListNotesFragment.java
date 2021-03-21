package ru.kirea.androidnotes.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.kirea.androidnotes.AppNotes;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.db.models.Note;
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
        AppNotes.inLog("ListNotesFragment.onCreate savedInstanceState = " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        super.onCreate(savedInstanceState);
        notePresenter = new NotePresenter(getContext(), this);

        /*if (notePresenter.isLandscape()) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }*/

        //создаем меню сверху
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppNotes.inLog("ListNotesFragment.onCreateView savedInstanceState = " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        NotePublisher.getInstance().add(this);
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppNotes.inLog("ListNotesFragment.onViewCreated savedInstanceState = " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        recyclerNotes = view.findViewById(R.id.recycler_notes);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_id);
        toolbar.setTitle(getString(R.string.menu_notes));
        //((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        showNotes();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        AppNotes.inLog("ListNotesFragment.onSaveInstanceState outState = " + outState.toString());
        notePresenter.saveInstanceState(outState);
        super .onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        AppNotes.inLog("ListNotesFragment.onActivityCreated savedInstanceState = " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        notePresenter.restoreInstanceState(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showFragmentInMain(Fragment fragment) {
        AppNotes.inLog("ListNotesFragment.showFragmentInMain");
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main_id, fragment); // замена фрагмента
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showFragmentInLandscape(Fragment fragment) {
        AppNotes.inLog("ListNotesFragment.showFragmentInLandscape");
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_tab_main_info_id, fragment); // замена фрагмента
        fragmentTransaction.commit();
    }

    @Override
    public void updateNotes() {
        AppNotes.inLog("ListNotesFragment.updateNotes");
        showNotes();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        AppNotes.inLog("ListNotesFragment.onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_fragment_list_nites, menu);
    }

    //обработка меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AppNotes.inLog("ListNotesFragment.onOptionsItemSelected");
        int id = item.getItemId();
        if (id == R.id.menu_add_id) { //добавление новой заметки
            notePresenter.addNote();
        }

        return true;
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
