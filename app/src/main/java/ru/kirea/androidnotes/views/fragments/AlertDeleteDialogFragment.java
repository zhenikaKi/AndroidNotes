package ru.kirea.androidnotes.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.db.models.Note;

public class AlertDeleteDialogFragment extends DialogFragment {
    public static String TAG = "AlertDeleteDialogFragment";

    private OnDialogButtonListener dialogButtonListener;
    private Note note;

    public static AlertDeleteDialogFragment newInstance(Note note, OnDialogButtonListener dialogButtonListener) {
        return new AlertDeleteDialogFragment(note, dialogButtonListener);
    }

    private AlertDeleteDialogFragment(Note note, OnDialogButtonListener dialogButtonListener) {
        this.note = note;
        this.dialogButtonListener = dialogButtonListener;
    }

    /*
    //оставил для себя: интерфейс можно брать из контекста.
    //Но в моем случае тут контекст от активити, а сам диалог запускается из фрагмента
    @Override
    public void onAttach(@NonNull Context context) {
        AppNotes.inLog("onAttach context = " + context);
        super.onAttach(context);
        if (context instanceof OnDialogButtonListener) {
            dialogButtonListener = (OnDialogButtonListener) context;
        }
    }

    @Override
    public void onDetach() {
        dialogButtonListener = null;
        super.onDetach();
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_title)
                .setMessage(String.format(getString(R.string.dialog_delete_message), note.getTitle()))
                .setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogButtonListener != null) {
                            dialogButtonListener.dialogButtonSuccess(note);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogButtonListener != null) {
                            dialogButtonListener.dialogButtonCancel();
                        }
                    }
                })
                .setCancelable(false)
                .create();
    }

    interface OnDialogButtonListener {
        void dialogButtonSuccess(Note note);
        void dialogButtonCancel();
    }
}
