package com.safepayu.wallet.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.AddMoneyFailed;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.PackageRequestProcessing;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.MemberShipPackageData;
import com.safepayu.wallet.paymentpackage.AddMoney;
import com.safepayu.wallet.utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.view.View.VISIBLE;

public class Membership extends AppCompatActivity {

    private Button mem_back_btn;
    private TextView buy, addmoneytowallet;
    private LinearLayout diamondPackage;
    private LinearLayout titaniumPackage;
    private LinearLayout goldPackage;
    private LinearLayout silverPackage;
    private LinearLayout proSilver;
    private LinearLayout proPackage;
    private LinearLayout nanoPackage;
    private LinearLayout promotionPackage;
    private TextView mobileNumber, gstcal;
    private TextView packageName;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    private String paidAmountFromWallet;


    private TextView packageAmouunt;
    //    private ProgressBar progressBar;
    LoadingDialog dialog;

    private ArrayList<MemberShipPackageData> memberShipPackageDataArrayList;
    private TextView tv_diamondName, tv_diamondAmount, tv_titaniumName, tv_titaniumAmount, tv_goldName, tv_goldAmount, tv_silverplusName, tv_silverplusAmount,
            tv_silverName, tv_silverAmount,
            tv_proName, tv_proAmount, tv_nanoName, tv_nanoAmount, tv_promotionName, tv_promotionAmount;
    private RadioGroup rg_addpackage;
    private String idofType = "";
    private String walletId;
    private String walletAmount;
    private String userid;
    private CardView cardView;
    String packetid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        dialog = new LoadingDialog(this, "Loading...");
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        loginUser.getMobile();
        progressDialog = new ProgressDialog(this);
        userid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid();

        tv_diamondName = findViewById(R.id.tv_diamondName);
        tv_diamondAmount = findViewById(R.id.tv_diamondAmount);
        tv_titaniumName = findViewById(R.id.tv_titaniumName);
        tv_titaniumAmount = findViewById(R.id.tv_titaniumAmount);
        tv_goldName = findViewById(R.id.tv_goldName);
        tv_goldAmount = findViewById(R.id.tv_goldAmount);
        tv_silverplusName = findViewById(R.id.tv_silverplusName);
        tv_silverplusAmount = findViewById(R.id.tv_silverplusAmount);
        tv_silverName = findViewById(R.id.tv_silverName);
        tv_silverAmount = findViewById(R.id.tv_silverAmount);
        tv_proName = findViewById(R.id.tv_proName);
        tv_proAmount = findViewById(R.id.tv_proAmount);
        tv_nanoName = findViewById(R.id.tv_nanoName);
        tv_nanoAmount = findViewById(R.id.tv_nanoAmount);
        tv_promotionName = findViewById(R.id.tv_promotionName);
        tv_promotionAmount = findViewById(R.id.tv_promotionAmount);
        buy = findViewById(R.id.buy);
        addmoneytowallet = findViewById(R.id.addmoneytowallet);
        rg_addpackage = findViewById(R.id.rg_addpackage);
        cardView = findViewById(R.id.bankdetails);

        gstcal = (TextView) findViewById(R.id.gstcal);

//        progressBar = findViewById(R.id.progressBar);
        diamondPackage = findViewById(R.id.diamondPackage);
        titaniumPackage = findViewById(R.id.titanium_package);
        goldPackage = findViewById(R.id.gold_package);
        silverPackage = findViewById(R.id.silver_package);
        proSilver = findViewById(R.id.silverplus_package);
        proPackage = findViewById(R.id.pro_package);
        nanoPackage = findViewById(R.id.nano_package);
        promotionPackage = findViewById(R.id.promotional_package);

        mobileNumber = findViewById(R.id.mobile_numb);
        packageName = findViewById(R.id.package_name);
        packageAmouunt = findViewById(R.id.pckg_amt);

        mobileNumber.setText(loginUser.getMobile());
        mem_back_btn = findViewById(R.id.mem_back_btn);
        mem_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        diamondPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }
                packageName.setText("DIAMOND");
                packageAmouunt.setText("200000");
                packetid = "DMD";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });

        titaniumPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }

                packageName.setText("TITANIUM");
                packageAmouunt.setText("150000");
                packetid = "TIT";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });

        goldPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }

                packageName.setText("GOLD");
                packageAmouunt.setText("100000");
                packetid = "GLD";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });
        proSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }

                packageName.setText("SILVER PLUS");
                packageAmouunt.setText("75000");
                packetid = "SLP";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });
        silverPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }

                packageName.setText("SILVER");
                packageAmouunt.setText("50000");
                packetid = "SLV";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });
        proPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }

                packageName.setText("PRO");
                packageAmouunt.setText("25000");
                packetid = "PRO";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });
        nanoPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }
                packageName.setText("NANO");
                packageAmouunt.setText("10000");
                packetid = "NANO";

                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });
        promotionPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (memberShipPackageDataArrayList == null) {
                    showErrorDialog("Reload", "We have getting problem retry");
                    return;
                }
                packageName.setText("PROMOTIONAL");
                packageAmouunt.setText("5000");
                packetid = "PROM";


                calculateTotalAmount(packageAmouunt.getText().toString());
            }
        });


        GetMemberShipPackages getMemberShipPackages = new GetMemberShipPackages();
        getMemberShipPackages.execute();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (packageName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please  Select  Package", Toast.LENGTH_LONG).show();

                } else if (idofType.equalsIgnoreCase("bank")) {

                    Intent intent = new Intent(getApplicationContext(), MemberBankAddPackages.class);
                    intent.putExtra("packageid", packetid);
                    intent.putExtra("packagename", packageName.getText().toString());

                    startActivity(intent);

                } else if (idofType.equalsIgnoreCase("wallet")) {
                    Log.e("aaaaaaaaaaaa", "bank");

                    GetWalletDetails getWalletDetails = new GetWalletDetails();
                    getWalletDetails.execute();


                } else {
                    Toast.makeText(getApplicationContext(), "Select Payment Type", Toast.LENGTH_LONG).show();
                }
            }
        });

        addmoneytowallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), WalletAddMoney.class));
            }
        });

        rg_addpackage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int radioButtonID=rg_addpackage.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioButtonID);
                String RadioText=radioButton.getText().toString();
                //Toast.makeText(Membership.this,RadioText, Toast.LENGTH_SHORT).show();

                if(radioButtonID==-1){
                    Toast.makeText(Membership.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }else {
                    if (RadioText.equalsIgnoreCase("Bank Transfer")){
                        idofType = "bank";
                        cardView.setVisibility(VISIBLE);
                        buy.setText("Proceed By Bank");
                    }else {
                        idofType = "wallet";
                        cardView.setVisibility(View.GONE);
                        buy.setText("Proceed By Wallet");
                    }
                }

//                try{
//                    if (checkedId == R.id.radiobutton_bank) {
//                        idofType = "bank";
//                        cardView.setVisibility(VISIBLE);
//                        buy.setText("Proceed By Bank");
//
//                    } else if (checkedId == R.id.radiobutton_walled) {
//                        idofType = "wallet";
//                        cardView.setVisibility(View.GONE);
//                        buy.setText("Proceed By Wallet");
//
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        });
    }


    private void calculateTotalAmount(String amount) {

        Double amountDouble = Double.parseDouble(amount);
        Double totalAmount = amountDouble + 0.18 * amountDouble;

        gstcal.setText("Total Amount to Pay = " + amountDouble + " + 18% GST = " + totalAmount);
        paidAmountFromWallet = totalAmount.toString();

    }


    class GetMemberShipPackages extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            /*params.put("orderid", orderId1 + "");
            if (amountToBePaid == null) {
                params.put("order_amount", amt);
            } else {
                params.put("order_amount", amountToBePaid);
            }
            params.put("order_currency", "INR");*/
            return requestHandler.sendPostRequest(URLs.URL_MEMBERSHIP_PACKAGES, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.hide();
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.optString("status").equalsIgnoreCase("success")) {
                    memberShipPackageDataArrayList = new ArrayList<>();
                    memberShipPackageDataArrayList.clear();
                    JSONArray jsonArray = obj.getJSONArray("msg");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MemberShipPackageData memberShipPackageData = new MemberShipPackageData();
                        memberShipPackageData.setId(jsonObject.optString("id"));
                        memberShipPackageData.setName(jsonObject.optString("name"));
                        memberShipPackageData.setAmount(jsonObject.optString("amount"));
                        memberShipPackageData.setBalance(jsonObject.optString("balance"));
                        memberShipPackageDataArrayList.add(memberShipPackageData);
                    }
                    tv_promotionName.setText(memberShipPackageDataArrayList.get(0).getName());
                    tv_promotionAmount.setText(memberShipPackageDataArrayList.get(0).getAmount());
                    tv_nanoName.setText(memberShipPackageDataArrayList.get(1).getName());
                    tv_nanoAmount.setText(memberShipPackageDataArrayList.get(1).getAmount());
                    tv_proName.setText(memberShipPackageDataArrayList.get(2).getName());
                    tv_proAmount.setText(memberShipPackageDataArrayList.get(2).getAmount());
                    tv_silverName.setText(memberShipPackageDataArrayList.get(3).getName());
                    tv_silverAmount.setText(memberShipPackageDataArrayList.get(3).getAmount());
                    tv_silverplusName.setText(memberShipPackageDataArrayList.get(4).getName());
                    tv_silverplusAmount.setText(memberShipPackageDataArrayList.get(4).getAmount());
                    tv_goldName.setText(memberShipPackageDataArrayList.get(5).getName());
                    tv_goldAmount.setText(memberShipPackageDataArrayList.get(5).getAmount());
                    tv_titaniumName.setText(memberShipPackageDataArrayList.get(6).getName());
                    tv_titaniumAmount.setText(memberShipPackageDataArrayList.get(6).getAmount());
                    tv_diamondName.setText(memberShipPackageDataArrayList.get(7).getName());
                    tv_diamondAmount.setText(memberShipPackageDataArrayList.get(7).getAmount());


                }

            } catch (JSONException e) {
                e.printStackTrace();
                showErrorDialog("Reload", "We have getting problem retry");
            }
        }


    }

    private void showErrorDialog(String btnText, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*GetMemberShipPackages getMemberShipPackages = new GetMemberShipPackages();
                        getMemberShipPackages.execute();*/
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error");
        alert.show();

    }

    class GetWalletDetails extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("bbbbbbbbbbbbbbb", "bank");

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
            dialog.hide();
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    JSONObject jsonObject = obj.getJSONObject("msg");
                    walletId = jsonObject.optString("wallet_id");
                    walletAmount = jsonObject.optString("amount");
                    if (Double.parseDouble(walletAmount) >= Double.parseDouble(paidAmountFromWallet)) {
                        Log.e("eeeeeeeeeeeeee", "bank");
                        //paidAmountFromWallet = packageAmouunt.getText().toString();
                        UpdateWalletAmount updateWalletAmount = new UpdateWalletAmount();
                        updateWalletAmount.execute();
                    } else {

                        addmoneytowallet.setVisibility(VISIBLE);
                        Log.e("ddddddddddd", "bank");

                        Toast.makeText(getApplicationContext(), "Insufficient Amount in wallet", Toast.LENGTH_LONG).show();
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("cccccccccccccc", "bank");
//                showErrorDialog("Reload", "We have getting problem retry");
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
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("wallet_id", walletId);
            params.put("amount", paidAmountFromWallet);
            params.put("operation", "debit");
            return requestHandler.sendPostRequest(URLs.URL_UPDATEWALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);

            System.out.println(s);
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.optString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Your Packages Updated", Toast.LENGTH_LONG).show();
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(paidAmountFromWallet, "Success", walletId, userid, "Bought Package", "debit");
                    createWalletTransaction.execute();
                    /*WalletActivity.GetWalletDetails getWalletDetails = new WalletActivity.GetWalletDetails();
                    getWalletDetails.execute();*/

                } else {
                    Toast.makeText(getApplicationContext(), "Error Uodating wallet", Toast.LENGTH_LONG).show();
                    CreateWalletTransaction createWalletTransaction = new CreateWalletTransaction(paidAmountFromWallet, "Failed", walletId, userid, "Bought Package", "debit");
                    createWalletTransaction.execute();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    class CreatePackage extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\t Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            Date date = new Date();

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("package_id", packetid);
            params.put("userid", userid);
            params.put("payment_mode", "wallet");
            //params.put("package_name",packageName);
            params.put("reference_no", date.toString());
            params.put("paidto", "Hixson Technologies");
            params.put("paidfrom", "wallet");
            params.put("buydate", date.toString());
            return requestHandler.sendPostRequest(URLs.URL_CREATE_PACKAGE, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), PackageRequestProcessing.class));


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
//            progressDialog.show();
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
            // progressBar.setVisibility(GONE);
//            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    Toast.makeText(Membership.this, "Wallet Transaction Created.", Toast.LENGTH_SHORT).show();

                    CreatePackage createPackage = new CreatePackage();
                    createPackage.execute();


                } else {
                    Toast.makeText(Membership.this, "Error Occured", Toast.LENGTH_SHORT).show();


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Membership.this, "Some Error has occured", Toast.LENGTH_SHORT).show();

            }
        }
        //Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
    }
}
