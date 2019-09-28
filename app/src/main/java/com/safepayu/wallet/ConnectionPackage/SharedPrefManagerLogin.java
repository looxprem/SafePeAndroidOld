package com.safepayu.wallet.ConnectionPackage;

import android.content.Context;
import android.content.SharedPreferences;

import com.safepayu.wallet.model.RememberPassword;

public class SharedPrefManagerLogin {
    private static final String SHARED_PREFR_NAME = "rememberPassword";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_PASSWORD = "password";

    private static SharedPrefManagerLogin loginInstance;
    private static Context loginCtx;
    private SharedPrefManagerLogin(Context context) {
        loginCtx = context;
    }
    public static synchronized SharedPrefManagerLogin getInstance(Context context) {
        if (loginInstance == null) {
            loginInstance = new SharedPrefManagerLogin(context);
        }
        return loginInstance;
    }
    public void rememberLogin(RememberPassword login) {
        SharedPreferences sharedPreferences = loginCtx.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOBILE,login.getMobile() );
        editor.putString(KEY_PASSWORD, login.getPassword());
        editor.apply();
    }
    //this method will give the logged in user
    public RememberPassword getUser() {
        SharedPreferences sharedPreferences = loginCtx.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        return new RememberPassword(
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

    public void changePassword(String newPassword){
        SharedPreferences sharedPreferences = loginCtx.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASSWORD,newPassword);
        editor.apply();
    }


}
