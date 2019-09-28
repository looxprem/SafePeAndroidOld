package com.safepayu.wallet.Activity;

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

public class TransferCommissionToWallet extends AppCompatActivity implements View.OnClickListener, PasscodeClickListener {
    Button btnSendMoneyToWallet,btnRetryOrBack;
    EditText withdrawAmount;
    LinearLayout withdrawView,messageView;
    TextView text_2,transferStatus,message,txtAmount;
    boolean isTransferSuccess = false;
    LinearLayout statusBack;
    LoadingDialog dialog;
    NumberFormat formatter = new DecimalFormat("#0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_commission_to_wallet);

        dialog = new LoadingDialog(this, "Please Wait...");

        withdrawAmount =findViewById(R.id.et_withdrawAmount);
        btnSendMoneyToWallet=findViewById(R.id.btn_sendToWallet);
        btnSendMoneyToWallet.setOnClickListener(this);

        withdrawView = findViewById(R.id.layout_withdraw);
        statusBack = findViewById(R.id.statusBack);
        messageView = findViewById(R.id.layout_messageView);
        text_2 = findViewById(R.id.text_2);
        transferStatus = findViewById(R.id.txtStatus);
        message = findViewById(R.id.tv_message);
        txtAmount = findViewById(R.id.txtAmount);
        btnRetryOrBack = findViewById(R.id.btn_retryOrBack);
        btnRetryOrBack.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        withdrawAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                formatter.format(4.0);
                if (TextUtils.isEmpty(s)) {
                    text_2.setText("");
                } else {
                    text_2.setText(formatter.format(Double.parseDouble(s.toString())) + " - Fee = "
                            + (formatter.format(Double.parseDouble(s.toString()) - ((Double.parseDouble(s.toString()) * 10) / 100))));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_sendToWallet:
                if (withdrawAmount.getText().toString().trim().equals("") || Integer.parseInt(withdrawAmount.getText().toString().trim()) <= 0) {
                    withdrawAmount.setError("Enter valid amount");
                }else if(Double.parseDouble(withdrawAmount.getText().toString())<100){
                    withdrawAmount.setError("mount must 100 or greater");
                } else {
                    if (SharedPrefManager.getInstance(TransferCommissionToWallet.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(TransferCommissionToWallet.this).getUser().getPasscode().equals("")) {
                        CreatePasscodeDialog dialog = new CreatePasscodeDialog(TransferCommissionToWallet.this);
                        dialog.show();
                    } else {
                        PasscodeDialog passcodeDialog = new PasscodeDialog(TransferCommissionToWallet.this, TransferCommissionToWallet.this, "wallet");
                        passcodeDialog.show();
                    }

                }
                break;
            case R.id.btn_retryOrBack:
//                if (isTransferSuccess){
//                    isTransferSuccess=false;
//                    withdrawView.setVisibility(View.VISIBLE);
//                    messageView.setVisibility(View.INVISIBLE);
//                    withdrawAmount.setText("");
//                }else{
//                    onBackPressed();
//                }
                finish();
                break;
        }
    }

    @Override
    public void onPasscodeMatch(boolean isPasscodeMatched) {
        if (isPasscodeMatched) {
            new TransferInWallet().execute(withdrawAmount.getText().toString().trim());
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

            params.put("userid", SharedPrefManager.getInstance(TransferCommissionToWallet.this).getUser().getUserid());
            params.put("amount", voids[0]);
            return requestHandler.sendPostRequest(URLs.URL_TRANSFER_IN_WALLET, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            withdrawView.setVisibility(View.GONE);
            messageView.setVisibility(View.VISIBLE);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("Response Success")) {
                    JSONObject jsonObject = obj.getJSONObject("data");
                    if (jsonObject.optInt("rescode") == 200) {
                        isTransferSuccess=true;
                        statusBack.setBackgroundColor(Color.parseColor("#43cd67"));
                        transferStatus.setText("Success");
                        message.setText("Transaction is successfull.");
                        txtAmount.setText("₹" + (formatter.format(Double.parseDouble(withdrawAmount.getText().toString()) - ((Double.parseDouble(withdrawAmount.getText().toString()) * 10) / 100))));

//                        Toast.makeText(TransferCommissionToWallet.this, jsonObject.optString("resdata"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.optInt("rescode") == 400) {
                        transferStatus.setText("Failed");
                        message.setText("Insufficient Amount In Commission Wallet");
                        btnRetryOrBack.setText("Back To Commission");
                        statusBack.setBackgroundColor(Color.parseColor("#FFCA28"));

//                        Toast.makeText(TransferCommissionToWallet.this, "Insufficient Amount In Commission Wallet", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    statusBack.setBackgroundColor(Color.parseColor("#EF5350"));
                    transferStatus.setText("Failed");
                    message.setText("Transaction failed. Please try again after sometime.");
                    btnRetryOrBack.setText("Back To Commission");
                    isTransferSuccess=true;
//                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                btnRetryOrBack.setText("Back To Commission");
                transferStatus.setVisibility(View.GONE);
                transferStatus.setText("Something went wrong please try again later");
            }


        }
    }
}
