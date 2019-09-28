package com.safepayu.wallet.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.safepayu.wallet.AddMoneyFailed;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.CreatePasscodeDialog;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_CVV;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_HOLDER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_MM;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_NUMBER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_YYYY;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_MODES;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_UPI_VPA;

public class PaymentTypeActivity extends AppCompatActivity implements CFClientInterface, PasscodeClickListener {
    private Button btn_card, btn_netBanking, btn_upi, btn_proceed_netBanking, proceed_upi, proceed_wallet, proceed_card;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;
    // private long orderId1;
    private String cfToken, lastTrnxId, orderId, paymentMode, rechargeStatus, updateWallet = "failed",ResposneMsg="";
    private String walletId, sendOrderId;
    private Button sendmoney_back_btn;
    private TextView tv_walletAddedAmount;
    private EditText et_cardNumber, et_year, et_month, et_cvv, et_name, et_upi;
    private String orderAmount;
    private LoginUser loginUser;
    private String user_id;
    AlertDialog.Builder builder;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private String paymentFor; //Wallet , recharge, bill, etc
    private TextView heading, payfromwalletOption;
    private double balance;
    private TextView wallet_amt;
    RelativeLayout cardBtnLayout, upiBtnLayout, NetBankingBtnLayout, WalletBtnLayout, radioLayout4;
    LinearLayout cardLayout, UpiLayout, NetBankingLayout, WalletLayout;
    boolean rechFromWallet = false;
    boolean paymentStatus = false;
    boolean isBlocked = false;

    private Dialog dialogPending;


    //  Recharge parameters
    private String rechargeAmount = null;
    private String refundedAmount = null;
    private String rechargeNumber = null;
    private String customerId = null;
    private String rechargeOperator = null;
    private String rechargeType = null;
    private String rechargeOptional1 = null;
    private String rechargeOptional2 = null;
    private String circleCode = null;
    private String StdCode = null;
    private String city = null;
    private String accountNo = null;


    //  Wallet parameters
    private String amountToAddInWallet;

    String operation = null;
    String updateAmount = null;
    String description = null;

    TextView PendingNote,PendingRechargeAmount,PendingMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);

        sendmoney_back_btn = findViewById(R.id.sendmoney_back_btn);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);

        cardBtnLayout = (RelativeLayout) findViewById(R.id.radioLayout1);
        upiBtnLayout = (RelativeLayout) findViewById(R.id.radioLayout3);
        NetBankingBtnLayout = (RelativeLayout) findViewById(R.id.radioLayout2);
        WalletBtnLayout = (RelativeLayout) findViewById(R.id.radioLayout4);

        cardLayout = (LinearLayout) findViewById(R.id.card_layout);
        UpiLayout = (LinearLayout) findViewById(R.id.upi_layout);
        //NetBankingLayout = (LinearLayout) findViewById(R.id.card_layout);
        // WalletLayout = (LinearLayout) findViewById(R.id.wallet_layout);


        payfromwalletOption = (TextView) findViewById(R.id.payfromwalletOption);

        dialogPending = new Dialog(PaymentTypeActivity.this, android.R.style.Theme_Light);
        dialogPending.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPending.setContentView(R.layout.pending_recharge);
        dialogPending.setCancelable(false);

        try{
            PendingNote=dialogPending.findViewById(R.id.pendingnote);
            PendingRechargeAmount=dialogPending.findViewById(R.id.rec_amtPending);
            PendingMobile=dialogPending.findViewById(R.id.mob_nopending);
        }catch (Exception e){
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        proceed_card = (Button) findViewById(R.id.btn_addMoney_card);
        btn_proceed_netBanking = (Button) findViewById(R.id.btn_proceed_netBanking);
        proceed_upi = (Button) findViewById(R.id.btn_pay_upi);
        proceed_wallet = (Button) findViewById(R.id.btn_proceed_wallet);


        //  btn_addMoney = findViewById(R.id.btn_addMoney);
        et_cardNumber = findViewById(R.id.et_cardNumber);
        et_year = findViewById(R.id.et_year);
        et_month = findViewById(R.id.et_month);
        et_cvv = findViewById(R.id.et_cvv);
        et_name = findViewById(R.id.et_name);
        et_upi = findViewById(R.id.et_upi);


        heading = findViewById(R.id.heading);
        wallet_amt = findViewById(R.id.wallet_amt_pay);
        RadioButton walletButton = (RadioButton) findViewById(R.id.wallet);

        progressBar = findViewById(R.id.progressBar);
        tv_walletAddedAmount = findViewById(R.id.tv_walletAddedAmount);
        // orderId1 = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();


        radioLayout4 = (RelativeLayout) findViewById(R.id.radioLayout4);


        Intent intent = getIntent();
        paymentFor = intent.getStringExtra("paymentFor"); // To ensure payment is for recharge or other


        if (paymentFor.equals("wallet")) {
            // Amount to accept from the payment
            amountToAddInWallet = intent.getStringExtra("amountToAdd");
            tv_walletAddedAmount.setText("₹ " + amountToAddInWallet);
            heading.setText("Add Money to Wallet");
            walletButton.setVisibility(View.INVISIBLE);
            wallet_amt.setVisibility(View.INVISIBLE);
            radioLayout4.setVisibility(View.INVISIBLE);
        } else if (paymentFor.equals("dthrecharge")) {
            heading.setText("Pay for DTH Recharge");
            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("customerid");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);
            try{
                PendingNote.setText("DTH Recharge Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (paymentFor.equals("waterbill")) {
            heading.setText("Pay for Water");
            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("customerid");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);

            try{
                PendingNote.setText("Water Bill Payment Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (paymentFor.equals("electricityrecharge")) {
            heading.setText("Pay for Electricity");
            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("customerid");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);

            try{
                PendingNote.setText("Electricity Bill Payment Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }

                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (paymentFor.equals("gasrecharge")) {
            heading.setText("Pay for Gas");
            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("customerid");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);

            try{
                PendingNote.setText("Gas Bill Payment Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (paymentFor.equals("landlinerecharge")) {
            heading.setText("Pay for Landline");
            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("customerid");
            StdCode = intent.getStringExtra("stdcode");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeOptional1 = intent.getStringExtra("optional1"); //  Optional
            rechargeOptional2 = intent.getStringExtra("optional2"); // Optional
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);
            if (rechargeOperator == "45") {
                accountNo = intent.getStringExtra("account_number");
            }
            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if(paymentFor.equals("billPay")){
            heading.setText("Bill Pay");

            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("mob");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);

            try{
                PendingNote.setText("Bill Payment Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }
            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else {
            heading.setText("Pay for Recharge");

            rechargeAmount = intent.getStringExtra("rechargeAmount"); // Recharge Amount
            rechargeNumber = intent.getStringExtra("mob");
            rechargeOperator = intent.getStringExtra("opid"); // recharge Operator Id
            rechargeOptional1 = intent.getStringExtra("optional1"); //  Optional
            rechargeOptional2 = intent.getStringExtra("optional2"); // Optional
            rechargeType = intent.getStringExtra("rechType"); // RechargeType
            tv_walletAddedAmount.setText("₹ " + rechargeAmount);
            wallet_amt.setText(rechargeNumber);

            try{
                PendingNote.setText("Recharge Pending\nPlease Wait");
                PendingRechargeAmount.setText(rechargeAmount);
                PendingMobile.setText(rechargeNumber);
            }catch (Exception e){
                e.printStackTrace();
            }
            try {

                if (Integer.parseInt(rechargeAmount) < balance) {

                    radioLayout4.setVisibility(View.INVISIBLE);
                }
                UserDetails userDetails = new UserDetails();
                userDetails.execute();

                GetWalletDetails getWalletDetails = new GetWalletDetails();
                getWalletDetails.execute();

                LastTransactionId lastTransactionId = new LastTransactionId();
                lastTransactionId.execute();
            } catch (Exception e) {
                Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        sendmoney_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


        cardBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(VISIBLE);
                UpiLayout.setVisibility(GONE);
                proceed_wallet.setVisibility(GONE);
                btn_proceed_netBanking.setVisibility(GONE);
            }
        });


        NetBankingBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(GONE);
                UpiLayout.setVisibility(GONE);
                proceed_wallet.setVisibility(GONE);
                btn_proceed_netBanking.setVisibility(VISIBLE);
            }
        });

        upiBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(GONE);
                UpiLayout.setVisibility(VISIBLE);
                proceed_wallet.setVisibility(GONE);
                btn_proceed_netBanking.setVisibility(GONE);
            }
        });

        WalletBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(GONE);
                UpiLayout.setVisibility(GONE);
                proceed_wallet.setVisibility(VISIBLE);
                btn_proceed_netBanking.setVisibility(GONE);
            }
        });

        proceed_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cardNumber.getText().toString().length() < 16) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Card Number", Toast.LENGTH_LONG).show();

                } else if (et_year.getText().toString().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Enter Valid year", Toast.LENGTH_LONG).show();
                } else if (et_month.getText().toString().length() < 2) {
                    Toast.makeText(getApplicationContext(), "Enter Valid month", Toast.LENGTH_LONG).show();

                } else if (et_cvv.getText().toString().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Enter Valid cvv", Toast.LENGTH_LONG).show();

                } else if (et_name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Valid name", Toast.LENGTH_LONG).show();

                } else {

                    paymentMode = "card";

                    GenerateToken generateToken = new GenerateToken();
                    generateToken.execute();

                    // triggerPayment(true);

                }

            }
        });

        btn_proceed_netBanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentMode = "nb";
                operation = "debit";
                GenerateToken generateToken = new GenerateToken();
                generateToken.execute();


                //  triggerPayment(true);


            }
        });
        proceed_upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_upi.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter valild Upi", Toast.LENGTH_LONG).show();
                } else {

                    paymentMode = "upi";
                    GenerateToken generateToken = new GenerateToken();
                    generateToken.execute();


                    //triggerPayment(true);
                }

            }
        });
        proceed_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(PaymentTypeActivity.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(PaymentTypeActivity.this).getUser().getPasscode().equals("")) {
                    CreatePasscodeDialog dialog = new CreatePasscodeDialog(PaymentTypeActivity.this);
                    dialog.show();
                } else {
                    PasscodeDialog passcodeDialog = new PasscodeDialog(PaymentTypeActivity.this, PaymentTypeActivity.this, "");
                    passcodeDialog.show();
                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);

    }

    @Override
    public void onPasscodeMatch(boolean isPasscodeMatched) {
        if (isPasscodeMatched) {


            if (balance >= Integer.parseInt(rechargeAmount)) {

                rechFromWallet = true;

                dialogPending.show();

                if (paymentFor.equals("recharge")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Mobile Recharge", "debit");
                    createWalletTransaction.execute();
                } else if (paymentFor.equals("dthrecharge")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "DTH Recharge", "debit");
                    createWalletTransaction.execute();
                } else if (paymentFor.equals("electricityrecharge")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Payment for Electricity", "debit");
                    createWalletTransaction.execute();
                } else if (paymentFor.equals("gasrecharge")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Payment for Gas", "debit");
                    createWalletTransaction.execute();
                } else if (paymentFor.equals("gasrecharge")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Payment for Landline", "debit");
                    createWalletTransaction.execute();
                } else if (paymentFor.equals("waterbill")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Payment for Water", "debit");
                    createWalletTransaction.execute();
                }else if (paymentFor.equals("billPay")) {
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(rechargeAmount, "Success", walletId, user_id, "Payment for Postpaid/Landline/Broadband", "debit");
                    createWalletTransaction.execute();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Not Enough Money in wallet", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class GenerateToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            Date date = new Date();
            long time = date.getTime();
            orderId = String.valueOf(time);

            params.put("orderid", orderId);

            if (paymentFor.equals("wallet")) {
                params.put("order_amount", amountToAddInWallet);
            }
//            else if (paymentFor.equals("dthrecharge")){
//                params.put("order_amount", rechargeAmount);
//            }
//
            else {
                params.put("order_amount", rechargeAmount);
                Log.e("aaaaaaaaaaa", rechargeOperator);
            }

            params.put("order_currency", "INR");

            return requestHandler.sendPostRequest(URLs.URL_CASHFREETOKEN, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.optString("status").equalsIgnoreCase("ok")) {
                    cfToken = obj.optString("cftoken");

                    if (paymentMode.equals("upi")) {
                        triggerPayment(false);
                    } else {
                        triggerPayment(false);
                    }


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
        String orderIdtoSend = orderId;
        if (amountToAddInWallet == null) {
            orderAmount = rechargeAmount;
        } else {
            orderAmount = amountToAddInWallet;
        }

        String orderNote;
        if (paymentFor.equalsIgnoreCase("wallet")) {
            orderNote = "Wallet topup";
        } else {
            orderNote = "Recharge";
        }
        String customerName = SharedPrefManager.getInstance(getApplicationContext()).getUser().getFirst_name();
        String customerPhone = SharedPrefManager.getInstance(getApplicationContext()).getUser().getMobile();
        String customerEmail = SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail();


        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderIdtoSend);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
//        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        // params.put(PARAM_NOTIFY_URL,customerEmail);
        //params.put(PARAM_PAYMENT_OPTION, "paypal");


        if (paymentMode.equalsIgnoreCase("card")) {
            params.put(PARAM_PAYMENT_OPTION, "card");
            params.put(PARAM_CARD_NUMBER, et_cardNumber.getText().toString());//Replace Card number
            params.put(PARAM_CARD_MM, et_month.getText().toString()); // Card Expiry Month in MM
            params.put(PARAM_CARD_YYYY, et_year.getText().toString()); // Card Expiry Year in YYYY
            params.put(PARAM_CARD_HOLDER, et_name.getText().toString()); // Card Holder name
            params.put(PARAM_CARD_CVV, et_cvv.getText().toString());

        } else if (paymentMode.equalsIgnoreCase("upi")) {

            params.put(PARAM_PAYMENT_MODES, "upi");
            params.put(PARAM_UPI_VPA, et_upi.getText().toString());

        } else if (paymentMode.equalsIgnoreCase("nb")) {

            params.put(PARAM_PAYMENT_MODES, "nb");
            //   params.put(PARAM_BANK_CODE, "3003");

        }

        for (Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);


        // cfPaymentService.doPayment(this, params, token, this, stage);


        if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.upiPayment(this, params, token, this, stage);
        } else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage);
        }



        /*if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.upiPayment(this, params, token, this, stage);
        }
        else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage);
        }*/

    }

    public void doPayment() {
        this.triggerPayment(false);
    }

    public void upiPayment() {
        this.triggerPayment(true);
    }

//    public void doPayment(Context context, Map<String, String> params, String token, CFClientInterface callback, String stage) {
//        this.triggerPayment(true);
//    }

    @Override
    public void onSuccess(Map<String, String> params) {

        paymentStatus = true;
        dialogPending.show();
        if (paymentFor.equalsIgnoreCase("wallet")) { //  Add Money
            operation = "credit"; //Credit to wallet
            updateAmount = amountToAddInWallet;
            description = "WALLET_TOPUP";

            //UpdateWalletAmount updateWalletAmount = new UpdateWalletAmount();
            // updateWalletAmount.execute();


            CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(updateAmount, "Success", walletId, user_id, description, operation);
            //Toast.makeText(this, "Payment Success - Creating Transaction", Toast.LENGTH_SHORT).show();
            createWalletTransaction.execute();


        } else {
            operation = "debit";
            updateAmount = rechargeAmount;
            description = "Mobile Recharge";


            ProcessRecharge processRecharge = new ProcessRecharge();
            processRecharge.execute();


        }
    }

    @Override
    public void onFailure(Map<String, String> params) {
        //   Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        paymentStatus = false;
        dialogPending.show();
        if (paymentFor.equalsIgnoreCase("wallet")) {
            operation = "credit";
            updateAmount = amountToAddInWallet;
            description = "WALLET_TOPUP";


            CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(updateAmount, "Failed", walletId, user_id, description, operation);
            Toast.makeText(this, "Payment Failed - Creating Transaction", Toast.LENGTH_SHORT).show();
            createWalletTransaction.execute();

        } else {
            dialogPending.dismiss();
            operation = "debit";
            updateAmount = rechargeAmount;
            description = "Mobile Recharge";

            Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
            intent.putExtra("mob", rechargeNumber);
            intent.putExtra("amount", rechargeAmount);
            intent.putExtra("orderid", sendOrderId);
            startActivity(intent);
            finish();

        }


        //startActivity(new Intent(getApplicationContext(), AddMoneyFailed.class), pu);
    }

    @Override
    public void onNavigateBack() {
        paymentStatus = false;

        if (paymentFor.equalsIgnoreCase("wallet")) {

            Intent intent = (new Intent(PaymentTypeActivity.this, AddMoneyFailed.class));
            intent.putExtra("transactionid", sendOrderId);
            intent.putExtra("amount", amountToAddInWallet);

            startActivity(intent);
            finish();
        } else {


            Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
            intent.putExtra("mob", rechargeNumber);
            intent.putExtra("amount", rechargeAmount);
            intent.putExtra("orderid", sendOrderId);
            startActivity(intent);
            finish();

        }
    }


    //    RememberPassword rememberPassword = SharedPrefManagerLogin.getInstance(this).getUser();
    final String mobile = SharedPrefManager.getInstance(PaymentTypeActivity.this).getUser().getMobile();


    class UserDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            return requestHandler.sendPostRequest(URLs.USER_BY_MOBILE, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.getString("status").equals("Success")) {

                    JSONArray messageArray = obj.getJSONArray("msg");

                    JSONObject myUserDetails = messageArray.getJSONObject(0);

                    isBlocked = myUserDetails.optString("blocked").equals("1") ? true : false;


                    if (isBlocked) {
                        WalletBtnLayout.setVisibility(View.INVISIBLE);
                        payfromwalletOption.setVisibility(VISIBLE);

                    }

                } else {

                    Toast.makeText(getApplicationContext(), obj.getString("Error Occured. Please try again later."), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *
     * Class for getting Wallet details of user
     *
     * */

    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(VISIBLE);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());
            //params.put("operation", operation);
            // params.put("order_amount", amoutPaid);
            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_WALLETDETAILS, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");
                    walletId = jsonObject.optString("wallet_id");
                    balance = Double.parseDouble(jsonObject.optString("amount"));

                    isBlocked = jsonObject.optString("blocked").equals("1") ? true : false;
                    wallet_amt.setText(String.valueOf(balance));
                    System.out.println(jsonObject);


                    if (isBlocked) {
                        WalletBtnLayout.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ProcessRecharge extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String u = null;
            // Random random = new Random();
            // int userID = random.nextInt(10000);

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            String Urlll="https://portal.specificstep.com/neo/api?username=DL1331&password=9867967827&utransactionid=" +orderId+
                    "&circlecode="+"51"+"&operatorcode="+"13"+"&number=9811871855&amount=10.00";

            if (paymentFor.equals("recharge")) {
                u = "https://portal.specificstep.com/neo/api?username=DL1331&password=9867967827&utransactionid=" +orderId+
                        "&circlecode="+"51"+"&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else if (paymentFor.equals("dthrecharge")) {
                u = "https://portal.specificstep.com/neo/api?username=DL1331&password=9867967827&utransactionid=" +orderId+
                        "&circlecode="+"51"+"&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else if (paymentFor.equals("billPay")) {
                u = "https://portal.specificstep.com/neo/bbps?username=DL1331&password=9867967827&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else if (paymentFor.equals("electricityrecharge")) {
                u = "https://portal.specificstep.com/neo/bbps?username=DL1331&password=9867967827&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else if (paymentFor.equals("gasrecharge")) {
                u = "https://portal.specificstep.com/neo/bbps?username=DL1331&password=9867967827&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else if (paymentFor.equals("landlinerecharge")) {
                if (rechargeOperator == "45") {
                    u = "http://api.rechapi.com/recharge.php?format=json&token=PfdJmExNgH1RsEbsvauc&mobile=" + rechargeNumber + "&amount=" + rechargeAmount + "&opid=" + rechargeOperator + "&urid=" + orderId + "&opvalue1=" + StdCode + "&opvalue2=" + accountNo;
                } else {
                    u = "http://api.rechapi.com/recharge.php?format=json&token=PfdJmExNgH1RsEbsvauc&mobile=" + rechargeNumber + "&amount=" + rechargeAmount + "&opid=" + rechargeOperator + "&urid=" + orderId + "&opvalue1=" + StdCode;
                }
            } else if (paymentFor.equals("waterbill")) {
                u = "https://portal.specificstep.com/neo/bbps?username=DL1331&password=9867967827&operatorcode="+rechargeOperator+"&number="+rechargeNumber+"&amount="+rechargeAmount;
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            }
            //params.put("order_currency", "INR");
            System.out.println(u);
            return requestHandler.sendGetRequest(u, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            System.out.println(s.getClass());
            System.out.println(s);
            String mobile1="",amount1="";

            Log.e("aaaaaaaaaaaaaa", "aaa");

            try {
                Log.e("bbbbbbbbbbbbb", "aaa");

                JSONObject response = new JSONObject(s);

                Log.e("cccccccccccccc", "aaa");
                try{
                    String recievedorderID = response.getString("TransactionID");
                    String UtransactionID = response.getString("UtransactionID");
                    String operatorId = response.getString("OperatorID");
                    String status = response.getString("Status");
                    String ResposneMessage= response.getString("ResposneMessage");
                    try{
                        mobile1 = response.getString("Number");
                    }catch (Exception e){
                        e.printStackTrace();
                        mobile1 = response.getString("number");
                    }
                    try{
                        amount1 = response.getString("Amount");
                    }catch (Exception e){
                        e.printStackTrace();
                        amount1 = response.getString("amount");
                    }

                    System.out.println(status);


                    rechargeStatus = ResposneMessage.toLowerCase();
                    ResposneMsg= ResposneMessage.toLowerCase();
                    sendOrderId = recievedorderID;

                    if (TextUtils.isEmpty(recievedorderID)){
                        rechargeStatus="failed";
                    }else {
                        if (status.equals("1")){
                            rechargeStatus="success";
                        }else {
                            rechargeStatus="pending";
                        }
                    }
                    CreateRechargeRecords createRechargeRecords = new CreateRechargeRecords(amount1, rechargeType, rechargeStatus , rechargeNumber, user_id, recievedorderID);
                    createRechargeRecords.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("dddddddddddddd", "aaa");
                Toast.makeText(PaymentTypeActivity.this, "Exception Occured", Toast.LENGTH_SHORT).show();

            }
        }
    }

    class CreateRechargeRecords extends AsyncTask<String, String, String> {

        String rechargeAmount = null;
        String rechargeType = null;
        String rechargeStatus = null;
        String customerNumber = null;
        String userId = null;
        String orderId = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(VISIBLE);
//            progressDialog.show();
            System.out.println("Save recharge");
        }

        public CreateRechargeRecords(String rechargeAmount, String rechargeType, String rechargeStatus, String customerNumber, String userId, String orderId) {
            this.rechargeAmount = rechargeAmount;
            this.rechargeType = rechargeType;
            this.rechargeStatus = rechargeStatus;
            this.customerNumber = customerNumber;
            this.userId = userId;
            this.orderId = orderId;
            Log.e("eeeeeeeeeeeee", "aaa");

        }


        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("amount", rechargeAmount);
            params.put("status", rechargeStatus);
            params.put("customer_number", customerNumber);
            params.put("userid", user_id);
            params.put("rech_type", rechargeType);
            params.put("order_id", orderId);
            Log.e("ffffffffffff", "aaa");

            return requestHandler.sendPostRequest(URLs.URL_SAVE_RECH_DETAILS, params);
        }
        @Override
        protected void onPostExecute(String result) {

            System.out.println(result);

            progressDialog.dismiss();
            if (result != null) {

                if (rechargeStatus.equalsIgnoreCase("success")){
                    dialogPending.dismiss();
                    Intent intent = (new Intent(PaymentTypeActivity.this, RechargeSuccess.class));
                    intent.putExtra("mob", rechargeNumber);
                    intent.putExtra("amount", rechargeAmount);
                    intent.putExtra("orderid", sendOrderId);
                    startActivity(intent);
                    finish();
                }else if (rechargeStatus.equalsIgnoreCase("pending")){
                    dialogPending.dismiss();
                    ShowAlertDialog("Recharge/Bill Payment Pending","\nRecharge/Bill Payment has been initiated.\nPlease wait for atleast 5 mins to 3 hours.\n" +
                            "Sorry for the inconvenience\n");
                }else {
                    Refund refund = new Refund(rechargeAmount, "Success", walletId, user_id, "Recharge Refund", "credit");
                    refund.execute();
                    if (ResposneMsg.contains("five minutes")){
                        ShowAlertDialog("Recharge/Bill Payment Failed","\nPlease leave an interval of five minutes before recharging the same number"
                        +"\nSorry for the inconvenience\n");
                    }else {
                        dialogPending.dismiss();
                        Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
                        intent.putExtra("mob", rechargeNumber);
                        intent.putExtra("amount", rechargeAmount);
                        intent.putExtra("orderid", sendOrderId);
                        startActivity(intent);
                        finish();
                    }
                }

//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    Log.e("gggggggggggg", "aaa");
//
//                    if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
//                        Toast.makeText(PaymentTypeActivity.this, "Recharge saved.", Toast.LENGTH_SHORT).show();
//
//                        if (rechargeStatus.equalsIgnoreCase("success")) {
//                            dialogPending.dismiss();
//                            Intent intent = (new Intent(PaymentTypeActivity.this, RechargeSuccess.class));
//                            intent.putExtra("mob", rechargeNumber);
//                            intent.putExtra("amount", rechargeAmount);
//                            intent.putExtra("orderid", sendOrderId);
//                            startActivity(intent);
//                            finish();
//                            Log.e("hhhhhhhhhhh", "aaa");
//
//
//                        } else if (rechargeStatus.equalsIgnoreCase("pending")) {
//                            Toast.makeText(PaymentTypeActivity.this, "Pending", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            dialogPending.dismiss();
//                            Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
//                            intent.putExtra("mob", rechargeNumber);
//                            intent.putExtra("amount", rechargeAmount);
//                            intent.putExtra("orderid", sendOrderId);
//                            startActivity(intent);
//                            finish();
//
//                            Refund refund = new Refund(rechargeAmount, "Success", walletId, user_id, "Recharge Refund", "credit");
//                            refund.execute();
//
//
//                        }
//
//                        //yhn agar status success h to success ka toast dikha dio ni to failed ka b
//                    } else {
//                        Toast.makeText(PaymentTypeActivity.this, "Cannot save recharge.", Toast.LENGTH_SHORT).show();
//
//
//                        Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
//                        intent.putExtra("mob", rechargeNumber);
//                        intent.putExtra("amount", rechargeAmount);
//                        intent.putExtra("orderid", sendOrderId);
//                        startActivity(intent);
//                        finish();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("xxxxxxx", result);
//                    Log.e("yyyyyyy", String.valueOf(e));
//
//                }
            } else {
                Toast.makeText(PaymentTypeActivity.this, "Response error.", Toast.LENGTH_SHORT).show();
                Log.e("kkkkkkkkkkkk", "aaa");

            }
        }
    }


    class CreateWalletTransaction extends AsyncTask<String, String, String> {

        String amount;
        String status;
        String walletId;
        String userId;
        String description;
        String operation; // Credit or Debit

        public CreateWalletTransaction(String amount, String status, String walletId, String userId, String description, String operation) {
            this.amount = amount;
            this.status = status;
            this.walletId = walletId;
            this.userId = userId;
            this.description = description;
            this.operation = operation;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(VISIBLE);
//            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("amount", this.amount);
            params.put("status", this.status);
            params.put("wallet_id", this.walletId);
            params.put("userid", this.userId);
            params.put("description", this.description);
            params.put("operation", this.operation);

            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET_TRANSC, params);
        }

        @Override
        protected void onPostExecute(String result) {
            // progressBar.setVisibility(GONE);
//            progressDialog.dismiss();
            System.out.println(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    //Toast.makeText(PaymentTypeActivity.this, "Wallet Transaction Created.", Toast.LENGTH_SHORT).show();


                    if (paymentFor.equalsIgnoreCase("wallet")) {


                        dialogPending.dismiss();
                        if (paymentStatus) {
                            Intent intent = (new Intent(PaymentTypeActivity.this, AddMoneySuccess.class));
                            intent.putExtra("transactionid", jsonObject.optString("msg"));
                            intent.putExtra("amount", amountToAddInWallet);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = (new Intent(PaymentTypeActivity.this, AddMoneyFailed.class));
                            intent.putExtra("transactionid", jsonObject.optString("msg"));
                            intent.putExtra("amount", amountToAddInWallet);
                            startActivity(intent);
                            finish();
                        }


                    } else {

                        if (rechFromWallet == true) {


                            ProcessRecharge processRecharge = new ProcessRecharge();
                            processRecharge.execute();


                        } else {


                        }

//                         if(rechargeStatus.equalsIgnoreCase("success")){
//
//                             Intent intent = (new Intent(PaymentTypeActivity.this, RechargeSuccess.class));
//                             intent.putExtra("mob", rechargeNumber);
//                             intent.putExtra("amount", rechargeAmount);
//                             intent.putExtra("orderid", sendOrderId);
//                             startActivity(intent);
//                             finish();
//
//                         }else{
//
//                             Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
//                             intent.putExtra("mob", rechargeNumber);
//                             intent.putExtra("amount", rechargeAmount);
//                             intent.putExtra("orderid", sendOrderId);
//                             startActivity(intent);
//                             finish();
//
//                         }

                    }


                } else {
                    Toast.makeText(PaymentTypeActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

                    if (paymentFor.equalsIgnoreCase("wallet")) {

                        Intent intent = (new Intent(PaymentTypeActivity.this, AddMoneyFailed.class));
                        intent.putExtra("transactionid", jsonObject.optString("msg"));
                        intent.putExtra("amount", amountToAddInWallet);
                        startActivity(intent);
                        finish();
                    } else {


                        if (rechFromWallet == true) {

                            Intent intent = (new Intent(PaymentTypeActivity.this, RechargeFailed.class));
                            intent.putExtra("mob", rechargeNumber);
                            intent.putExtra("amount", rechargeAmount);
                            intent.putExtra("orderid", sendOrderId);
                            startActivity(intent);
                            finish();
                        }


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                builder.setTitle("Recharge Detail")
                        .setMessage("Error In Processing")
//                        .setMessage(result+"\n\n"+e.toString())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        //Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
    }


    class Refund extends AsyncTask<String, String, String> {

        String amount;
        String status;
        String walletId;
        String userId;
        String description;
        String operation; // Credit or Debit

        public Refund(String amount, String status, String walletId, String userId, String description, String operation) {
            this.amount = amount;
            this.status = status;
            this.walletId = walletId;
            this.userId = userId;
            this.description = description;
            this.operation = operation;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(VISIBLE);
//            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("amount", this.amount);
            params.put("status", this.status);
            params.put("wallet_id", this.walletId);
            params.put("userid", this.userId);
            params.put("description", this.description);
            params.put("operation", "credit");

            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET_TRANSC, params);
        }

        @Override
        protected void onPostExecute(String result) {
            // progressBar.setVisibility(GONE);
//            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    Toast.makeText(PaymentTypeActivity.this, "Amount Refunded", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(PaymentTypeActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();


                }

            } catch (JSONException e) {
                e.printStackTrace();
                builder.setTitle("Recharge Detail")
                        .setMessage("Error In Processing")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        //Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
    }

    /*
     *
     * Class to get last Transaction id
     *
     * */

    public class LastTransactionId extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            // HashMap<String, String> params = new HashMap<>();
            //params.put("userid", SharedPrefManager.getInstance(appContext).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_GET_LAST_WALLET_TNX_ID);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // progressBar.setVisibility(GONE);
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                    lastTrnxId = jsonObject1.getString("transaction_id");

                    System.out.println(lastTrnxId);
                    orderId = String.valueOf(Long.parseLong(lastTrnxId) + 1);
                    //walletMoney.setText(jsonObject1.optString("amount"));
                }
            } catch (Exception e) {
                // e.printStackTrace();
                lastTrnxId = null;
            }
        }
    }

    private void ShowAlertDialog(String Heading,String ResponseMessage){
        new AlertDialog.Builder(PaymentTypeActivity.this)
                .setTitle(Heading)
                .setMessage(ResponseMessage)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
                .setCancelable(false);
    }

}
