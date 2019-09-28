package com.safepayu.wallet.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.CreatePasscodeDialog;
import com.safepayu.wallet.R;
import com.safepayu.wallet.utils.LoadingDialog;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;


public class Commission extends AppCompatActivity  {
    TextView current_referral_business, send_to_wallet, left_business, right_business, total_business, sponser_business, matching_business;
    LoadingDialog dialog;
    LinearLayout getMembershipLayout,withdraw_layout, get_mem_layout;
    EditText withdrawAmount;
    TextView text_2;
    ImageView close;
    boolean showCurrentScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);

        left_business = findViewById(R.id.left_business);
        send_to_wallet = findViewById(R.id.send_to_wallet);
        current_referral_business = findViewById(R.id.current_referral_business);
        right_business = findViewById(R.id.right_business);
        total_business = findViewById(R.id.total_business);
        sponser_business = findViewById(R.id.sponser_business);
        matching_business = findViewById(R.id.matching_business);
        getMembershipLayout = findViewById(R.id.layout_isMembership);
        getMembershipLayout.setVisibility(View.GONE);
        send_to_wallet.setVisibility(View.GONE);

        dialog = new LoadingDialog(this, "Loading...");

        Button backBtn = (Button) findViewById(R.id.send_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        send_to_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CustomDialogClass d = new CustomDialogClass(Commission.this);
//                d.setCancelable(false);
//                d.show();

                startActivity(new Intent(Commission.this,TransferCommissionToWallet.class));

            }
        });

        new CheckPackage().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetCommissionWallet().execute();
    }


    class GetCommissionWallet extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressbar.setVisibility(View.VISIBLE);
//            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());
            // params.put("order_amount", amoutPaid);
            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_GET_COMMISSION, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            dialog.dismiss();
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("Response Success")) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    if (jsonObject.optInt("rescode") == 200) {
                        JSONObject mainObj = jsonObject.getJSONObject("resdata");


                        String totleftbusiness = mainObj.optString("totleftbusiness");
                        String totrightbusiness = mainObj.optString("totrightbusiness");
                        String totmatchtbusiness = mainObj.optString("totmatchtbusiness");
                        String totdirectbusiness = mainObj.optString("totdirectbusiness");
                        String totmatchincome = mainObj.optString("totmatchincome");
                        String totdirectincome = mainObj.optString("totdirectincome");
                        String totincome = mainObj.optString("totincome");
                        String totwithdrawwallet = mainObj.optString("totwithdrawwallet");
                        String totcurrentwallet = mainObj.optString("totcurrentwallet");


                        current_referral_business.setText("₹" + totcurrentwallet);
//                        send_to_wallet.setText(totcurrentwallet);
                        left_business.setText("₹" + totleftbusiness);
                        right_business.setText("₹" + totrightbusiness);
                        total_business.setText("₹" + totincome);
                        sponser_business.setText("₹" + totdirectincome);
                        matching_business.setText("₹" + totmatchincome);


                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error Login user.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    class CheckPackage extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            //otp = otpNum;
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("userid", SharedPrefManager.getInstance(Commission.this).getUser().getUserid());
            return requestHandler.sendPostRequest(URLs.URL_GET_PACKAGES, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {

                    JSONObject userJson = jsonObject.getJSONObject("msg");
                    String package_name = userJson.getString("package_name");
                    String package_status = userJson.getString("status");


                    if (package_status.equalsIgnoreCase("APPROVED")) {
//                        send_to_wallet
                        send_to_wallet.setVisibility(View.VISIBLE);
                        getMembershipLayout.setVisibility(View.GONE);

                    } else {
                        getMembershipLayout.setVisibility(View.VISIBLE);
                        send_to_wallet.setVisibility(View.GONE);
                    }

                } else {
                    getMembershipLayout.setVisibility(View.VISIBLE);
                    send_to_wallet.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Server TimeOut", Toast.LENGTH_SHORT).show();
            }


        }
    }

    class TransferInWallet extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            //otp = otpNum;
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("userid", SharedPrefManager.getInstance(Commission.this).getUser().getUserid());
            params.put("amount", voids[0]);
            return requestHandler.sendPostRequest(URLs.URL_TRANSFER_IN_WALLET, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("Response Success")) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    if (jsonObject.optInt("rescode") == 200) {

                        new GetCommissionWallet().execute();
                        Toast.makeText(Commission.this, jsonObject.optString("resdata"), Toast.LENGTH_SHORT).show();

                    } else if (jsonObject.optInt("rescode") == 400) {

                        Toast.makeText(Commission.this, "Insufficient Amount In Commission Wallet", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;

        public Dialog d;
        public Button btn_send_to_wallet, cancel;
        TextView mem_txt;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_commission_amount_transfer_dialog);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            btn_send_to_wallet = (Button) findViewById(R.id.btn_send_to_wallet);
            close = findViewById(R.id.close);
            withdraw_layout = findViewById(R.id.withdraw_layout);
            get_mem_layout = findViewById(R.id.get_membership);
            mem_txt = findViewById(R.id.mem_txt);
            withdrawAmount = findViewById(R.id.withdrawAmount);
            text_2 = findViewById(R.id.text_2);


            btn_send_to_wallet.setOnClickListener(this);
            close.setOnClickListener(this);
            mem_txt.setOnClickListener(this);

//            new CheckPackage().execute();

            withdrawAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    NumberFormat formatter = new DecimalFormat("#0.00");
                    formatter.format(4.0);
                    if (TextUtils.isEmpty(s)) {
                        text_2.setText("");
                    } else {
                        text_2.setText(formatter.format(Double.parseDouble(s.toString())) + "- Fee = "
                                + (formatter.format(Double.parseDouble(s.toString()) - ((Double.parseDouble(s.toString()) * 10) / 100))));
                    }
                }
            });


            //no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.mem_txt:

                    Intent i = new Intent(c, Membership.class);
                    startActivity(i);
                    finish();
                    break;

                case R.id.btn_send_to_wallet:
                    if (withdrawAmount.getText().toString().trim().equals("") || Integer.parseInt(withdrawAmount.getText().toString().trim()) <= 0) {
                        withdrawAmount.setError("Enter valid amount");
                    } else {
//                        if (SharedPrefManager.getInstance(Commission.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(Commission.this).getUser().getPasscode().equals("")) {
//                            CreatePasscodeDialog passcodeDialog = new CreatePasscodeDialog(Commission.this);
//                            passcodeDialog.show();
//                        } else {
//                            if (SharedPrefManager.getInstance(Commission.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(Commission.this).getUser().getPasscode().equals("")) {
//                                CreatePasscodeDialog dialog = new CreatePasscodeDialog(Commission.this);
//                                dialog.show();
//                            } else {
//                                PasscodeDialog passcodeDialog = new PasscodeDialog(Commission.this, Commission.this, "wallet");
//                                passcodeDialog.show();
//                            }
//                        }

                    }
                    break;

                case R.id.close:
                    dismiss();
                    break;
            }

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showCurrentScreen = true;
//        if (SharedPrefManager.getInstance(Commission.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(Commission.this).getUser().getPasscode().equals("")) {
//            CreatePasscodeDialog dialog = new CreatePasscodeDialog(Commission.this);
//            dialog.show();
//        } else {
//            PasscodeDialog passcodeDialog = new PasscodeDialog(Commission.this, Commission.this, "");
//            passcodeDialog.show();
//        }
    }
}
