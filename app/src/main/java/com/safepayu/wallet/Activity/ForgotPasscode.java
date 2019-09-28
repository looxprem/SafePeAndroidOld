package com.safepayu.wallet.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class ForgotPasscode extends AppCompatActivity {

    EditText edit_number, enter_otp, enter_password;
    Button btn_request_otp, btn_continue, btn_conform_password, resend_btn;
    TextView timer, label, back_forgot_password;
    String str_edit_otp, str_edit_conf_pass, str_edit_number, number = "", random, multxc = "MULTXC", otpnumber, messageforOtp, username = "krishnamurari", str_edit_password = "", password = "918429";
    private int randomPIN, Otpval;
    private Integer otpToSend;
    boolean resend_top = false;
    String session_id, userid;
    LinearLayout layout1, layout2, layout3;
    private AlertDialog alertDialog;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passcode);
        edit_number = (EditText) findViewById(R.id.number_forgot);
        label = findViewById(R.id.label);
        timer = findViewById(R.id.timer);
        enter_otp = (EditText) findViewById(R.id.enter_otp);
        enter_password = (EditText) findViewById(R.id.enter_password);

        otpToSend = 0;
        Random r = new Random();
        Otpval = r.nextInt(9999 - 1000) + 1000;


        btn_request_otp = (Button) findViewById(R.id.request_otp);

        resend_btn = (Button) findViewById(R.id.resend_otp);

        btn_continue = (Button) findViewById(R.id.continue_otp);

        btn_conform_password = (Button) findViewById(R.id.conformPassword);

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_top = true;
                sentOtp sentOtp = new sentOtp();
                sentOtp.execute();

            }
        });


        btn_request_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_number.getText().toString().length() != 10) {

                    Toast.makeText(getApplicationContext(), "Invalid number", Toast.LENGTH_SHORT).show();

                } else {

                    str_edit_number = edit_number.getText().toString();
                    CheckMobileExists checkMobileExists = new CheckMobileExists();
                    checkMobileExists.execute();
                }
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueOtp();
            }
        });
        btn_conform_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conformPassword();
            }
        });
        back_forgot_password = (TextView) findViewById(R.id.back_forgot_password);
        progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        back_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    void continueOtp() {
        str_edit_otp = enter_otp.getText().toString().trim();

        System.out.println(str_edit_otp);
        System.out.println(Otpval);


        if (TextUtils.isEmpty(str_edit_otp)) {
            enter_otp.setError("Please Enter OPT");
            enter_otp.requestFocus();
            return;
        }
        if (str_edit_otp.equals(String.valueOf(Otpval))) {
            //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
            Toast.makeText(getApplicationContext(), "matched OTP", Toast.LENGTH_SHORT).show();
            label.setText("Enter New Passcode");
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();

        }
    }

    void conformPassword() {
        str_edit_conf_pass = enter_password.getText().toString().trim();

        if (TextUtils.isEmpty(str_edit_conf_pass) || enter_password.getText().toString().trim().length() < 4) {

            enter_password.setError("Please Enter Valid Passcode");
            enter_password.requestFocus();
            return;
        } else {
            changepass(userid, str_edit_conf_pass);
        }

    }


    class CheckMobileExists extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            //reg_progres1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("mobile", str_edit_number);
            // params.put("otp",otpget);


            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//
            System.out.print(s.getClass());

            //  reg_progres1.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {

                    JSONArray msg = jsonObject.optJSONArray("msg");

                    userid = msg.optJSONObject(0).optString("userid");


                    // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();

                    // et_mobile.setText("");

                    Toast.makeText(getApplicationContext(), "Sending Otp", Toast.LENGTH_SHORT).show();
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);

                    sentOtp sentOtp = new sentOtp();
                    sentOtp.execute();
                    //Log.e("ERROR FOR CHECK MOBILE", s);
                } else {
                    Log.e("ERROR FOR CHECK MOBILE", s);

                    Toast.makeText(getApplicationContext(), "User not registered.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class sentOtp extends AsyncTask<Void, Void, String> {


        //    int   otpSend = r.nextInt();

        //Otpval = r.ne(9999 - 1000) + 1000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //params.put("userid", SharedPrefManager.getInstance(Registeration.this).getUser().getUserid());
            params.put("mobile", str_edit_number);
            params.put("otp", String.valueOf(Otpval));

            return requestHandler.sendPostRequest(URLs.URL_SENTOPT, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("Status").equalsIgnoreCase("Success")) {

                    session_id = jsonObject.optString("Details");

                    new CountDownTimer(59000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            timer.setVisibility(View.VISIBLE);
                            resend_btn.setVisibility(View.GONE);
                            timer.setText("00:" + millisUntilFinished / 1000);
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            timer.setVisibility(View.GONE);
                            resend_btn.setVisibility(View.VISIBLE);
                        }

                    }.start();


                    if (resend_top == true) {
                        Toast.makeText(getApplicationContext(), "Otp sent again", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(getApplicationContext(), jsonObject.optString("msg"), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ForgotPasscode.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void changepass(final String userid, final String password) {


        class PasswordChange extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // pdLoading.setMessage("\t Loading...");
                //  pdLoading.show();
                //  pdLoading.setCancelable(false);
                //progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                //  Log.e("ggggggggggggg",userid);
                //  System.out.println("Userid: -"+id);
                params.put("userid", SharedPrefManager.getInstance(ForgotPasscode.this).getUser().getUserid());
                params.put("passcode", password);
                // params.put("otp",otpget);
//                            Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                return requestHandler.sendPostRequest(URLs.UPDATE_PASSCODE, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //pdLoading.dismiss();
                Log.e("aaaaaaaaaaa", s);

                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.optString("status").equalsIgnoreCase("Response Success")) {
                        JSONObject jsonObject = obj.getJSONObject("data");
                        if (jsonObject.optInt("rescode") == 200) {
                            LoginUser user = SharedPrefManager.getInstance(ForgotPasscode.this).getUser();
                            user.setPasscode(password);
                            SharedPrefManager.getInstance(ForgotPasscode.this).userLogin(user);
                            Toast.makeText(ForgotPasscode.this, "Passcode Reset Successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } else {

                        Toast.makeText(getApplicationContext(), "Error Occurred Try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(s);
            }
        }
        PasswordChange ul = new PasswordChange();
        ul.execute();

    }
}
