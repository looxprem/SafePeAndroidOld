package com.safepayu.wallet.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.safepayu.wallet.Activity.Dth;
import com.safepayu.wallet.Activity.MobileRecharge;
import com.safepayu.wallet.Activity.PostpaidBillpay;
import com.safepayu.wallet.Activity.SendToBank;
import com.safepayu.wallet.Activity.Utilities;
import com.safepayu.wallet.Activity.SendMoney;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.R;
import com.safepayu.wallet.paymentpackage.AddmoneyThroughcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private LinearLayout addMoney,sendMoney,recharge,payBill,dth,utilities,payShop,sendToBank;
    private TextView walletMoney;
    ProgressBar progressBar;
    String str_wallet_money,user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        walletMoney=(TextView) view.findViewById(R.id.wallet_amount);
        addMoney =(LinearLayout)view.findViewById(R.id.layout_add_money);
        sendMoney =(LinearLayout)view.findViewById(R.id.layout_send_money);
        progressBar=(ProgressBar)view.findViewById(R.id.home_wallet_prograssbar);
        recharge =(LinearLayout)view.findViewById(R.id.layout_recharge);
        payBill =(LinearLayout)view.findViewById(R.id.layout_pay_bill);
        dth =(LinearLayout)view.findViewById(R.id.layout_dth);
        utilities =(LinearLayout)view.findViewById(R.id.layout_utilities);
        payShop =(LinearLayout)view.findViewById(R.id.pay_shop);
        sendToBank =(LinearLayout)view.findViewById(R.id.layout_send_bank);
        LoginUser user = SharedPrefManager.getInstance(getActivity()).getUser();
        user_id=user.getUserid();
        addMoney.setOnClickListener(this);
        sendMoney.setOnClickListener(this);
        recharge.setOnClickListener(this);
        payBill.setOnClickListener(this);
        dth.setOnClickListener(this);
        utilities.setOnClickListener(this);
        payShop.setOnClickListener(this);
        sendToBank.setOnClickListener(this);
        WalletAmount walletAmount=new WalletAmount();
        walletAmount.execute();
        return view;
    }

    @Override
    public void onClick(View view) {
        LinearLayout l=(LinearLayout)view;
        switch(l.getId()) {
            case R.id.layout_add_money:
               startActivity(new Intent(getActivity(), AddmoneyThroughcall.class));
                break;
            case R.id.layout_send_money:
                startActivity(new Intent(getActivity(), SendMoney.class));
                break;
            case R.id.layout_recharge:
                startActivity(new Intent(getActivity(), MobileRecharge.class));
                break;
            case R.id.layout_pay_bill:
                startActivity(new Intent(getActivity(), PostpaidBillpay.class));
                break;
            case R.id.layout_dth:
                startActivity(new Intent(getActivity(), Dth.class));
                break;
            case R.id.layout_utilities:
                startActivity(new Intent(getActivity(), Utilities.class));
                break;
            case R.id.pay_shop:
                startActivity(new Intent(getActivity(), SendMoney.class));
                break;
            case R.id.layout_send_bank:
                startActivity(new Intent(getActivity(), SendToBank.class));
                break;


        }
    }

    class WalletAmount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(getContext()).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_GET_WALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                if(jsonObject.optString("status").equalsIgnoreCase("Success")){
                    JSONObject jsonObject1=jsonObject.getJSONObject("msg");
                    walletMoney.setText(jsonObject1.optString("amount"));
                }
            }catch (Exception e){

            }
        }
    }
}
