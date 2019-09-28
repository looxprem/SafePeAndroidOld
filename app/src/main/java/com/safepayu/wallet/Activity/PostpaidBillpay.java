package com.safepayu.wallet.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.GatewayRechargePage;
import com.safepayu.wallet.adapterpackage.OfferAdapter;
import com.safepayu.wallet.model.Offer;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.OperatorDetail;
import com.safepayu.wallet.paymentpackage.AddmoneyThroughcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostpaidBillpay extends AppCompatActivity {
    Button backbtn;
    private String mode = "TEST";
    String user_id, name,id,str_mobile,email;
    List<OperatorDetail> data;
    EditText mobileET, amountET;
    Spinner operatorSP;
    ProgressDialog progressDialog;
    View rootView;
    Button sendButton,CheckBillBtn;
    ArrayList<Offer> offers = new ArrayList<>();
    OfferAdapter adapter;String balance = "0";
    TextView textView1,textView2,BillAmountTV;

    String[] a = new String[50];
    String Alloperaors = "[\n" +
            "    {\n" +
            "        \"name\": \"AIRTEL\",\n" +
            "        \"id\": \"48\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"BSNL\",\n" +
            "        \"id\": \"49\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"DOCOMO\",\n" +
            "        \"id\": \"7\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"IDEA\",\n" +
            "        \"id\": \"50\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"MTS\",\n" +
            "        \"id\": \"11\"\n" +
            "    },\t\n" +
            "    {\n" +
            "        \"name\": \"TATA INDICOM\",\n" +
            "        \"id\": \"9\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"UNINOR\",\n" +
            "        \"id\": \"12\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"VODAFONE\",\n" +
            "        \"id\": \"2\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"JIO\",\n" +
            "        \"id\": \"147\"\n" +
            "    } \n" +
            "]";
    private String operatorId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpaid_billpay);

        mobileET = (EditText)  findViewById(R.id.mobile);
        amountET = (EditText)  findViewById(R.id.amount);
        operatorSP = (Spinner)  findViewById(R.id.operator);
        sendButton = (Button)  findViewById(R.id.btn);
        textView1=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView22);
        BillAmountTV=findViewById(R.id.amountBill);
        CheckBillBtn=findViewById(R.id.btnCheckBill);

        backbtn = (Button)  findViewById(R.id.postpaid_back_btn);


        backbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});


        //SetOperator();
        SetOperatorStatic();



        balance();
        signIn();
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id=loginUser.getUserid();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount;
                amount = (amountET.getText().toString());
                if (mobileET.getText().toString().trim().length() == 0 || mobileET.getText().toString().trim().length() < 10) {
                    mobileET.setError("Enter number");
                    mobileET.requestFocus();
                    return;
                } else if (operatorId.trim().length() == 0) {
                    Toast.makeText(PostpaidBillpay.this, "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.equalsIgnoreCase("")) {

                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                } else {

                    final String mobile = mobileET.getText().toString();
                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(PostpaidBillpay.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("mob", mobile);
                    intent.putExtra("opid", operatorId);
                    intent.putExtra("paymentFor", "billPay");
                    intent.putExtra("rechType", "Mobile");
                    startActivity(intent);
                }

            }
        });

        CheckBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mobileET.getText().toString().trim().length() == 0 || mobileET.getText().toString().trim().length() < 10) {
                    mobileET.setError("Enter number");
                    mobileET.requestFocus();
                    return;
                } else if (operatorId.trim().length() == 0) {
                    Toast.makeText(PostpaidBillpay.this, "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new CheckBill().execute();
                }

            }
        });

        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operatorId = data.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void SetOperatorStatic() {

        final List<String> listop = new ArrayList<String>();
        data = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(Alloperaors);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject userJson = jArray.getJSONObject(i);
                data.add(new OperatorDetail(
                        userJson.getString("id"),
                        userJson.getString("name")

                ));
                a[i] = userJson.getString("id");
            }
            for (OperatorDetail detail : data) {
                System.out.println("in detail" + detail);
                listop.add(detail.getName());
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listop);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorSP.setAdapter(spinnerArrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void MobRecharge() {
        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
            final String mobile = mobileET.getText().toString();
            String amount = amountET.getText().toString();
            String a = operatorSP.getSelectedItem().toString();
              String op[] = a.split(":");
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
                params.put("domain", "api.sakshamapp.com");
                params.put("recharge_circle", "");
                params.put("format", "json");
                params.put("recharge_number", mobile);
                params.put("amount", amount);
                params.put("recharge_operator", a);
                params.put("rtype", "PP");
                //  return requestHandler.sendPostRequest(URLs.URL_Recharge, params);
                String u="http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=postpaid&circle=1&amount="+amount+"&number="+mobile+"&operator="+a+"&remarks=aaaaaaa&id="+user_id+"";
                System.out.println("in hash mapping"+u);
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
                    String msg = obj.getString("response");
                    //   Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(PostpaidBillpay.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(PostpaidBillpay.this);
                    }
                    builder.setTitle("Recharge  Detail")
                            .setMessage(msg)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            // .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            //      public void onClick(DialogInterface dialog, int which) {
                            //          // do nothing
                            //       }
                            ///   })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
        }
        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
        ul.execute();
        System.out.println("after execute");


    }
    void PaymentGateway(){
        final String mobile = mobileET.getText().toString();
        String amount = amountET.getText().toString();
        String a = operatorSP.getSelectedItem().toString();
        String order_id = System.currentTimeMillis() + "";
        Intent i = new Intent(PostpaidBillpay.this, GatewayRechargePage.class);
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
    private void SetOperator() {
        operatorSP = (Spinner)  findViewById(R.id.operator);
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
                return requestHandler.sendPostRequest(URLs.URL_OPERATORS_postpaid, params);
            }

            @Override
            protected void onPostExecute(String result) {
                data = new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println("under try befor json array");
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject userJson = jArray.getJSONObject(i);
                        data.add(new OperatorDetail(
                                userJson.getString("id"),
                                userJson.getString("id")
                        ));
                    }
                    for (OperatorDetail detail : data) {
                        System.out.println("in detail" + detail);
                        listop.add(detail.getName());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PostpaidBillpay.this, android.R.layout.simple_spinner_item, listop);
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
    private void  signIn(){
        RememberPassword rememberPassword= SharedPrefManagerLogin.getInstance(this).getUser();
        final String rememberMobile=rememberPassword.getMobile();
        final String remember_password=rememberPassword.getPassword();
        class UserLogin extends AsyncTask<Void, Void, String>{
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
                    System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {
                        JSONObject userJson = obj.getJSONObject("data");
                        User user1= new User();
                        name= userJson.getString("full_name");
                        str_mobile= userJson.getString("mobile_number");
                        email=userJson.getString("email_id");
                        System.out.println("name: "+name+"str mobile:  "+str_mobile+"email: "+email);
                    }
                    else {
                        Toast.makeText(PostpaidBillpay.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
    }

    class CheckBill extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PostpaidBillpay.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();

            String Urlll="https://portal.specificstep.com/neo/bbps/bill?username=DL1331&password=9867967827&" +
                    "operatorcode="+operatorId+"&number="+mobileET.getText().toString().trim();

            return requestHandler.sendGetRequest(Urlll, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject response = new JSONObject(s);
                try{
                    String amount = response.getString("amount");
                    BillAmountTV.setText(amount);
                }catch (Exception e){
                    BillAmountTV.setText("Unale to get bill amount");
                    e.printStackTrace();
                }

                amountET.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                CheckBillBtn.setVisibility(View.GONE);
                sendButton.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("dddddddddddddd", "aaa");
            }
        }
    }
}
