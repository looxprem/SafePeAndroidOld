package com.safepayu.wallet.AdditionalPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RedeemWallet extends AppCompatActivity {
    private ProgressDialog pdLoading;
   private EditText pancard,adharcard,walletMobileNumber;
   private Button submit;
   private Spinner spinner;
   private ImageView clearLyout;
   private String multxc="MULTXC",otpnumber,messageforOtp="Your Redeem Request has been accepted. Please wait 24 to 48 hours working days to confirm Transaction",username="krishnamurari",password="918429";
   private String str_id,str_Amount,str_walletMobileNumber,str_acType,str_pancard,str_adharcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_wallet);

        pdLoading = new ProgressDialog(RedeemWallet.this);
        spinner = (Spinner) findViewById(R.id.redeem_wallet_type);
        pancard=(EditText)findViewById(R.id.wallet_pan_code);
        walletMobileNumber=(EditText)findViewById(R.id.wallet_mobilenumber);
        adharcard=(EditText)findViewById(R.id.wallet_adharcard);
        submit=(Button)findViewById(R.id.redeem_wallet_submit);
        clearLyout=(ImageView)findViewById(R.id.clear_btn_wallet_redeem);
        RememberPassword rememberPassword= SharedPrefManagerLogin.getInstance(this).getUser();
        otpnumber=rememberPassword.getMobile();

        LoginUser loginUser= SharedPrefManager.getInstance(this).getUser();
        str_id=loginUser.getUserid();
        Intent i= getIntent();
        str_Amount=  i.getStringExtra("amount");
        System.out.println("amount "+str_Amount);

        clearLyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<String> walletType = new ArrayList<String>();
        walletType.add("Paytm");
        walletType.add("Airtel Money");
        walletType.add("Oxigen Wallet ");
        walletType.add("MobiKwik");
        walletType.add("FreeCharge");
        walletType.add("OLA Money");
        walletType.add("PayZapp");
        walletType.add("PayCash");
        ArrayAdapter<String> walletdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, walletType);
        walletdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(walletdataAdapter);
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

               mRedeemToWallet();
            }
        });
    }

    public static boolean validAdhaarCard(String in){
        return  in.length()==12 && android.util.Patterns.PHONE.matcher(in).matches();
    }
    public static boolean validMobile(String in){
        return  in.length()==10 && android.util.Patterns.PHONE.matcher(in).matches();
    }
    void mRedeemToWallet(){
        str_walletMobileNumber=walletMobileNumber.getText().toString().trim();
        str_pancard=pancard.getText().toString().trim();
        str_adharcard=adharcard.getText().toString().trim();
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        // Matcher matcher = pattern .matcher(str_pancard);
      /*  if (!(matcher.matches())) {
            pancard.setError("Please enter valid PanCard");
            pancard.requestFocus();
            return;
        }*/

        if (!validMobile(str_walletMobileNumber)) {
            walletMobileNumber.setError("Please enterValid Mobile Number");
            walletMobileNumber.requestFocus();
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
                params.put("mobile_no", str_walletMobileNumber);
                params.put("wallet_type", str_acType);
                params.put("pan_card_number", str_pancard);
                params.put("adhar_card", str_adharcard);
                return requestHandler.sendPostRequest(URLs.URL_WALLET_REDEEM, params);
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
                        new AlertDialog.Builder(RedeemWallet.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Redeem")
                                .setMessage(R.string.message_redeem)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(new Intent(RedeemWallet.this, Navigation.class));
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

            }

        }
        SendOtp sp=new SendOtp();
        sp.execute();
    }


}
