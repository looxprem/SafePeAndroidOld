package com.safepayu.wallet.AdditionalPage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.R;

public class ContactUs extends AppCompatActivity {
    EditText editEmail,editSubject,editMessage;
    TextView visit_website, sendmail;
   private Button submit, callButtonIndia, callButtonUsa;
    String str_mail,str_subject,str_message,id,trueStr="true";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ImageView clear_btn=(ImageView)findViewById(R.id.clear_btn_contactUs);
        editEmail=(EditText)findViewById(R.id.contactus_email);
        visit_website=(TextView) findViewById(R.id.visit_website);
        editSubject=(EditText)findViewById(R.id.contactus_subject);
        editMessage=(EditText)findViewById(R.id.contactus_message);
        submit=(Button)findViewById(R.id.contactus_submit);
        callButtonIndia = (Button) findViewById(R.id.call_button_india);
        callButtonUsa = (Button) findViewById(R.id.call_button_usa);
        sendmail = (TextView) findViewById(R.id.sendmail);
        LoginUser user= SharedPrefManager.getInstance(this).getUser();
        id=user.getUserid();
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs();
            }
        });


        //-------------askr permission-------------------------
        ActivityCompat.requestPermissions(ContactUs.this,
                new String[]{Manifest.permission.CALL_PHONE}, 1);



        //-------------END askr permission-------------------------

        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@safepayu.com"});


                try{
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }catch (Exception e){
                    Log.e("SendEmailERRO", e.toString());
                }
//                Uri uri = Uri.parse("mailto:support@safepayu.com");
//                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//                startActivity(Intent.createChooser(i, "Send mail"));

            }
        });

        callButtonIndia.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onClickWhatsApp("8860722217");
                return true;
            }
        });

        visit_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safepayu.com/")));
            }
        });
    }

    public void onClickWhatsApp(String number) {

        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));

    }

    public void onClickTelegram() {

        try {

            Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
            telegramIntent.setData(Uri.parse("http://telegram.me/+1(762)2220117"));
            startActivity(telegramIntent);

        } catch (Exception e) {
            // show error message
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ContactUs.this, "Permission denied to Call Phone", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    void contactUs(){
        str_mail=editEmail.getText().toString().trim();
        str_subject=editSubject.getText().toString().trim();
        str_message=editMessage.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(str_mail).matches()) {
            editEmail.setError("Enter a valid email");
            editEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_subject)) {
            editSubject.setError("Enter Subject");
            editSubject.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_message)) {
            editMessage.setError("Enter Message");
            editMessage.requestFocus();
            return;
        }
        class Contactus extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("SUBMIT", trueStr);
                params.put("email", str_mail);
                params.put("subject", str_subject);
                params.put("message", str_message);
                return requestHandler.sendPostRequest(URLs.URL_CONTACTUS, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // progressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(ContactUs.this).setMessage("Message Status: "+obj.getString("message")).setPositiveButton("Ok",null).show();

                    }
                    else {
                        System.out.println("elseeeeeeeeeee");
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Contactus c=new Contactus();
        c.execute();
        //text_money.setText("Rs. "+str_money);
    }
}
