package com.safepayu.wallet.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.AddBeneficiary;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.CreatePasscodeDialog;
import com.safepayu.wallet.R;
import com.safepayu.wallet.adapterpackage.UpiBenAdapter;
import com.safepayu.wallet.model.BankBeneficiaryModel;
import com.safepayu.wallet.model.UpiBenModel;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendMoneyActivity extends AppCompatActivity implements PasscodeClickListener {
    Activity activity;
    private ListView listViewUpiBen;
    private ArrayList<UpiBenModel> upiBenModelList;
    private List<UpiBenModel> simpleUpiBenModelList;
    private List<BankBeneficiaryModel> simpleBankBeneficiaryList;
    private Spinner spinner, bankBenSpinner;
    private Button send_back_btn;
    private String userid, lastTrnxId, orderId;
    private RelativeLayout paytmAddlayout, paytmSelectlayout, upiAddlayout, upiSelectLayout, bankAddLayout, bankSelectLayout;
    private Button paytmAddBtn, paytmSelectBtn, upiAddBtn, upiSelectBtn, bankAddBtn, bankSelectBtn, withdrawBtn;
    private TextView paytmNum, upiAdd, BeneficiaryId, bankName, bankAccountNum, bankIFSC, getMemText;
    private EditText withdrawAmount;
    private LinearLayout withdrawAmountlayout;
    private String PaytmBeneficiaryId = null;
    private String UPiBeneficiaryId = null;
    private String BankBeneficiaryId = null;
    private String paytm = null;
    private String upi = null;
    private String bankAccountNumber = null;
    private String bankIfsc = null;
    private String bankAccountName = null;
    private String walletId;
    private String SendBenid = null;
    private String mode = null;
    private String walletAmount = null;
    private String amountToWithdraw = null;
    private Double amountwithdrawn = null;
    private RadioButton upiRadioBtn, bankRadioBtn;
    private Boolean paymentStatus = false, pendingPayment = false;
    public ProgressDialog progressDialog;
    private LinearLayout getMemberShipLayout, withdrawlAreaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money2);

        activity = this;
        send_back_btn = findViewById(R.id.send_back_btn);

        send_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        progressDialog = new ProgressDialog(SendMoneyActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        paytmAddlayout = (RelativeLayout) findViewById(R.id.paytmBenAddLayout);
        paytmSelectlayout = (RelativeLayout) findViewById(R.id.paytmBenSelectLayout);
        upiAddlayout = (RelativeLayout) findViewById(R.id.upimBenAddlayout);
        upiSelectLayout = (RelativeLayout) findViewById(R.id.upiBenSelectLayout);
        bankAddLayout = (RelativeLayout) findViewById(R.id.BankBenAddlayout);
        bankSelectLayout = (RelativeLayout) findViewById(R.id.BankBenSelectLayout);
        withdrawAmountlayout = (LinearLayout) findViewById(R.id.withdrawAmountlayout);


        getMemberShipLayout = (LinearLayout) findViewById(R.id.get_membership);
        withdrawlAreaLayout = (LinearLayout) findViewById(R.id.withdrawl_area);

        spinner = (Spinner) findViewById(R.id.upiBenSpinner);
        bankBenSpinner = (Spinner) findViewById(R.id.bankBenSpinner);

        bankRadioBtn = (RadioButton) findViewById(R.id.bankradiobtn);
        upiRadioBtn = (RadioButton) findViewById(R.id.upiradiobtn);

        bankAddBtn = (Button) findViewById(R.id.BankBenAddBtn);
        paytmAddBtn = (Button) findViewById(R.id.paytmBenAddBtn);
        upiAddBtn = (Button) findViewById(R.id.upiBenAddBtn);

        bankSelectBtn = (Button) findViewById(R.id.BankBenSelectBtn);
        paytmSelectBtn = (Button) findViewById(R.id.paytmBenSelectBtn);
        upiSelectBtn = (Button) findViewById(R.id.upiBenSelectBtn);
        withdrawBtn = (Button) findViewById(R.id.btnWithdraw);

        paytmNum = (TextView) findViewById(R.id.paytmBenNumber);
        upiAdd = (TextView) findViewById(R.id.UpiBenNumber);
        bankName = (TextView) findViewById(R.id.BankBenName);
        bankAccountNum = (TextView) findViewById(R.id.BankBenNumber);
        getMemText = (TextView) findViewById(R.id.get_mem_txt);

        withdrawAmount = (EditText) findViewById(R.id.withdrawAmount);

        simpleUpiBenModelList = new ArrayList<UpiBenModel>();
        simpleBankBeneficiaryList = new ArrayList<BankBeneficiaryModel>();

        spinner.setVisibility(View.GONE);
        bankBenSpinner.setVisibility(View.GONE);


        getMemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMoneyActivity.this, Membership.class);
                startActivity(i);
                finish();

            }
        });


        upiAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                i.putExtra("purpose", "Upi");
                startActivity(i);

            }
        });


        paytmAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                i.putExtra("purpose", "Paytm");
                startActivity(i);

            }
        });

        bankAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                i.putExtra("purpose", "Bank");
                startActivity(i);

            }
        });


        upiSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                // i.putExtra("purpose", "Upi");
                //startActivity(i);

                withdrawAmountlayout.setVisibility(View.VISIBLE);
                withdrawBtn.setText("Withdraw To UPI");
                SendBenid = UPiBeneficiaryId;
                mode = "upi";

            }
        });
        paytmSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                // i.putExtra("purpose", "Upi");
                //startActivity(i);

                withdrawAmountlayout.setVisibility(View.VISIBLE);
                withdrawBtn.setText("Withdraw To Paytm");
                SendBenid = PaytmBeneficiaryId;
                mode = "paytm";

            }
        });

        bankSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SendMoneyActivity.this, AddBeneficiary.class);
                // i.putExtra("purpose", "Upi");
                //startActivity(i);

                withdrawAmountlayout.setVisibility(View.VISIBLE);
                withdrawBtn.setText("Withdraw To Bank");
                SendBenid = BankBeneficiaryId;
                mode = "banktransfer";

            }
        });


        withdrawAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

                TextView calculatedAmount = (TextView) findViewById(R.id.calculatedamount);

                if (!withdrawAmount.getText().toString().equals("")) {

                    Double value = Double.parseDouble(withdrawAmount.getText().toString());
                    Double tax = 0.036 * value > 10 ? 0.036 * value : 10;

                    calculatedAmount.setText("" + value.toString() + " - Tax = " + (value - tax) + "");

                }


            }

            @Override
            public void afterTextChanged(final Editable s) {

                TextView calculatedAmount = (TextView) findViewById(R.id.calculatedamount);

                if (!withdrawAmount.getText().toString().equals("")) {

                    Double value = Double.parseDouble(withdrawAmount.getText().toString());

                    Double tax = 0.036 * value > 10 ? 0.036 * value : 10;
                    calculatedAmount.setText("" + value.toString() + " - Tax = " + (value - tax) + "");

                }


            }
        });


        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(SendMoneyActivity.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(SendMoneyActivity.this).getUser().getPasscode().equals("")) {
                    CreatePasscodeDialog dialog = new CreatePasscodeDialog(SendMoneyActivity.this);
                    dialog.show();
                } else {
                    PasscodeDialog passcodeDialog = new PasscodeDialog(SendMoneyActivity.this, SendMoneyActivity.this, "");
                    passcodeDialog.show();
                }
//                PasscodeDialog passcodeDialog = new PasscodeDialog(SendMoneyActivity.this, SendMoneyActivity.this, "");
//                passcodeDialog.show();
            }
        });


        userid = SharedPrefManager.getInstance(this).getUser().getUserid();


        LastTransactionId lastTransactionId = new LastTransactionId();
        lastTransactionId.execute();

        GetWalletDetails getWalletDetails = new GetWalletDetails();
        getWalletDetails.execute();

        GetUpiBeneficiary getUpiBeneficiary = new GetUpiBeneficiary();
        getUpiBeneficiary.execute();

        GetBankBeneficiary getBankBeneficiary = new GetBankBeneficiary();
        getBankBeneficiary.execute();

        CheckPackage getPackageDetails = new CheckPackage();
        getPackageDetails.execute();


        // Log.i("BENID", getPaytmBeneficiary.getBeneficiaryId().toString());
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.bankradiobtn:
                if (checked)
                    spinner.setVisibility(View.GONE);
                bankBenSpinner.setVisibility(View.VISIBLE);
                bankBenSpinner.setSelection(0);

                withdrawBtn.setText("Withdraw To Bank");
                SendBenid = "";

                break;
            case R.id.upiradiobtn:
                if (checked)

                    bankBenSpinner.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                spinner.setSelection(0);
                withdrawBtn.setText("Withdraw To UPI");
                SendBenid = "";

                break;
        }
    }

    @Override
    public void onPasscodeMatch(boolean isPasscodeMatched) {
        if (isPasscodeMatched) {
            String getWalletAmount = withdrawAmount.getText().toString();

            if (SendBenid.equals("")) {
                Toast.makeText(getApplicationContext(), "Select Beneficiary", Toast.LENGTH_LONG).show();
            } else {

                if (getWalletAmount.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter amount", Toast.LENGTH_LONG).show();

                } else {
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Double tax = 0.036 * Double.parseDouble(getWalletAmount) > 10 ? 0.036 * Double.parseDouble(getWalletAmount) : 10;

                    amountwithdrawn = Double.parseDouble(getWalletAmount) - tax;
                    amountToWithdraw = getWalletAmount;

//                double amount = Double.parseDouble(withdrawAmount.getText().toString());
//
                    if (Double.parseDouble(walletAmount) < amountwithdrawn || Double.parseDouble(getWalletAmount) < 100) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Insufficient wallet amount", Toast.LENGTH_LONG).show();
                    } else {
                        SendTransferRequest transferRequest = new SendTransferRequest();
                        transferRequest.execute();
                    }
                }
            }
        }
    }

    public class CheckLimit extends AsyncTask<String, String, String> {

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
            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.CHECK_LIMIT, params);

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();


            Log.i("RESULT_PAYTM", result);
            try {
                JSONObject obj = new JSONObject(result);

                if (obj.optString("status").equalsIgnoreCase("Success")) {

                    JSONObject message = obj.optJSONObject("msg");
                    PaytmBeneficiaryId = message.getString("benId");
                    paytm = message.getString("paytm");

                    if (paytm == null) {
                        paytmSelectlayout.setVisibility(View.GONE);
                    } else {
                        paytmAddlayout.setVisibility(View.GONE);
                        paytmNum.setText(paytm);
                    }

                } else {
                    paytmSelectlayout.setVisibility(View.GONE);
                    PaytmBeneficiaryId = null;
                    paytm = null;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public class GetPaytmBeneficiary extends AsyncTask<String, String, String> {

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
            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.URL_GET_PAYTM_BEN, params);

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();


            Log.i("RESULT_PAYTM", result);


            try {
                JSONObject obj = new JSONObject(result);

                if (obj.optString("status").equalsIgnoreCase("Success")) {

                    JSONObject message = obj.optJSONObject("msg");
                    PaytmBeneficiaryId = message.getString("benId");
                    paytm = message.getString("paytm");

                    if (paytm == null) {
                        paytmSelectlayout.setVisibility(View.GONE);
                    } else {
                        paytmAddlayout.setVisibility(View.GONE);
                        paytmNum.setText(paytm);
                    }

                } else {
                    paytmSelectlayout.setVisibility(View.GONE);
                    PaytmBeneficiaryId = null;
                    paytm = null;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    //    public class GetUpiBeneficiary extends AsyncTask<String,String, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //progressBar_WalletHistory.setVisibility(View.VISIBLE);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... param) {
//            RequestHandler requestHandler = new RequestHandler();
//            HashMap<String, String> params = new HashMap<>();
//            params.put("userid", userid);
//            return requestHandler.sendPostRequest(URLs.URL_GET_UPI_BEN,params);
//
//        }
//        @Override
//        protected void onPostExecute(String result){
//            // initialize
//            progressDialog.dismiss();
//
//            Log.i("RESULT_UPI", result);
//            try {
//                JSONObject obj = new JSONObject(result);
//
//                if(obj.optString("status").equalsIgnoreCase("Success")){
//
//                    JSONObject message = obj.optJSONObject("msg");
//                    UPiBeneficiaryId = message.getString("benId");
//                    upi = message.getString("upi");
//
//
//                    if(upi == null){
//                        upiSelectLayout.setVisibility(View.GONE);
//                    }else{
//                        upiAddlayout.setVisibility(View.VISIBLE);
//                        upiAdd.setText(upi);
//                    }
//
//
//                }else{
//                    upiSelectLayout.setVisibility(View.GONE);
//                    UPiBeneficiaryId = null;
//                    upi = null;
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
    public class GetUpiBeneficiary extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            progressDialog.show();
            //progressBar_WalletHistory.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //System.out.println("Number: -"+number);
            params.put("userid", userid);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_GET_UPI_BEN, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  Log.i("ERRORFORCHECKMOBILE", s);
            // System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            //  System.out.print(s.getClass());

            progressDialog.dismiss();
            //progressBar_WalletHistory.setVisibility(View.INVISIBLE);

            // Log.e("Name",response);
            try {
                JSONObject responseObject = new JSONObject(s);

                if (responseObject.getString("status").equalsIgnoreCase("Success")) {


                    JSONArray messageObject = responseObject.getJSONArray("msg");
                    System.out.println(messageObject);

                    // List<UpiBenModel> categories = new ArrayList<UpiBenModel>();

                    UpiBenModel upiBenModel1 = new UpiBenModel("BENID", "SELECT UPI");
                    simpleUpiBenModelList.add(upiBenModel1);

                    for (int i = 0; i < messageObject.length(); i++) {

                        JSONObject beneficiary = messageObject.getJSONObject(i);


                        UpiBenModel upiBenModel = new UpiBenModel(
                                beneficiary.getString("benId"), //beneficiary id
                                beneficiary.getString("upi") // beneficiary upi address
                        );

                        simpleUpiBenModelList.add(upiBenModel);


                    }

                    ArrayAdapter<UpiBenModel> upiBenModelArrayAdapter = new ArrayAdapter<UpiBenModel>(getApplicationContext(), android.R.layout.simple_spinner_item, simpleUpiBenModelList);

                    upiBenModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(upiBenModelArrayAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            UpiBenModel upiben = (UpiBenModel) parent.getSelectedItem();

                            //displayUserData(upiben);
                            withdrawToUPI(upiben);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    // populateListItems();

                } else {
                    //
                    Toast.makeText(getApplicationContext(), "No UPI Beneficiary", Toast.LENGTH_LONG).show();

                    //spinner.setVisibility(View.GONE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error Getting UPI Beneficiary", Toast.LENGTH_LONG).show();
            }


        }
    }

    // Withdraw to UPI
    void withdrawToUPI(UpiBenModel upiBen) {
        String upiAdd = upiBen.getUpi();
        String benId = upiBen.getBenId();
        if (!upiAdd.equals("SELECT UPI")) {
            withdrawAmountlayout.setVisibility(View.VISIBLE);
            withdrawBtn.setText("Withdraw To UPI " + (upiAdd.equalsIgnoreCase("SELECT UPI") ? "" : "(" + upiAdd + ")") + "");
            SendBenid = benId;
            mode = "upi";
        }
    }


    //display for upi
    void displayUserData(UpiBenModel upiBen) {
        String upiAdd = upiBen.getUpi();
        String benId = upiBen.getBenId();
        String data = "Upi Address : " + upiAdd + " Beneficiary ID: " + benId;
        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

    }

    void populateListItems() {
        listViewUpiBen = (ListView) findViewById(R.id.listViewupi_ben);
        listViewUpiBen.setDivider(null);

        UpiBenAdapter upiBenAdapter = new UpiBenAdapter(activity, upiBenModelList);
        listViewUpiBen.setAdapter(upiBenAdapter);
    }


    public class GetBankBeneficiary extends AsyncTask<String, String, String> {


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
            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.URL_GET_BANK_BEN, params);

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();

            Log.i("RESULT_BANK", result);

            try {
                JSONObject resultObject = new JSONObject(result);

                if (resultObject.optString("status").equalsIgnoreCase("Success")) {

                    JSONArray message = resultObject.getJSONArray("msg");

                    BankBeneficiaryModel bankBeneficiary1 = new BankBeneficiaryModel("BENID", "SELECT BANK", "", "SAFEPE123");
                    simpleBankBeneficiaryList.add(bankBeneficiary1);
                    for (int i = 0; i < message.length(); i++) {

                        JSONObject beneficaryObject = message.getJSONObject(i);

                        BankBeneficiaryModel bankBeneficiary = new BankBeneficiaryModel(
                                beneficaryObject.getString("benId"),
                                beneficaryObject.getString("name"),
                                beneficaryObject.getString("bank_account"),
                                beneficaryObject.getString("ifsc_code")
                        );

                        simpleBankBeneficiaryList.add(bankBeneficiary);
                    }


                    ArrayAdapter<BankBeneficiaryModel> bankBenModelArrayAdapter = new ArrayAdapter<BankBeneficiaryModel>(getApplicationContext(), android.R.layout.simple_spinner_item, simpleBankBeneficiaryList);

                    bankBenModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bankBenSpinner.setAdapter(bankBenModelArrayAdapter);
                    bankBenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            BankBeneficiaryModel bankben = (BankBeneficiaryModel) parent.getSelectedItem();
                            // displayUserData(bankben);
                            withdrawToBank(bankben);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });


                } else {

                    final int visibility = bankBenSpinner.getVisibility();

                    if (visibility == View.VISIBLE) {
                        bankBenSpinner.setVisibility(View.GONE);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    //withdraw to bank
    void withdrawToBank(BankBeneficiaryModel bankBen) {
        String accNum = bankBen.getBankAccount();
        String benId = bankBen.getBenId();
        String bankIFSC = bankBen.getBankIFSC();
        if (!accNum.equals("")) {

            withdrawAmountlayout.setVisibility(View.VISIBLE);
            withdrawBtn.setText("Withdraw To Bank " + (accNum.equalsIgnoreCase("") ? "" : "(" + accNum + ")") + "");
            SendBenid = benId;
            mode = "banktransfer";
        }

    }

    //Display for bank
    void displayUserData(BankBeneficiaryModel bankBen) {
        String accNum = bankBen.getBankAccount();
        String benId = bankBen.getBenId();

        String data = "Bank Account: " + accNum + " Beneficiary ID: " + benId;
        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();

    }


    public class SendTransferRequest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar_WalletHistory.setVisibility(View.VISIBLE);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... param) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", userid);
            params.put("wallet_id", walletId);
            params.put("benid", SendBenid);
            params.put("mode", mode);
            params.put("amount", amountwithdrawn.toString());
            params.put("netamount", withdrawAmount.getText().toString());


            return requestHandler.sendPostRequest(URLs.URL_PAYMENT_TRANSFER, params);

        }

        @Override
        protected void onPostExecute(String result) {
            // initialize
            progressDialog.dismiss();


            Log.i("RESULT", result);


            try {
                JSONObject obj = new JSONObject(result);
                Intent toStatusActivity = new Intent(getApplicationContext(), WithdrawlStatusActivity.class);
                String localTransactionId = obj.optString("msg");
                if (obj.optString("status").equalsIgnoreCase("Success")) {

                    toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", amountToWithdraw)
                            .putExtra("paymentmode", mode).putExtra("payment_status", "Success").putExtra("msg", "");


                    Toast.makeText(getApplicationContext(), "Withdraw Successfull", Toast.LENGTH_LONG).show();

                    //  new UpdateWalletAmount().execute();

//                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(amountToWithdraw, "Success", walletId, userid, "Withdraw to self", "debit");
//                    createWalletTransaction.execute();


                } else if (obj.optString("status").equalsIgnoreCase("Pending")) {

                    paymentStatus = false;
                    pendingPayment = true;

                    toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", amountToWithdraw)
                            .putExtra("paymentmode", mode).putExtra("payment_status", "Pending").putExtra("msg", "");


                    Toast.makeText(getApplicationContext(), obj.optString("msg"), Toast.LENGTH_LONG).show();
//                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(amountToWithdraw, "Pending", walletId, userid, "Withdraw to self", "debit");
//                    createWalletTransaction.execute();
                } else if (obj.optString("status").equalsIgnoreCase("Failed")) {
                    Toast.makeText(getApplicationContext(), obj.optString("msg"), Toast.LENGTH_LONG).show();

                    toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", amountToWithdraw)
                            .putExtra("paymentmode", mode).putExtra("payment_status", "Failed").putExtra("msg", "");

                } else {
                    if (obj.optString("msg").equalsIgnoreCase("Withdrawl blocked")) {
                        Toast.makeText(getApplicationContext(), "Withdraw Blocked", Toast.LENGTH_LONG).show();

                        toStatusActivity = new Intent(getApplicationContext(), WithdrawlStatusActivity.class);
                        toStatusActivity.putExtra("transactionid", "").putExtra("amount", amountToWithdraw)
                                .putExtra("paymentmode", mode).putExtra("payment_status", "Failed");

                    }

                    paymentStatus = false;

//                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(amountToWithdraw, "Failed", walletId, userid, "Withdraw to self", "debit");
//                    createWalletTransaction.execute();
                }
                startActivity(toStatusActivity);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    class UpdateWalletAmount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("amount", amountToWithdraw);
            params.put("wallet_id", walletId);
            params.put("operation", "debit");
            //params.put("order_currency", "INR");
            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
//            progressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Wallet Amount Updated", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
//                    startActivity(intent);
//                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", userid);

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
                    walletId = jsonObject.optString("wallet_id");
                    walletAmount = jsonObject.optString("amount");
                    //  tv_walletAmount.setText("₹ " + walletAmount);


                } else {
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(VISIBLE);
            progressDialog.show();
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
            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET_TRANSC, params);
        }

        @Override
        protected void onPostExecute(String result) {
            // progressBar.setVisibility(GONE);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {


                    String localTransactionId = jsonObject.optString("msg");

                    Toast.makeText(SendMoneyActivity.this, "Wallet Transaction Created.", Toast.LENGTH_SHORT).show();
                    Intent toStatusActivity = new Intent(getApplicationContext(), WithdrawlStatusActivity.class);

//
//                startActivity( new Intent(getApplicationContext(),WithdrawlamountSuccess.class).putExtra("transactionid","").putExtra("amount",amountwithdrawn)
//                        .putExtra("paymentmode", mode));
//                finish();

                    if (paymentStatus) {
                        toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", this.amount)
                                .putExtra("paymentmode", mode).putExtra("payment_status", "Success").putExtra("msg", "");

                    } else if (pendingPayment) {

                        toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", this.amount)
                                .putExtra("paymentmode", mode).putExtra("payment_status", "Pending").putExtra("msg", "");

                    } else {
                        toStatusActivity.putExtra("transactionid", localTransactionId).putExtra("amount", this.amount)
                                .putExtra("paymentmode", mode).putExtra("payment_status", "Failed").putExtra("msg", "");
                    }

                    startActivity(toStatusActivity);
                    finish();


                } else {
                    Toast.makeText(SendMoneyActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

                    Intent toStatusActivity = new Intent(getApplicationContext(), WithdrawlStatusActivity.class);
                    toStatusActivity.putExtra("transactionid", "null").putExtra("amount", this.amount)
                            .putExtra("paymentmode", mode).putExtra("payment_status", "Failed");
                    startActivity(toStatusActivity);
                    finish();
                }


            } catch (JSONException e) {
                e.printStackTrace();
//            builder.setTitle("Recharge Detail")
//                    .setMessage("Error In Processing")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
            }
        }
        //Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
    }


    class CheckPackage extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.URL_GET_PACKAGES, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {

                    JSONObject userJson = jsonObject.getJSONObject("msg");
                    String package_name = userJson.getString("package_name");
                    String package_status = userJson.getString("status");


                    if (package_status.equalsIgnoreCase("APPROVED")) {

                        withdrawlAreaLayout.setVisibility(View.VISIBLE);

                    } else {
                        getMemberShipLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                    getMemberShipLayout.setVisibility(View.VISIBLE);

                }
            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Server TimeOut", Toast.LENGTH_SHORT).show();
            }


        }
    }

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

}





