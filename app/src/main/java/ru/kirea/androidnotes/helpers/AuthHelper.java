package ru.kirea.androidnotes.helpers;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AuthHelper {

    public enum AuthType {NONE, GOOGLE, VK}
    public final static String KEY_CHANGE_SIGNED = "changeSigned";


    private static AuthHelper instance = new AuthHelper();
    private GoogleSignInClient googleSignInClient;
    private boolean startedAuth = false;

    private AuthHelper() {
    }

    public static AuthHelper getInstance() {
        return instance;
    }

    //проверить тип авторизации
    public AuthType getAuthType(Context context) {
        if (context == null) {
            return AuthType.NONE;
        }
        Preferences preferences = new Preferences(context);

        //проверяем google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            return AuthType.GOOGLE;
        }

        //проверяем вк
        String vkToken = preferences.getVkToken();
        if (vkToken != null) {
            return AuthType.VK;
        }

        return AuthType.NONE;
    }

    //получить клиента от гугла
    public GoogleSignInClient getGoogleSignInClient(Context context) {
        if (googleSignInClient == null) {
            initGoogleSignInClient(context);
        }
        return googleSignInClient;
    }

    //получить инфу по авторизации
    public String getAuthInfo(Context context) {
        AuthType authType = getAuthType(context);
        Preferences preferences = new Preferences(context);
        switch (authType) {
            case GOOGLE:
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
                if (account != null) {
                    return account.getEmail();
                }
            case VK:
                int vkUserId = preferences.getVkUserId();
                if (vkUserId != 0) {
                    return "id" + vkUserId;
                }
        }

        return null;
    }

    public boolean isStartedAuth() {
        return startedAuth;
    }

    public void setStartedAuth(boolean startedAuth) {
        this.startedAuth = startedAuth;
    }

    private void initGoogleSignInClient(Context context) {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить идентификатор пользователя, его почту и основной профайл
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }
}
