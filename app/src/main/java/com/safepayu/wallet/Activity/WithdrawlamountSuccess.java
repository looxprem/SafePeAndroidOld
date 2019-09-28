package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.Homepage;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WithdrawlamountSuccess extends AppCompatActivity {

    private TextView withdrawnAmount,walletAmount,transaction_id;
    private String amountwithdrawn,amountwallet,idtransaction, paymentMode;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawlamount_success);
        withdrawnAmount=(TextView)findViewById(R.id.withdrawn_amt);


        walletAmount=(TextView)findViewById(R.id.tv_walletAmount);
        transaction_id=(TextView)findViewById(R.id.orderid);
        button=(Button)findViewById(R.id.gotowallet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(),WalletActivity.class));
                finish();

            }
        });
        Intent intent = getIntent();
        amountwithdrawn = intent.getStringExtra("amount");
        idtransaction = intent.getStringExtra("transactionid");
        paymentMode = intent.getStringExtra("paymentmode");
        withdrawnAmount.setText(amountwithdrawn);
        transaction_id.setText(idtransaction);

        TextView remark  = (TextView) findViewById(R.id.remarktext);


        if(paymentMode.equals("upi")){
            remark.setText("Account Credit by Cashfree - Cashfreepayout@yesbank");

        }
        else if(paymentMode.equals("banktransfer")) {
            remark.setText("Account Credit by PASFAR TECHNOLOGIES - SAFEPAYU");

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
                    walletAmount.setText( "₹ " + jsonObject.optString("amount"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}


