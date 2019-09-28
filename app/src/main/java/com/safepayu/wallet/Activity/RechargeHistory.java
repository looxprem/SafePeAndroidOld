package com.safepayu.wallet.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;
import com.safepayu.wallet.adapterpackage.HistoryAdapter;
import com.safepayu.wallet.adapterpackage.RechargeHIstoryAdapter;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.RechargeModel;
import com.safepayu.wallet.model.transaction_hist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class RechargeHistory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Activity activity ;
    private ListView listViewRecharges;
    private ArrayList<RechargeModel> rechargeModels;
    RechargeModel rechargeModel;
    //ProgressDialog progressDialog;
    Button fromSearch, backbtn, btnLoadMore;
    String user_id;

    HistoryAdapter adapter;
    int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);
       // ProgressDialog progressDialog = new ProgressDialog(this);


        activity = this;
        rechargeModels = new ArrayList<>();

        backbtn=(Button)findViewById(R.id.rechargehis_back_btn);
        LoginUser loginUser = SharedPrefManager.getInstance(RechargeHistory.this).getUser();


        //
        user_id=loginUser.getUserid();
        count = 1;

       // Log.e("url","Hello how r u");
       // progressDialog.setMessage("Loading..");
       // progressDialog.show();

            GetRechargeHistory getRechargeHistory = new GetRechargeHistory();
            getRechargeHistory.execute();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RechargeHistory.super.onBackPressed();
            }
        });


        btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");
        btnLoadMore.setBackground(getResources().getDrawable(R.drawable.blue_border));

        // Adding button to listview at footer
        //listViewRecharges.addFooterView(btnLoadMore);

        /**
         * Listening to Load More button click event
         * */
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                count++;
                new GetRechargeHistory().execute();
            }
        });
    }
    @Override
    public void onRefresh() {
    }

    private void getRechargeHistory(){
       // Log.e("url","Hello how r u");
        //final ProgressDialog progressDialog=new ProgressDialog(this);
        //progressDialog.setMessage("Loading..");
       // progressDialog.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST,
                URLs.URL_RECH_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  progressDialog.dismiss();
                        Log.e("Name",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("msg");
                            Log.e("Array",jsonArray.toString());
                            System.out.println(jsonArray.length());

//                            for (int i=0;i<jsonArray.length();i++){
//                                System.out.println(i);
//                            }
                            for (int j=0;j<jsonArray.length();j++) {
                                JSONObject o = jsonArray.getJSONObject(j);
                                System.out.println(o);

                                RechargeModel rechargeModel = new RechargeModel(
                                        o.getString("recharge_id"),
                                        o.getString("amount"),
                                        o.getString("status"),
                                        o.getString("customer_number"),
                                        o.getString("rech_type"),
                                        o.getString("recharge_time"),
                                        o.getString("order_id")
                                );
                              //  Log.e("Userid", rechargeModel.getUserid());

                                rechargeModels.add(rechargeModel);

                            }

                            populateListItems();

                            // Log.e("User", String.valueOf(listitems.get(2).getEmail()));

//                            adapter = new MyAdapter(listitems,getApplicationContext());
//                            recyclerView.setAdapter(adapter);
//                            System.out.println(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error",error.toString());

                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    void populateListItems(){
        listViewRecharges = (ListView) findViewById(R.id.listViewRecharges);
        listViewRecharges.setDivider(null);
        RechargeHIstoryAdapter rechargeHIstoryAdapter = new RechargeHIstoryAdapter(activity , rechargeModels);
        listViewRecharges.setAdapter(rechargeHIstoryAdapter);
        listViewRecharges.addFooterView(btnLoadMore);
    }


    class GetRechargeHistory extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
          //  progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //System.out.println("Number: -"+number);
            params.put("userid", user_id);
            params.put("count", String.valueOf(count));
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_RECH_HISTORY, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  Log.i("ERRORFORCHECKMOBILE", s);
           // System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
          //  System.out.print(s.getClass());

          //  progressDialog.dismiss();
           // Log.e("Name",response);
            try {
                JSONObject jsonObject=new JSONObject(s);

                if(jsonObject.getString("status").equalsIgnoreCase("Success")){


                    JSONArray jsonArray=jsonObject.getJSONArray("msg");
                   // Log.e("Array",jsonArray.toString());
                  //  System.out.println(jsonArray.length());

//                for (int i=0;i<jsonArray.length();i++){
//                    System.out.println(i);
//                }
                    for (int j=0;j<jsonArray.length();j++) {
                        JSONObject o = jsonArray.getJSONObject(j);
                      //  System.out.println(o);

                        RechargeModel rechargeModel = new RechargeModel(
                                o.getString("recharge_id"),
                                o.getString("amount"),
                                o.getString("status"),
                                o.getString("customer_number"),
                                o.getString("rech_type"),
                                o.getString("recharge_time"),
                                o.getString("order_id")
                        );
                        //  Log.e("Userid", rechargeModel.getUserid());

                        rechargeModels.add(rechargeModel);

                    }

                    populateListItems();

                }else{
                    //
                    Toast.makeText(getApplicationContext(), "No history found", Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}

