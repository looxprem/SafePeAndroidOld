package com.safepayu.wallet.HisotyPackage;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.adapterpackage.RedeemHistoryAdaper;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class RedeemHistory extends AppCompatActivity {

   RedeemHistoryAdaper redeemHistoryAdaper;
    RecyclerView recyclerView;
    List<User> data;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_history);
        ImageView  clearRedeem=(ImageView)findViewById(R.id.clear_btn_redeedHistory);
        LoginUser sessionUser= SharedPrefManager.getInstance(this).getUser();
        id=sessionUser.getUserid();
        redeemHIstoryList();
        clearRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void redeemHIstoryList(){
        class RedeemHIstoryList extends AsyncTask<String,String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar_WalletHistory.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... param) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id",id);
                return requestHandler.sendPostRequest(URLs.URL_REDEEM_HiISTORY,params);

            }
            @Override
            protected void onPostExecute(String result){
               // progressBar_WalletHistory.setVisibility(View.GONE);
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
                                userJson.getString("redeem_id"),
                                userJson.getString("date"),
                                userJson.getString("amount"),
                                userJson.getString("acount_holder_name"),
                                userJson.getString("acount_number"),
                                userJson.getString("bank_name"),
                                userJson.getString("ifsc_code"),
                                userJson.getString("pan_card_number"),
                                userJson.getString("adhar_card"),
                                userJson.getString("status")
                        ));
                    }
                    recyclerView = (RecyclerView) findViewById(R.id.redeem_history);
                    // data = fill_with_data();

                    redeemHistoryAdaper = new RedeemHistoryAdaper(RedeemHistory.this,data);
                    System.out.println("qqqqqqqqqqqqqqq");
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(RedeemHistory.this, LinearLayoutManager.VERTICAL, false);

                    // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(horizontalLayoutManager);
                    recyclerView.setAdapter(redeemHistoryAdaper);
                    System.out.println("aaaaaaaaaaaaaaaa");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        RedeemHIstoryList rh = new RedeemHIstoryList();
        rh.execute();
        System.out.println("after executeddddddddddddd");
    }
}
