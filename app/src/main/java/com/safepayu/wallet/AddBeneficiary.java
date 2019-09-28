package com.safepayu.wallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.Activity.SendMoneyActivity;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;

public class AddBeneficiary extends AppCompatActivity {

    String purpose, userid, bname, ifscCode;
    boolean isPaytm = false, isUPI = false, isBank = false;
    TextView ifscres;
    TextView title ;
    ImageView logoImage;
    LinearLayout paytmlayout, upilayout, banklayout;
    Button paytmaddBtn , upiaddbtn, bankaddbtn, send_back_btn;
    EditText paytmNUmber, upiAdd, accountName, accountNumber, confirmAccountNumber, bankIfsc;
    public ProgressDialog progressDialog;

    String sendpaytm , sendupi, sendbankname, sendbankaccount, sendbankifsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beneficiary);
        bname = "Safepayu";

        send_back_btn = findViewById(R.id.send_back_btn);

        send_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userid = SharedPrefManager.getInstance(this).getUser().getUserid();

        title = (TextView) findViewById(R.id.title);
        logoImage = (ImageView) findViewById(R.id.logoimage);

        paytmlayout = (LinearLayout) findViewById(R.id.addPaytmBenLayout);
        upilayout = (LinearLayout) findViewById(R.id.addUPIBenLayout);
        banklayout = (LinearLayout) findViewById(R.id.addBankBenLayout);


        paytmaddBtn  = (Button) findViewById(R.id.paytmAddBtn) ;
        upiaddbtn  = (Button) findViewById(R.id.upiAddBtn) ;
        bankaddbtn  = (Button) findViewById(R.id.bankAddBtn) ;

        paytmNUmber = (EditText) findViewById(R.id.paytmnumber);
        upiAdd = (EditText) findViewById(R.id.upiaddress);
        bankIfsc = (EditText) findViewById(R.id.ifscCode);
        accountName = (EditText) findViewById(R.id.accountName);
        accountNumber = (EditText) findViewById(R.id.accountNumber);
        confirmAccountNumber = (EditText) findViewById(R.id.confirmAccountNumber);

        ifscres = (TextView) findViewById(R.id.ifscres);

        progressDialog = new ProgressDialog(AddBeneficiary.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);




        paytmaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paytmNUmber.getText().toString() == "" || paytmNUmber.getText().toString().length() != 10 ){
                    Toast.makeText(getApplicationContext(), "Enter Valid Number", Toast.LENGTH_SHORT).show();
                }else{
                    isPaytm = true;
                    sendpaytm = paytmNUmber.getText().toString();
                    AddNewBeneficiary addNewBeneficiary = new AddNewBeneficiary();
                    addNewBeneficiary.execute();
                }



            }
        });


        upiaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upiAdd.getText().toString() == "" || upiAdd.getText().toString().indexOf('@') == -1 ){
                    Toast.makeText(getApplicationContext(), "Enter Valid Number", Toast.LENGTH_SHORT).show();
                }else{
                    isUPI = true;
                    sendupi = upiAdd.getText().toString();
                    AddNewBeneficiary addNewBeneficiary = new AddNewBeneficiary();
                    addNewBeneficiary.execute();
                }
            }
        });

        bankaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountName.getText().toString() == ""){
                    Toast.makeText(getApplicationContext(), "Enter Valid Account Name", Toast.LENGTH_SHORT).show();

                }else if( accountNumber.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(), "Enter Account Number", Toast.LENGTH_SHORT).show();

                }else if(confirmAccountNumber.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(), "Enter Valid Confirm Account Number", Toast.LENGTH_SHORT).show();

                }else if( bankIfsc.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(), "Enter Valid IFSC Code", Toast.LENGTH_SHORT).show();

                }
                else if(!accountNumber.getText().toString().equals(confirmAccountNumber.getText().toString())){

                    Toast.makeText(getApplicationContext(), "Account Number And Confirm account don't match", Toast.LENGTH_SHORT).show();
                }
                else{
                    isBank = true;
                    sendbankaccount = accountNumber.getText().toString();
                    sendbankname = accountName.getText().toString();
                    sendbankifsc = bankIfsc.getText().toString();
                    AddNewBeneficiary addNewBeneficiary = new AddNewBeneficiary();
                    addNewBeneficiary.execute();
                }
            }
        });



        Intent intent = getIntent();
        purpose =  intent.getStringExtra("purpose");



        if(purpose.equals("Paytm")){
            title.setText("Add Paytm Beneficiary");
            logoImage.setBackgroundResource(R.drawable.paytm_logo);
            paytmlayout.setVisibility(View.VISIBLE);


        }else if(purpose.equals("Upi")){
            title.setText("Add UPI Beneficiary");
            logoImage.setBackgroundResource(R.drawable.upi_logo);
            upilayout.setVisibility(View.VISIBLE);
        }else{
            title.setText("Add Bank Beneficiary");
            logoImage.setBackgroundResource(R.drawable.banktansfer);
            banklayout.setVisibility(View.VISIBLE);
        }
    }

    public void verifyIfscCode(View v){

        ifscCode  = bankIfsc.getText().toString();



        if(ifscCode.length() !=0){
              new VerifyIFSCCode().execute();
        }else{
            ifscres.setText("Enter valid IFSC Code");
        }

    }

    public class VerifyIFSCCode extends AsyncTask<String,String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar_WalletHistory.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            return requestHandler.sendGetRequest("https://ifsc.razorpay.com/"+ifscCode, params).toString();

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();

            if(result.equalsIgnoreCase("Not Found")){

                ifscres.setText("Invalid IFSC Code");

            }else{

                try {
                    JSONObject obj = new JSONObject(result.toString());

                    String name = obj.getString("BANK");
                    String branch = obj.getString("BRANCH");
                    String centre = obj.getString("CENTRE");
                    String state = obj.getString("STATE");
                    ifscres.setText(name+", "+branch+", "+centre+", "+state);

                } catch (JSONException e) {
                    e.printStackTrace();
                    ifscres.setText("Error finding IFSC");
                    ifscres.setTextColor(getResources().getColor(R.color.red));
                }

            }



        }

    }

    public class AddNewBeneficiary extends AsyncTask<String,String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar_WalletHistory.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            if(isBank){
                params.put("bname" , sendbankname);
                params.put("accnum" , sendbankaccount);
                params.put("bankifsc" , sendbankifsc);
            }
            if(isPaytm){
                params.put("bname" , bname);
                params.put("paytm" , sendpaytm);
            }
            if(isUPI){
                params.put("bname" , bname);
                params.put("upiadd" , sendupi);
            }

            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.URL_ADD_BEN, params).toString();

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();


            Log.i("RESULT_PAYTM", result);
            System.out.println(result.length());


            try {
                JSONObject obj = new JSONObject(result.toString());

                if (obj.optString("status").equalsIgnoreCase("Success")) {

                  //  JSONObject message = obj.optJSONObject("msg");
                    Toast.makeText(getApplicationContext(), "Beneficiary Added", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddBeneficiary.this, SendMoneyActivity.class);
                    startActivity(i);

                } else {

                    Toast.makeText(getApplicationContext(), "Error Adding Beneficiary", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddBeneficiary.this, SendMoneyActivity.class);
                    startActivity(i);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
