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
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddMoneySuccess extends AppCompatActivity {
    private String transacId, amount;

    TextView texttransactionid, recenttransactions, textAmount;
    Button gotowallet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_success);

        Intent intent = getIntent();

       transacId =  intent.getStringExtra("transactionid");
       amount =  intent.getStringExtra("amount");

        gotowallet = (Button) findViewById(R.id.gotowallet);
        texttransactionid = (TextView) findViewById(R.id.orderid);
        recenttransactions = (TextView) findViewById(R.id.recentransaction);
        textAmount = (TextView) findViewById(R.id.amount);

        GetWalletDetails getWalletDetails = new GetWalletDetails();
        getWalletDetails.execute();

        texttransactionid.setText(transacId);
        textAmount.setText(amount);
        gotowallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
                finish();
            }
        });

        recenttransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletHistory.class));
                finish();

            }
        });
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
                   TextView walletAmount =  (TextView)findViewById(R.id.updatedWalletAmount);
                    walletAmount.setText( "₹ " + jsonObject.optString("amount"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

