package ru.kirea.androidnotes.presenters;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.List;

import ru.kirea.androidnotes.db.models.ItemType;

public interface NoteView {

    //показать фрейм в основном контейнере
    void showFragmentInMain(BottomSheetDialogFragment fragment);

    //показать заметку в альбомной ориентации
    void showFragmentInLandscape(BottomSheetDialogFragment fragment);

    //обновить список заметок
    void showNotes(List<ItemType> notes);
}
