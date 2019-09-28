package com.safepayu.wallet.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.safepayu.wallet.APIClient;
import com.safepayu.wallet.APIInterface;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.adapterpackage.ClickListener;
import com.safepayu.wallet.adapterpackage.OfferAdapter;
import com.safepayu.wallet.adapterpackage.RecyclerTouchListener;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.MobileOffersResponseModel;
import com.safepayu.wallet.model.Offer;
import com.safepayu.wallet.model.OperatorDetail;
import com.safepayu.wallet.model.RememberPassword;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileRecharge extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String mode = "TEST";
    String user_id, name, id;
    TextView Offersbtn;
    String nameid, str_mobile, email;
    Button backbtn;
    String Alloperaors = "[\n" +
            "    {\n" +
            "        \"name\": \"AIRTEL\",\n" +
            "        \"id\": \"12\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"BSNL\",\n" +
            "        \"id\": \"16\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"DOCOMO\",\n" +
            "        \"id\": \"7\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"IDEA\",\n" +
            "        \"id\": \"23\"\n" +
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
            "        \"id\": \"13\"\n" +
            "    }, \n" +
            "    {\n" +
            "        \"name\": \"JIO\",\n" +
            "        \"id\": \"147\"\n" +
            "    } \n" +
            "]";
    List<OperatorDetail> data;
    double balance = 0;
    EditText mobileET, amountET;
    Spinner operatorSP;
    View rootView;
    Button sendButton;
    SuperRecyclerView recyclerView;
    ArrayList<Offer> offers = new ArrayList<>();
    String[] a = new String[50];
    OfferAdapter adapter;
    AlertDialog.Builder builder;
    ProgressBar progressBar;
    private String getMobforOpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);

        mobileET = findViewById(R.id.mobile_recharge);
        amountET = findViewById(R.id.amount);
        operatorSP = findViewById(R.id.operator);
        progressBar = findViewById(R.id.progressBar);
        backbtn = findViewById(R.id.recharge_back_btn);
        sendButton = findViewById(R.id.recharge_btn);
        Offersbtn = findViewById(R.id.Offersbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LoginUser loginUser = SharedPrefManager.getInstance(this).getUser();
        user_id = loginUser.getUserid();
        id = loginUser.getUserid();
        signIn();

        SetOperator();
        balance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_offer);
        dialog.setTitle("Select Offer");
        dialog.setCancelable(false);

        final SuperRecyclerView recyclerView = (SuperRecyclerView) dialog.findViewById(R.id.list);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel);

        TabLayout tabs = (TabLayout) dialog.findViewById(R.id.tabLayout);
        tabs.addTab(tabs.newTab().setText("FULL TALKTIME")); // FTT
        tabs.addTab(tabs.newTab().setText("Top Up"));  // TUP
        /*tabs.addTab(tabs.newTab().setText("4G"));*/
        tabs.addTab(tabs.newTab().setText("3G"));  // 3G
        tabs.addTab(tabs.newTab().setText("2G"));  // 2G
        tabs.addTab(tabs.newTab().setText("ROAMING"));   // RMG
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() == "FULL TALKTIME") {
                    //setOffersToRecyclerView(recyclerView, "FTT");
                    adapter.setOfferType("FULL TT");
                } else if (tab.getText() == "Top Up") {
                    //setOffersToRecyclerView(recyclerView, "TUP");
                    adapter.setOfferType("Topup");
                } else if (tab.getText() == "3G") {
                    //setOffersToRecyclerView(recyclerView, "3G");
                    adapter.setOfferType("3G/4G");
                } else if (tab.getText() == "2G") {
                    //setOffersToRecyclerView(recyclerView, "2G");
                    adapter.setOfferType("2G");
                } else if (tab.getText() == "ROAMING") {
                    //setOffersToRecyclerView(recyclerView, "RMG");
                    adapter.setOfferType("");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                try {
                    String amount = adapter.getAmount(position);
                    amountET.setText(amount);
                    dialog.dismiss();
                } catch (Exception e) {
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mobileET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String mobileNO = mobileET.getText().toString();

                if (mobileNO.length() >= 10) {
                    getMobforOpr = mobileNO.substring(0, 4);
                    getOpertaor getMobfor = new getOpertaor();
                    getMobfor.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                    Toast.makeText(MobileRecharge.this, "Select Operator", Toast.LENGTH_SHORT).show();
                    return;
                } else if (amount.equalsIgnoreCase("")) {
                    amountET.setError("Enter Amount");
                    amountET.requestFocus();
                    return;
                } else {
                    final String mobile = mobileET.getText().toString();
                    String amount1 = amountET.getText().toString();
                    Intent intent = new Intent(MobileRecharge.this, PaymentTypeActivity.class);
                    intent.putExtra("rechargeAmount", amount1);
                    intent.putExtra("mob", mobile);
                    intent.putExtra("opid", operatorId);
                    intent.putExtra("paymentFor", "recharge");
                    intent.putExtra("rechType", "Mobile");
                    startActivity(intent);
                    finish();
                }

            }
        });
        Offersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNO = mobileET.getText().toString();
                if (mobileNO.length() >= 10) {
                    String apiUrl = "http://api.sakshamapp.com/MobileOffer?mobile=" + mobileNO;
                    new MobileRecharge.GetOfferData(recyclerView, "Topup").execute(apiUrl);

                    //GetMobileOffer();

                    dialog.show();
                } else {
                    Toast.makeText(MobileRecharge.this, " Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SetOperator() {

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

    String operatorId = "";

    public void getidSpinner() {
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

    class getOpertaor extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(rootView.VISIBLE);
        }

        @Override
        protected String doInBackground(String... param) {

            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            String u = "http://api.rechapi.com/mob_details.php?format=json&token=PfdJmExNgH1RsEbsvauc&mobile=" + mobileET.getText().toString();
            return requestHandler.sendGetRequest(u, params);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(a);
            progressBar.setVisibility(View.GONE);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String service = jsonObject.getJSONObject("data").getString("service");
                String status = jsonObject.getJSONObject("data").getString("opId");
                JSONArray jArray = new JSONArray(Alloperaors);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject userJson = jArray.getJSONObject(i);
                    if (service.equalsIgnoreCase(userJson.getString("name"))) {
                        operatorSP.setSelection(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


//    private void MobRecharge() {
//        //getidSpinner();
//        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
//
//            final String mobile = mobileET.getText().toString();
//            String amount = amountET.getText().toString();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressBar.setVisibility(rootView.VISIBLE);
//            }
//
//            @Override
//            protected String doInBackground(String... param) {
//
//                Random random = new Random();
//                int userID = random.nextInt(10000);
//
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//                String u = "http://api.rechapi.com/recharge.php?format=json&token=PfdJmExNgH1RsEbsvauc&mobile=" + mobile + "&amount=" + amount + "&opid=" + operatorId + "&urid=" + userID;
//                return requestHandler.sendGetRequest(u, params);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                System.out.println(a);
//                progressBar.setVisibility(View.GONE);
//                data = new ArrayList<>();
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String orderID = jsonObject.getJSONObject("data").getString("orderId");
//                    String status = jsonObject.getJSONObject("data").getString("status");
//                    String mobile = jsonObject.getJSONObject("data").getString("mobile");
//                    String amount = jsonObject.getJSONObject("data").getString("amount");
//                    String operatorId = jsonObject.getJSONObject("data").getString("operatorId");
//                    String error_code = jsonObject.getJSONObject("data").getString("error_code");
//                    String service = jsonObject.getJSONObject("data").getString("service");
//                    saveMobRechargeData(amount, status.toLowerCase(), mobile, "Mobile", orderID, user_id);
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
//            }
////Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//        }
//        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
//        ul.execute();
//
//    }
//
//    private void saveMobRechargeData(final String amount, final String status, final String custNumber, final String rechargeType, final String orederId, final String userId) {
//        //getidSpinner();
//        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressBar.setVisibility(rootView.VISIBLE);
//            }
//
//            @Override
//            protected String doInBackground(String... param) {
//                RequestHandler requestHandler = new RequestHandler();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("amount", amount);
//                params.put("status", status);
//                params.put("customer_number", custNumber);
//                params.put("userid", userId);
//                params.put("rech_type", rechargeType);
//                params.put("order_id", orederId);
//                return requestHandler.sendPostRequest(URLs.URL_Recharge_SAVE_DETAILS, params);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                System.out.println(a);
//                progressBar.setVisibility(View.GONE);
//                data = new ArrayList<>();
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
//                        Toast.makeText(MobileRecharge.this, "Recharge Success", Toast.LENGTH_SHORT).show();
//                        //yhn agar status success h to success ka toast dikha dio ni to failed ka bs
//                    } else
//                        Toast.makeText(MobileRecharge.this, "Recharge Failed", Toast.LENGTH_SHORT).show();
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
//            }
////Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
//        }
//        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
//        ul.execute();
//
//
//    }

    private void GetMobileOffer(){
        final APIInterface api = APIClient.getMobileOffer().create(APIInterface.class);

        api.getMobileOffer("9811871855").enqueue(new Callback<MobileOffersResponseModel>() {
            @Override
            public void onResponse(Call<MobileOffersResponseModel> call, Response<MobileOffersResponseModel> response) {


                try{
                    Offer offer = new Offer();
                    offer.setCategory(response.body().getCategory());
                    offer.setSubCategory(response.body().getSubCategory());
                    offer.setValidity(response.body().getValidity());
                    offer.setShortdesc(response.body().getDescription());
                    offer.setAmount(String.valueOf(response.body().getAmount()));
                    offer.setTalktime(String.valueOf(response.body().getTalktime()));
                    offers.add(offer);
                    adapter = new OfferAdapter(MobileRecharge.this, offers);
                    recyclerView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<MobileOffersResponseModel> call, Throwable t) {

            }
        });
    }

    public class GetOfferData extends AsyncTask<String, String, String> {
        private SuperRecyclerView recyclerView;
        private String offerType;

        public GetOfferData(SuperRecyclerView recyclerView, String offerType) {
            this.recyclerView = recyclerView;
            this.offerType = offerType;
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls) {
            String apiUrl = urls[0];
            String result = null;
            try {
                URL url = new URL(apiUrl);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                con.setAllowUserInteraction(false);
                con.setInstanceFollowRedirects(true);
                // con.setRequestMethod("POST");
                con.connect();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try {
                        InputStream in = new BufferedInputStream(con.getInputStream());

                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(in));

                        String json;

                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = sb.toString().trim();
                } else {

                    //    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                // return null;
            }
            return result;
        }

        protected void onPostExecute(String response) {


            if (response.length()>0){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Offer offer = new Offer();
                            offer.setCategory(obj.getString("category"));
                            offer.setSubCategory(obj.getString("subCategory"));
                            offer.setValidity(obj.getString("validity"));
                            offer.setShortdesc(obj.getString("description"));
                            offer.setAmount(obj.getString("amount"));
                            offer.setTalktime(obj.getString("talktime"));
                            offers.add(offer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            adapter = new OfferAdapter(MobileRecharge.this, offers);
            recyclerView.setAdapter(adapter);
        }
    }

    public void setMobileOperatorDetails() {
        System.out.print("sdsfsd");
        class MobileRechrgeClass2 extends AsyncTask<String, String, String> {

            final String mobile = mobileET.getText().toString();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();

                params.put("mobile", mobile);
                System.out.println("in hash mapping" + mobile);
                return requestHandler.sendPostRequest(URLs.URL_OPERATORS_info, params);

            }

            @Override
            protected void onPostExecute(String result) {

                System.out.println("dismiss loading");
                System.out.println("result" + result);
                data = new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println(data);
                    System.out.println(result);
                    System.out.println("under try befor json array");
                    JSONObject obj = new JSONObject(result);
                    String code = obj.getString("Operator_code");
                    String op = obj.getString("Operator");
                    System.out.println(code + "    " + op);
                    operatorSP.setSelection(getIndexOfOperator(code));
                    //   Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
        }
        MobileRechrgeClass2 ul = new MobileRechrgeClass2();
        ul.execute();
        System.out.println("after execute");


    }

    private int getIndexOfOperator(String oper) {

        int size = a.length;
        for (int i = 0; i < size; i++) {
            if (a[i].equals(oper)) {
                return i;
            }
        }
        return 0;
    }

    void balance() {
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
                        balance = Double.parseDouble(jsonObject1.optString("amount"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
        WalletMoney walletMoney = new WalletMoney();
        walletMoney.execute();
    }

    @Override
    public void onRefresh() {

    }

    private void signIn() {
        RememberPassword rememberPassword = SharedPrefManagerLogin.getInstance(this).getUser();
        final String rememberMobile = rememberPassword.getMobile();
        final String remember_password = rememberPassword.getPassword();
        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                    if (obj.getString("status").equals("Success")) {
                        JSONArray userJson = obj.getJSONArray("msg");
                        LoginUser user = null;
                        for (int i = 0; i < userJson.length(); i++) {
                            JSONObject jsonObject = userJson.getJSONObject(i);
                            user = new LoginUser();
                            user.setUserid(jsonObject.optString("userid"));
                            user.setFirst_name(jsonObject.optString("first_name"));
                            user.setLast_name(jsonObject.optString("last_name"));
                            user.setEmail(jsonObject.optString("email"));
                            user.setMobile(jsonObject.optString("mobile"));
                            user.setDob(jsonObject.optString("dob"));
                            user.setReferral_code(jsonObject.optString("referral_code"));


                        }

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        Log.e("userid=", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
    }


}
