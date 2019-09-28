package com.safepayu.wallet.Activity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.safepayu.wallet.AddMoneyFailed;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.view.View.VISIBLE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_UPI_VPA;

public class WalletUpiPayment extends AppCompatActivity implements CFClientInterface {
    private EditText et_upi;
    private Button btn_pay;
    private String amountToAddInWallet;
    private ProgressBar progressBar;
    private long orderId1;
    private String cfToken;
    private String walletId;
    private Button sendmoney_back_btn;
    private String user_id;
    AlertDialog.Builder builder;
    private LoginUser loginUser;
    private String rechargeAmount;
    private String mobile;
    private String operator_id;
    private String orderAmount;
    String operation = "null";
    String updateAmount = "null";
    String description = "null";
    private String rechargeStatus,ResposneMsg="",sendOrderId;
    private Dialog dialogPending;
    TextView PendingNote,PendingRechargeAmount,PendingMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_upi_payment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();

        et_upi = findViewById(R.id.et_upi);
        btn_pay = findViewById(R.id.btn_pay);
        sendmoney_back_btn = findViewById(R.id.sendmoney_back_btn);
        progressBar = findViewById(R.id.progressBar);
        orderId1 = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        Intent intent = getIntent();
        amountToAddInWallet = intent.getStringExtra("amountToAdd");
        walletId = intent.getStringExtra("walletId");
        rechargeAmount = intent.getStringExtra("rechargeAmount");
        mobile = intent.getStringExtra("mob");
        operator_id = intent.getStringExtra("opid");
        operation = intent.getStringExtra("operation");

        dialogPending = new Dialog(WalletUpiPayment.this, android.R.style.Theme_Light);
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


        GenerateToken generateToken = new GenerateToken();
        generateToken.execute();
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_upi.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter valild Upi", Toast.LENGTH_LONG).show();
                } else {
                    triggerPayment(true);
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
            if (amountToAddInWallet == null) {
                params.put("order_amount", rechargeAmount);
            } else {
                params.put("order_amount", amountToAddInWallet);
            }

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
                    // triggerPayment(true);
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
        if (amountToAddInWallet == null) {
            orderAmount = rechargeAmount;
        } else {
            orderAmount = amountToAddInWallet;
        }
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
       /* params.put(PARAM_PAYMENT_OPTION, "wallet");
        params.put(PARAM_BANK_CODE, "4007");*/

        params.put(PARAM_PAYMENT_OPTION, "upi");
        params.put(PARAM_UPI_VPA, et_upi.getText().toString());


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
        if (rechargeAmount == null) { // amountPaid if for add money
            // Recharge task
            operation = "credit"; //Credit to wallet
            updateAmount = amountToAddInWallet;
            description = "WALLET_TOPUP";

            RechargeMobile rechargeMobile = new RechargeMobile();
            rechargeMobile.execute();

        } else {

            //Only execute when add money is done
            // Wallet Task
            operation = "debit";
            updateAmount = rechargeAmount;
            description = "Mobile Recharge";
            UpdateWalletAmount updateWalletAmount = new UpdateWalletAmount();
            updateWalletAmount.execute();
        }

        CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(updateAmount,"Success",walletId, user_id, description, operation );
        createWalletTransaction.execute();


    }

    @Override
    public void onFailure(Map<String, String> params) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        if (rechargeAmount == null) {
            operation = "credit";
            updateAmount = amountToAddInWallet;
            description = "WALLET_TOPUP";
        } else {
            operation = "debit";
            updateAmount = rechargeAmount;
            description = "Mobile Recharge";
        }


        CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(updateAmount,"Success",walletId, user_id, description, operation );
        createWalletTransaction.execute();

    }

    @Override
    public void onNavigateBack() {

    }


    class UpdateWalletAmount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("wallet_id", walletId);
            if (amountToAddInWallet == null) {
                params.put("amount", rechargeAmount);
                params.put("operation", "debit");
            } else {
                params.put("amount", amountToAddInWallet);
                params.put("operation", "credit");
            }

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


                  //  startActivity(new Intent(getApplicationContext(), AddMoneySuccess.class));
//                    Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
//                    startActivity(intent);
//                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RechargeMobile extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            Random random = new Random();
            int userID = random.nextInt(10000);
            String orderID=String.valueOf(orderId1);

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();


            String u = "http://api.rechapi.com/recharge.php?format=json&token=c8PwyRoBqAU5Jf7juS4i&mobile=" + mobile +
                    "&amount=" + rechargeAmount + "&opid=" + operator_id + "&urid=" + userID;
            //params.put("order_currency", "INR");

            String url = "https://portal.specificstep.com/neo/api?username=DL1331&password=9867967827&utransactionid=" +orderID+
                    "&circlecode="+"51"+"&operatorcode="+operator_id+"&number="+mobile+"&amount="+rechargeAmount;
            return requestHandler.sendPostRequest(url, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            String mobile1="",amount1="";
            try {
                JSONObject response = new JSONObject(s);
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

                saveMobRechargeData(amount1, rechargeStatus, mobile, "Mobile", sendOrderId, user_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveMobRechargeData(final String amount, final String status, final String custNumber, final String rechargeType, final String orederId, final String userId) {
        //getidSpinner();
        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(VISIBLE);
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("amount", amount);
                params.put("status", status);
                params.put("customer_number", custNumber);
                params.put("userid", userId);
                params.put("rech_type", rechargeType);
                params.put("order_id", orederId);
                return requestHandler.sendPostRequest(URLs.URL_SAVE_RECH_DETAILS, params);
            }

            @Override
            protected void onPostExecute(String result) {
                progressBar.setVisibility(View.GONE);

                try{
                    if (rechargeStatus.equalsIgnoreCase("success")){
                        dialogPending.dismiss();
                        Intent intent = (new Intent(WalletUpiPayment.this, RechargeSuccess.class));
                        intent.putExtra("mob", custNumber);
                        intent.putExtra("amount", rechargeAmount);
                        intent.putExtra("orderid", sendOrderId);
                        startActivity(intent);
                        finish();
                    }else if (rechargeStatus.equalsIgnoreCase("pending")){
                        dialogPending.dismiss();
                        ShowAlertDialog("Recharge/Bill Payment Pending","\nRecharge/Bill Payment has been initiated.\nPlease wait for atleast 3 hours.\n" +
                                "Sorry for the inconvenience\n");
                    }else {
                        Refund refund = new Refund(rechargeAmount, "Success", walletId, user_id, "Recharge Refund", "credit");
                        refund.execute();
                        if (ResposneMsg.contains("five minutes")){
                            ShowAlertDialog("Recharge/Bill Payment Failed","\nPlease leave an interval of five minutes before recharging the same number");
                        }else {
                            dialogPending.dismiss();
                            Intent intent = (new Intent(WalletUpiPayment.this, RechargeFailed.class));
                            intent.putExtra("mob", custNumber);
                            intent.putExtra("amount", rechargeAmount);
                            intent.putExtra("orderid", sendOrderId);
                            startActivity(intent);
                            finish();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
//                        Toast.makeText(WalletUpiPayment.this, "Recharge Success", Toast.LENGTH_SHORT).show();
//                        //yhn agar status success h to success ka toast dikha dio ni to failed ka bs
//
//                        Intent intent = (new Intent(WalletUpiPayment.this, RechargeSuccess.class));
//                        intent.putExtra("mob", mobile);
//                        intent.putExtra("amount", rechargeAmount);
//                        finish();
//                    } else{
//
//                        Toast.makeText(WalletUpiPayment.this, "Recharge Failed", Toast.LENGTH_SHORT).show();
//
//                        Intent intent = (new Intent(WalletUpiPayment.this, RechargeFailed.class));
//                        intent.putExtra("mob", mobile);
//                        intent.putExtra("amount", rechargeAmount);
//                        finish();
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    builder.setTitle("Recharge Detail")
//                            .setMessage("Error In Processing")
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
            }
        }
        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
        ul.execute();


    }

    class CreateWalletTransaction extends AsyncTask<String, String, String> {

        String amount;
        String status;
        String walletId;
        String userId;
        String description;
        String operation; // Credit or Debit


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(VISIBLE);
        }

        public CreateWalletTransaction(String amount, String status, String walletId, String userId, String description, String operation) {
            this.amount = amount;
            this.status = status;
            this.walletId = walletId;
            this.userId = userId;
            this.description = description;
            this.operation = operation;
        }

        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("amount", amount);
            params.put("status", status);
            params.put("wallet_id", walletId);
            params.put("userid", userId);
            params.put("description", description);
            params.put("operation", operation);
            return requestHandler.sendPostRequest(URLs.URL_NEW_TRANSC, params);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    Toast.makeText(WalletUpiPayment.this, "Success", Toast.LENGTH_SHORT).show();

                    if(rechargeAmount == null){

                        Intent intent = (new Intent(WalletUpiPayment.this, AddMoneySuccess.class));
                        intent.putExtra("trnsactionid", jsonObject.optString("msg"));
                        intent.putExtra("amount", amountToAddInWallet);
                        startActivity(intent);
                        finish();

                    }else{


                    }

                } else
                {
                    Toast.makeText(WalletUpiPayment.this, "Error Occured", Toast.LENGTH_SHORT).show();

                    if(rechargeAmount == null){

                        Intent intent = (new Intent(WalletUpiPayment.this, AddMoneyFailed.class));
                        intent.putExtra("trnsactionid", jsonObject.optString("msg"));
                        intent.putExtra("amount", amountToAddInWallet);
                        startActivity(intent);
                        finish();

                    }else{


                    }

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
                    Toast.makeText(WalletUpiPayment.this, "Amount Refunded", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(WalletUpiPayment.this, "Error Occured", Toast.LENGTH_SHORT).show();


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

    private void ShowAlertDialog(String Heading,String ResponseMessage){
        new AlertDialog.Builder(WalletUpiPayment.this)
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

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
