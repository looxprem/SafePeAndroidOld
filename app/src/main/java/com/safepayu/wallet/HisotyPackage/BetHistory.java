package com.safepayu.wallet.HisotyPackage;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.adapterpackage.AdapterBetHistory;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.User;

public class BetHistory extends AppCompatActivity {
    ProgressBar progressBar_bethistory;
    RecyclerView recyclerView;
    AdapterBetHistory adapterBetHistory;
    List<User> data;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet_history);
        progressBar_bethistory=(ProgressBar)findViewById(R.id.progressBar_bethistory);
       ImageView clear_bet=(ImageView)findViewById(R.id.clear_btn_bet_history);
        clear_bet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LoginUser sessionUser= SharedPrefManager.getInstance(this).getUser();
        id=sessionUser.getUserid();
        betHistory();
    }
    private void betHistory(){
        class BetHistoryClass extends AsyncTask<String,String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar_bethistory.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id",id);
                System.out.println("in hash mappinnnnnnnnnn");
                return requestHandler.sendPostRequest(URLs.URL_BET_HISTORY,params);

            }
            @Override
            protected void onPostExecute(String result){
                progressBar_bethistory.setVisibility(View.GONE);
                System.out.println("dismiss loading");
                data=new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println("under try befor json array");
                    JSONObject obj = new JSONObject(result);
                    System.out.println(obj);
                    JSONArray jArray = new JSONArray(obj.getString("data"));

                    System.out.println("after jsonarray"+jArray);
                    //converting response to json object
                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject userJson = jArray.getJSONObject(i);
                        data.add(new User(
                                userJson.getString("date"),
                                userJson.getString("bet_number"),
                                userJson.getString("coins"),
                                userJson.getString("status")
                        ));
                    }
                    recyclerView = (RecyclerView) findViewById(R.id.bet_history);
                    // data = fill_with_data();
                    adapterBetHistory = new AdapterBetHistory(BetHistory.this,data);
                    System.out.println("qqqqqqqqqqqqqqq");
                    LinearLayoutManager betHorizontalLayoutManager = new LinearLayoutManager(BetHistory.this, LinearLayoutManager.VERTICAL, false);

                    // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(betHorizontalLayoutManager);
                    recyclerView.setAdapter(adapterBetHistory);
                    System.out.println("aaaaaaaaaaaaaaaa");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        BetHistoryClass ul = new BetHistoryClass();
        ul.execute();
        System.out.println("after executeddddddddddddd");
    }
}
