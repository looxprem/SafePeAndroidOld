package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WithdrawlStatusActivity extends AppCompatActivity {

    private TextView withdrawnAmount, walletAmount, transaction_id, status, msg;
    private String amountwithdrawn, amountwallet, idtransaction, paymentMode, paymentStatus, message;
    private Button button;
    private LinearLayout statusBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawl_status);


        message = null;
        withdrawnAmount = (TextView) findViewById(R.id.txtAmount);
        walletAmount = (TextView) findViewById(R.id.tv_walletAmount);
        transaction_id = (TextView) findViewById(R.id.txtTransacID);
        status = (TextView) findViewById(R.id.txtStatus);
        msg = (TextView) findViewById(R.id.txtMsg);
        statusBack = (LinearLayout) findViewById(R.id.statusBack);

        button = (Button) findViewById(R.id.gotowallet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
                finish();

            }
        });
        Intent intent = getIntent();
        amountwithdrawn = intent.getStringExtra("amount");
        paymentStatus = intent.getStringExtra("payment_status");
        idtransaction = intent.getStringExtra("transactionid");
        paymentMode = intent.getStringExtra("paymentmode");
        message = intent.getStringExtra("msg");

        withdrawnAmount.setText("₹" + amountwithdrawn);
        transaction_id.setText(idtransaction);

        TextView remark = (TextView) findViewById(R.id.remarktext);

        if (paymentStatus.equalsIgnoreCase("Success")) {
            statusBack.setBackgroundColor(Color.parseColor("#43cd67"));
            status.setText("Success");
            msg.setText("Transaction is successfull.");


            if (paymentMode.equals("upi")) {
                remark.setText("Account Credit by Cashfree - Cashfreepayout@yesbank");

            } else if (paymentMode.equals("banktransfer")) {
                remark.setText("Account Credit by PASFAR TECHNOLOGIES - SAFEPAYU");

            }


        } else if (paymentStatus.equalsIgnoreCase("Pending")) {
            statusBack.setBackgroundColor(Color.parseColor("#FFCA28"));
            status.setText("Pending");
            msg.setText("Transaction is pending. Please wait for few hours to update.");

        } else {
            statusBack.setBackgroundColor(Color.parseColor("#EF5350"));
            status.setText("Failed");
            findViewById(R.id.labelTrancId).setVisibility(View.GONE);
            msg.setText("Transaction failed. Please try again after sometime.");
            if (message != null) {
                if (message.equalsIgnoreCase("same_account")) {
                    msg.setText("Cannot Transfer to same account.");
                }
                if (message.equalsIgnoreCase("block")) {
                    msg.setText("Unable to perform transaction");
                }
            }


        }

        GetWalletDetails getWalletDetails = new GetWalletDetails();
        getWalletDetails.execute();


    }

    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
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
            //progressBar.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");
                    // walletId = jsonObject.optString("wallet_id");
                    //walletAmount = jsonObject.optString("amount");
                    // TextView walletAmount =  (TextView)findViewById(R.id.updatedWalletAmount);
                    walletAmount.setText("₹" + jsonObject.optString("amount"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

