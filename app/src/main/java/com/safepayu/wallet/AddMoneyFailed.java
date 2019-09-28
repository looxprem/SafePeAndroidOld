package com.safepayu.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.Activity.SendMoneyActivity;
import com.safepayu.wallet.Activity.WalletActivity;
import com.safepayu.wallet.Activity.WalletAddMoney;

public class AddMoneyFailed extends AppCompatActivity {
    private String trnasactionId, amount;
    TextView texttransactionid, textTryAgain;
    Button gotowallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_failed);

        Intent i = getIntent();
        trnasactionId = i.getStringExtra("trnsactionid");
        amount = i.getStringExtra("amount");

        gotowallet = (Button) findViewById(R.id.gotowallet);
        texttransactionid = (TextView) findViewById(R.id.orderid);
        textTryAgain = (TextView) findViewById(R.id.tryagain);

        texttransactionid.setText(trnasactionId);

        gotowallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));
                finish();
            }
        });

        textTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WalletAddMoney.class));
                finish();
            }
        });



    }
}
