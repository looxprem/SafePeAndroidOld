package com.safepayu.wallet.AdditionalPage;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class DisplayConformationBet extends AppCompatActivity {
    String str_total_coins,user_id,currentTime,str_msg;
    TextView txtV_total_coins,txt_message;
    ImageView back_btn;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_conformation_bet);
        txtV_total_coins=(TextView)findViewById(R.id.bet_amount_display);
        txt_message=(TextView)findViewById(R.id.message_display);
        back_btn=(ImageView)findViewById(R.id.back_btn_success_wallet);
        //dispalyWalletAmount();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            str_total_coins = bundle.getString("totalcoins");
            str_msg=bundle.getString("message");

        }
        txtV_total_coins.setText(str_total_coins);
        txt_message.setText(str_msg);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               // startActivity(new Intent(DisplayConformationBet.this, Homepage.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }
       // this.doubleBackToExitPressedOnce = true;
        finish();
        //startActivity(new Intent(DisplayConformationBet.this,Homepage.class));
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    }
    void updateWalletAmount(){
        LoginUser user = SharedPrefManager.getInstance(this).getUser();
       user_id=user.getUserid();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        currentTime = df.format(c.getTime());

        class WalletMoney extends AsyncTask<String,Void,String> {
            int i;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler=new RequestHandler();
                HashMap<String,String> params=new HashMap<>();
                params.put("id",user_id);
                params.put("date",currentTime);
                return requestHandler.sendPostRequest(URLs.URL_WALLET_UPDATE,params);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              //  progressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("TRUE")) {
                         Toast.makeText(DisplayConformationBet.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(DisplayConformationBet.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        WalletMoney walletMoney=new WalletMoney();
        walletMoney.execute();
    }

}
