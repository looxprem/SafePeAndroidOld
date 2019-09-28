package com.safepayu.wallet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.WalletSharedPrefManager;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;

import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.Results;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.WalletModel;

public class MainActivity extends AppCompatActivity{
    TextView sign_in;
    ImageView pass_visible,pass_invisible;
    TextView registration,forgot_password;
    ProgressBar progressbar;
    String number="",str_edit_password="";
    EditText editmobile,edit_password;
    String rememberMobile,remember_password;
    ArrayList<Results> data;

    public String getAppVersion(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this,Homepage.class));
        }*/
      //  unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        if(!isNetworkAvailable()){
            Toast.makeText(this.getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
        }
        sign_in=(TextView)findViewById(R.id.signin);
        editmobile=(EditText)findViewById(R.id.edit_mobile);
        edit_password=(EditText)findViewById(R.id.edit_password);
        registration=(TextView)findViewById(R.id.register);
        forgot_password=(TextView)findViewById(R.id.forgot);
        progressbar = (ProgressBar)findViewById(R.id.progressBar1);
        pass_visible=(ImageView)findViewById(R.id.password_visible);
        pass_invisible=(ImageView)findViewById(R.id.password_invisible);

        //rememberPassword();
        pass_visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
                pass_visible.setVisibility(View.GONE);
                pass_invisible.setVisibility(View.VISIBLE);
            }
        });
        pass_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                pass_visible.setVisibility(View.VISIBLE);
                pass_invisible.setVisibility(View.GONE);
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=editmobile.getText().toString().trim();
                CheckMobileExists check = new CheckMobileExists();
                check.execute();

                // //startActivity(new Intent(getApplicationContext(),Homepage.class));
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });
    }
    void rememberPassword(){
        RememberPassword rememberPassword=SharedPrefManagerLogin.getInstance(this).getUser();
        rememberMobile=rememberPassword.getMobile();
        remember_password=rememberPassword.getPassword();
        if(rememberMobile!=null){
            editmobile.setText(rememberMobile);
        }
        if(rememberPassword!=null){
            edit_password.setText(remember_password);
        }
    }
    public static boolean validMobile(String in){
        return  in.length()==10 &&android.util.Patterns.PHONE.matcher(in).matches();
    }
   private void  signIn(){

        str_edit_password= edit_password.getText().toString().trim();
        System.out.println("number-: "+number);

        if (!validMobile(number)) {
            editmobile.setError("Enter a valid mobile Number");
            editmobile.requestFocus();
            return;
        }
       if (TextUtils.isEmpty(str_edit_password)) {
           edit_password.setError("Enter a valid password");
           edit_password.requestFocus();
           return;
       }
      RememberPassword rememberPassword=new RememberPassword(number,str_edit_password);
       SharedPrefManagerLogin.getInstance(getApplicationContext()).rememberLogin(rememberPassword);


        class UserLogin extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressbar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", number);
                params.put("password", str_edit_password);
                params.put("appversion", getAppVersion());
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getInt("appversion")==1) {
                        if (obj.getString("status").equals("Success")) {

                            JSONArray userJson = obj.getJSONArray("msg");
                            LoginUser user = null;
                            for (int i = 0; i < userJson.length(); i++) {
                                JSONObject jsonObject = userJson.getJSONObject(i);
                                user = new LoginUser();
                                user.setUserid(jsonObject.optString("userid"));
                                user.setFirst_name(jsonObject.optString("first_name"));
                                user.setLast_name(jsonObject.optString("last_name"));
                                user.setEmail(jsonObject.optString("email"));
                                user.setMobile(jsonObject.optString("mobile"));
                                user.setDob(jsonObject.optString("dob"));
                                user.setReferral_code(jsonObject.optString("referral_code"));
                                user.setPasscode(jsonObject.optString("passcode"));

                            }


                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            Log.e("userid=", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());

                            GetWalletDetails getWalletDetails = new GetWalletDetails();
                            getWalletDetails.execute();

                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {
                            System.out.println("elseeeeeeeeeee");
                            Toast.makeText(getApplicationContext(), "Invalid Credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        appInstallDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
    }

    public  void appInstallDialog(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Install update")
                .setMessage("Application Update is available ")
                .setPositiveButton("UPDATE NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        finish();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();*/
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history) {
            startActivity(new Intent(MainActivity.this,WinnerResultHisory.class));
            return true;
        }
        else  if(id == R.id.share){
            Intent intent=new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"X90 Entertaiment game ");

            String shareMessage="still using App for game, just download X90 http://bit.ly/2obnPHZ";

            intent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
            startActivity(Intent.createChooser(intent,"Sharing via"));
            // mShareActionProvider = (ShareActionProvider) item.getActionProvider();
            return true;
        }
        if (id == R.id.update) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.update_alert);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/2obnPHZ")));
                }
            });
            builder.setNegativeButton("cancel", null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    class CheckMobileExists extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            System.out.println("Number: -"+number);
            params.put("mobile", number);

            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressbar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    signIn();

                } else {

                     Toast.makeText(getApplicationContext(), "User Not Registered", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());
            // params.put("order_amount", amoutPaid);
            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_WALLETDETAILS, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");

                    WalletModel wallet = new WalletModel(jsonObject.optString("wallet_id"), jsonObject.optString("userid"),
                            jsonObject.optString("amount"), "active");
                    WalletSharedPrefManager.getInstance(getApplicationContext()).saveWalletDetails(wallet);

                    Toast.makeText(getApplicationContext(), "Success Wallet Data", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), Navigation.class));
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Error Login user.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


}
