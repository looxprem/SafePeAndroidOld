package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.R;

public class RechargeSuccess extends AppCompatActivity {

    private TextView rec_amt;
    private TextView mob_no, tv_orderid;
    private Button home;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Navigation.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_success);

        Intent intent = getIntent();
        String mob = intent.getStringExtra("mob");
        String amount = intent.getStringExtra("amount");
        String orderid = intent.getStringExtra("orderid");

        rec_amt = findViewById(R.id.rec_amt);
        mob_no = findViewById(R.id.mob_no);
        tv_orderid = (TextView) findViewById(R.id.orderid) ;
      //  home = findViewById(R.id.home);

        mob_no.setText(mob);
        rec_amt.setText(amount);
        tv_orderid.setText(orderid);

//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(RechargeSuccess.this, Navigation.class));
//                finishAffinity();
//            }
//        });

        Button gotoWallet = (Button) findViewById(R.id.gotowallet);

        gotoWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
                finish();
            }
        });
    }
}
