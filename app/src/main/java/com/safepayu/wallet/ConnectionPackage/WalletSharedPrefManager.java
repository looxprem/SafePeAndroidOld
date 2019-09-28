package com.safepayu.wallet.ConnectionPackage;

import android.content.Context;
import android.content.SharedPreferences;

import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.WalletModel;

public class WalletSharedPrefManager {

    private static final String SHARED_PREFR_NAME = "wallet";
    private static final String KEY_WALLET_AMOUNT = "null";
    private static final String KEY_WALLET_ID = "null";
    private static final String KEY_STATUS = "inactive";
    private static final String KEY_USERID = "user";

    private static WalletSharedPrefManager walletInstance;
    private static Context walletContext;

    private WalletSharedPrefManager(Context context) {
        walletContext = context;
    }
    public static synchronized WalletSharedPrefManager getInstance(Context context) {
        if (walletInstance == null) {
            walletInstance = new WalletSharedPrefManager(context);
        }
        return walletInstance;
    }
    public void saveWalletDetails(WalletModel wallet) {
        SharedPreferences sharedPreferences = walletContext.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_WALLET_ID,wallet.getWalletId());
        editor.putString(KEY_USERID,wallet.getUserId());
        editor.putString(KEY_WALLET_AMOUNT,wallet.getWalletAmount());
        editor.putString(KEY_STATUS,wallet.getStatus());
        editor.apply();
    }

    public void  updateWalletAmount(String newWalletAmount){
        SharedPreferences sharedPreferences = walletContext.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_WALLET_AMOUNT, newWalletAmount);
        editor.apply();

    }


    public WalletModel getWalletDetails() {
        SharedPreferences sharedPreferences = walletContext.getSharedPreferences(SHARED_PREFR_NAME, Context.MODE_PRIVATE);
        String walletId = sharedPreferences.getString(KEY_WALLET_ID, "");
        String walletAmount = sharedPreferences.getString(KEY_WALLET_AMOUNT, "");
        String userId = sharedPreferences.getString(KEY_USERID, "");
        String walletStatus = sharedPreferences.getString(KEY_STATUS, "");
        WalletModel wallet = new WalletModel(walletId, walletAmount, userId, walletStatus);
        return wallet;
    }

}
