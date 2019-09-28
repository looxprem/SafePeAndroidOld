package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.MainActivity;
import com.safepayu.wallet.R;
import com.safepayu.wallet.Registration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

public class ValidateOtp extends AppCompatActivity {

    private EditText otpEt;
    private String otp, otpval;
    private String session_id;
    private Button otp_validate;
    private String firstname,lastname,email,mobile,dob,password, referralRecieved;
    private String otpget;
    private ProgressBar progressBar;
    private TextView resendotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);
        Intent intent = getIntent();
        session_id = intent.getStringExtra("session_id");
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        email = intent.getStringExtra("email");
        mobile = intent.getStringExtra("mobile");
        dob = intent.getStringExtra("dob");
        password = intent.getStringExtra("password");
        otpval = intent.getStringExtra("otpval");
        referralRecieved = intent.getStringExtra("referral_recieved");


        progressBar = findViewById(R.id.progressBar);
        resendotp = (TextView) findViewById(R.id.resendOtp);
        otpEt = findViewById(R.id.otp_et);
        otp_validate = findViewById(R.id.otp_validate);
        otp_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!otpEt.getText().toString().equalsIgnoreCase("")){
                    otpget=otpEt.getText().toString();
                    verifyOtp verifyOtp = new verifyOtp();
                    verifyOtp.execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Enter Otp",Toast.LENGTH_LONG).show();
                }

            }
        });


        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SentOtp sentOtp = new SentOtp();
                sentOtp.execute();

            }
        });

    }

    class SentOtp extends AsyncTask<Void, Void, String> {
       // Random r = new Random();
       // int Otpval = r.nextInt(9999 - 1000) + 1000;
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
            params.put("mobile", mobile.toString());
            params.put("otp", otpval.toString());

            return requestHandler.sendPostRequest(URLs.URL_SENTOPT, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("Status").equalsIgnoreCase("Success")) {

                    session_id = jsonObject.optString("Details");

                    //String session_id = jsonObject.optString("Details");
                    Toast.makeText(getApplicationContext(), "OTP resent", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(Registration.this, ValidateOtp.class);
//                    intent.putExtra("session_id", session_id);
//                    intent.putExtra("firstname",firstname2);
//                    intent.putExtra("lastname",lastname2);
//                    intent.putExtra("email",email2);
//                    intent.putExtra("mobile",mobile2);
//                    intent.putExtra("dob",dob2);
//                    intent.putExtra("password",password2);
//                    intent.putExtra("otpval",Otpval);
//                    startActivity(intent);
//                    finish();


                } else {
                    Toast.makeText(ValidateOtp.this, "Error Sending Otp", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class verifyOtp extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            otp = otpEt.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("session_id", session_id);
            params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_VERIFY_OTP, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("Status").equalsIgnoreCase("Success")) {
                    Registeration registeration=new Registeration();
                    registeration.execute();
                    finish();
                } else {
                    Toast.makeText(ValidateOtp.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    class Registeration extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("fname", firstname);
            params.put("lname", lastname);
            params.put("email", email);
            params.put("mobile", mobile);
            params.put("dob", dob);
            params.put("password", password);
            params.put("referral_recieved", referralRecieved);
            return requestHandler.sendPostRequest(URLs.URL_REGESTRATION, params).toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(ValidateOtp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    try{
                        Timer timer = new Timer();
                        timer.wait(2000);
                    }
                    catch (Exception e){
                        Toast.makeText(ValidateOtp.this, "", Toast.LENGTH_SHORT).show();

                    }
                    startActivity(new Intent(ValidateOtp.this, MainActivity.class));
                    finish();
                } else {

                    Toast.makeText(ValidateOtp.this, jsonObject.optString("Error Occured"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

