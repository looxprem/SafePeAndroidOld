package com.safepayu.wallet.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.AdditionalPage.Redeem;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SendToBank extends AppCompatActivity {

    String user_id,str_wallet_money="0",str_amount="0";
    int int_wallet_amount=0,int_editAmount=0;
    EditText amount;
    TextView total_coins;
    ProgressBar progressBar;
    Button redeemBtn,redeemToWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_bank);

        Button back=(Button)findViewById(R.id.sendbank_back_btn);
        total_coins=(TextView)findViewById(R.id.redeem_total_coins);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_redeem);
        redeemBtn=(Button)findViewById(R.id.redeem_btn);
      //  redeemToWallet=(Button)findViewById(R.id.redeem_to_wallet);
        amount=(EditText)findViewById(R.id.editredeemAmount);

        back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemMethod();
            }
        });

        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        user_id=user.getUserid();
        dispalyWalletAmount();
    }

    void dispalyWalletAmount(){
        class WalletMoney extends AsyncTask<String,Void,String> {
            int i;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler=new RequestHandler();
                HashMap<String,String> params=new HashMap<>();
                params.put("id",user_id);
                return requestHandler.sendPostRequest(URLs.URL_DISPLAY_WALLET_AMOUNT,params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                try {
                    System.out.print(s); //System.out.print("111111111111111111111111111111111111");
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("TRUE")) {
                        Toast.makeText(SendToBank.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray jArray = new JSONArray(obj.getString("data"));
                        if(jArray.length()>0) {
                            JSONObject userJson = jArray.getJSONObject(i);
                            User user = new User();
                            user.setWallet_amount(userJson.getString("amount"));
                            str_wallet_money = user.getWallet_amount();
                            int_wallet_amount = Integer.parseInt(str_wallet_money);
                            total_coins.setText(str_wallet_money);
                        }
                        else {
                            total_coins.setText(str_wallet_money);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        WalletMoney walletMoney=new WalletMoney();
        walletMoney.execute();
    }
    void redeemMethod(){
        str_amount=amount.getText().toString().trim();

        if (!Patterns.PHONE.matcher(str_amount).matches()) {
            amount.setError("Plz Enter min 500");
            amount.requestFocus();
            return;
        }
        if(str_amount!=null){
            int_editAmount=Integer.parseInt(str_amount);
        }
        if(int_wallet_amount>int_editAmount){
            Intent intent=  new Intent(this, Redeem.class);
            intent.putExtra("amount",str_amount);
            startActivity(intent);
        }else {
            Toast.makeText(this,"Insufficient Balance",Toast.LENGTH_SHORT).show();
        }

    }
}
