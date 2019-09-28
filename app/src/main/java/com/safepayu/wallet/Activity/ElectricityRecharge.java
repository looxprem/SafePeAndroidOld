package com.safepayu.wallet.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.GatewayRechargePage;
import com.safepayu.wallet.adapterpackage.OfferAdapter;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.Offer;
import com.safepayu.wallet.model.OperatorDetail;
import com.safepayu.wallet.GatewayRechargePage;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.paymentpackage.AddmoneyThroughcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class ElectricityRecharge extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<OperatorDetail> data;
    private String mode = "TEST";
    EditText mobileET, amountET, cityName;
    TextView city;
    Spinner operatorSP;
    Spinner stateSP;
    View rootView;
    String str_mobile, email, id, name;
    String user_id;
    Button sendButton, backbtn;
    SuperRecyclerView recyclerView;
    ArrayList<Offer> offers = new ArrayList<>();
    OfferAdapter adapter;
    String balance = "0";
    private String states[];
    private String selectedState;
    private String operators[];
    private String selectedOperator;
    String operatorId;
    String circleCode;
    LinearLayout linearLayout;
    TextView operator;
    String[] a = new String[50];
    String Alloperaors = "[\n" +
            "    {\n" +
            "        \"name\": \"Select Operators\",\n" +
            "        \"id\": \"0\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Eastern Power Distribution Company\nOf Andhra Pradesh Limited\",\n" +
            "        \"id\": \"23\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Southern Power Distribution Company\nLtd Of Andhra Pradesh\",\n" +
            "        \"id\": \"18\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"India Power Corporation Limited\nBihar\",\n" +
            "        \"id\": \"11\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Muzaffarpur Vidyut Vitran Limited\",\n" +
            "        \"id\": \"31\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bses Rajdhani\",\n" +
            "        \"id\": \"9\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bses Yamuna\",\n" +
            "        \"id\": \"10\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Tata Power\",\n" +
            "        \"id\": \"28\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Torrent Power Surat,Gujarat\",\n" +
            "        \"id\": \"5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Torrent Power Ahmedabad,Gujarat\",\n" +
            "        \"id\": \"1\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"India Power Corporation West Bengal\",\n" +
            "        \"id\": \"29\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Madhya Pradesh Paschim Kshetra\nVidyut Vitaran Company Ltd\n(indore) - Nonrapdr\",\n" +
            "        \"id\": \"54\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Torrent Power Bhiwandi,Maharashtra\",\n" +
            "        \"id\": \"6\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Brihan Mumbai Electric\nSupply And Transport Undertaking\",\n" +
            "        \"id\": \"8\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Tata Power – Mumbai\",\n" +
            "        \"id\": \"28\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Tripura State Electricity Corporation Ltd\",\n" +
            "        \"id\": \"19\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Meghalaya Power Distribution Corporation Ltd\",\n" +
            "        \"id\": \"25\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Odisha Discoms(b2b)\",\n" +
            "        \"id\": \"27\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Odisha Discoms(b2c)\",\n" +
            "        \"id\": \"27\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bharatpur Electricity Services Ltd\",\n" +
            "        \"id\": \"20\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bikaner Electricity Supply Limited\",\n" +
            "        \"id\": \"21\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Kota Electricity Distribution Ltd\",\n" +
            "        \"id\": \"24\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Rajasthan Vidyut Vitran Nigam Limited\",\n" +
            "        \"id\": \"17\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Torrent Power Agra,Uttar Pradesh\",\n" +
            "        \"id\": \"7\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Noida Power Company Limited\",\n" +
            "        \"id\": \"16\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Uttar Pradesh Power Corp Ltd\",\n" +
            "        \"id\": \"32\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Bangalore Electricity Supply Company\",\n" +
            "        \"id\": \"55\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Chhattisgarh State Power Distribution Co. Ltd\",\n" +
            "        \"id\": \"53\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Dnh Power Distribution Company Limited\",\n" +
            "        \"id\": \"22\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Adani Bill\",\n" +
            "        \"id\": \"3\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"North Bihar Power Distribution Company Ltd\",\n" +
            "        \"id\": \"33\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Paschim Gujarat Vij Company Limited\",\n" +
            "        \"id\": \"60\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Dakshin Gujarat Vij Company Limited\",\n" +
            "        \"id\": \"57\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Madhya Gujarat Vij Company Limited\",\n" +
            "        \"id\": \"59\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Uttar Gujarat Vij Company Limited\",\n" +
            "        \"id\": \"58\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"North Delhi Power Limited Tata Power – Ddl\",\n" +
            "        \"id\": \"15\"\n" +
            "    }\n"+
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_recharge);

        linearLayout = (LinearLayout) findViewById(R.id.operatorlayout);
        mobileET = (EditText) findViewById(R.id.mobile);
        amountET = (EditText) findViewById(R.id.amount);
        cityName = (EditText) findViewById(R.id.city);
        city = (TextView) findViewById(R.id.textView6);
        operator = (TextView) findViewById(R.id.textView5);
        operatorSP = (Spinner) findViewById(R.id.operator);
        stateSP = (Spinner) findViewById(R.id.state);
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();
        sendButton = (Button) findViewById(R.id.btn);
        backbtn = (Button) findViewById(R.id.electricty_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //SetOperator();

        SetOperatorStatic();

        signIn();
        balance();
        stateSP.setOnItemSelectedListener(this);
        states = new String[]{"Select State", "Andhra Pradesh", "Assam", "Bihar & Jharkhand", "Chennai", "Delhi/NCR", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu & Kashmir", "Karnataka", "Kerala", "Kolkata", "Madhya Pradesh", "Maharashtra", "Mumbai", "North East", "Orissa", "Punjab", "Rajasthan", "Tamil Nadu", "Uttar Pradesh", "West Bengal"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, simple_spinner_item, states);

        // Drop down layout style - list view with radio button
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        stateSP.setAdapter(stateAdapter);
        stateSP.setOnItemSelectedListener(this);

        operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operatorId = data.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount;
                amount = (amountET.getText().toString());
                final String mobile = mobileET.getText().toString();
                if (mobile.equalsIgnoreCase("")) {
                    mobileET.setError("Enter Customer Id");
                    mobileET.requestFocus();
                    return;
                }
//                else if(stateSP.getSelectedItem().equals("Select State")){
//                    Toast.makeText(ElectricityRecharge.this, "Select State", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(cityName.getText().toString().trim().length()==0){
//                    cityName.setError("Enter City");
//                    cityName.requestFocus();
//                    return;
//                }
                else if(operatorSP.getSelectedItem().equals("Select Operators")){
                    Toast.makeText(ElectricityRecharge.this, "Select Operators", Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.equalsIgnoreCase("")) {
                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                } else {
                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(ElectricityRecharge.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("customerid", mobile);
                    intent.putExtra("opid", operatorId);
                    intent.putExtra("paymentFor", "electricityrecharge");
                    intent.putExtra("rechType", "Electricity");
                    startActivity(intent);
                }
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

    private void MobRecharge() {
        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
            final String mobile = mobileET.getText().toString();
            String amount = amountET.getText().toString();
            String a = operatorSP.getSelectedItem().toString();

            //   String op[] = a.split(":");
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
                params.put("recharge_circle", "");
                params.put("format", "json");
                params.put("recharge_number", mobile);
                params.put("amount", amount);
                params.put("recharge_operator", a);
                System.out.println("in hash mappinnnnnnnnnn");
                //return requestHandler.sendPostRequest(URLs.URL_Recharge_UTILITY, params);
                String u = "http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=electricity&circle=1&amount=" + amount + "&number=" + mobile + "&operator=" + a + "&remarks=aaaaaaa&id=" + user_id + "";
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
                    String msg = obj.getString("response");
                    //   Toast.makeText(  this,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
                    //     Toast.makeText(  this, msg, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ElectricityRecharge.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ElectricityRecharge.this);
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
//Toast.makeText(  this,mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
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
                return requestHandler.sendPostRequest(URLs.URL_ELECTRICITY, params);
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
                                userJson.getString("id")

                        ));
                    }
                    for (OperatorDetail detail : data) {
                        System.out.println("in detail" + detail);
                        listop.add(detail.getName());
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ElectricityRecharge.this, simple_spinner_item, listop);
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
                    System.out.println("hellooooojson object" + obj);
                    if (obj.getString("status").equals("TRUE")) {

                        JSONObject userJson = obj.getJSONObject("data");
                        User user1 = new User();
                        name = userJson.getString("full_name");
                        str_mobile = userJson.getString("mobile_number");
                        email = userJson.getString("email_id");
                        System.out.println("name: " + name + "str mobile:  " + str_mobile + "email: " + email);

                    } else {
                        System.out.println("elseeeeeeeeeee");
                        Toast.makeText(ElectricityRecharge.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
        System.out.println("after executeddddddddddddd");

    }

    void PaymentGateway() {
        System.out.println("Inside Payment Gateway function call");
        final String mobile = mobileET.getText().toString();
        String amount = amountET.getText().toString();
        String a = operatorSP.getSelectedItem().toString();
        // Toast.makeText(  getContext(),"271",Toast.LENGTH_LONG).show();
        String order_id = System.currentTimeMillis() + "";
        //Intent i = new Intent(  getContext(), Cart.class);
        Intent i = new Intent(this, GatewayRechargePage.class);
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

        /////////  finish();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        linearLayout.setVisibility(View.VISIBLE);
        operator.setVisibility(View.VISIBLE);
        String s = String.valueOf(stateSP.getSelectedItem());
        if (s.contentEquals("Andhra Pradesh")) {
            circleCode = String.valueOf(5);
            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "APEPDCL - ANDHRA PRADESH", "APSPDCL - ANDHRA PRADESH", "Telangana State Southern Power Distribution Compan", "Telangana Co-Operative Electric Supply Society Ltd"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("APEPDCL - ANDHRA PRADESH")) {
                        operatorId = String.valueOf(144);
                    } else if (selectedOperator.equalsIgnoreCase("APSPDCL - ANDHRA PRADESH")) {
                        operatorId = String.valueOf(145);
                    } else if (selectedOperator.equalsIgnoreCase("Telangana State Southern Power Distribution Compan")) {
                        operatorId = String.valueOf(197);
                    } else if (selectedOperator.equalsIgnoreCase("Telangana Co-Operative Electric Supply Society Ltd")) {
                        operatorId = String.valueOf(196);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Assam")) {
            circleCode = String.valueOf(19);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "APDCL (RAPDR) - ASSAM", "APDCL (Non-RAPDR) - ASSAM"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("APDCL (RAPDR) - ASSAM")) {
                        operatorId = String.valueOf(53);
                    } else if (selectedOperator.equalsIgnoreCase("APDCL (Non-RAPDR) - ASSAM")) {
                        operatorId = String.valueOf(181);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Bihar & Jharkhand")) {
            circleCode = String.valueOf(17);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "India Power - BIHAR", "Muzaffarpur Vidyut Vitran", "NBPDCL - BIHAR", "SBPDCL - BIHAR", "JBVNL - JHARKHAND"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("India Power - BIHAR")) {
                        operatorId = String.valueOf(62);
                    } else if (selectedOperator.equalsIgnoreCase("Muzaffarpur Vidyut Vitran")) {
                        operatorId = String.valueOf(154);
                    } else if (selectedOperator.equalsIgnoreCase("NBPDCL - BIHAR")) {
                        operatorId = String.valueOf(155);
                    } else if (selectedOperator.equalsIgnoreCase("SBPDCL - BIHAR")) {
                        operatorId = String.valueOf(158);
                    } else if (selectedOperator.equalsIgnoreCase("JBVNL - JHARKHAND")) {
                        operatorId = String.valueOf(182);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Delhi/NCR")) {
            circleCode = String.valueOf(1);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "BSES Rajdhani - DELHI", "BSES Yamuna - DELHI", "Tata Power - DELHI"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("BSES Rajdhani - DELHI")) {
                        operatorId = String.valueOf(56);
                    } else if (selectedOperator.equalsIgnoreCase("BSES Yamuna - DELHI")) {
                        operatorId = String.valueOf(57);
                    } else if (selectedOperator.equalsIgnoreCase("Tata Power - DELHI")) {
                        operatorId = String.valueOf(74);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Gujarat")) {
            circleCode = String.valueOf(8);

            operatorSP.setOnItemSelectedListener(this);

            operators = new String[]{"Select Operators", "Torrent Power - Ahemdabad", "Torrent Power - Bhiwandi", "Torrent Power - Surat", "UGVCL - GUJARAT", "PGVCL - GUJARAT", "MGVCL - GUJARAT", "DGVCL - GUJARAT"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Torrent Power - Ahemdabad")) {
                        operatorId = String.valueOf(193);
                    } else if (selectedOperator.equalsIgnoreCase("Torrent Power - Bhiwandi")) {
                        operatorId = String.valueOf(192);
                    } else if (selectedOperator.equalsIgnoreCase("Torrent Power - Surat")) {
                        operatorId = String.valueOf(191);
                    } else if (selectedOperator.equalsIgnoreCase("UGVCL - GUJARAT")) {
                        operatorId = String.valueOf(163);
                    } else if (selectedOperator.equalsIgnoreCase("PGVCL - GUJARAT")) {
                        operatorId = String.valueOf(157);
                    } else if (selectedOperator.equalsIgnoreCase("MGVCL - GUJARAT")) {
                        operatorId = String.valueOf(158);
                    } else if (selectedOperator.equalsIgnoreCase("DGVCL - GUJARAT")) {
                        operatorId = String.valueOf(150);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Haryana")) {
            circleCode = String.valueOf(16);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "DHBVN - HARYANA"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("DHBVN - HARYANA")) {
                        operatorId = String.valueOf(187);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Himachal Pradesh")) {
            circleCode = String.valueOf(21);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Himachal Pradesh State Electricity Board"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Himachal Pradesh State Electricity Board")) {
                        operatorId = String.valueOf(185);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Karnataka")) {
            circleCode = String.valueOf(7);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "GESCOM - KARNATAKA", "CESCOM - KARNATAKA", "HESCOM - KARNATAKA", "BESCOM - BENGALURU"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("GESCOM - KARNATAKA")) {
                        operatorId = String.valueOf(177);
                    } else if (selectedOperator.equalsIgnoreCase("CESCOM - KARNATAKA")) {
                        operatorId = String.valueOf(183);
                    } else if (selectedOperator.equalsIgnoreCase("HESCOM - KARNATAKA")) {
                        operatorId = String.valueOf(184);
                    } else if (selectedOperator.equalsIgnoreCase("BESCOM - BENGALURU")) {
                        operatorId = String.valueOf(54);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Kerala")) {
            circleCode = String.valueOf(14);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Kerala State Electricity Board Ltd. (KSEB)"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Kerala State Electricity Board Ltd. (KSEB)")) {
                        operatorId = String.valueOf(195);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Madhya Pradesh")) {
            circleCode = String.valueOf(10);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Madhya Kshetra Vitaran - MADHYA PRADESH", "Paschim Kshetra Vitaran - MADHYA PRADESH"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Madhya Kshetra Vitaran - MADHYA PRADESH")) {
                        operatorId = String.valueOf(66);
                    } else if (selectedOperator.equalsIgnoreCase("Paschim Kshetra Vitaran - MADHYA PRADESH")) {
                        operatorId = String.valueOf(70);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Maharashtra")) {
            circleCode = String.valueOf(4);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "MSEDC - MAHARASHTRA", "SNDL Power - NAGPUR"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("MSEDC - MAHARASHTRA")) {
                        operatorId = String.valueOf(67);
                    } else if (selectedOperator.equalsIgnoreCase("SNDL Power - NAGPUR")) {
                        operatorId = String.valueOf(159);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Mumbai")) {
            circleCode = String.valueOf(2);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Tata Power - MUMBAI", "Reliance Energy - MUMBAI", "BEST Undertaking - MUMBAI"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Tata Power - MUMBAI")) {
                        operatorId = String.valueOf(161);
                    } else if (selectedOperator.equalsIgnoreCase("Reliance Energy - MUMBAI")) {
                        operatorId = String.valueOf(71);
                    } else if (selectedOperator.equalsIgnoreCase("BEST Undertaking - MUMBAI")) {
                        operatorId = String.valueOf(55);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("North East")) {
            circleCode = String.valueOf(20);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "TSECL - TRIPURA", "MEPDCL - MEGHALAYA"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("TSECL - TRIPURA")) {
                        operatorId = String.valueOf(76);
                    } else if (selectedOperator.equalsIgnoreCase("MEPDCL - MEGHALAYA")) {
                        operatorId = String.valueOf(152);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Orissa")) {
            circleCode = String.valueOf(18);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "SOUTHCO - ODISHA", "NESCO - ODISHA", "WESCO - ODISHA"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("SOUTHCO - ODISHA")) {
                        operatorId = String.valueOf(160);
                    } else if (selectedOperator.equalsIgnoreCase("NESCO - ODISHA")) {
                        operatorId = String.valueOf(156);
                    } else if (selectedOperator.equalsIgnoreCase("WESCO - ODISHA")) {
                        operatorId = String.valueOf(180);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Punjab")) {
            circleCode = String.valueOf(15);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "PSPCL - PUNJAB"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("PSPCL - PUNJAB")) {
                        operatorId = String.valueOf(186);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Rajasthan")) {
            circleCode = String.valueOf(13);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Bharatpur Electricity Service Limited", "Kota Electricity Distribution", "Jaipur Vidyut Vitran Nigam", "Jodhpur Vidyut Vitran Nigam", "Ajmer Vidyut Vitran Nigam", "BESL - BHARATPUR", "BKESL - BIKANER", "Tata Power AJMER - RAJASTHAN"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Bharatpur Electricity Service Limited")) {
                        operatorId = String.valueOf(188);
                    } else if (selectedOperator.equalsIgnoreCase("Kota Electricity Distribution")) {
                        operatorId = String.valueOf(151);
                    } else if (selectedOperator.equalsIgnoreCase("Jaipur Vidyut Vitran Nigam")) {
                        operatorId = String.valueOf(63);
                    } else if (selectedOperator.equalsIgnoreCase("Jodhpur Vidyut Vitran Nigam")) {
                        operatorId = String.valueOf(65);
                    } else if (selectedOperator.equalsIgnoreCase("Ajmer Vidyut Vitran Nigam")) {
                        operatorId = String.valueOf(52);
                    } else if (selectedOperator.equalsIgnoreCase("BESL - BHARATPUR")) {
                        operatorId = String.valueOf(146);
                    } else if (selectedOperator.equalsIgnoreCase("BKESL - BIKANER")) {
                        operatorId = String.valueOf(147);
                    } else if (selectedOperator.equalsIgnoreCase("Tata Power AJMER - RAJASTHAN")) {
                        operatorId = String.valueOf(162);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Tamil Nadu")) {
            circleCode = String.valueOf(6);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "TNEB - TAMIL NADU"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("TNEB - TAMIL NADU")) {
                        operatorId = String.valueOf(179);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("Uttar Pradesh")) {
            circleCode = String.valueOf(11);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "Torrent Power - Agra", "UPPCL (URBAN) - UTTAR PRADESH", "Noida Power - NOIDA", "UPPCL (RURAL) - UTTAR PRADESH"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Torrent Power - Agra")) {
                        operatorId = String.valueOf(194);
                    } else if (selectedOperator.equalsIgnoreCase("UPPCL (URBAN) - UTTAR PRADESH")) {
                        operatorId = String.valueOf(165);
                    } else if (selectedOperator.equalsIgnoreCase("Noida Power - NOIDA")) {
                        operatorId = String.valueOf(68);
                    } else if (selectedOperator.equalsIgnoreCase("UPPCL (RURAL) - UTTAR PRADESH")) {
                        operatorId = String.valueOf(143);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (s.contentEquals("West Bengal")) {
            circleCode = String.valueOf(12);

            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators", "CESC - WEST BENGAL", "India Power - WEST BENGAL", "WBSEDCL - WEST BENGAL"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("CESC - WEST BENGAL")) {
                        operatorId = String.valueOf(58);
                    } else if (selectedOperator.equalsIgnoreCase("India Power - WEST BENGAL")) {
                        operatorId = String.valueOf(178);
                    } else if (selectedOperator.equalsIgnoreCase("WBSEDCL - WEST BENGAL")) {
                        operatorId = String.valueOf(189);
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Operator", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            operatorSP.setOnItemSelectedListener(this);
            operators = new String[]{"Select Operators"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

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
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
