package com.safepayu.wallet.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.GatewayRechargePage;
import com.safepayu.wallet.R;
import com.safepayu.wallet.adapterpackage.OfferAdapter;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.Offer;
import com.safepayu.wallet.model.OperatorDetail;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Broadband extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<OperatorDetail> data;
    EditText mobileET, amountET;
    String operator_id;
    EditText customer_id;
    TextView std;
    TextView account;
    EditText accountno;
    EditText stdcode;
    Spinner operatorSP;
    private String mode = "TEST";
    String name, id;
    TextView Offersbtn;
    String nameid, str_mobile, email;
    View rootView;
    Button sendButton, backbtn;
    SuperRecyclerView recyclerView;
    String user_id;
    ArrayList<Offer> offers = new ArrayList<>();
    OfferAdapter adapter;
    AlertDialog.Builder builder;
    String balance = "0";
    private String operators[];
    private String selectedOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadband);
        mobileET = (EditText) findViewById(R.id.mobile);
        amountET = (EditText) findViewById(R.id.amount);
        operatorSP = (Spinner) findViewById(R.id.operator);
        LoginUser loginUser = SharedPrefManager.getInstance(Broadband.this).getUser();
        user_id = loginUser.getUserid();
        sendButton = (Button) findViewById(R.id.btn);
        customer_id = (EditText) findViewById(R.id.customerid);
        backbtn = (Button) findViewById(R.id.broadband_back_btn);
        std = (TextView) findViewById(R.id.textView4);
        stdcode = (EditText) findViewById(R.id.stdcode);
        account = (TextView) findViewById(R.id.textView3);
        accountno = (EditText) findViewById(R.id.accountno);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SetOperator();
        signIn();
        balance();
        operatorSP.setOnItemSelectedListener(this);
        operators = new String[]{"Select Operators", "RELIANCE INFOCOM LANDLINE", "BSNL LANDLINE", "AIRTEL LANDLINE", "TATA DOCOMO CDMA LANDLINE", "MTNL DELHI LANDLINE", "TIKONA INTERNET LANDLINE"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operators);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        operatorSP.setAdapter(dataAdapter);
        operatorSP.setOnItemSelectedListener(this);
        selectedOperator = operatorSP.getSelectedItem().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Broadband.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Broadband.this);
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount;
                String stdcod = stdcode.getText().toString();
                String Customerid = customer_id.getText().toString();
                String accountnumber = accountno.getText().toString();
                amount = (amountET.getText().toString());
                if (customer_id.getText().toString().trim().length()==0){
                    customer_id.setError("Enter Landline Number/Userid");
                    customer_id.requestFocus();
                    return;
                }else  if (operatorSP.getSelectedItem().equals("Select Operators")) {
                    Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                }else if (stdcod.equalsIgnoreCase("")) {
                    stdcode.setError("Enter STD Code");
                    stdcode.requestFocus();
                    return;
                }else if (amount.equalsIgnoreCase("")) {
                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                } else {
                    Log.e("Selected Operator", selectedOperator);
                   if (selectedOperator.equalsIgnoreCase("RELIANCE INFOCOM LANDLINE")) {
                        operator_id = String.valueOf(42);

                    } else if (selectedOperator.equalsIgnoreCase("BSNL LANDLINE")) {
                        if (accountnumber.equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Enter Account No", Toast.LENGTH_SHORT).show();
                        } else {
                            operator_id = String.valueOf(45);
                        }
                    } else if (selectedOperator.equalsIgnoreCase("AIRTEL LANDLINE")) {
                        operator_id = String.valueOf(44);
                    } else if (selectedOperator.equalsIgnoreCase("TATA DOCOMO CDMA LANDLINE")) {
                        operator_id = String.valueOf(46);
                    } else if (selectedOperator.equalsIgnoreCase("MTNL DELHI LANDLINE")) {
                        operator_id = String.valueOf(47);
                    } else if (selectedOperator.equalsIgnoreCase("TIKONA INTERNET LANDLINE")) {
                        operator_id = String.valueOf(43);
                    }

                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(Broadband.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("customerid", Customerid);
                    intent.putExtra("opid", operator_id);
                    intent.putExtra("stdcode", stdcod);
                    if (operator_id == "45") {
                        intent.putExtra("account_number", accountnumber);
                    }
                    intent.putExtra("paymentFor", "landlinerecharge");
                    intent.putExtra("rechType", "Landline");
                    startActivity(intent);
                }
            }
        });
    }

    private void MobRecharge() {
        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
            final String mobile = mobileET.getText().toString();
            String amount = amountET.getText().toString();
            String a = operatorSP.getSelectedItem().toString();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("username", "x90@gmail.com");
                params.put("password", "1234");
                params.put("domain", "whitelabel.sakshamapp.com");
                params.put("recharge_circle", "9");
                params.put("format", "json");
                params.put("recharge_number", mobile);
                params.put("amount", amount);
                params.put("recharge_operator", a);
                System.out.println("in hash mappinnnnnnnnnn");
                String u = "http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=datacard&circle=1&amount=" + amount + "&number=" + mobile + "&operator=" + a + "&remarks=aaaaaaa&id=" + user_id + "";
                System.out.println("in hash mapping" + u);
                return requestHandler.sendGetRequest(u, params);
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println(mobile + "  " + amount + "  " + a + "  " + a);
                System.out.println("dismiss loading");
                data = new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println("under try befor json array");
                    JSONObject obj = new JSONObject(result);
                    System.out.println(obj);
                    boolean error;
                    error = obj.getBoolean("error");
                    String msg = obj.getString("response");
                    if (!error) {
                        //   Toast.makeText(Broadband.this,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
                        builder.setTitle("Recharge  Detail")
                                .setMessage(msg)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                // .setNegatsiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                //      public void onClick(DialogInterface dialog, int which) {
                                //          // do nothing
                                //       }
                                ///   })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        builder.setTitle("Recharge  Detail")
                                .setMessage("Recharge is Failure")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//Toast.makeText(Broadband.this,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
        }
        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
        ul.execute();
        System.out.println("after execute");
    }

    private void SetOperator() {
        operatorSP = (Spinner) findViewById(R.id.operator);
        final List<String> listop = new ArrayList<String>();
        class MobileRechrgeClass extends AsyncTask<String, String, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();

                System.out.println("in hash mappinnnnnnnnnn");
                return requestHandler.sendPostRequest(URLs.URL_DATACARD, params);
            }

            @Override
            protected void onPostExecute(String result) {
                System.out.println("dismiss loading");
                data = new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println("under try befor json array");
                    // JSONObject obj = new JSONObject(result);
                    // System.out.println(obj);
                    JSONArray jArray = new JSONArray(result);

                    System.out.println("after jsonarray" + jArray);
                    //converting response to json object
                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject userJson = jArray.getJSONObject(i);
                        data.add(new OperatorDetail(
                                userJson.getString("id"),
                                userJson.getString("name")

                        ));
                    }
                    for (OperatorDetail detail : data) {
                        System.out.println("in detail" + detail);
                        listop.add(detail.getName());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Broadband.this, android.R.layout.simple_spinner_item, listop);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    operatorSP.setAdapter(spinnerArrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        MobileRechrgeClass ul = new MobileRechrgeClass();
        ul.execute();
        System.out.println("after executeddddddddddddd");
    }

    void balance() {
        class WalletMoney extends AsyncTask<String, Void, String> {
            int i;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", user_id);
                return requestHandler.sendPostRequest(URLs.URL_DISPLAY_WALLET_AMOUNT, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("TRUE")) {
                        // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jArray = new JSONArray(obj.getString("data"));
                        if (jArray.length() > 0) {
                            JSONObject userJson = jArray.getJSONObject(i);
                            User user = new User();
                            user.setWallet_amount(userJson.getString("amount"));
                            balance = user.getWallet_amount();
                            System.out.print(s + " " + balance);
                        } else {
                            balance = "0";
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        WalletMoney walletMoney = new WalletMoney();
        walletMoney.execute();
    }

    void PaymentGateway() {
        System.out.println("Inside Payment Gateway function call");
        final String mobile = mobileET.getText().toString();
        String amount = amountET.getText().toString();
        String a = operatorSP.getSelectedItem().toString();
        String order_id = System.currentTimeMillis() + "";
        Intent i = new Intent(Broadband.this, GatewayRechargePage.class);
        i.putExtra("mobile", mobile);
        i.putExtra("amount", amount);
        i.putExtra("circleCode", " ");
        i.putExtra("operatorCode", a);
        i.putExtra("order_id", order_id);
        i.putExtra("mode", mode);
        i.putExtra("name", name);
        i.putExtra("email", email);
        i.putExtra("phone", mobile);
        i.putExtra("id", user_id);
        i.putExtra("isFromOrder", true);
        i.putExtra("mode", mode);
        startActivity(i);
    }

    private void signIn() {
        RememberPassword rememberPassword = SharedPrefManagerLogin.getInstance(this).getUser();
        final String rememberMobile = rememberPassword.getMobile();
        final String remember_password = rememberPassword.getPassword();
        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /// pdLoading.setMessage("\t Loading...");
                //  pdLoading.show();
                ///  pdLoading.setCancelable(false);
                //progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile_number", rememberMobile);
                params.put("password", remember_password);
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  progressbar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("TRUE")) {
                        JSONObject userJson = obj.getJSONObject("data");
                        User user1 = new User();
                        name = userJson.getString("full_name");
                        str_mobile = userJson.getString("mobile_number");
                        email = userJson.getString("email_id");
                        System.out.println("name: " + name + "str mobile:  " + str_mobile + "email: " + email);
                    } else {
                        Toast.makeText(Broadband.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = String.valueOf(operatorSP.getSelectedItem());
        if (s.equalsIgnoreCase("BSNL LANDLINE")) {
            accountno.setVisibility(View.VISIBLE);
            account.setVisibility(View.VISIBLE);
        } else {
            accountno.setVisibility(View.GONE);
            account.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
