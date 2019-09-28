package com.safepayu.wallet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.safepayu.wallet.Activity.ValidateOtp;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MobileVerify extends AppCompatActivity {

    private EditText mobileNum, otpNum;
    private Button sendOtp, verifyOtp;
    private LinearLayout mobileVerifyLayout, otpVerifylayout;
    private ProgressBar progressBar;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify);

        sendOtp = (Button) findViewById(R.id.sendOtp);
        verifyOtp = (Button) findViewById(R.id.validateOtpbtn);
        mobileNum = (EditText) findViewById(R.id.mobileNum);
        otpNum = (EditText) findViewById(R.id.otpNum);
        mobileVerifyLayout = (LinearLayout) findViewById(R.id.verifyMobile);
        otpVerifylayout = (LinearLayout) findViewById(R.id.validateOtp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mobileNumber =  mobileNum.getText().toString();

                if (mobileNumber.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    CheckMobileExists mb = new CheckMobileExists();
                    mb.execute();
                }


            }
        });


    }

//
//    class verifyOtp extends AsyncTask<Void, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //otp = otpNum;
//             progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            RequestHandler requestHandler = new RequestHandler();
//            HashMap<String, String> params = new HashMap<>();
//
//            // params.put("session_id", session_id);
//            // params.put("otp",otpget);
//            return requestHandler.sendPostRequest(URLs.URL_VERIFY_OTP, params);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressBar.setVisibility(View.GONE);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (jsonObject.optString("Status").equalsIgnoreCase("Success")) {
//                    ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
//                    registeration.execute();
//                } else {
//                    Toast.makeText(ValidateOtp.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }


    class CheckMobileExists extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("mobile", mobileNumber);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("ERRORFORCHECKMOBILE", s);
            System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            System.out.print(s.getClass());

            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                   // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();
                    Toast.makeText(getApplicationContext(), "User Mobile already exists", Toast.LENGTH_SHORT).show();
                    mobileNum.setText("");

                    Log.e("ERROR FOR CHECK MOBILE", s);
                } else {
                    Log.e("ERROR FOR CHECK MOBILE", s);
                   // Toast.makeText(ValidateOtp.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}