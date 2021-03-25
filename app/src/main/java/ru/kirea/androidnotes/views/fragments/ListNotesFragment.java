package ru.kirea.androidnotes.views.fragments;

import android.app.Activity;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
        super.onCreate(savedInstanceState);
        notePresenter = new NotePresenter(this, this);

        if (notePresenter.isLandscape()) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        //создаем меню сверху
        setHasOptionsMenu(true);
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

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_id);
        toolbar.setTitle(getString(R.string.menu_notes));

        notePresenter.startObserve(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        notePresenter.saveInstanceState(outState);
        super .onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        notePresenter.restoreInstanceState(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showFragmentInMain(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main_id, fragment); // замена фрагмента
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showFragmentInLandscape(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_tab_main_info_id, fragment); // замена фрагмента
        fragmentTransaction.commit();
    }

    @Override
    public void updateNotes() {
        notePresenter.getNotes();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_list_nites, menu);
    }

    //обработка меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_id) { //добавление новой заметки
            notePresenter.addNote();
        }

        return true;
    }

    //показать список заметок
    @Override
    public void showNotes(List<Note> notes) {
        ListNotesAdapter adapter = new ListNotesAdapter(notes);
        adapter.setNoteClickable(new NoteClickable() {
            @Override
            public void noteClick(Note note) {
                notePresenter.noteSelected(note.getId());
            }

            @Override
            public void noteMenuClick(View view, final Note note) {
                Activity activity = requireActivity();
                PopupMenu popupMenu = new PopupMenu(activity, view);
                activity.getMenuInflater().inflate(R.menu.popup_menu_note, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_copy_id: //скопировать текст
                                notePresenter.copyText(note);
                                break;
                            case R.id.menu_delete_id: //удалить заметку
                                notePresenter.delete(note);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        recyclerNotes.setAdapter(adapter);

        recyclerNotes.setLayoutManager(new LinearLayoutManager(context));
        recyclerNotes.setItemAnimator(new DefaultItemAnimator());
        //recyclerNotes.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
    }
}
