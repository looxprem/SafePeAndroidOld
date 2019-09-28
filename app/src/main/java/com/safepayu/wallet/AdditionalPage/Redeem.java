package com.safepayu.wallet.AdditionalPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.Homepage;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class Redeem extends AppCompatActivity  {
    ProgressDialog pdLoading;
    EditText name,acNumber,bankname,acType,ifscCode,pancard,adharcard;
    Button submit;
    Spinner spinner;
    ImageView clearLyout;
    String multxc="MULTXC",otpnumber,messageforOtp="Your Redeem Request has been accepted. Please wait 24 to 48 hours working days to confirm Transaction",username="krishnamurari",password="918429";
    String str_id,str_Amount,str_name,str_acNumber,str_bankname,str_acType,str_ifscCode,str_pancard,str_adharcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.redeemtoolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_redeem);
        pdLoading = new ProgressDialog(Redeem.this);
        name=(EditText)findViewById(R.id.redeem_name);
        acNumber=(EditText)findViewById(R.id.redeem_accountno);
        bankname=(EditText)findViewById(R.id.redeem_bankname);
         spinner = (Spinner) findViewById(R.id.redeem_ac_type);
        ifscCode=(EditText)findViewById(R.id.redeem_ifsc_code);
        pancard=(EditText)findViewById(R.id.redeem_pan_code);
        adharcard=(EditText)findViewById(R.id.adharcard);
        submit=(Button)findViewById(R.id.redeem_submit);
        clearLyout=(ImageView)findViewById(R.id.clear_btn_contactUs);

        RememberPassword rememberPassword=SharedPrefManagerLogin.getInstance(this).getUser();
        otpnumber=rememberPassword.getMobile();
        System.out.println("otpnumberotpnumber "+otpnumber);
        clearLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LoginUser loginUser= SharedPrefManager.getInstance(this).getUser();
        str_id=loginUser.getUserid();
        Intent i= getIntent();
        str_Amount=  i.getStringExtra("amount");
        System.out.println("amount "+str_Amount);
        List<String> categories = new ArrayList<String>();
        categories.add("Saving");
        categories.add("Current");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
       // spinner.setOnItemClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_acType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                redeemnow();
            }
        });

    }
    public static boolean validAdhaarCard(String in){
        return  in.length()==12 && android.util.Patterns.PHONE.matcher(in).matches();
    }
    void redeemnow(){
        str_name=name.getText().toString().trim();
        str_acNumber=acNumber.getText().toString().trim();
        str_bankname=bankname.getText().toString().trim();
       // str_acType=acType.getText().toString().trim();
        str_ifscCode=ifscCode.getText().toString().trim();
        str_pancard=pancard.getText().toString().trim();
        str_adharcard=adharcard.getText().toString().trim();

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

       // Matcher matcher = pattern .matcher(str_pancard);
      /*  if (!(matcher.matches())) {
            pancard.setError("Please enter valid PanCard");
            pancard.requestFocus();
            return;
        }*/
        if (TextUtils.isEmpty(str_name)) {
            name.setError("Please enter Holder Name");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_acNumber)) {
            acNumber.setError("Please enter Account Number");
            acNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_bankname)) {
            bankname.setError("Please enter Bank Name");
            bankname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(str_ifscCode)) {
            ifscCode.setError("Please enter Ifsc Code");
            ifscCode.requestFocus();
            return;
        }

        if (!validAdhaarCard(str_adharcard)) {
            adharcard.setError("Please enterValid Adhaar Card");
            adharcard.requestFocus();
            return;
        }

        class RedeemClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\t Plz Wait...");
                pdLoading.show();
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", str_id);
                params.put("amount", str_Amount);
                params.put("acount_holder_name", str_name);
                params.put("acount_number", str_acNumber);
                params.put("bank_name", str_bankname);
                params.put("account_type", str_acType);
                params.put("ifsc_code", str_ifscCode);
                params.put("pan_card_number", str_pancard);
                params.put("adhar_card", str_adharcard);
                return requestHandler.sendPostRequest(URLs.URL_REDEEM, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressBar.setVisibility(View.GONE);
                pdLoading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(Redeem.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Redeem")
                                .setMessage(R.string.message_redeem)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(new Intent(Redeem.this, Navigation.class));
                                    }
                                }).show();

                        sendOtp();
                       // finish();

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
        RedeemClass redeemClass=new RedeemClass();
        redeemClass.execute();
    }
    void sendOtp(){
        class SendOtp extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("sender", multxc);
                params.put("sendto", otpnumber);
                params.put("message", messageforOtp);
                return requestHandler.sendPostRequest(URLs.URL_OTPSEND, params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject obj = new JSONObject(s);
                    System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {
                        System.out.println("obj.getString"+obj.getString("status"));
                       // Toast.makeText(getApplicationContext(), obj.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        System.out.println("obj.getString elseee"+obj.getString("status"));
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        SendOtp sp=new SendOtp();
        sp.execute();
    }


}
