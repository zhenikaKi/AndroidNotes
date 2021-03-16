package ru.kirea.androidnotes.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.views.fragments.NoteFragment;

import android.content.res.Configuration;
import android.os.Bundle;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Bundle bundle = getIntent().getExtras();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || bundle == null) {
            finish();
            return ;
        }

        if (savedInstanceState == null ) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_note_info_id, NoteFragment.newInstance(bundle.getLong(NoteFragment.KEY_NOTE_ID)))
                    .commit();
        }
    }
}
