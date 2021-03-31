package ru.kirea.androidnotes.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.BuildConfig;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.AuthHelper;
import ru.kirea.androidnotes.presenters.AuthPresenter;

public class AboutFragment extends Fragment {

    private AuthPresenter authPresenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authPresenter = new AuthPresenter(this);
        authPresenter.setOnChaneSignListener(new AuthPresenter.OnChaneSignListener() {
            @Override
            public void changeSigned() {
                requireActivity().recreate();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_id);
        toolbar.setTitle(getString(R.string.menu_about));

        String title = String.format(getString(R.string.about_title), BuildConfig.VERSION_NAME);
        ((TextView) view.findViewById(R.id.about_title_id)).setText(title);
        TextView authTextType = view.findViewById(R.id.about_auth_type_id);
        TextView authEmail = view.findViewById(R.id.about_auth_email_id);

        //покажем инфу по авторизации
        Context context = getContext();
        boolean auth = false;
        if (context != null) {
            String info = AuthHelper.getInstance().getAuthInfo(context);
            AuthHelper.AuthType authType = AuthHelper.getInstance().getAuthType(context);
            if (info != null) {
                auth = true;
                authTextType.setText(getString(authType == AuthHelper.AuthType.GOOGLE ? R.string.button_google_sign : R.string.button_vk_sign));
                authEmail.setText(info);
            }
        }
        (view.findViewById(R.id.about_auth_info_id)).setVisibility(auth ? View.VISIBLE : View.GONE);

        //выход из аккаунта
        view.findViewById(R.id.about_auth_sign_out_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authPresenter.signOut();
            }
        });
    }
}
