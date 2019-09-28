package com.safepayu.wallet.Activity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class Dth extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String user_id;
    String operator_id;
    EditText customer_id;
    //    private String mode = "TEST";
//    List<OperatorDetail> data;
    EditText mobileET, amountET;
    private Spinner operatorSP;
    //    View rootView;
    Button sendButton;
    String[] a = new String[50];
    ProgressBar progressBar;
    String str_mobile, name, id, email;
    //    SuperRecyclerView recyclerView;
//    ArrayList<Offer> offers = new ArrayList<>();
//    OfferAdapter adapter;
    private String operators[];
    private String selectedOperator;
    int balance = 0;
    int Rechargeamount = 0;

    //    String balance = "0";
//    private String getCustomerIDforOpr;
//    String Alloperators = "[\n" +
//            "    {\n" +
//            "        \"name\": \"VIDEOCON DTH\",\n" +
//            "        \"id\": \"28\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "        \"name\": \"SUN DTH\",\n" +
//            "        \"id\": \"26\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "        \"name\": \"BIG TV DTH\",\n" +
//            "        \"id\": \"24\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "        \"name\": \"TATA SKY DTH\",\n" +
//            "        \"id\": \"27\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "        \"name\": \"AIRTEL DTH\",\n" +
//            "        \"id\": \"23\"\n" +
//            "    },\t\n" +
//            "    {\n" +
//            "        \"name\": \"DISH DTH\",\n" +
//            "        \"id\": \"25\"\n" +
//            "    }, \n" +
//            "]";
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth);
        Button backbtn = (Button) findViewById(R.id.dth_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mobileET = (EditText) findViewById(R.id.mobile);
        operatorSP = (Spinner) findViewById(R.id.operator);
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();
        //signIn();
        sendButton = (Button) findViewById(R.id.btn);
        customer_id = (EditText) findViewById(R.id.customerid);
        amountET = (EditText) findViewById(R.id.amountid);
        //Rechargeamount=Integer.valueOf(amountET.getText().toString());
//        signIn();
//        SetOperator();
//        balance();
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Dth.this, "Coming Soon", Toast.LENGTH_SHORT).show();
//               /* String amount = amountET.getText().toString();
//                if(Integer.parseInt(balance)>Integer.parseInt(amount))
//                    MobRecharge();
//                else
//                  //  PaymentGateway();
//                    startActivity(new Intent(Dth.this, AddmoneyThroughcall.class));
//*/
//            }
//        });
//         SetOperator();
//        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                operatorId = data.get(i).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        operatorSP.setOnItemSelectedListener(this);
        operators = new String[]{"Select Operators", "VIDEOCON DTH", "SUN DTH", "BIG TV DTH", "TATA SKY DTH", "AIRTEL DTH", "DISH DTH"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operators);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        operatorSP.setAdapter(dataAdapter);
        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                selectedOperator = operators[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectedOperator = operatorSP.getSelectedItem().toString();
        Log.e("Selected Operator", selectedOperator);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount;
                amount = (amountET.getText().toString());
                String Customerid = customer_id.getText().toString();
                if (Customerid.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Enter Customer Id", Toast.LENGTH_SHORT).show();
                    customer_id.setError("Enter Customer Id");
                    customer_id.requestFocus();
                    return;
                }else if (selectedOperator.equalsIgnoreCase("Select Operators")) {
                    Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(getApplicationContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                }  else {
                    Log.e("Selected Operator", selectedOperator);
                    if (selectedOperator.equalsIgnoreCase("VIDEOCON DTH")) {
                        operator_id = String.valueOf(92);

                    } else if (selectedOperator.equalsIgnoreCase("SUN DTH")) {
                        operator_id = String.valueOf(93);
                    } else if (selectedOperator.equalsIgnoreCase("BIG TV DTH")) {
                        operator_id = String.valueOf(94);
                    } else if (selectedOperator.equalsIgnoreCase("TATA SKY DTH")) {
                        operator_id = String.valueOf(95);
                    } else if (selectedOperator.equalsIgnoreCase("AIRTEL DTH")) {
                        operator_id = String.valueOf(91);
                    } else if (selectedOperator.equalsIgnoreCase("DISH DTH")) {
                        operator_id = String.valueOf(76);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Operator Selected", Toast.LENGTH_SHORT).show();
                    }


                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(Dth.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("customerid", Customerid);
                    intent.putExtra("opid", operator_id);
                    intent.putExtra("paymentFor", "dthrecharge");
                    intent.putExtra("rechType", "DTH");
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class WalletMoney extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", user_id);
            return requestHandler.sendPostRequest(URLs.URL_GET_WALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                    balance = Integer.parseInt(jsonObject1.optString("amount"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    class DTHrecharge extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", user_id);
            return requestHandler.sendPostRequest(URLs.URL_GET_WALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                    balance = Integer.parseInt(jsonObject1.optString("amount"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

//    private void MobRecharge() {
//        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
//            final String mobile = mobileET.getText().toString();
//            String amount = amountET.getText().toString();
//            String a = operatorSP.getSelectedItem().toString();
//
//            //   String op[] = a.split(":");
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected String doInBackground(String... param) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("username", "x90@gmail.com");
//                params.put("password", "1234");
//                params.put("domain", "whitelabel.sakshamapp.com");
//                params.put("recharge_circle", "");
//                params.put("format", "json");
//                params.put("recharge_number", mobile);
//                params.put("amount", amount);
//                params.put("recharge_operator", a);
//                System.out.println("in hash mappinnnnnnnnnn");
//                // return requestHandler.sendPostRequest(URLs.URL_Recharge, params);
//                String u = "http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=dth&circle=1&amount=" + amount + "&number=" + mobile + "&operator=" + a + "&remarks=aaaaaaa&id=" + user_id + "";
//                System.out.println("in hash mapping" + u);
//                return requestHandler.sendGetRequest(u, params);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                System.out.println(mobile + "  " + amount + "  " + a + "  " + a);
//                System.out.println("dismiss loading");
//                data = new ArrayList<>();
//                //  pdLoading.dismiss();
//                try {
//                    System.out.println("under try befor json array");
//                    JSONObject obj = new JSONObject(result);
//                    System.out.println(obj);
//                    String msg = obj.getString("response");
//                    //   Toast.makeText( this ,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//                    //  Toast.makeText( this , msg, Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(Dth.this, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(Dth.this);
//                    }
//                    builder.setTitle("Recharge  Detail")
//                            .setMessage(msg)
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // continue with delete
//                                }
//                            })
//                            // .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            //      public void onClick(DialogInterface dialog, int which) {
//                            //          // do nothing
//                            //       }
//                            ///   })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
////Toast.makeText( this ,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//        }
//        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
//        ul.execute();
//        System.out.println("after execute");
//
//
//    }
//
//    private void SetOperator() {
//        operatorSP = (Spinner) findViewById(R.id.operator);
//        final List<String> listop = new ArrayList<String>();
//        class MobileRechrgeClass extends AsyncTask<String, String, String> {
//
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected String doInBackground(String... param) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//
//                System.out.println("in hash mappinnnnnnnnnn");
//                return requestHandler.sendPostRequest(URLs.URL_OPERATORS_dth, params);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                System.out.println("dismiss loading");
//                data = new ArrayList<>();
//                //  pdLoading.dismiss();
//                try {
//                    System.out.println("under try befor json array");
//                    // JSONObject obj = new JSONObject(result);
//                    // System.out.println(obj);
//                    JSONArray jArray = new JSONArray(result);
//
//                    System.out.println("after jsonarray" + jArray);
//                    //converting response to json object
//                    for (int i = 0; i < jArray.length(); i++) {
//
//                        JSONObject userJson = jArray.getJSONObject(i);
//                        data.add(new OperatorDetail(
//                                userJson.getString("id"),
//                                userJson.getString("code")
//
//
//                        ));
//                    }
//                    for (OperatorDetail detail : data) {
//                        System.out.println("in detail" + detail);
//                        listop.add(detail.getName());
//                    }
//                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Dth.this, android.R.layout.simple_spinner_item, listop);
//                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    operatorSP.setAdapter(spinnerArrayAdapter);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        MobileRechrgeClass ul = new MobileRechrgeClass();
//        ul.execute();
//        System.out.println("after executeddddddddddddd");
//    }
//
//    private void signIn() {
//
//        RememberPassword rememberPassword = SharedPrefManagerLogin.getInstance(this).getUser();
//        final String rememberMobile = rememberPassword.getMobile();
//        final String remember_password = rememberPassword.getPassword();
//        class UserLogin extends AsyncTask<Void, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                /// pdLoading.setMessage("\t Loading...");
//                //  pdLoading.show();
//                ///  pdLoading.setCancelable(false);
//                //progressbar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("mobile_number", rememberMobile);
//                params.put("password", remember_password);
//                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                //  progressbar.setVisibility(View.GONE);
//                try {
//                    JSONObject obj = new JSONObject(s);
//                    System.out.println("hellooooojson object" + obj);
//                    if (obj.getString("status").equals("TRUE")) {
//
//                        JSONObject userJson = obj.getJSONObject("data");
//                        User user1 = new User();
//                        name = userJson.getString("full_name");
//                        str_mobile = userJson.getString("mobile_number");
//                        email = userJson.getString("email_id");
//                        System.out.println("name: " + name + "str mobile:  " + str_mobile + "email: " + email);
//
//                    } else {
//                        System.out.println("elseeeeeeeeeee");
//                        Toast.makeText(Dth.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        UserLogin ul = new UserLogin();
//        ul.execute();
//        System.out.println("after executeddddddddddddd");
//
//    }
//
//    void balance() {
//        class WalletMoney extends AsyncTask<String, Void, String> {
//            int i;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                //progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected String doInBackground(String... strings) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("id", user_id);
//                return requestHandler.sendPostRequest(URLs.URL_DISPLAY_WALLET_AMOUNT, params);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                //progressBar.setVisibility(View.GONE);
//                try {
//                    JSONObject obj = new JSONObject(s);
//                    if (obj.getString("status").equals("TRUE")) {
//                        // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                        JSONArray jArray = new JSONArray(obj.getString("data"));
//                        if (jArray.length() > 0) {
//                            JSONObject userJson = jArray.getJSONObject(i);
//                            User user = new User();
//                            user.setWallet_amount(userJson.getString("amount"));
//                            balance = user.getWallet_amount();
//                            System.out.print(s + " " + balance);
//                        } else {
//                            balance = "0";
//                        }
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        WalletMoney walletMoney = new WalletMoney();
//        walletMoney.execute();
//    }
//
//    void PaymentGateway() {
//        System.out.println("Inside Payment Gateway function call");
//        final String mobile = mobileET.getText().toString();
//        String amount = amountET.getText().toString();
//        String a = operatorSP.getSelectedItem().toString();
//        // Toast.makeText(   this ,"271",Toast.LENGTH_LONG).show();
//        String order_id = System.currentTimeMillis() + "";
//        //Intent i = new Intent(   this , Cart.class);
//        Intent i = new Intent(this, GatewayRechargePage.class);
//        i.putExtra("mobile", mobile);
//        i.putExtra("amount", amount);
//        i.putExtra("circleCode", " ");
//        i.putExtra("operatorCode", a);
//        i.putExtra("order_id", order_id);
//        i.putExtra("mode", mode);
//        i.putExtra("name", name);
//        i.putExtra("email", email);
//        i.putExtra("phone", mobile);
//        i.putExtra("id", user_id);
//        i.putExtra("isFromOrder", true);
//
//
//        startActivity(i);
//
//        /////////  finish();
//
//    }
////    private void SetOperator() {
////
////        final List<String> listop = new ArrayList<String>();
////        data = new ArrayList<>();
////        try {
////            JSONArray jArray = new JSONArray(Alloperators);
////            for (int i = 0; i < jArray.length(); i++) {
////                JSONObject userJson = jArray.getJSONObject(i);
////                data.add(new OperatorDetail(
////                        userJson.getString("id"),
////                        userJson.getString("name")
////
////                ));
////                a[i] = userJson.getString("id");
////            }
////            for (OperatorDetail detail : data) {
////                System.out.println("in detail" + detail);
////                listop.add(detail.getName());
////            }
////            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listop);
////            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////            operatorSP.setAdapter(spinnerArrayAdapter);
////
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////    }
//
//    String operatorId = "";
//
//    public void getidSpinner() {
//        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                operatorId = data.get(i).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
//    public void setMobileOperatorDetails() {
//        System.out.print("sdsfsd");
//        class MobileRechrgeClass2 extends AsyncTask<String, String, String> {
//
//            final String mobile = mobileET.getText().toString();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected String doInBackground(String... param) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//
//                params.put("mobile", mobile);
//                System.out.println("in hash mapping" + mobile);
//                return requestHandler.sendPostRequest(URLs.URL_OPERATORS_info, params);
//
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//
//                System.out.println("dismiss loading");
//                System.out.println("result" + result);
//                data = new ArrayList<>();
//                //  pdLoading.dismiss();
//                try {
//                    System.out.println(data);
//                    System.out.println(result);
//                    System.out.println("under try befor json array");
//                    JSONObject obj = new JSONObject(result);
//                    String code = obj.getString("Operator_code");
//                    String op = obj.getString("Operator");
//                    System.out.println(code + "    " + op);
//                    operatorSP.setSelection(getIndexOfOperator(code));
//                    //   Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//                    //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
////Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//        }
//        MobileRechrgeClass2 ul = new MobileRechrgeClass2();
//        ul.execute();
//        System.out.println("after execute");
//
//
//    }
//    private int getIndexOfOperator(String oper) {
//
//        int size = a.length;
//        for (int i = 0; i < size; i++) {
//            if (a[i].equals(oper)) {
//                return i;
//            }
//        }
//        return 0;
//    }
//
//
//    class getOpertaor extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(rootView.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... param) {
//
//            RequestHandler requestHandler = new RequestHandler();
//            HashMap<String, String> params = new HashMap<>();
//            String u = "http://api.rechapi.com/mob_details.php?format=json&token=c8PwyRoBqAU5Jf7juS4i&mobile=" + getCustomerIDforOpr;
//            return requestHandler.sendGetRequest(u, params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            System.out.println(a);
//            progressBar.setVisibility(View.GONE);
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String service = jsonObject.getJSONObject("data").getString("service");
//                String status = jsonObject.getJSONObject("data").getString("opId");
//                JSONArray jArray = new JSONArray(Alloperators);
//                for (int i = 0; i < jArray.length(); i++) {
//                    JSONObject userJson = jArray.getJSONObject(i);
//                    if (service.equalsIgnoreCase(userJson.getString("name"))) {
//                        operatorSP.setSelection(i);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
