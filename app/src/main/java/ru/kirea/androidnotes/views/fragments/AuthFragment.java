package ru.kirea.androidnotes.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.models.AuthListener;
import ru.kirea.androidnotes.presenters.AuthPresenter;

public class AuthFragment extends Fragment {
    private AuthPresenter authPresenter;
    private AuthListener authListener;

    public static AuthFragment newInstance(AuthListener authListener) {
        AuthFragment authFragment = new AuthFragment();
        authFragment.setAuthListener(authListener);
        return authFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authPresenter = new AuthPresenter(this);
        authPresenter.setOnChaneSignListener(new AuthPresenter.OnChaneSignListener() {
            @Override
            public void changeSigned() {
                if (authListener != null) {
                    authListener.chaneSign();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButton(view);
    }

    public void setAuthListener(AuthListener authListener) {
        this.authListener = authListener;
    }

    private void initButton(View view) {
        MaterialButton buttonSignIn = view.findViewById(R.id.button_google_sign_id);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authPresenter.googleSignIn();
            }
        });

        MaterialButton buttonVkSignIn = view.findViewById(R.id.button_vk_sign_id);
        buttonVkSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authPresenter.vkSignIn();
            }
        });
    }
}
