package ru.kirea.androidnotes.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.kirea.androidnotes.R;
import ru.kirea.androidnotes.helpers.AuthHelper;
import ru.kirea.androidnotes.helpers.AuthHelper.AuthType;
import ru.kirea.androidnotes.helpers.Preferences;

public class AuthPresenter {
    public static final int RC_SIGN_IN = 400;

    private Fragment fragment;
    private Context context;
    private OnChaneSignListener onChaneSignListener;
    private GoogleSignInClient googleSignInClient;

    public AuthPresenter(Context context) {
        this.context = context;
    }

    public AuthPresenter(Fragment fragment) {
        this.fragment = fragment;

        if (fragment.getContext() != null) {
            context = fragment.getContext();
            googleSignInClient = AuthHelper.getInstance().getGoogleSignInClient(fragment.getContext());
        }
    }

    //авторизация в Google
    public void googleSignIn() {
        if (googleSignInClient != null) {
            fragment.startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }
    }

    //авторизация в vk
    public void vkSignIn() {
        VK.login(fragment.requireActivity(), Arrays.asList(VKScope.WALL, VKScope.PHOTOS));
    }

    //обработка ответа от всплывающих окон
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if (account != null) {
                        String text = String.format(fragment.getString(R.string.auth_success), account.getEmail());
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                        if (onChaneSignListener != null) {
                            onChaneSignListener.changeSigned();
                        }
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            } else {
                VKAuthCallback vkAuthCallback = new VKAuthCallback() {
                    @Override
                    public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                        String text = String.format(context.getString(R.string.auth_success), String.valueOf(vkAccessToken.getUserId()));
                        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                        Preferences.setSetting(context, Preferences.VK_TOKEN, vkAccessToken.getAccessToken());
                        Preferences.setSettingInt(context, Preferences.VK_USER_ID, vkAccessToken.getUserId());
                        if (onChaneSignListener != null) {
                            onChaneSignListener.changeSigned();
                        }
                    }

                    @Override
                    public void onLoginFailed(int i) {

                    }
                };

                VK.onActivityResult(requestCode, resultCode, data, vkAuthCallback);
            }
        }
    }

    //выйти из аккаунта
    public void signOut() {
        AuthType authType = AuthHelper.getInstance().getAuthType(context);
        if (authType == AuthType.GOOGLE) {
            if (googleSignInClient != null) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (onChaneSignListener != null) {
                            onChaneSignListener.changeSigned();
                        }
                    }
                });
            }
        } else  if (authType == AuthType.VK) {
            Preferences.setSetting(context, Preferences.VK_TOKEN, null);
            Preferences.setSettingInt(context, Preferences.VK_USER_ID, null);
            if (onChaneSignListener != null) {
                onChaneSignListener.changeSigned();
            }
        }
    }

    public void setOnChaneSignListener(OnChaneSignListener onChaneSignListener) {
        this.onChaneSignListener = onChaneSignListener;
    }

    public interface OnChaneSignListener {
        void changeSigned();
    }
}
