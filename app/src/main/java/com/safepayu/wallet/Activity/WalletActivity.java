package com.safepayu.wallet.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_BANK_CODE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_MODES;

public class WalletActivity extends AppCompatActivity implements CFClientInterface, PasscodeClickListener {
    private LinearLayout linearLayout_Option;
    private TextView tv_addMoneyToSafepe, tv_walletAmount;
    private EditText et_amount;
    private Button btn_card;
    private Button btn_netBanking;
    private String cfToken;
    private long orderId1;
    static String amountPaid;
    private static String walletId;
    private static String walletAmount;
    private ProgressBar progressBar;
    private Button sendmoney_back_btn;
    private TextView send_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        orderId1 = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        progressBar = findViewById(R.id.progressBar);
        sendmoney_back_btn = findViewById(R.id.sendmoney_back_btn);
        tv_walletAmount = findViewById(R.id.tv_walletAmount);
        tv_addMoneyToSafepe = findViewById(R.id.tv_addMoneyToSafepe);
        linearLayout_Option = findViewById(R.id.linearLayout_Option);
        et_amount = findViewById(R.id.et_amount);
        btn_card = findViewById(R.id.btn_card);
        btn_netBanking = findViewById(R.id.btn_netBanking);
        send_txt = findViewById(R.id.send_txt);


        GetWalletDetails getWalletDetails = new GetWalletDetails();
        getWalletDetails.execute();

        // walletId =  WalletSharedPrefManager.getInstance(getApplicationContext()).getWalletDetails().getWalletId();
        //walletAmount =  WalletSharedPrefManager.getInstance(getApplicationContext()).getWalletDetails().getWalletAmount();
        //tv_walletAmount.setText(walletAmount);


        LinearLayout wallet_history = (LinearLayout) findViewById(R.id.wallet_history);
        // LinearLayout reddem_history=(LinearLayout)rootView.findViewById(R.id.redeem_history);
        LinearLayout recharge_history = (LinearLayout) findViewById(R.id.recharge_history);

        wallet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WalletHistory.class));
            }
        });

        recharge_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RechargeHistory.class));
            }
        });

        send_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMoneyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tv_addMoneyToSafepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WalletAddMoney.class);
                startActivity(intent);
                finish();


            }
        });
        btn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_amount.getText().toString().equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), CardPaymentActivity.class);
                    intent.putExtra("amountPaid", et_amount.getText().toString());
                    intent.putExtra("walletId", walletId);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Amount", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_netBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_amount.getText().toString().equalsIgnoreCase("")) {
                    amountPaid = et_amount.getText().toString();
                    GenerateToken generateToken = new GenerateToken();
                    generateToken.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Amount", Toast.LENGTH_LONG).show();
                }
            }
        });

        sendmoney_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onPasscodeMatch(boolean isPasscodeMatched) {


    }

    class GenerateToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("orderid", orderId1 + "");
            params.put("order_amount", amountPaid);
            params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_CASHFREETOKEN, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.optString("status").equalsIgnoreCase("ok")) {
                    cfToken = obj.optString("cftoken");
                    triggerPayment(true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private void triggerPayment(boolean isUpiIntent) {
        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        //String token = "N59JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.wC9JiM0YGZ0UmM2QTY5MWNiojI0xWYz9lIsIDO2QTM2MTN1EjOiAHelJCLiIlTJJiOik3YuVmcyV3QyVGZy9mIsICM1IiOiQnb19WbBJXZkJ3biwiI2UDNzITMiojIklkclRmcvJye.5GDXBNHNOKFnS5uhLP2EbyJ2MiSnhrop_ElD1JlCB-XO0qWS90sCgS617lZfAkoFr9";
        String token = cfToken;


        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "PROD";

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "93391d04c9a85fcdcd6c67d29339";
        String orderId = orderId1 + "";
        String orderAmount = amountPaid;
        String orderNote = "Test Order";
        String customerName = SharedPrefManager.getInstance(getApplicationContext()).getUser().getFirst_name();
        String customerPhone = SharedPrefManager.getInstance(getApplicationContext()).getUser().getMobile();
        String customerEmail = SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail();
        ;

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        // params.put(PARAM_NOTIFY_URL,customerEmail);
        //params.put(PARAM_PAYMENT_OPTION, "paypal");

        params.put(PARAM_PAYMENT_MODES, "nb");
        params.put(PARAM_BANK_CODE, "3003");


        for (Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(this, params, token, this, stage);

        /*if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.upiPayment(this, params, token, this, stage);
        }
        else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage);
        }*/

    }

    public void doPayment(Context context, Map<String, String> params, String token, CFClientInterface callback, String stage) {
        this.triggerPayment(true);
    }

    @Override
    public void onSuccess(Map<String, String> params) {
        //linearLayout_Option.setVisibility(View.GONE);
        UpdateWalletAmount updateWalletAmount = new UpdateWalletAmount();
        updateWalletAmount.execute();


    }

    @Override
    public void onFailure(Map<String, String> params) {

    }

    @Override
    public void onNavigateBack() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetWalletDetails getWalletDetails = new GetWalletDetails();
        getWalletDetails.execute();

//        PasscodeDialog passcodeDialog = new PasscodeDialog(WalletActivity.this, WalletActivity.this, "");
//        passcodeDialog.show();

    }

    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");
                    walletId = jsonObject.optString("wallet_id");
                    walletAmount = jsonObject.optString("amount");
                    tv_walletAmount.setText("₹ " + walletAmount);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    class UpdateWalletAmount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            //String customerName = SharedPrefManager.getInstance(getApplicationContext()).getUser().getFirst_name();

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("wallet_id", walletId);
            params.put("amount", amountPaid);

            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Wallet Amount Updated", Toast.LENGTH_LONG).show();
                    GetWalletDetails getWalletDetails = new GetWalletDetails();
                    getWalletDetails.execute();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
