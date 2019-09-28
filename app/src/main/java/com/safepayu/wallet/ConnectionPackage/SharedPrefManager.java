package com.safepayu.wallet.ConnectionPackage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.safepayu.wallet.model.User;
import com.safepayu.wallet.MainActivity;
import com.safepayu.wallet.model.LoginUser;

import java.security.acl.LastOwnerException;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_FULLNAME = "keyusername";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE = "phone";
    private static final String KEY_CITY = "alterphone";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_Address = "keyaddress";
    private static final String KEY_DOB = "dob";
    private static final String KEY_DATE = "date";
    private static final String KEY_SEX = "sex";
    private static final String KEY_PINCODE= "pincode";
    private static final String KEY_STATE= "STATE";
    private static final String KEY_COUNTRY= "CONTRY";
    private static final String KEY_REFERRALCODE= "REFERRALCODE";
    private static final String FIRSR_NAMW= "firstname";
    private static final String LAST_NAME= "lastname";
    private static final String EMAIL= "email";
    private static final String MOBILE= "mobile";
    private static final String DOB= "dob";
    private static final String REFERAL= "referal";
    private static final String PASSCODE= "passcode";


    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    public void userLogin(LoginUser user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUserid());
        editor.putString(FIRSR_NAMW,user.getFirst_name());
        editor.putString(LAST_NAME,user.getLast_name());
        editor.putString(EMAIL,user.getEmail());
        editor.putString(MOBILE,user.getMobile());
        editor.putString(DOB,user.getDob());
        editor.putString(REFERAL,user.getReferral_code());
        editor.putString(PASSCODE,user.getPasscode());

        editor.apply();
    }
    public void walletAmount(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.apply();
    }
    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILE, null) != null;
    }

    //this method will give the logged in user
    public LoginUser getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
         LoginUser loginUser=new LoginUser();
         loginUser.setUserid(sharedPreferences.getString(KEY_ID, ""));
         loginUser.setFirst_name(sharedPreferences.getString(FIRSR_NAMW, ""));
         loginUser.setLast_name(sharedPreferences.getString(LAST_NAME, ""));
         loginUser.setEmail(sharedPreferences.getString(EMAIL, ""));
         loginUser.setMobile(sharedPreferences.getString(MOBILE, ""));
         loginUser.setDob(sharedPreferences.getString(DOB, ""));
         loginUser.setReferral_code(sharedPreferences.getString(REFERAL, ""));
         loginUser.setPasscode(sharedPreferences.getString(PASSCODE, ""));




        return loginUser;
    }
    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
      //  MainActivity mn=new MainActivity();
        editor.clear();
        editor.apply();
//        mn.visibleLoginSection();
      //mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }
    public void logoutprofile() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
       // mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }

    public void setBoolean(String key,Boolean value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public Boolean getBoolean(String key){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getBoolean(key,false);
    }
    public void setString(String key,String value){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getString(String key){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
        //        editor.apply();
        return sharedPreferences.getString(key,null);

    }

}
