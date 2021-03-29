package ru.kirea.androidnotes.presenters;

import java.util.List;

import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.db.models.ItemType;

public interface NoteView {

    //показать фрейм в основном контейнере
    void showFragmentInMain(Fragment fragment);

    //показать заметку в альбомной ориентации
    void showFragmentInLandscape(Fragment fragment);

    //обновить список заметок
    void showNotes(List<ItemType> notes);
}
