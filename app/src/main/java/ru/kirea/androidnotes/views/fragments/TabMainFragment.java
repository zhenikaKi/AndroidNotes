package ru.kirea.androidnotes.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.models.AuthListener;

public class TabMainFragment extends Fragment {
    private AuthListener authListener;

    public static TabMainFragment newInstance(AuthListener authListener) {
        TabMainFragment authFragment = new TabMainFragment();
        authFragment.setAuthListener(authListener);
        return authFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initButton(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_tab_main_id, new ListNotesFragment());
            fragmentTransaction.commit();
        }
    }

    private void initButton(View view) {
        //обработчик вкладок
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.menu_setting_id: //настройки приложения
                        fragment = new SettingsFragment();
                        break;
                    case R.id.menu_about_id: //о приложении
                        fragment = AboutFragment.newInstance(authListener);
                        break;
                    default:
                        fragment = new ListNotesFragment();
                }

                item.setChecked(true);

                //обновляем экран
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_tab_main_id, fragment);
                fragmentTransaction.commit();

                return false;
            }
        });
    }

    public void setAuthListener(AuthListener authListener) {
        this.authListener = authListener;
    }
}
