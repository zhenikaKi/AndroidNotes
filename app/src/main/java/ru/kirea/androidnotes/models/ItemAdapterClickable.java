package ru.kirea.androidnotes.models;

import android.view.View;

public interface ItemAdapterClickable {
    void itemClick(int position);

    void itemMenuClick(View v, int position);
}
