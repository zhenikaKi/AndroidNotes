package ru.kirea.androidnotes.views.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return ;
        }

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(getIntent().getExtras());

        if (savedInstanceState == null ) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_note_info_id, fragment)
                    .commit();
        }
    }
}
