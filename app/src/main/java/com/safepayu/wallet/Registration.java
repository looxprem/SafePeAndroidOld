package com.safepayu.wallet;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.Activity.ValidateOtp;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.MainActivity;
import com.safepayu.wallet.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class Registration extends Activity {

    private EditText et_firstname, et_lastname, et_email, et_mobile, et_password, referralcode;
    private Button btn_signup;
    private ProgressBar reg_progres1;
    private TextView et_dob;
    private  String referralCOde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        reg_progres1 =findViewById(R.id.reg_progres1);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_dob = findViewById(R.id.et_dob);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(btn_signupListner);
        referralcode=findViewById(R.id.referralcode);
        et_dob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.back_arrow);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }



    private String firstname2, lastname2, email2, mobile2, dob2, password2;

    private View.OnClickListener btn_signupListner = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkValidation()) {
                firstname2 = et_firstname.getText().toString();
                lastname2 = et_lastname.getText().toString();
                email2 = et_email.getText().toString();
                mobile2 = et_mobile.getText().toString();
                dob2 = et_dob.getText().toString();
                password2 = et_password.getText().toString();
                referralCOde = referralcode.getText().toString();



                CheckReferralCode checkReferralCode = new CheckReferralCode();
                checkReferralCode.execute();

            }
        }
    };

    private  View.OnClickListener backarrow = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

    };

    private boolean checkValidation() {
        if (et_firstname.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_LONG).show();
            return false;

        } else if (et_lastname.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_LONG).show();
            return false;

        } else if (et_email.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
            return false;

        } else if (et_mobile.getText().toString().length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
            return false;

        } else if (et_dob.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter dob", Toast.LENGTH_LONG).show();
            return false;

        } else if (et_password.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return false;

        }  else if (referralcode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter referral Code", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;

        }

    }


    public void datePicker(){
        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    class CheckMobileExists extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            reg_progres1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("mobile", mobile2);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("ERRORFORCHECKMOBILE", s);
            System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            System.out.print(s.getClass());

            reg_progres1.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();
                    Toast.makeText(getApplicationContext(), "User Mobile already exists", Toast.LENGTH_SHORT).show();
                    et_mobile.setText("");
                    //Log.e("ERROR FOR CHECK MOBILE", s);
                } else {
                    Log.e("ERROR FOR CHECK MOBILE", s);

                    Toast.makeText(getApplicationContext(), "Sending Otp", Toast.LENGTH_SHORT).show();
                    sentOtp sentOtp=new sentOtp();
                    sentOtp.execute();
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
            reg_progres1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("fname", firstname2);
            params.put("lname", lastname2);
            params.put("email", email2);
            params.put("mobile", mobile2);
            params.put("dob", dob2);
            params.put("password", password2);
            params.put("referral_recieved", referralCOde);
            return requestHandler.sendPostRequest(URLs.URL_REGESTRATION, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                reg_progres1.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("success")) {
                    sentOtp sentOtp = new sentOtp();
                    sentOtp.execute();
                } else {
                    Toast.makeText(Registration.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class sentOtp extends AsyncTask<Void, Void, String> {
        Random r = new Random();
        int Otpval = r.nextInt(9999 - 1000) + 1000;
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
            params.put("mobile", mobile2);
            params.put("otp", String.valueOf(Otpval));

            return requestHandler.sendPostRequest(URLs.URL_SENTOPT, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("Status").equalsIgnoreCase("Success")) {

                    String session_id = jsonObject.optString("Details");
                    //Toast.makeText(getApplicationContext(), jsonObject.optString("msg"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Registration.this, ValidateOtp.class);
                    intent.putExtra("session_id", session_id);
                    intent.putExtra("firstname",firstname2);
                    intent.putExtra("lastname",lastname2);
                    intent.putExtra("email",email2);
                    intent.putExtra("mobile",mobile2);
                    intent.putExtra("dob",dob2);
                    intent.putExtra("password",password2);
                    intent.putExtra("otpval",String.valueOf(Otpval));
                    intent.putExtra("referral_recieved", referralCOde);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(Registration.this, "Something went wrong please contact to administrator", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    class CheckReferralCode extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            reg_progres1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("referral_code", referralCOde);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_VALIDATE_REFERRAL, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.i("ERRORFORCHECKMOBILE", s);
//            System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            System.out.print(s.getClass());

            reg_progres1.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();
//                    Toast.makeText(getApplicationContext(), "User Mobile already exists", Toast.LENGTH_SHORT).show();
//                    et_mobile.setText("");
                    //Log.e("ERROR FOR CHECK MOBILE", s);

                    CheckMobileExists checkmobexists = new CheckMobileExists();
                    checkmobexists.execute();
                } else {
//                    Log.e("ERROR FOR CHECK MOBILE", s);
//
                    Toast.makeText(getApplicationContext(), "Invalid Referral Code", Toast.LENGTH_SHORT).show();
//                    sentOtp sentOtp=new sentOtp();
//                    sentOtp.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
