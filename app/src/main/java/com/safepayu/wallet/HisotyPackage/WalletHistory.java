package com.safepayu.wallet.HisotyPackage;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.adapterpackage.WalletHistoryAdapter;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.WalletHistoryModel;


public class WalletHistory extends AppCompatActivity {
    Activity activity ;
    private ListView listViewRecharges;
    private ArrayList<WalletHistoryModel> walletHistoryModels;
    private Button btnLoadMore;
    Button back_btn;
    ImageView clear_btn;

    String userid;
    int count = 0;
    ProgressBar progressBar_WalletHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        activity = this;
        walletHistoryModels = new ArrayList<>();
        progressBar_WalletHistory=(ProgressBar)findViewById(R.id.progressBar_walletHistory);

        back_btn = (Button) findViewById(R.id.wallet_hist_back_btn);
       // back_btn=(ImageView)findViewById(R.id.clear_btn_wallet_history);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LoginUser sessionUser= SharedPrefManager.getInstance(this).getUser();
        userid=sessionUser.getUserid();
        count = 1;

        GetWalletHistory getWalletHistory = new GetWalletHistory();
        getWalletHistory.execute();

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
                new GetWalletHistory().execute();
            }
        });


    }

    class GetWalletHistory extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            //  progressDialog.show();
            progressBar_WalletHistory.setVisibility(View.VISIBLE);



        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //System.out.println("Number: -"+number);
            params.put("userid", userid);
            params.put("count", String.valueOf(count));
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_WALLET_HIST, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  Log.i("ERRORFORCHECKMOBILE", s);
            // System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            //  System.out.print(s.getClass());

            //  progressDialog.dismiss();
            progressBar_WalletHistory.setVisibility(View.INVISIBLE);

            // Log.e("Name",response);
            try {
                JSONObject jsonObject=new JSONObject(s);

                if(jsonObject.getString("status").equalsIgnoreCase("Success")){


                    JSONArray jsonArray=jsonObject.getJSONArray("msg");
                    // Log.e("Array",jsonArray.toString());
                    //  System.out.println(jsonArray.length());

                for (int i=0;i<jsonArray.length();i++){
                    System.out.println(i);
                }
                    for (int j=0;j<jsonArray.length();j++) {
                        JSONObject o = jsonArray.getJSONObject(j);
                        //  System.out.println(o);

                        WalletHistoryModel walletHistoryModel = new WalletHistoryModel(
                                o.getString("transaction_id"),
                                o.getString("amount"),
                                o.getString("status"),
                                o.getString("description"),
                                o.getString("operation"),
                                o.getString("date")
                        );
                        //  Log.e("Userid", rechargeModel.getUserid());

                        walletHistoryModels.add(walletHistoryModel);

                    }

                    populateListItems();

                }else{
                    //
                    Toast.makeText(getApplicationContext(), "No history found", Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error Getting Wallet History", Toast.LENGTH_LONG).show();
            }


        }
    }

    void populateListItems(){
        listViewRecharges = (ListView) findViewById(R.id.listViewTransc);
        listViewRecharges.setDivider(null);

        WalletHistoryAdapter walletHistoryAdapter = new WalletHistoryAdapter(activity , walletHistoryModels);
        listViewRecharges.setAdapter(walletHistoryAdapter);
        listViewRecharges.addFooterView(btnLoadMore);

    }



}
