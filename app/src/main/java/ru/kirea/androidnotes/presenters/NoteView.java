package ru.kirea.androidnotes.presenters;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface NoteView {

    //открыть новое окно
    void runActivity(Intent intent);

    //обновить фрагмент с заметкой
    void updateFragment(Fragment fragment);
}
