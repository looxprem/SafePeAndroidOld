package com.safepayu.wallet.ConnectionPackage;

import android.content.Context;
import android.os.AsyncTask;

import com.safepayu.wallet.Homepage;

import org.json.JSONObject;

import java.util.HashMap;


public class LastTransactionId extends AsyncTask<Void, Void, String> {

    private Context appContext;
    private String lastTransactionId;

    public LastTransactionId(Context mContext) {

        this.appContext = mContext;
    }

    public String getLastTransactionId(){

        this.execute();
        return  this.lastTransactionId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
       // HashMap<String, String> params = new HashMap<>();
        //params.put("userid", SharedPrefManager.getInstance(appContext).getUser().getUserid());
        //params.put("password", str_edit_password);
        return requestHandler.sendPostRequest(URLs.URL_GET_LAST_WALLET_TNX_ID);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                lastTransactionId = jsonObject1.getString("transaction_id");
                //walletMoney.setText(jsonObject1.optString("amount"));
            }
        } catch (Exception e) {
           // e.printStackTrace();

            lastTransactionId = null;
        }

    }
}
