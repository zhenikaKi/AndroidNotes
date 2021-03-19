package ru.kirea.androidnotes.presenters;

import androidx.fragment.app.Fragment;

public interface NoteView {

    //показать фрейм в основном контейнере
    void showFragmentInMain(Fragment fragment);

    //показать заметку в альбомной ориентации
    void showFragmentInLandscape(Fragment fragment);
}
