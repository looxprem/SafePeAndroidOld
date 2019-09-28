package com.safepayu.wallet.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.R;

public class RechargeFailed extends AppCompatActivity {

    String mobile, rechamount, transId;

    TextView mobileNum, rechAmount, tranId;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Navigation.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_failed);


        Intent i = getIntent();

        mobile = i.getStringExtra("mob");
        rechamount = i.getStringExtra("amount");
        transId = i.getStringExtra("orderid");

        mobileNum = findViewById(R.id.mob_no);
        rechAmount = findViewById(R.id.rec_amt);
        tranId = findViewById(R.id.orderid);


        mobileNum.setText(mobile);
        rechAmount.setText(rechamount);
        tranId.setText(transId);


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
