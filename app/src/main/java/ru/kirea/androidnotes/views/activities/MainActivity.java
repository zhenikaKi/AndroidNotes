package ru.kirea.androidnotes.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.AuthHelper;
import ru.kirea.androidnotes.presenters.AuthPresenter;
import ru.kirea.androidnotes.views.fragments.AuthFragment;
import ru.kirea.androidnotes.views.fragments.TabMainFragment;

public class MainActivity extends AppCompatActivity {

    private AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        AuthHelper.AuthType authType = AuthHelper.getInstance().getAuthType(this);

        if (savedInstanceState == null || authType == AuthHelper.AuthType.NONE || AuthHelper.getInstance().isStartedAuth()) {
            //проверим, авторизован ли пользователь
            Fragment fragment;
            if (authType != AuthHelper.AuthType.NONE) {
                fragment = new TabMainFragment(); //загрузим фрейм со списом заметок по умолчанию
                AuthHelper.getInstance().setStartedAuth(false);
            } else {
                fragment = new AuthFragment(); //загрузим фрейм авторизации
                authPresenter = new AuthPresenter(this);
                authPresenter.setOnChaneSignListener(new AuthPresenter.OnChaneSignListener() {
                    @Override
                    public void changeSigned() {
                        recreate();
                    }
                });
                AuthHelper.getInstance().setStartedAuth(true);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_main_id, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //вк отправляет ответ авторизации в activity, поэтому тут его перехватываем
        if (authPresenter != null) {
            authPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
