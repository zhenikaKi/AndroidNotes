package ru.kirea.androidnotes.views.fragments;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_id);
        toolbar.setTitle(getString(R.string.menu_about));

        //получим версию приложения
        PackageInfo pInfo;
        //получаем информацию по приложению
        try {
            pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
        } catch (Exception e) {
            pInfo = null;
        }
        String version = pInfo != null ? pInfo.versionName : "";

        String title = getString(R.string.about_title);
        title = String.format(title, version);
        ((TextView) view.findViewById(R.id.about_title_id)).setText(title);
    }
}
