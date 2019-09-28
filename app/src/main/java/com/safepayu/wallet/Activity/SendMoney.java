package com.safepayu.wallet.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.paymentpackage.SendMoneyActivity;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SendMoney extends AppCompatActivity  {
    private TextView text_money,send_50,send_500,send_1000,text_wallet_money_sendmoney, infoErrorText, messageText;
    private EditText edit_money, edit_mobile;
    Button sendButton;
    private int int_editAmount;
    private int int_wallet_amount;
    Double walletAmount;
    Boolean validReciever = false;
    ProgressDialog progressDialog;
    String str_text_money,str_money,id,str_wallet_money="0",remark="Amount Add to your Wallet",action="credit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        send_50=(TextView)findViewById(R.id.send_50);
        Button back=(Button)findViewById(R.id.sendmoney_back_btn);
        text_wallet_money_sendmoney=(TextView)findViewById(R.id.text_wallet_money_sendmoney);
        send_500=(TextView)findViewById(R.id.send_500);
        send_1000=(TextView)findViewById(R.id.send_1000);

        infoErrorText = (TextView) findViewById(R.id.error);
        messageText = (TextView) findViewById(R.id.messageText);

        edit_money=(EditText)findViewById(R.id.edit_send_money);
        edit_mobile=(EditText)findViewById(R.id.mobile_num);

        progressDialog = new ProgressDialog(SendMoney.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        sendButton=(Button)findViewById(R.id.send_money_button);
        LoginUser loginUser= SharedPrefManager.getInstance(this).getUser();
        id = loginUser.getUserid();


        /*
            Fetch wallet details (amount , id, status) of user
        */

        new GetWalletDetails().execute();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        send_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("50");
            }
        });
        send_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("500");
            }
        });

        send_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_money.setText("1000");
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMoney();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendMoney.this, Navigation.class));
            }
        });


        edit_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

                    String mobile = edit_mobile.getText().toString();

                    if(mobile.length() == 10){

                        new FindUser(mobile).execute();

                    }



            }

            @Override
            public void afterTextChanged(final Editable s) {

                    String mobile = edit_mobile.getText().toString();

                    if(mobile.length() == 10){

                        new FindUser(mobile).execute();

                    }




            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class FindUser extends AsyncTask<String,Void,String> {
        String mobile;

        public FindUser(String mobile) {
            this.mobile = mobile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler requestHandler=new RequestHandler();
            HashMap<String,String> params=new HashMap<>();

            params.put("mobile",this.mobile);

            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE,params);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            try
            {
                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").equalsIgnoreCase("Success")) {
                    // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray msgArray = obj.getJSONArray("msg");
                    JSONObject userObject = msgArray.getJSONObject(0);
                    String userName = userObject.getString("first_name")+" "+userObject.getString("last_name");

                    infoErrorText.setText(userName);
                    infoErrorText.setTextColor(Color.parseColor("#4caf50"));
                    validReciever = true;
                }
                else{
                   // text_wallet_money_sendmoney.setText("Rs.0");

                    infoErrorText.setText("No account linked with this number.");
                    infoErrorText.setTextColor(Color.parseColor("#cd4354"));
                }

            }
            catch (JSONException e) {

                e.printStackTrace();

                infoErrorText.setText("Error Fetching details");
                infoErrorText.setTextColor(Color.parseColor("#cd4354"));

            }

        }
    }


    class MoneyTransfer extends AsyncTask<String,Void,String> {
        String mobile, amount;

        public MoneyTransfer(String mobile, String amount) {
            this.mobile = mobile;
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler requestHandler=new RequestHandler();
            HashMap<String,String> params=new HashMap<>();
            params.put("userid", id);
            params.put("mobile",this.mobile);
            params.put("amount",this.amount);

            return requestHandler.sendPostRequest(URLs.URL_SEND_TRANSFER,params);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            try
            {

                Intent statusActivity  = new Intent(getApplicationContext(), WithdrawlStatusActivity.class);

                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").equalsIgnoreCase("Success")) {
                    // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    messageText.setTextColor(Color.parseColor("#4caf50"));
                    messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    messageText.setText("Successfull. \n Money sent to "+this.mobile);


                    statusActivity.putExtra("transactionid",obj.optString("msg")).putExtra("amount", this.amount)
                            .putExtra("paymentmode", "null").putExtra("payment_status","Success");
                    startActivity(statusActivity);
                    finish();
                }
                else{

                    // Error Sending money

                    messageText.setTextColor(Color.parseColor("#cd4354"));
                    messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    messageText.setText("Failed. \n ");

                    statusActivity.putExtra("transactionid",obj.optString("msg")).putExtra("amount", this.amount)
                            .putExtra("paymentmode", "null").putExtra("payment_status","Failed");

                    if(obj.optString("msg").equalsIgnoreCase("same_account")){
                        statusActivity.putExtra("msg", "same_account");
                    }
                    startActivity(statusActivity);
                    finish();
                }

            }
            catch (JSONException e) {

                e.printStackTrace();
                messageText.setTextColor(Color.parseColor("#cd4354"));
                messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                messageText.setText("Unknown Error Occured. Try again later");


            }

        }
    }




    private void sendMoney(){

        str_money = edit_money.getText().toString().trim();
        String str_mobile = edit_mobile.getText().toString().trim();

        if(str_mobile.length() != 10){
            edit_mobile.setError("Enter valid Mobile number");
            edit_mobile.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(str_money)){
            edit_money.setError("Please Enter Amount");
            edit_money.requestFocus();
            return;
        }

        if(Double.parseDouble(str_money) > walletAmount){
            messageText.setTextColor(Color.parseColor("#cd4354"));
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            messageText.setText("Insufficient wallet amount");
            return;
        }

        if(Double.parseDouble(str_money) < 10 || Double.parseDouble(str_money) > 100000){
            messageText.setTextColor(Color.parseColor("#cd4354"));
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            messageText.setText("Minimum amount to send is ₹10 and maximum amount to send is ₹1,00,000");
            return;
        }

        if(validReciever){
            new MoneyTransfer(str_mobile, str_money).execute();
        }
    }

    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
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
            progressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");
                    //walletId = jsonObject.optString("wallet_id");
                    walletAmount = Double.parseDouble(jsonObject.optString("amount"));
                   // tv_walletAmount.setText("₹ " + walletAmount);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


}
