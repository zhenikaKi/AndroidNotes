package ru.kirea.androidnotes.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.views.fragments.TabMainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        //загрузим фрейм со списом заметок по умолчанию
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_main_id, new TabMainFragment());
            fragmentTransaction.commit();
        }
    }
}
