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
import android.widget.Spinner;
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
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.paymentpackage.AddmoneyThroughcall;
import com.safepayu.wallet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class Gas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<OperatorDetail> data;
    String[] a = new String[50];
    EditText mobileET, amountET;
    Spinner operatorSP;
    Spinner stateSP;
    View rootView;
    Button sendButton, backbtn;
    String user_id;
    String str_mobile, email, name, id;
    private String mode = "TEST";
    SuperRecyclerView recyclerView;
    ArrayList<Offer> offers = new ArrayList<>();
    OfferAdapter adapter;
    String balance = "0";
    private String states[];
    private String demo[];
    private String selectedState;
    private String operators[];
    private String selectedOperator;
    String operatorId;
    String circleCode;
    String Alloperaors = "[\n" +
            "    {\n" +
            "        \"name\": \"Select Operators\",\n" +
            "        \"id\": \"0\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Gujarat Gas Company Limited\",\n" +
            "        \"id\": \"35\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Haryana City Gas\",\n" +
            "        \"id\": \"38\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Indraprasth Gas Limited (igl)\",\n" +
            "        \"id\": \"36\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Mahanagar Gas Limited\",\n" +
            "        \"id\": \"37\"\n" +
            "    },\t\n" +
            "    {\n" +
            "        \"name\": \"Sabarmati Gas Limited\",\n" +
            "        \"id\": \"41\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"Tripura Natural Gas Company Ltd\",\n" +
            "        \"id\": \"40\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"Adani Bill\",\n" +
            "        \"id\": \"3\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"Siti Energy\",\n" +
            "        \"id\": \"39\"\n" +
            "    } \n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);
        mobileET = (EditText) findViewById(R.id.mobile);
        amountET = (EditText) findViewById(R.id.amount);
        operatorSP = (Spinner) findViewById(R.id.operators);
        stateSP = (Spinner) findViewById(R.id.state);
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();
        sendButton = (Button) findViewById(R.id.btn);
        backbtn = (Button) findViewById(R.id.gas_back_btn);
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
        states = new String[]{"Select State", "Gujarat", "Haryana", "Maharashtra", "North East", "Uttar Pradesh"};
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
//                else if (stateSP.getSelectedItem().equals("Select State")) {
//                    Toast.makeText(Gas.this, "Select State", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                else if (operatorSP.getSelectedItem().equals("Select Operators")) {
                    Toast.makeText(Gas.this, "Select Operators", Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.equalsIgnoreCase("")) {
                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                } else {

                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(Gas.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("customerid", mobile);
                    intent.putExtra("opid", operatorId);
                    intent.putExtra("paymentFor", "gasrecharge");
                    intent.putExtra("rechType", "Gas");
                    startActivity(intent);
                }
            }
        });
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

    private void SetOperator() {
        operatorSP = (Spinner) findViewById(R.id.operators);
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
                return requestHandler.sendPostRequest(URLs.URL_GAS, params);
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
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Gas.this, android.R.layout.simple_spinner_item, listop);
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
                        Toast.makeText(Gas.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
        // Toast.makeText(getContext(),"271",Toast.LENGTH_LONG).show();
        String order_id = System.currentTimeMillis() + "";
        //Intent i = new Intent(getContext(), Cart.class);
        Intent i = new Intent(Gas.this, GatewayRechargePage.class);
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
        String s = String.valueOf(stateSP.getSelectedItem());

        if (s.contentEquals("Gujarat")) {
            circleCode = String.valueOf(8);

            operatorSP.setOnItemSelectedListener(this);

            operators = new String[]{"Select Operators", "Adani Gas - Gujrat", "Gujarat Gas", "Sabarmati Gas"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Adani Gas - Gujrat")) {
                        operatorId = String.valueOf(48);
                    } else if (selectedOperator.equalsIgnoreCase("Gujarat Gas")) {
                        operatorId = String.valueOf(49);
                    } else if (selectedOperator.equalsIgnoreCase("Sabarmati Gas")) {
                        operatorId = String.valueOf(167);
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
            operators = new String[]{"Select Operators", "Haryana City Gas", "Adani Gas - Haryana"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Haryana City Gas")) {
                        operatorId = String.valueOf(166);
                    }
                    if (selectedOperator.equalsIgnoreCase("Adani Gas - Haryana")) {
                        operatorId = String.valueOf(172);
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
            operators = new String[]{"Select Operators", "Mahanagar Gas"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Mahanagar Gas")) {
                        operatorId = String.valueOf(51);
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
            operators = new String[]{"Select Operators", "Tripura Natural Gas"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Tripura Natural Gas")) {
                        operatorId = String.valueOf(169);
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
            operators = new String[]{"Select Operators", "Indraprastha Gas", "Siti Energy Gas"};
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, simple_spinner_item, operators);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            operatorSP.setAdapter(dataAdapter);
            operatorSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                    selectedOperator = operators[i];
                    if (selectedOperator.equalsIgnoreCase("Indraprastha Gas")) {
                        operatorId = String.valueOf(50);
                    } else if (selectedOperator.equalsIgnoreCase("Siti Energy Gas")) {
                        operatorId = String.valueOf(168);
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
